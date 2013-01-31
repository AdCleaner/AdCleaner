package cz.cuni.adcleaner.gui;

import javax.swing.*;

import cz.cuni.adcleaner.VideoSection;
import java.util.concurrent.TimeUnit;

/**
 * GUI wrapper for PossibleAd class
 */
public class VideoSectionPanel extends JPanel {
    private VideoSection videoSection;
    private JLabel labelStart, labelEnd;
    private JTextField textStart, textEnd;
    private JCheckBox cutFromVideo;

    private boolean toCut = true;

    private final String timeDelimiter = ":";

    //Constructor
    public VideoSectionPanel(VideoSection videosection)
    {
        this.videoSection = videosection;
    }

    //getter of VideoSection
    public VideoSection getVideoSection()
    {
        setInfoFromGUI();
        if (toCut)
        {
            return this.videoSection;
        }
        return null;
    }
    
    /**
     * Make a result info about advertisements
     *
     * @param panel - panel into which components are putted
     */
    public void putIntoPanel(JPanel panel)
    {
        //Text label for starting item
        labelStart = new JLabel("Start:");

        //Field where you can change start of advertisement
        textStart = new JTextField(11);
        textStart.setText(startTimeToString());
        textStart.setToolTipText("You can change time when ad begins.");

        //Text label for ending item
        labelEnd = new JLabel("End:");

        //Field where you can change end of advertisement
        textEnd = new JTextField(11);
        textEnd.setText(endTimeToString());
        textEnd.setToolTipText("You can change time when ad ends.");

        //CheckBox for selecting if it will be cut from video
        cutFromVideo = new JCheckBox("Cut it out?");
        cutFromVideo.setSelected(toCut);
        cutFromVideo.setToolTipText("Cut this advertisement from video file.");

        //put it all together
        panel.add(labelStart);
        panel.add(textStart);
        panel.add(labelEnd);
        panel.add(textEnd);
        panel.add(cutFromVideo);

        //update panel
        panel.invalidate();
    }

    /**
     * Make different log messages, depending on some settings
     * 
     * @return string message about proccessing video section
     */
    public String message()
    {
        setInfoFromGUI();
        String result = "";

        result += videoSection.toString();
        if (toCut)
        {
            result += " and it will be cut";
        }
        result += ".";

        return result;
    }

    /**
     * Sets the time from textFields
     * If not in right form the Program crashes!!!
     */
    private void setInfoFromGUI()
    {
        String t = textStart.getText();
        String[] startTime = t.split(timeDelimiter);

        t = textEnd.getText();
        String[] endTime = t.split(timeDelimiter);

        //in miliseconds
        //HOUR, MINUTE, SECOND, MILISECOND
        long start = Integer.parseInt(startTime[0]);
        start *= 60; //now in minutes
        start += Integer.parseInt(startTime[1]);
        start *= 60; // now in seconds
        start += Integer.parseInt(startTime[2]);
        if (videoSection.getTimeUnit() == TimeUnit.MILLISECONDS)
        {
            start *= 1000; // now in miliseconds
            start += Integer.parseInt(startTime[3]);
        }

        long end = Integer.parseInt(endTime[0]);
        end *= 60; //now in minutes
        end += Integer.parseInt(endTime[1]);
        end *= 60; // now in seconds
        end += Integer.parseInt(endTime[2]);
        if (videoSection.getTimeUnit() == TimeUnit.MILLISECONDS)
        {
            end *= 1000; // now in miliseconds
            end += Integer.parseInt(endTime[3]);
        }
        
        videoSection.setTime(start, end);

        //not working right now...
        toCut = cutFromVideo.isSelected();
    }

    /**
     * Get starting time of video section from VideoSection
     * 
     * @return time is in string format hour:minute:second:milisecond
     */
    private String startTimeToString()
    {
        String result = "";
        long start = this.videoSection.getStart();
        if (videoSection.getTimeUnit() == TimeUnit.SECONDS)
        {
            start *= 1000;
        }
        int milisecond = (int) (start % 1000);
        start /= 1000;
        int second = (int) (start % 60);
        start /= 60;
        int minute = (int) (start % 60);
        start /= 60;
        int hour = (int) start;

        result += twoDigitString(hour);
        result += timeDelimiter;
        result += twoDigitString(minute);
        result += timeDelimiter;
        result += twoDigitString(second);
        String milisecondString = Integer.toString(milisecond);
        if (milisecondString.length() == 1) 
        {
            milisecondString = "00" + milisecondString;
        }
        result += timeDelimiter;
        result += milisecondString;
        
        return result;
    }

    /**
     * Get ending time of video section from VideoSection
     * 
     * @return time is in string format hour:minute:second:milisecond
     */
    private String endTimeToString()
    {
        String result = "";
        long end = this.videoSection.getEnd();
        if (videoSection.getTimeUnit() == TimeUnit.SECONDS)
        {
            end *= 1000;
        }
        int milisecond = (int) (end % 1000);
        end /= 1000;
        int second = (int) (end % 60);
        end /= 60;
        int minute = (int) (end % 60);
        end /= 60;
        int hour = (int) end;

        result += twoDigitString(hour);
        result += timeDelimiter;
        result += twoDigitString(minute);
        result += timeDelimiter;
        result += twoDigitString(second);
        String milisecondString = Integer.toString(milisecond);
        if (milisecondString.length() == 1) 
        {
            milisecondString = "00" + milisecondString;
        }
        result += timeDelimiter;
        result += milisecondString;
        
        return result;
    }
    
    private String twoDigitString(int timeValue)
    {
        String result = Integer.toString(timeValue);

        if (result.length() == 1) 
        {
            result = "0" + result;
        }

        return result;
    }
}
