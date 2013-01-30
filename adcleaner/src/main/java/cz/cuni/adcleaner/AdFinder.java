package cz.cuni.adcleaner;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cz.cuni.adcleaner.audio.MediaToolApplier;
import cz.cuni.adcleaner.audio.VolumeElevationDetectorAdapter;
import cz.cuni.adcleaner.descriptors.ImageSnapListener;

/**
 * @author Ondřej Heřmánek (ondra.hermanek@gmail.com)
 */
public class AdFinder implements IAdFinder {
    private Thread processThread = null;
    private MediaToolApplier applier;
    private IMediator mediator;

    @Override
    public boolean stopVideoProcessing() {
        if (processThread == null)
            return false;

        if (!processThread.isAlive())
            return false;

        // Didn't find any better way to kill the thread
        processThread.stop();
        return  true;
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
    public boolean startVideoProcessing(String videoFilePath) {
        final File video = new File(videoFilePath);
        if (!video.exists())
            return false;

        processThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Processing started");
                int granularity = 5;
                int numberOfContinuousSections = 5;
                long calibrationIntervalLength = 20;
                double maxElevation = 1.2;

                try
                {
                    applier = new MediaToolApplier(video);
                    applier.apply(new ImageSnapListener());
                    applier.apply(new VolumeElevationDetectorAdapter(
                        numberOfContinuousSections,
                        maxElevation,
                        granularity,
                        calibrationIntervalLength)
                    );

                    applier.run();

                    System.out.println("publishing results");
                    mediator.publishResults(getProcessingResults());
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
}
