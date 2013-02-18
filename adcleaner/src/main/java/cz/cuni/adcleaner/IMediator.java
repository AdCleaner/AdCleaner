package cz.cuni.adcleaner;

import java.util.List;

import cz.cuni.adcleaner.ads.VideoSection;

/**
 * @author Ondřej Heřmánek (ondra.hermanek@gmail.com)
 */
public interface IMediator {
    void registerWindow(IWindow window);

    void registerAdFinder(IAdFinder adFinder);

    boolean startVideoProcessing(String videoFilePath);

    boolean stopProcessing();

    void publishResults(List<VideoSection> videoSections);
}
