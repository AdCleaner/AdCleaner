package cz.cuni.adcleaner;

import java.io.File;
import java.util.List;

import cz.cuni.adcleaner.ads.VideoSection;

/**
 * @author Ondřej Heřmánek (ondra.hermanek@gmail.com)
 */
public interface IMediator {
    void registerWindow(IWindow window);

    void registerAdFinder(IAdFinder adFinder);

    boolean startVideoProcessing(File videoFile);

    boolean stopVideoProcessing();

    void publishResults(List<VideoSection> videoSections);

    void reportProgress(int progress);

    boolean startCuttingAds(List<VideoSection> videoSections, File inputVideo);

    boolean stopCuttingAds();

    void cuttingAdsFinished(File outputFile);

    void reportActionFailed(String reason);
}
