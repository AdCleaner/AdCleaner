package cz.cuni.adcleaner;

import java.util.List;

import cz.cuni.adcleaner.ads.VideoSection;

/**
 * @author Ondřej Heřmánek (ondra.hermanek@gmail.com)
 */
public interface IWindow {
    void registerMediator(IMediator mediator);

    void processResults(List<VideoSection> videoSections);

    void setProgress(int progress);

    void cuttingAdsFromVideoFinished();
}
