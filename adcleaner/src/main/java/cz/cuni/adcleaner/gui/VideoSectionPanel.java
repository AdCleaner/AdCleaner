package cz.cuni.adcleaner.gui;

import javax.swing.*;

import cz.cuni.adcleaner.ads.VideoSection;
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
    private String errorMessage = "";
    /**
     * classicTime
     * true - 00:00:00:000
     * false - (0h) (0m) (0s) (0ms)
     */
    private final boolean classicTime = false; 

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

    //getter for error message
    public String errorMessage()
    {
        return this.errorMessage;
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

        if (toCut)
        {
            result += startTimeToString();
            result += " - ";
            result += endTimeToString();
            result += " - this secion will be in result.";
        }
        else
        {
            result += "NOT IN RESULT - ";
            result += startTimeToString();
            result += " - ";
            result += endTimeToString();
            result += " - NOT IN RESULT";
        }

        return result;
    }

    /**
     * Checks the start and end time format
     * 
     * @return true - valid
     *         false - unvalid
     */
    public boolean validateTimes()
    {
        errorMessage = "";
        if (classicTime)
        {
            if (!validateClassicTime(textStart.getText()))
            {
                errorMessage = "Start time: ";
                errorMessage += textStart.getText();
                errorMessage += " is in wrong format. Use: ";
                errorMessage += "00:00:00:000 - 01:59:59:999 format.";
                return false;
            }
            if (!validateClassicTime(textEnd.getText()))
            {
                errorMessage = "End time: ";
                errorMessage += textEnd.getText();
                errorMessage += " is in wrong format. Use: ";
                errorMessage += "00:00:00:000 - 01:59:59:999 format.";
                return false;
            }
        }
        else
        {
            if (!validateShortTime(textStart.getText()))
            {
                errorMessage = "Start time: ";
                errorMessage += textStart.getText();
                errorMessage += " is in wrong format. Use: ";
                errorMessage += "\"(0h) (0m) (0s) (0ms)\" - ";
                errorMessage += "\"(1h) (59m) (59s) (999ms)\" format.";
                return false;
            }
            if (!validateShortTime(textEnd.getText()))
            {
                errorMessage = "End time: ";
                errorMessage += textEnd.getText();
                errorMessage += " is in wrong format. Use: ";
                errorMessage += "\"(0h) (0m) (0s) (0ms)\" - ";
                errorMessage += "\"(1h) (59m) (59s) (999ms)\" format.";
                return false;
            }
        }
        return true;
    }

    private boolean validateClassicTime(String timeString)
    {
        timeString = timeString.toLowerCase();

        //format
        if (!timeString.matches("\\d{2}:\\d{2}:\\d{2}:\\d{3}"))
        {
            return false;
        }
        //data
        String[] time = timeString.split(timeDelimiter);
        if (Integer.parseInt(time[0]) > 1) //HOURS
        {
            return false;
        }
        if (Integer.parseInt(time[1]) > 59) //MINUTES
        {
            return false;
        }
        if (Integer.parseInt(time[2]) > 59) //SECONDS
        {
            return false;
        }

        return true;
    }

    private boolean validateShortTime(String timeString)
    {
        timeString = timeString.toLowerCase();

        //format
        if (!timeString.matches("(\\d+h)? ?(\\d+m)? ?(\\d+s)? ?(\\d+ms)?"))
        {
            return false;
        }
        //data
        String[] time = timeString.split(" ");
        for (int i = 0; i < time.length; ++i)
        {
            if (!validateValue(time[i]))
            {
                return false;
            }
        }

        return true;
    }

    private boolean validateValue(String value)
    {
        value = value.toLowerCase();

        if (value.lastIndexOf("ms") != -1)
        {
            if (Integer.parseInt(value.replaceAll("ms", "")) > 999)
            {
                return false;
            }
        }
        else if (value.lastIndexOf("s") != -1)
        {
            if (Integer.parseInt(value.replaceAll("s", "")) > 59)
            {
                return false;
            }
        }
        else if (value.lastIndexOf("m") != -1)
        {
            if (Integer.parseInt(value.replaceAll("m", "")) > 59)
            {
                return false;
            }
        }
        else if (value.lastIndexOf("h") != -1)
        {
            if (Integer.parseInt(value.replaceAll("h", "")) > 1)
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Sets the time from textFields
     * If not in right form the Program crashes!!!
     */
    private void setInfoFromGUI()
    {
        long start = parseTime(textStart.getText());
        long end = parseTime(textEnd.getText());
        
        videoSection.setTime(start, end);

        //not working right now...
        toCut = cutFromVideo.isSelected();
    }
    
    private long parseTime(String timeString)
    {
        long result = 0;
        if (classicTime)
        {
            String[] time = timeString.split(timeDelimiter);
            //HOUR, MINUTE, SECOND, MILISECOND
            result = Integer.parseInt(time[0]);
            result *= 60; //now in minutes
            result += Integer.parseInt(time[1]);
            result *= 60; // now in seconds
            result += Integer.parseInt(time[2]);
            if (videoSection.getTimeUnit() == TimeUnit.MILLISECONDS)
            {
                result *= 1000; // now in miliseconds
                result += Integer.parseInt(time[3]);
            }
        }
        else
        {
            String[] time = timeString.split(" ");
            for (int i = 0; i < time.length; ++i)
            {
                result += parseValue(time[i]);
            }
            if (videoSection.getTimeUnit() == TimeUnit.SECONDS)
            {
                result /= 1000;
            }
        }
        return result;
    }

    /**
     * Takes string and converts it into long (miliseconds from time units)
     * 
     * @param value - String with format: "5h" or "04m" or "666ms"
     * @return long - time in miliseconds
     */
    private long parseValue(String value)
    {
        if (value.lastIndexOf("ms") != -1)
        {
            return Integer.parseInt(value.replaceAll("ms", ""));
        }
        if (value.lastIndexOf("s") != -1)
        {
            return 1000*Integer.parseInt(value.replaceAll("s", ""));
        }
        if (value.lastIndexOf("m") != -1)
        {
            return 60*1000*Integer.parseInt(value.replaceAll("m", ""));
        }
        if (value.lastIndexOf("h") != -1)
        {
            return 60*60*1000*Integer.parseInt(value.replaceAll("h", ""));
        }
        return 0;
    }

    /**
     * Get starting time of video section from VideoSection
     * 
     * @return time is in string format hour:minute:second:milisecond
     */
    private String startTimeToString()
    {
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

        return timeToString(hour, minute, second, milisecond);
    }
    
    /**
     * Get ending time of video section from VideoSection
     * 
     * @return time is in string format hour:minute:second:milisecond
     */
    private String endTimeToString()
    {
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

        return timeToString(hour, minute, second, milisecond);
    }
    
    private String timeToString(int hour, int minute, int second, int milisecond)
    {
        String result = "";
        
        if (classicTime)
        {
            result += twoDigitString(hour);
            result += timeDelimiter;
            result += twoDigitString(minute);
            result += timeDelimiter;
            result += twoDigitString(second);
            result += timeDelimiter;
            result += threeDigitString(milisecond);
        }
        else
        {
            if (hour > 0)
            {
                result += Integer.toString(hour) + "h ";
            }
            if (minute > 0)
            {
                result += Integer.toString(minute) + "m ";
            }
            if (second > 0)
            {
                result += Integer.toString(second) + "s ";
            }
            result += Integer.toString(milisecond) + "ms";
        }
        
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

    private String threeDigitString(int timeValue)
    {
        String result = Integer.toString(timeValue);

        if (result.length() == 1) 
        {
            result = "00" + result;
        }
        else if (result.length() == 2)
        {
            result = "0" + result;
        }

        return result;
    }
}
