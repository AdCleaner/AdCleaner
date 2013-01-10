package cz.cuni.adcleaner.descriptors;

import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.MediaToolAdapter;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.Global;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
* @author Ondřej Heřmánek (ondra.hermanek@gmail.com)
*/
public class ImageSnapListener extends MediaToolAdapter {
    private static final double SECONDS_BETWEEN_FRAMES = 50;

    private static final String outputFilePrefix = "c:/Temp/mysnapshot";

    // The video stream index, used to ensure we display frames from one and
    // only one video stream from the media container.
    private static int mVideoStreamIndex = -1;

    // Time of last frame write
    private static long mLastPtsWrite = Global.NO_PTS;

    public static final long MICRO_SECONDS_BETWEEN_FRAMES =
        (long)(Global.DEFAULT_PTS_PER_SECOND * SECONDS_BETWEEN_FRAMES);

    public void onVideoPicture(IVideoPictureEvent event) {

        if (event.getStreamIndex() != mVideoStreamIndex) {
            // if the selected video stream id is not yet set, go ahead an
            // select this lucky video stream
            if (mVideoStreamIndex == -1)
                mVideoStreamIndex = event.getStreamIndex();
            // no need to show frames from this video stream
            else
                return;
        }

        // if uninitialized, back date mLastPtsWrite to get the very first frame
        if (mLastPtsWrite == Global.NO_PTS)
            mLastPtsWrite = event.getTimeStamp() - MICRO_SECONDS_BETWEEN_FRAMES;

        // if it's time to write the next frame
        if (event.getTimeStamp() - mLastPtsWrite >=
                MICRO_SECONDS_BETWEEN_FRAMES) {

            String outputFilename = dumpImageToFile(event.getImage());

            // indicate file written
            double seconds = ((double) event.getTimeStamp()) /
                Global.DEFAULT_PTS_PER_SECOND;
            System.out.printf(
                    "at elapsed time of %6.3f seconds wrote: %s\n",
                    seconds, outputFilename);

            // update last write time
            mLastPtsWrite += MICRO_SECONDS_BETWEEN_FRAMES;
        }

    }

    private String dumpImageToFile(BufferedImage image) {
        try {
            String outputFilename = outputFilePrefix +
                 System.currentTimeMillis() + ".png";
            ImageIO.write(image, "png", new File(outputFilename));
            return outputFilename;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
