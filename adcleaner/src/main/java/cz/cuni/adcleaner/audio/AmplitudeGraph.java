package cz.cuni.adcleaner.audio;

import java.io.File;

import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.RefineryUtilities;

import com.xuggle.mediatool.MediaToolAdapter;
import com.xuggle.mediatool.event.IAudioSamplesEvent;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.IAudioSamples.Format;

import cz.cuni.adcleaner.LineChartFrame;

public class AmplitudeGraph {

	private final File video;
	XYSeries series;

	public AmplitudeGraph(File video) {
		this.video = video;
		series = new XYSeries("Video Amplitude");
	}

	public void show() {

		new MediaToolApplyer(video).apply(new MediaToolAdapter() {
			@Override
			public void onAudioSamples(IAudioSamplesEvent event) {
				IAudioSamples samples = event.getAudioSamples();
				// for (int i = 0; i < samples.getNumSamples(); ++i){
				series.add(new XYDataItem((Number) event.getTimeStamp(), (Number) samples.getSample(0, 0, Format.FMT_S16)));
				// }
				super.onAudioSamples(event);
			}
		});
				
		final LineChartFrame frame = new LineChartFrame("Amplitude", "Time", "Amplitude", series);
		frame.pack();
		RefineryUtilities.centerFrameOnScreen(frame);
		frame.setVisible(true);
	}
}
