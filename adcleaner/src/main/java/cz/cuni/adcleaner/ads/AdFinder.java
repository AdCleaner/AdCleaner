package cz.cuni.adcleaner.ads;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cz.cuni.adcleaner.IAdFinder;
import cz.cuni.adcleaner.IMediator;
import cz.cuni.adcleaner.audio.AmplitudeGraphAdapter;
import cz.cuni.adcleaner.audio.AmplitudeMaxesGraphAdapter;
import cz.cuni.adcleaner.MediaToolApplier;
import cz.cuni.adcleaner.audio.VolumeElevationDetectorAdapter;
import cz.cuni.adcleaner.images.ImageSnapAdapter;
import cz.cuni.adcleaner.images.ScreenShotsManager;
import cz.cuni.adcleaner.utilities.ProgressReportingAdapter;
import cz.cuni.adcleaner.utilities.VideoUtils;

/**
 * @author Ondřej Heřmánek (ondra.hermanek@gmail.com)
 */
public class AdFinder implements IAdFinder {
    private Thread processThread = null;
    private IMediator mediator;
    private ScreenShotsManager manager;

    @Override
    public boolean stopVideoProcessing() {
        // First clean created data ...
        cleanUp();

        // Then attempt to stop the processThread
        if (processThread == null || !processThread.isAlive())
            return false;

        // Didn't find any better way to kill the thread
        processThread.stop();
        return true;
    }

    @Override
    public boolean startVideoProcessing(File videoFile) {
        if (!videoFile.exists())
            return false;

        processThread = createProcessThread(videoFile, this.mediator);

        // Start
        processThread.start();
        return true;
    }

    private Thread createProcessThread(final File video, final IMediator mediator) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                findAds(video, mediator);
            }
        });
    }

    private void findAds(File video, IMediator mediator) {
        System.out.println("Processing started");
        int granularity = 5, numberOfContinuousSections = 8, calibrationIntervalLength = 40;
        double maxElevation = 1.5;
        long videoLength = VideoUtils.getVideoDurationInSeconds(video);

        manager = new ScreenShotsManager(video.getName());

        VolumeElevationDetectorAdapter volumeAdapter =
            new VolumeElevationDetectorAdapter(
                numberOfContinuousSections,
                maxElevation,
                granularity,
                calibrationIntervalLength,
                manager);

        ProgressReportingAdapter
            progressReporter = new ProgressReportingAdapter(mediator, videoLength);
        AmplitudeGraphAdapter amplitudeGraphAdapter = new AmplitudeGraphAdapter();
        AmplitudeMaxesGraphAdapter amplitudeMaxesGraphAdapter
            = new AmplitudeMaxesGraphAdapter(videoLength);

        try
        {
            // Create tool to browse the video file and register audio/video sample listeners
            MediaToolApplier applier = new MediaToolApplier(video);
            applier.apply(new ImageSnapAdapter(manager));
            applier.apply(volumeAdapter);
            applier.apply(amplitudeGraphAdapter);
            applier.apply(amplitudeMaxesGraphAdapter);
            applier.apply(progressReporter);

            // Process the video
            applier.run();

            // Show amplitude graphs
            System.out.println("Showing graphs...");
            amplitudeGraphAdapter.show();
            amplitudeMaxesGraphAdapter.show();

            // Report results
            System.out.println("publishing results...");
            progressReporter.reportFinish();
            mediator.publishResults(volumeAdapter.getLouderSections());
            //TODO: debugmediator.publishResults(getProcessingResults());
            System.out.println("Processing finished.");
        }
        catch (Exception ex)
        {
            mediator.reportActionFailed("Failed to process the whole video file. More info in STDOUT.");
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        finally
        {
           stopVideoProcessing();
        }
    }

    @Override
    public void registerMediator(IMediator mediator) {
        this.mediator = mediator;
    }

    private void cleanUp()
    {
        this.manager.cleanUp();
    }
}
