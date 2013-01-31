package cz.cuni.adcleaner.audio;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.buffer.CircularFifoBuffer;
import org.apache.commons.lang3.tuple.MutablePair;

import com.xuggle.mediatool.MediaToolAdapter;
import com.xuggle.mediatool.event.IAudioSamplesEvent;
import com.xuggle.mediatool.event.IOpenEvent;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.IAudioSamples.Format;

import cz.cuni.adcleaner.VideoSection;
import cz.cuni.adcleaner.descriptors.ScreenShotsManager;

public class VolumeElevationDetectorAdapter extends MediaToolAdapter {

    private VideoSection calibrationSection;
    private int[] calibrationSections;
    private final double maxElevation;
    private final int granularity;
    private List<VideoSection> louderSections;
    private ScreenShotsManager manager;
    private boolean calibrationEnd = false;
    private long expectedValue;
    private long variance;
    private CircularFifoBuffer buffer;
    private Long currentLouderSectionStart = null;
    private long videoDuration;

    public VolumeElevationDetectorAdapter(
        int numberOfContinuousSections, double maxElevation, int granularity,
        long calibrationIntervalLength, ScreenShotsManager manager) {
        this.maxElevation = maxElevation;
        this.granularity = granularity;
        this.manager = manager;

        calibrationSection = new VideoSection(0L, calibrationIntervalLength, TimeUnit.SECONDS);
        buffer = new CircularFifoBuffer(numberOfContinuousSections);
        int numberOfSections = (int) (calibrationSection.getDuration() / granularity) + 1;
        calibrationSections = new int[numberOfSections];
        louderSections = new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onAudioSamples(IAudioSamplesEvent event) {
        Long timeStamp = event.getTimeStamp(TimeUnit.SECONDS);
        IAudioSamples audioSamples = event.getAudioSamples();

        if (calibrationSection.isInSection(timeStamp)) {
            int index = (int) (timeStamp / granularity);
            Integer max = getMaxSample(audioSamples);
            if (calibrationSections[index] < max) {
                calibrationSections[index] = max;
            }
        } else if (!calibrationEnd) {
            finishCalibration();
        } else {
            MutablePair<Long, Integer> pair = getElementForTimestamp(timeStamp);
            Integer maxSample = getMaxSample(audioSamples);

            if (pair.getRight() < maxSample) {
                pair.setValue(maxSample);
            }
            if (currentLouderSectionStart == null) {
                if (isVolumeElevated()) {
                    currentLouderSectionStart = ((MutablePair<Long, Integer>)buffer.get()).getKey();
                    System.out.println("Louder section starts:" + currentLouderSectionStart);
                }
            } else {
                if (isVolumeLowered()) {
                    Long currentLouderSectionEnd = ((MutablePair<Long, Integer>)buffer.get()).getKey();
                    System.out.println("Louder section ends:" + currentLouderSectionEnd);

                    if (manager.compareScreensForShotBoundary(currentLouderSectionStart)) {
                        louderSections.add(new VideoSection(
                            currentLouderSectionStart,
                            currentLouderSectionEnd,
                            TimeUnit.SECONDS)
                        );
                    }

                    currentLouderSectionStart = null;
                }
            }
        }

        super.onAudioSamples(event);
    }

    @Override
    public void onOpen(IOpenEvent event) {
        videoDuration = event.getSource().getContainer().getDuration() / 1_000_000; // seconds
    }

    private MutablePair<Long, Integer> getElementForTimestamp(Long timeStamp) {
        MutablePair<Long, Integer> element = null;
        if (buffer.isEmpty()) {
            element = new MutablePair<Long, Integer>(timeStamp, Integer.MIN_VALUE);
            buffer.add(element);
        } else {
            MutablePair<Long, Integer> lastElementInBuffer = getLastAddedElement();
            Long sectionStart = timeStamp / (long) granularity;

            if (lastElementInBuffer.getKey() / granularity != sectionStart) {
                element = new MutablePair<Long, Integer>(timeStamp, Integer.MIN_VALUE);
                buffer.add(element);
            } else {
                element = lastElementInBuffer;
            }
        }

        return element;
    }

    @SuppressWarnings("unchecked")
    private MutablePair<Long, Integer> getLastAddedElement() {
        Object[] array = buffer.toArray();
        return (MutablePair<Long, Integer>) array[array.length - 1];
    }

    @SuppressWarnings("unchecked")
    private boolean isVolumeElevated() {
        for (Object object : buffer) {
            MutablePair<Long, Integer> pair = ((MutablePair<Long, Integer>) object);
            Integer volume = pair.getValue();

            if (volume - expectedValue < variance * maxElevation) {
                return false;
            }
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    private boolean isVolumeLowered() {
        for (Object object : buffer) {
            MutablePair<Long, Integer> pair = ((MutablePair<Long, Integer>) object);
            Integer volume = pair.getValue();

            if (volume - expectedValue > variance * maxElevation) {
                return false;
            }
        }

        return true;
    }

    private Integer getMaxSample(IAudioSamples audioSamples) {
        Integer max = Integer.MIN_VALUE;

        for (int i = 0; i < audioSamples.getNumSamples(); ++i) {
            int sample = audioSamples.getSample(i, 0, Format.FMT_S16);
            if (max < sample) {
                max = sample;
            }
        }

        return max;
    }

    private void finishCalibration() {
        calibrationEnd = true;
        expectedValue = 0;
        for (int i = 0; i < calibrationSections.length; i++) {
            expectedValue += calibrationSections[i] / calibrationSections.length;
        }

        variance = 0;
        for (int i = 0; i < calibrationSections.length; i++) {
            variance +=
                Math.abs(calibrationSections[i] - expectedValue) / calibrationSections.length;
        }
    }

    public List<VideoSection> getLouderSections() {
        if (currentLouderSectionStart != null) {
            louderSections
                .add(new VideoSection(currentLouderSectionStart, videoDuration, TimeUnit.SECONDS));
            currentLouderSectionStart = null;
        }

        return louderSections;
    }
}