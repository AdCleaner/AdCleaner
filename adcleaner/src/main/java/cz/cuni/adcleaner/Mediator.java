package cz.cuni.adcleaner;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;

import cz.cuni.adcleaner.descriptors.*;
import cz.cuni.adcleaner.gui.MainWindow;

/**
 * @author Ondřej Heřmánek (ondra.hermanek@gmail.com)
 */
public class Mediator implements IMediator {
    private IWindow window;
    private IAdFinder adFinder;

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
    public boolean startVideoProcessing(String videoFilePath) {
        if (this.window == null || this.adFinder == null)
            return false;

        return this.adFinder.startVideoProcessing(videoFilePath);
    }

    @Override
    public boolean stopProcessing() {
        return this.adFinder.stopVideoProcessing();
    }

    @Override
    public void publishResults(List<VideoSection> videoSections) {
        this.window.processResults(videoSections);
    }
}
