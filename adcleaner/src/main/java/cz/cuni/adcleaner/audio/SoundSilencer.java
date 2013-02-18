package cz.cuni.adcleaner.audio;

import java.io.File;
import java.nio.ShortBuffer;
import java.util.List;

import com.xuggle.mediatool.MediaToolAdapter;
import com.xuggle.mediatool.event.IAudioSamplesEvent;

import cz.cuni.adcleaner.MediaToolApplier;
import cz.cuni.adcleaner.ads.VideoSection;

public class SoundSilencer {

	private final List<VideoSection> sections;

	/**
	 * 
	 * @param loudersections
	 *            volume of these sections will be silenced
	 */
	public SoundSilencer(List<VideoSection> loudersections) {
		this.sections = loudersections;
	}

	public void silence(File inputVideo, File outputVideo) {
		MediaToolApplier applier = new MediaToolApplier(inputVideo);
		applier.apply(outputVideo, new VolumeAdjustTool(sections));
		applier.run();
	}

	private static class VolumeAdjustTool extends MediaToolAdapter {

		private final List<VideoSection> randomSections;

		public VolumeAdjustTool(List<VideoSection> sections) {
			this.randomSections = sections;
		}

		private int sectionIterator = 0;

		@Override
		public void onAudioSamples(IAudioSamplesEvent event) {
			if (sectionIterator < randomSections.size()) {
				VideoSection currentSection = randomSections.get(sectionIterator);
				Long timeStamp = event.getTimeStamp(currentSection.getTimeUnit());
				if (currentSection.getStart() < timeStamp) {
					adjustVolume(event);
					if (currentSection.getEnd() < timeStamp) {
						sectionIterator++;
					}
				}
			}
			super.onAudioSamples(event);
		}

		private void adjustVolume(IAudioSamplesEvent event) {
			ShortBuffer buffer = event.getAudioSamples().getByteBuffer().asShortBuffer();
			for (int i = 0; i < buffer.limit(); ++i) {
				buffer.put(i, (short) 0);
			}
		}
	}
}
