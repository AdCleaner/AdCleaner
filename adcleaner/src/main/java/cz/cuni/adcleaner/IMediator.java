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

    boolean stopProcessing();

    void publishResults(List<VideoSection> videoSections);

    void reportProgress(int progress);

    void cutAdsFromVideo(List<VideoSection> videoSections);

    void cuttingAdsFromVideoFinished();
}
