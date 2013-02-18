package cz.cuni.adcleaner.audio;

import java.io.File;
import java.util.List;

import cz.cuni.adcleaner.MediaToolApplier;
import cz.cuni.adcleaner.ads.VideoSection;
import cz.cuni.adcleaner.images.ScreenShotsManager;


public class VolumeAdDetector {

    private int granularity = 5;
    private int numberOfContinousSections = 5;
    private long callibrationIntervalLength = 20;
    private double maxElevation = 1.2;
    private final File video;

    public VolumeAdDetector(File video) {
        this.video = video;
    }

    public int getNumberOfContinousSections() {
        return numberOfContinousSections;
    }

    /**
     * How much must the new volume exceed the variation of volume in normal section. Should be mark
     * higher than 1.
     */
    public void setNumberOfContinousSections(int numberOfContinousSections) {
        this.numberOfContinousSections = numberOfContinousSections;
    }

    public double getMaxElevation() {
        return maxElevation;
    }

    public void setMaxElevation(double maxElevation) {
        this.maxElevation = maxElevation;
    }

    /**
     * In seconds
     */
    public int getGranularity() {
        return granularity;
    }

    /**
     * In seconds
     */
    public void setGranularity(int granularity) {
        this.granularity = granularity;
    }

    /**
     * In seconds
     */
    public long getCallibrationIntervalLength() {
        return callibrationIntervalLength;
    }

    /**
     * In seconds
     */
    public void setCallibrationIntervalLength(long callibrationIntervalLength) {
        this.callibrationIntervalLength = callibrationIntervalLength;
    }

    public List<VideoSection> run() {
        VolumeElevationDetectorAdapter mediaTool = new VolumeElevationDetectorAdapter(
            numberOfContinousSections,
            maxElevation,
            granularity,
            callibrationIntervalLength,
            new ScreenShotsManager(video.getName())
        );
        new MediaToolApplier(video).apply(mediaTool);

        return mediaTool.getLouderSections();
    }
}
