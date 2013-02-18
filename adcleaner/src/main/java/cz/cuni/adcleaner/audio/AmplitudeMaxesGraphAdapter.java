package cz.cuni.adcleaner.audio;

import com.xuggle.mediatool.MediaToolAdapter;
import com.xuggle.mediatool.event.IAudioSamplesEvent;
import com.xuggle.xuggler.IAudioSamples;

import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.RefineryUtilities;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import cz.cuni.adcleaner.video.VideoUtils;

/**
 * @author Ondřej Heřmánek (ondra.hermanek@gmail.com)
 */
public class AmplitudeMaxesGraphAdapter extends MediaToolAdapter {
    private final int granularity;
    private final ArrayList<Integer> maxes;

    /**
     * Granularity in seconds
     *
     * @param video
     */
    public AmplitudeMaxesGraphAdapter(File video) {
        this(video, 5);
    }

    protected AmplitudeMaxesGraphAdapter(File video, int granularity) {
        this.granularity = granularity;

        long videoDuration = VideoUtils.getVideoDuration(video) / 1000_000; // seconds
        int intervals = (int) videoDuration / granularity + 1;

        this.maxes = new ArrayList<>(intervals);
        for (int i = 0; i < intervals; i++) {
                maxes.add(Integer.MIN_VALUE);
        }
    }

    @Override
    public void onAudioSamples(IAudioSamplesEvent event) {
            IAudioSamples samples = event.getAudioSamples();
            for (int i = 0; i < samples.getNumSamples(); ++i) {
                int index = (int) (event.getTimeStamp(TimeUnit.SECONDS) / granularity);
                int amplitude = samples.getSample(i, 0, IAudioSamples.Format.FMT_S16);
                if (maxes.get(index) < amplitude) {
                        maxes.set(index, amplitude);
                }
            }

            super.onAudioSamples(event);
    }

    public void show() {
        XYSeries series = new XYSeries("Amplitude Maxes");
        for (int i = 0; i < maxes.size(); i++) {
                series .add(new XYDataItem((Number) (i * granularity), (Number) maxes.get(i)));
        }

        LineChartFrame frame = new LineChartFrame("Amplitude Maxes", "Time", "Amplitude Maxes", series);
        frame.pack();
        RefineryUtilities.centerFrameOnScreen(frame);
        frame.setVisible(true);
    }
}
