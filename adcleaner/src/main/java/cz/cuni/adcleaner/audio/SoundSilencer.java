package cz.cuni.adcleaner.audio;

import java.io.File;
import java.nio.ShortBuffer;
import java.util.List;

import com.xuggle.mediatool.MediaToolAdapter;
import com.xuggle.mediatool.event.IAudioSamplesEvent;

import cz.cuni.adcleaner.MediaToolApplier;
import cz.cuni.adcleaner.ads.VideoSection;
import cz.cuni.adcleaner.utilities.ProgressReportingAdapter;

public class SoundSilencer {

        private final List<VideoSection> sections;
        private final ProgressReportingAdapter progressReporter;

	/**
	 * 
	 * @param loudersections
	 *            volume of these sections will be silenced
	 */
	public SoundSilencer(List<VideoSection> loudersections,
                             ProgressReportingAdapter progressReporter) {
		this.sections = loudersections;
                this.progressReporter = progressReporter;
	}

	public void silence(File inputVideo, File outputVideo) {
		MediaToolApplier applier = new MediaToolApplier(inputVideo);
		applier.apply(outputVideo, new VolumeAdjustTool(sections));
                applier.apply(progressReporter);
		applier.run();

                progressReporter.reportFinish();
	}

	private static class VolumeAdjustTool extends MediaToolAdapter {

		private final List<VideoSection> randomSections;
                private int sectionIterator = 0;
                private VideoSection currentSection = null;

		public VolumeAdjustTool(List<VideoSection> sections) {
			this.randomSections = sections;

                        if (sections.size() > 0)
                            currentSection = sections.get(0);
		}

		@Override
		public void onAudioSamples(IAudioSamplesEvent event) {
			if (currentSection != null) {
				Long timeStamp = event.getTimeStamp(currentSection.getTimeUnit());
				if (currentSection.getStart() < timeStamp) {
					adjustVolume(event);
					if (currentSection.getEnd() < timeStamp) {
                                                // Next video section
                                                if (++sectionIterator < randomSections.size())
                                                {
						    currentSection = randomSections.get(
                                                        sectionIterator);
                                                }
                                                else
                                                {
                                                    currentSection = null;
                                                }
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
