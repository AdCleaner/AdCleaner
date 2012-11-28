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
    private JLabel labelStart, labelEnd;
    private JTextField textStart, textEnd;
    private JButton play;
    private JCheckBox advertisement, cutFromVideo;

    //TODO!! get for isAd and toCut
    //TODO!! get and set for start and end values

    /**
     * Default constructor - for testing purposes
     */
    public PossibleAd()
    {
        setStartAndEndTime(0, 0, 0, 0, 0, 0, 0, 0);

        isAd = true;
        toCut = false; //crop
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
                      int endFrame)
    {
        setStartAndEndTime(startHour, startMinute, startSecond, startFrame,
                           endHour, endMinute, endSecond, endFrame);

        isAd = true; //Default option
        toCut = false; //Default option
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
        if (startFrame > 31)
        {
            startSecond += startFrame / 32;
            startFrame = startFrame % 32;
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
        if (endFrame > 31)
        {
            endSecond += endFrame / 32;
            endFrame = endFrame % 32;
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
    public String timeToString(boolean start)
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

    /**
     * Sets the time from textFields
     * If not in right form the Program crashes!!!
     */
    public void setInfoFromGUI()
    {
        String t = textStart.getText();
        String[] startTime = t.split(":");
        startingHour = Integer.parseInt(startTime[0]);
        startingMinute = Integer.parseInt(startTime[1]);
        startingSecond = Integer.parseInt(startTime[2]);
        startingFrame = Integer.parseInt(startTime[3]);

        t = textEnd.getText();
        String[] endTime = t.split(":");
        endingHour = Integer.parseInt(endTime[0]);
        endingMinute = Integer.parseInt(endTime[1]);
        endingSecond = Integer.parseInt(endTime[2]);
        endingFrame = Integer.parseInt(endTime[3]);

        isAd = advertisement.isSelected();
        toCut = cutFromVideo.isSelected();
    }

    /**
     * Make a result info about advertisements
     * 
     * @param panel - panel into which components are putted
     */
    public void putIntoPanel(JPanel panel)
    {
        //Text label for starting tiem
        labelStart = new JLabel("Start:");

        //Field where you can change start of advertisement
        textStart = new JTextField(11);
        textStart.setText(timeToString(true));
        textStart.setToolTipText("You can change time when ad begins.");

        //Text label for ending tiem
        labelEnd = new JLabel("End:");

        //Field where you can change end of advertisement
        textEnd = new JTextField(11);
        textEnd.setText(timeToString(false));
        textEnd.setToolTipText("You can change time when ad ends.");

        //Button for playing video from start time
        play = new JButton("Play");
        play.setToolTipText("Play video from start time - NOT IMPLEMENTED");
        //TODO implement Action Listener
        //play.addActionListener(this);

        //CheckBox for selecting if it is advertisement
        advertisement = new JCheckBox("Is it an Ad?");
        advertisement.setSelected(isAd);
        advertisement.setToolTipText("If it is not an advertisement uncheck.");

        //CheckBox for selecting if it will be cut from video
        cutFromVideo = new JCheckBox("Crop");
        cutFromVideo.setSelected(toCut);
        cutFromVideo.setToolTipText("Cut this advertisement from video file.");

        //put it all together
        panel.add(labelStart);
        panel.add(textStart);
        panel.add(labelEnd);
        panel.add(textEnd);
        panel.add(play);
        panel.add(advertisement);
        panel.add(cutFromVideo);

        //update panel
        panel.invalidate();
    }
}
