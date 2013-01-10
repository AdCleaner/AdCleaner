package cz.cuni.adcleaner.audio;

import java.io.File;
import java.nio.ShortBuffer;
import java.util.List;

import com.xuggle.mediatool.MediaToolAdapter;
import com.xuggle.mediatool.event.IAudioSamplesEvent;

import cz.cuni.adcleaner.VideoSection;

public class VolumeRandomizer {

	private final List<VideoSection> louderSections;
	private final double volumeScale;

	/**
	 * 
	 * @param louderSections
	 *            volume of these sections will be scales
	 * @param volumeScale
	 *            by how much will be volume scales value from interval (0,1)
	 */
	public VolumeRandomizer(List<VideoSection> louderSections, double volumeScale) {
		this.louderSections = louderSections;
		// we are making all the sections except the one in louderSections list
		// quieter to
		// avoid noise
		this.volumeScale = 1 - volumeScale;
	}

	public void randomize(File inputVideo, File outputVideo) {
		MediaToolApplier applier = new MediaToolApplier(inputVideo);
                applier.apply(outputVideo, new VolumeAdjustTool(louderSections, volumeScale));
                applier.run();
	}

	private static class VolumeAdjustTool extends MediaToolAdapter {

		private final double volumeScale;
		private final List<VideoSection> randomSections;

		public VolumeAdjustTool(List<VideoSection> randomSections, double volumeScale) {
			this.randomSections = randomSections;
			this.volumeScale = volumeScale;
		}

		private int sectionIterator = 0;

		@Override
		public void onAudioSamples(IAudioSamplesEvent event) {
			if (sectionIterator < randomSections.size()) {
				VideoSection currentSection = randomSections.get(sectionIterator);
				Long timeStamp = event.getTimeStamp(currentSection.getTimeuUnit());
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
				buffer.put(i, (short) (buffer.get(i) * volumeScale));
			}
		}
	}
}
