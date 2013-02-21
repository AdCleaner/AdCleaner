package cz.cuni.adcleaner;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import cz.cuni.adcleaner.ads.AdCutter;
import cz.cuni.adcleaner.ads.VideoSection;
import cz.cuni.adcleaner.audio.SoundSilencer;
import cz.cuni.adcleaner.utilities.ProgressReportingAdapter;
import cz.cuni.adcleaner.utilities.VideoUtils;

/**
 * @author Ondřej Heřmánek (ondra.hermanek@gmail.com)
 */
public class Mediator implements IMediator {
    private IWindow window;
    private IAdFinder adFinder;
    private IAdCutter adCutter;

    public void registerWindow(IWindow window)
    {
        this.window = window;
        this.window.registerMediator(this);
    }

    public void registerAdFinder(IAdFinder adFinder)
    {
        this.adFinder = adFinder;
        this.adFinder.registerMediator(this);
    }

    @Override
    public boolean startVideoProcessing(File videoFile) {
        if (this.window == null || this.adFinder == null)
            return false;

        return this.adFinder.startVideoProcessing(videoFile);
    }

    @Override
    public boolean stopVideoProcessing() {
        return this.adFinder.stopVideoProcessing();
    }

    @Override
    public void publishResults(List<VideoSection> videoSections) {
        this.window.processResults(videoSections);
    }

    @Override
    public void reportProgress(int progress) {
        this.window.setProgress(progress);
    }

    @Override
    public boolean startCuttingAds(List<VideoSection> videoSections, File inputVideo) {
        this.adCutter = new AdCutter();
        adCutter.registerMediator(this);
        return adCutter.startVideoCutting(videoSections, inputVideo);
    }

    @Override
    public boolean stopCuttingAds() {
        return adCutter.stopVideoCutting();
    }

    @Override
    public void cuttingAdsFinished(File outputFile) {
        window.cuttingAdsFromVideoFinished(outputFile);
    }

    @Override
    public void reportActionFailed(String reason) {
        this.window.processActionFailed(reason);
    }

}
