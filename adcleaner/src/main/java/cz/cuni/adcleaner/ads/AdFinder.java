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

    private List<VideoSection> getProcessingResults() {
        return new LinkedList<VideoSection>() {{
            add(new VideoSection(45L, 120L, TimeUnit.SECONDS));
            add(new VideoSection(200L, 250L, TimeUnit.SECONDS));
            add(new VideoSection(300L, 500L, TimeUnit.SECONDS));
            add(new VideoSection(555L, 556L, TimeUnit.SECONDS));
            add(new VideoSection(600L, 666L, TimeUnit.SECONDS));
        }};
    }

    @Override
    public boolean startVideoProcessing(File videoFile) {
        if (!videoFile.exists())
            return false;

        final File video = videoFile;
        final AdFinder adFinder = this;

        processThread = new Thread(new Runnable() {
            @Override
            public void run() {
            System.out.println("Processing started");
            int granularity = 5;
            int numberOfContinuousSections = 5;
            long calibrationIntervalLength = 20;
            double maxElevation = 1.2;
            long videoLength = VideoUtils.getVideoDuration(video) / 1000000; // Seconds

            manager = new ScreenShotsManager(video.getName());

            VolumeElevationDetectorAdapter volumeAdapter =
                new VolumeElevationDetectorAdapter(
                    numberOfContinuousSections,
                    maxElevation,
                    granularity,
                    calibrationIntervalLength,
                    manager);

            ProgressReportingAdapter progresReporter = new ProgressReportingAdapter(adFinder, videoLength);
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
                applier.apply(progresReporter);

                // Process the video
                applier.run();

                // Show amplitude graphs
                System.out.println("Showing graphs...");
                amplitudeGraphAdapter.show();
                amplitudeMaxesGraphAdapter.show();

                // Report results
                System.out.println("publishing results...");
                progresReporter.reportFinish();
                mediator.publishResults(volumeAdapter.getLouderSections());
                //TODO: debug mediator.publishResults(getProcessingResults());
                System.out.println("Processing finished.");
            }
            catch (Exception ex)
            {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }
            finally
            {
               stopVideoProcessing();
            }
            }
        });

        // Start
        processThread.start();
        return true;
    }

    @Override
    public void registerMediator(IMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public void reportProgress(int progress) {
        this.mediator.reportProgress(progress);
    }

    private void cleanUp()
    {
        this.manager.cleanUp();
    }
}
