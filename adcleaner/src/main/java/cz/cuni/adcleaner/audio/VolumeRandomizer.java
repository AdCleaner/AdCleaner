package cz.cuni.adcleaner.audio;

import java.io.File;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.xuggle.mediatool.MediaToolAdapter;
import com.xuggle.mediatool.event.IAudioSamplesEvent;
import com.xuggle.xuggler.IContainer;

import cz.cuni.adcleaner.VideoSection;

public class VolumeRandomizer {

	private final int numberOfSections;

	public VolumeRandomizer(int numberOfSections) {
		this.numberOfSections = numberOfSections;
	}

	public List<VideoSection> randomize(File inputVideo, File outputVideo) {
		List<VideoSection> randomSections = generateRandomSections(inputVideo);
		new MediaToolApplyer(inputVideo).apply(outputVideo, new VolumeAdjustTool(randomSections));
		return randomSections;
	}

	private List<VideoSection> generateRandomSections(File inputVideo) {
		IContainer container = null;
		try {
			container = IContainer.make();
			if (container.open(inputVideo.getAbsolutePath(), IContainer.Type.READ, null) < 0) {
				throw new IllegalArgumentException("Could not open file: " + inputVideo.toString());
			}

			List<Long> times = new ArrayList<>();
			long duration = container.getDuration() / 1000; // milliseconds
			for (int i = 0; i < numberOfSections * 2; ++i) {
				times.add(Math.abs(new Random().nextLong()) % duration);
			}

			Collections.sort(times);
			List<VideoSection> videoSections = new ArrayList<>();
			for (int i = 0; i < times.size() / 2; i++) {
				videoSections.add(new VideoSection(times.get(2 * i), times.get(2 * i + 1)));
			}
			return videoSections;
		} finally {
			if (container != null) {
				container.close();
			}
		}
	}

	private static class VolumeAdjustTool extends MediaToolAdapter {

		private static final double M_VOLUME = 0.5;

		private final List<VideoSection> randomSections;

		public VolumeAdjustTool(List<VideoSection> randomSections) {
			this.randomSections = randomSections;
		}

		private int sectionIterator = 0;

		@Override
		public void onAudioSamples(IAudioSamplesEvent event) {
			if (sectionIterator < randomSections.size()) {
				VideoSection currentSection = randomSections.get(sectionIterator);
				Long timeStamp = event.getTimeStamp(TimeUnit.MILLISECONDS);
				if (currentSection.getStart() < timeStamp) {
					if (currentSection.getEnd() < timeStamp) {
						sectionIterator++;
					}
				} else {
					adjustVolume(event);
				}
			} else {
				adjustVolume(event);
			}
			super.onAudioSamples(event);
		}

		private void adjustVolume(IAudioSamplesEvent event) {
			ShortBuffer buffer = event.getAudioSamples().getByteBuffer().asShortBuffer();
			for (int i = 0; i < buffer.limit(); ++i) {
				buffer.put(i, (short) (buffer.get(i) * M_VOLUME));
			}
		}
	}
}
