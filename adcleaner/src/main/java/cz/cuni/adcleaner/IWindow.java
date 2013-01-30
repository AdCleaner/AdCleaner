package cz.cuni.adcleaner;

import java.util.List;

/**
 * @author Ondřej Heřmánek (ondra.hermanek@gmail.com)
 */
public interface IWindow {
    void registerMediator(IMediator mediator);

    void processResults(List<VideoSection> videoSections);
}
