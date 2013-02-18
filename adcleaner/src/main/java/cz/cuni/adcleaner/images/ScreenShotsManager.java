package cz.cuni.adcleaner.images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;

import javax.imageio.ImageIO;

/**
 * @author Ondřej Heřmánek (ondra.hermanek@gmail.com)
 */
public class ScreenShotsManager {

    private final String outputDirectory = "~/.adcelaner/screens";

    private String filePrefix;

    private LinkedList<ScreenShot> screenshots = new LinkedList<ScreenShot>();

    private ImageSimilarityComparer comparer = new ImageSimilarityComparer();

    /**
     * Creates ScreenShotsManager with specified prefix that will be used to distinguish screenshots
     * @param videoFileName The name of the video file
     */
    public  ScreenShotsManager(String videoFileName)
    {
        filePrefix = videoFileName.substring(0, videoFileName.indexOf("."));

        File outputDir = new File(outputDirectory);
        if (!outputDir.exists())
            outputDir.mkdirs();
    }

    /**
     * Saves given screenshot to the temporary hard-drive location.
     * @param screenshot Screenshot to save
     * @return Returns true on success, false otherwise
     */
    public boolean saveScreenshot(BufferedImage screenshot, long seconds) {
        try {
            String outputFilename = String.format(
                "%s/%s_%s.png",
                outputDirectory,
                filePrefix,
                seconds
            );

            // Dump file to disc
            ImageIO.write(screenshot, "png", new File(outputFilename));

            // Remember screenshot position
            screenshots.add(new ScreenShot(seconds, outputFilename));

            // Return file name on success
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This methods determines whether around specified second an shot boundary appeared.
     *
     * The two closest older screenshots are found and compared. If they "differ" to much,
     * probably and shot boundary found.
     *
     * @param seconds The seconds marker of media file
     * @return Returns true if shot boundary PROBABLY encountered
     */
    public boolean compareScreensForShotBoundary(long seconds)
    {
        ScreenShot last = null;
        ScreenShot lastButOne = null;

        // Screenshots are create in ascending order of appearance (logical)
        // So we just need to browse the list and remember two last screenshots
        // until the screenshot mark is higher than requested
        for(ScreenShot scr : screenshots)
        {
            // The "younger" screenshot found, quit searching
            if (scr.mark > seconds)
                break;

            // Delete passed screenshot (won't be asking for it again)
            if(lastButOne != null)
                lastButOne.deleteScreenshot();

            lastButOne = last;
            last = scr;
        }

        // Not enough screens yet - definitely a shot boundary found
        if (lastButOne == null)
            return true;

        // Compare those two screenshot for similarity
        return !compareScreenshotsForSimilarity(last.screenShotPath, lastButOne.screenShotPath);
    }

    public void cleanUp()
    {
        for(ScreenShot screen : screenshots)
        {
            screen.deleteScreenshot();
        }
    }

    /**
     * Compares two screenshots given by paths for similarity
     * @param screenshot1
     * @param screenshot2
     * @return Returns true if those two screenshots are quite similar
     */
    private boolean compareScreenshotsForSimilarity(String screenshot1, String screenshot2)
    {
        return comparer.areImagesSimilar(new File(screenshot1), new File(screenshot2));
    }

    private class ScreenShot
    {
        public long mark;
        public String screenShotPath;
        private boolean deleted = false;

        public ScreenShot(long second, String path)
        {
            this.mark = second;
            this.screenShotPath = path;
        }

        public void deleteScreenshot()
        {
            if (!deleted)
            {
                new File(screenShotPath).delete();
                deleted = true;
            }
        }
    }
}
