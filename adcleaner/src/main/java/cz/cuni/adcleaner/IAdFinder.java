package cz.cuni.adcleaner;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Ondřej Heřmánek (ondra.hermanek@gmail.com)
 */
public interface IAdFinder {
    boolean stopVideoProcessing();

    boolean startVideoProcessing(File videoFile);

    void registerMediator(IMediator mediator);
}
