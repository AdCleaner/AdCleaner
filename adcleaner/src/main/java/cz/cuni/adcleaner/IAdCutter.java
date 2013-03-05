package cz.cuni.adcleaner;

import java.io.File;
import java.util.List;

import cz.cuni.adcleaner.ads.VideoSection;

/**
 * @author Ondrej Hermanek (ondra.hermanek@gmail.com)
 */
public interface IAdCutter {
    void registerMediator(IMediator mediator);

    boolean stopVideoCutting();

    boolean startVideoCutting(List<VideoSection> videoSections, File inputVideo);
}
