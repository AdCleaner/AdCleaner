package cz.cuni.adcleaner.images.samples;

import java.awt.image.BufferedImage;
import java.io.File;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.ToolFactory;

import cz.cuni.adcleaner.images.ImageSnapAdapter;
import cz.cuni.adcleaner.images.ScreenShotsManager;


/**
 * @author Ondřej Heřmánek (ondra.hermanek@gmail.com)
 */
public class CaptureVideoFrame {
    private static final String inputFilename = "data/video1.mp4";

    public static void run() {

        IMediaReader mediaReader = ToolFactory.makeReader(inputFilename);

        // stipulate that we want BufferedImages created in BGR 24bit color space
        mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);

        ScreenShotsManager manager = new ScreenShotsManager(new File(inputFilename).getName());
        mediaReader.addListener(new ImageSnapAdapter(manager));
        // read out the contents of the media file and
        // dispatch events to the attached listener
        while (mediaReader.readPacket() == null) ;

        System.out.println("1: " + manager.compareScreensForShotBoundary(1));
        System.out.println("150: " + manager.compareScreensForShotBoundary(150));
    }
}
