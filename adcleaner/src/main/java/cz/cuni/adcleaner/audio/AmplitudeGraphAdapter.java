package cz.cuni.adcleaner.audio;

import com.xuggle.mediatool.MediaToolAdapter;
import com.xuggle.mediatool.event.IAudioSamplesEvent;
import com.xuggle.xuggler.IAudioSamples;

import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.RefineryUtilities;

/**
 * @author Ondřej Heřmánek (ondra.hermanek@gmail.com)
 */
public class AmplitudeGraphAdapter extends MediaToolAdapter {

    XYSeries series = new XYSeries("Video Amplitude");

    @Override
    public void onAudioSamples(IAudioSamplesEvent event) {
            IAudioSamples samples = event.getAudioSamples();
            // for (int i = 0; i < samples.getNumSamples(); ++i){
            series.add(new XYDataItem((Number) event.getTimeStamp(), (Number) samples.getSample(0, 0, IAudioSamples.Format.FMT_S16)));
            // }
            super.onAudioSamples(event);
    }

    public void show() {
        LineChartFrame frame = new LineChartFrame("Amplitude", "Time", "Amplitude", series);
        frame.pack();
        RefineryUtilities.centerFrameOnScreen(frame);
        frame.setVisible(true);
    }
}
