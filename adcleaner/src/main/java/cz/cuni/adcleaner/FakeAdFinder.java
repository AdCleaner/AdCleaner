package cz.cuni.adcleaner;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Ondřej Heřmánek (ondra.hermanek@gmail.com)
 */
public class FakeAdFinder implements IAdFinder {

    @Override
    public List<VideoSection> ProcessVideo(String videoFilePath) {
        return new LinkedList<VideoSection>() {{
            add(new VideoSection(45L, 120L, TimeUnit.SECONDS));
            add(new VideoSection(200L, 250L, TimeUnit.SECONDS));
            add(new VideoSection(300L, 500L, TimeUnit.SECONDS));
            add(new VideoSection(555L, 556L, TimeUnit.SECONDS));
            add(new VideoSection(600L, 666L, TimeUnit.SECONDS));
        }};
    }
}
