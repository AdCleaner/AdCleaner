package cz.cuni.adcleaner.images;

import com.xuggle.mediatool.MediaToolAdapter;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.Global;

/**
* @author Ondřej Heřmánek (ondra.hermanek@gmail.com)
*/
public class ImageSnapListener extends MediaToolAdapter {
    // Create screenshot every 1 second
    private final double SECONDS_BETWEEN_FRAMES = 1;

    // The video stream index, used to ensure we display frames from one and
    // only one video stream from the media container.
    private int mVideoStreamIndex = -1;

    // Time of last frame write
    private long mLastPtsWrite = Global.NO_PTS;

    private final long MICRO_SECONDS_BETWEEN_FRAMES =
        (long)(Global.DEFAULT_PTS_PER_SECOND * SECONDS_BETWEEN_FRAMES);

    private ScreenShotsManager manager;

    public ImageSnapListener(ScreenShotsManager manager)
    {
       this.manager = manager;
    }

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
        if (event.getTimeStamp() - mLastPtsWrite >= MICRO_SECONDS_BETWEEN_FRAMES) {
            // Get media timeStamp
            long seconds = event.getTimeStamp() / Global.DEFAULT_PTS_PER_SECOND;

            manager.saveScreenshot(event.getImage(), seconds);

            System.out.printf("at elapsed time of %s seconds.\n",seconds);

            // update last write time
            mLastPtsWrite += MICRO_SECONDS_BETWEEN_FRAMES;
        }

    }
}
