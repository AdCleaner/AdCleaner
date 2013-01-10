package cz.cuni.adcleaner;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cz.cuni.adcleaner.audio.MediaToolApplier;
import cz.cuni.adcleaner.audio.VolumeElevationDetectorAdapter;
import cz.cuni.adcleaner.descriptors.ImageSnapListener;

/**
 * @author Ondřej Heřmánek (ondra.hermanek@gmail.com)
 */
public class AdFinder implements IAdFinder {
    @Override
    public List<VideoSection> ProcessVideo(String videoFilePath) {
        MediaToolApplier applier;
        try
        {
            applier = new MediaToolApplier(new File(videoFilePath));
            applier.apply(new VolumeElevationDetectorAdapter(0, 0, 0, 0));
            applier.apply(new ImageSnapListener());

            applier.run();
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        finally
        {

        }


        return new LinkedList<VideoSection>() {{
            add(new VideoSection(45L, 120L, TimeUnit.SECONDS));
            add(new VideoSection(200L, 250L, TimeUnit.SECONDS));
            add(new VideoSection(300L, 500L, TimeUnit.SECONDS));
            add(new VideoSection(555L, 556L, TimeUnit.SECONDS));
            add(new VideoSection(600L, 666L, TimeUnit.SECONDS));
        }};
    }
}
