package cz.cuni.adcleaner;

import javax.swing.*;

/**
 * Class for application result. They are possible advertisements from scanning
 * video file
 */
public class PossibleAd
                    //implements ActionListener
{
    private int startingHour, startingMinute, startingSecond, startingFrame;
    private int endingHour, endingMinute, endingSecond, endingFrame;
    private boolean isAd, toCut;

    /**
     * number of frames in 1 second (must be bigger then 1)
     */
    private static int NUMBER_OF_FRAMES = 32;


    /**
     * Default constructor - for testing purposes
     */
    public PossibleAd()
    {
        this(0, 0, 0, 0, 0, 0, 0, 0, true, false);
    }

    /**
     * Constructor for result from app
     * @param startHour    Hour
     * @param startMinute  Minute
     * @param startSecond  Second
     * @param startFrame   Frame
     * @param endHour      Hour
     * @param endMinute    Minute
     * @param endSecond    Second
     * @param endFrame     Frame
     */
    public PossibleAd(int startHour,
                      int startMinute,
                      int startSecond,
                      int startFrame,
                      int endHour,
                      int endMinute,
                      int endSecond,
                      int endFrame,
                      boolean isAd,
                      boolean toCut)
    {
        setStartAndEndTime(startHour, startMinute, startSecond, startFrame,
                           endHour, endMinute, endSecond, endFrame);

        this.isAd = isAd;
        this.toCut = toCut;
    }
    
    public String getStartingTime()
    {
        return timeToString(true);
    }

    public String getEndingTime()
    {
        return timeToString(false);
    }

    public int getStartingHour() {
        return startingHour;
    }

    public int getStartingMinute() {
        return startingMinute;
    }

    public int getStartingSecond() {
        return startingSecond;
    }

    public int getStartingFrame() {
        return startingFrame;
    }

    public int getEndingHour() {
        return endingHour;
    }

    public int getEndingMinute() {
        return endingMinute;
    }

    public int getEndingSecond() {
        return endingSecond;
    }

    public int getEndingFrame() {
        return endingFrame;
    }

    public boolean getIsAd() {
        return isAd;
    }

    public boolean getToCut() {
        return toCut;
    }

    public void setStartingHour(int startingHour) {
        this.startingHour = startingHour;
    }

    public void setStartingMinute(int startingMinute) {
        this.startingMinute = startingMinute;
    }

    public void setStartingSecond(int startingSecond) {
        this.startingSecond = startingSecond;
    }

    public void setStartingFrame(int startingFrame) {
        this.startingFrame = startingFrame;
    }

    public void setEndingHour(int endingHour) {
        this.endingHour = endingHour;
    }

    public void setEndingMinute(int endingMinute) {
        this.endingMinute = endingMinute;
    }

    public void setEndingSecond(int endingSecond) {
        this.endingSecond = endingSecond;
    }

    public void setEndingFrame(int endingFrame) {
        this.endingFrame = endingFrame;
    }

    public void setIsAd(boolean ad) {
        isAd = ad;
    }

    public void setToCut(boolean toCut) {
        this.toCut = toCut;
    }

    /**
     * Public setter for most of private variables
     */
    public void setStartAndEndTimeProperly(int startHour,
                                           int startMinute,
                                           int startSecond,
                                           int startFrame,
                                           int endHour,
                                           int endMinute,
                                           int endSecond,
                                           int endFrame)
    {
        setStartAndEndTime(startHour, startMinute, startSecond, startFrame,
                           endHour, endMinute, endSecond, endFrame);
    }

    /**
     * Private setter for most of private variables
     */
    private void setStartAndEndTime(int startHour,
                                   int startMinute,
                                   int startSecond,
                                   int startFrame,
                                   int endHour,
                                   int endMinute,
                                   int endSecond,
                                   int endFrame)
    {
        //set start Frame
        if (startFrame < 0) { startFrame = 0; }
        if (startFrame > (NUMBER_OF_FRAMES - 1))
        {
            startSecond += startFrame / NUMBER_OF_FRAMES;
            startFrame = startFrame % NUMBER_OF_FRAMES;
        }
        startingFrame = startFrame;

        //set start Second
        if (startSecond < 0) { startSecond = 0; }
        if (startSecond > 59)
        {
            startMinute += startSecond / 60;
            startSecond = startSecond % 60;
        }
        startingSecond = startSecond;

        //set start Minute
        if (startMinute < 0) { startMinute = 0; }
        if (startMinute > 59)
        {
            startHour += startMinute / 60;
            startMinute = startMinute % 60;
        }
        startingMinute = startMinute;

        //set start Hour
        if (startHour < 0) { startHour = 0; }
        if (startHour > 100) { startHour = 99; }
        startingHour = startHour;


        //set end Frame
        if (endFrame < 0) { endFrame = 0; }
        if (endFrame > (NUMBER_OF_FRAMES - 1))
        {
            endSecond += endFrame / NUMBER_OF_FRAMES;
            endFrame = endFrame % NUMBER_OF_FRAMES;
        }
        endingFrame = endFrame;

        //set end Second
        if (endSecond < 0) { endSecond = 0; }
        if (endSecond > 59)
        {
            endMinute += endSecond / 60;
            endSecond = endSecond % 60;
        }
        endingSecond = endSecond;

        //set end Minute
        if (endMinute < 0) { endMinute = 0; }
        if (endMinute > 59)
        {
            endHour += endMinute / 60;
            endMinute = endMinute % 60;
        }
        endingMinute = endMinute;

        //set end Hour
        if (endHour < 0) { endHour = 0; }
        if (endHour > 100) { endHour = 99; }
        endingHour = endHour;
    }

    public String message()
    {
        String result = "";

        if (isAd)
        {
            result += timeToString(true);
            result += " - ";
            result += timeToString(false);
            result += " is an Advertisement";
            if (toCut)
            {
                result += " and it will be cut";
            }
            result += ".";
        }
        else
        {
            result += timeToString(true);
            result += " - ";
            result += timeToString(false);
            result += " is not an Advertisement.";
        }

        return result;
    }

    /**
     * Get time as String
     * 
     * @param start - if true then Start Time will be returned
     *              - if false then End Time will be returned
     * @return string time in hour:minute:second:frame form
     */
    private String timeToString(boolean start)
    {
        String result = "";

        if (start) //startTime
        {
            result += Integer.toString(startingHour);
            result += ":";
            result += Integer.toString(startingMinute);
            result += ":";
            result += Integer.toString(startingSecond);
            result += ":";
            result += Integer.toString(startingFrame);
        }
        else //endTime
        {
            result += Integer.toString(endingHour);
            result += ":";
            result += Integer.toString(endingMinute);
            result += ":";
            result += Integer.toString(endingSecond);
            result += ":";
            result += Integer.toString(endingFrame);
        }

        return result;
    }
}
