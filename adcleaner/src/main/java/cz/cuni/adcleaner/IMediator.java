package cz.cuni.adcleaner;

import java.awt.event.ItemListener;
import java.io.File;
import java.util.List;

/**
 * @author Ondřej Heřmánek (ondra.hermanek@gmail.com)
 */
public interface IMediator {
    void registerWindow(IWindow window);

    void registerAdFinder(IAdFinder adFinder);

    List<VideoSection> processVideo(File videoFile);
}
