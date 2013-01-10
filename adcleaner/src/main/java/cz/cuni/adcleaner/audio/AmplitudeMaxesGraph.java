package cz.cuni.adcleaner.audio;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.RefineryUtilities;

import com.xuggle.mediatool.MediaToolAdapter;
import com.xuggle.mediatool.event.IAudioSamplesEvent;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.IAudioSamples.Format;

import cz.cuni.adcleaner.LineChartFrame;
import cz.cuni.adcleaner.VideoUtils;

public class AmplitudeMaxesGraph {

	private final File video;
	private final int granularity;

	public AmplitudeMaxesGraph(File video) {
		this(video, 1);
	}

	/**
	 * Granularity in seconds
	 * 
	 * @param video
	 * @param granularity
	 */
	public AmplitudeMaxesGraph(File video, int granularity) {
		this.video = video;
		this.granularity = granularity;
	}

	public void show() {
		long videoDuration = VideoUtils.getVideoDuration(video) / 1000_000; // seconds
		int intervals = (int) videoDuration / granularity + 1;

		final ArrayList<Integer> maxes = new ArrayList<>(intervals);
		for (int i = 0; i < intervals; i++) {
			maxes.add(Integer.MIN_VALUE);
		}

		new MediaToolApplier(video).apply(new MediaToolAdapter() {

			@Override
			public void onAudioSamples(IAudioSamplesEvent event) {
				IAudioSamples samples = event.getAudioSamples();
				for (int i = 0; i < samples.getNumSamples(); ++i) {
					int index = (int) (event.getTimeStamp(TimeUnit.SECONDS) / granularity);
					int amplitude = samples.getSample(i, 0, Format.FMT_S16);
					if (maxes.get(index) < amplitude) {
						maxes.set(index, amplitude);
					}
				}
				super.onAudioSamples(event);
			}
		});

		XYSeries series = new XYSeries("Amplitude Maxes");
		for (int i = 0; i < maxes.size(); i++) {
			series .add(new XYDataItem((Number) (i * granularity), (Number) maxes.get(i)));
		}

		final LineChartFrame frame = new LineChartFrame("Amplitude Maxes", "Time", "Amplitude Maxes", series);
		frame.pack();
		RefineryUtilities.centerFrameOnScreen(frame);
		frame.setVisible(true);
	}
}
