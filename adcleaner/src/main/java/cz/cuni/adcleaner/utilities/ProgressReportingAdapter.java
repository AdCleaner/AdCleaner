package cz.cuni.adcleaner.utilities;

import com.xuggle.mediatool.MediaToolAdapter;
import com.xuggle.mediatool.event.IVideoPictureEvent;

import cz.cuni.adcleaner.IMediator;
import cz.cuni.adcleaner.ads.AdFinder;

/**
 * @author Ondřej Heřmánek (ondra.hermanek@gmail.com)
 */
public class ProgressReportingAdapter extends MediaToolAdapter {
    // Start at 0%
    private int progressToReport = 0;

    // Report every 1% of progress
    private int reportStep = 1;

    private IMediator mediator;
    private long videoFileLength;

    public ProgressReportingAdapter(IMediator mediator, long videoFileLength)
    {
       this.mediator = mediator;
       this.videoFileLength = videoFileLength;
    }

    /**
     * Listens to video processing, reports progress.
     * @param event
     */
    public void onVideoPicture(IVideoPictureEvent event) {
        long timeStamp = event.getTimeStamp() / 1000000; // Seconds
        int progress = (int)( ((double) timeStamp / (double) videoFileLength) * 100);

        if (progress == this.progressToReport) {
            reportProgress();
            this.progressToReport += reportStep;
        }
    }

    /**
     * 100% progress has to be reported manually
     */
    public void reportFinish() {
        this.progressToReport = 100;
        reportProgress();
    }

    private void reportProgress() {
        this.mediator.reportProgress(this.progressToReport);
    }
}
