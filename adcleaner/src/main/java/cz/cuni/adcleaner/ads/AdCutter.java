package cz.cuni.adcleaner.ads;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import cz.cuni.adcleaner.IAdCutter;
import cz.cuni.adcleaner.IMediator;
import cz.cuni.adcleaner.audio.SoundSilencer;
import cz.cuni.adcleaner.utilities.ProgressReportingAdapter;
import cz.cuni.adcleaner.utilities.VideoUtils;

/**
 * @author Ondrej Hermanek (ondra.hermanek@gmail.com)
 */
public class AdCutter implements IAdCutter {
    private IMediator mediator;
    private Thread processThread = null;

    @Override
    public void registerMediator(IMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public boolean stopVideoCutting() {
        // Then attempt to stop the processThread
        if (processThread == null || !processThread.isAlive())
            return false;

        // Didn't find any better way to kill the thread
        processThread.stop();
        return true;
    }

    @Override
    public boolean startVideoCutting(List<VideoSection> videoSections, File inputVideo) {
        // Add "_out" to the output file name before extension
        String inputFilePath = inputVideo.getAbsolutePath();
        String outputFilePath =
            inputFilePath.substring(0, inputFilePath.lastIndexOf("."))
            + "_out"
            + inputFilePath.substring(inputFilePath.lastIndexOf("."), inputFilePath.length());

        final File outputVideo = new File(outputFilePath);

        // Create output file
        if (outputVideo.exists())
            outputVideo.delete();

        try {
            outputVideo.createNewFile();
        } catch (IOException e) {
            mediator.reportActionFailed("Failed to create output file.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        // Filter only section to be cut
        final List<VideoSection> sectionToSilence = new LinkedList<VideoSection>();
        for(VideoSection section : videoSections) {
            if (section.getCut())
                sectionToSilence.add(section);
        }

        (processThread = createProcessingThread(inputVideo, outputVideo, mediator, sectionToSilence)).start();
        return  true;

    }

    private Thread createProcessingThread(final File inputVideo, final File outputVideo,
              final IMediator mediator, final List<VideoSection> sectionsToCut) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                cutAds(inputVideo, outputVideo, mediator, sectionsToCut);
            }
        });
    }

    private void cutAds(File inputVideo, File outputVideo,
            IMediator mediator, List<VideoSection> sectionsToCut) {
        try
        {
            long videoDuration = VideoUtils.getVideoDurationInSeconds(inputVideo);
            ProgressReportingAdapter progressReporter
                = new ProgressReportingAdapter(mediator, videoDuration);
            SoundSilencer silencer = new SoundSilencer(sectionsToCut, progressReporter);
            silencer.silence(inputVideo, outputVideo);

            // And done
            mediator.cuttingAdsFinished(outputVideo);
        }
        catch (Exception ex) {
            mediator.reportActionFailed("Failed to cut ads from video file. More info in STDOUT.");
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
}
