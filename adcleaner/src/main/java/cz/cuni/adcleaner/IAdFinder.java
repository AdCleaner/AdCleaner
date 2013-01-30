package cz.cuni.adcleaner;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Ondřej Heřmánek (ondra.hermanek@gmail.com)
 */
public interface IAdFinder {
    boolean stopVideoProcessing();

    boolean startVideoProcessing(String videoFilePath);

    void registerMediator(IMediator mediator);
}
