package cz.cuni.adcleaner.gui;

import javax.swing.*;

import cz.cuni.adcleaner.PossibleAd;

/**
 * GUI wrapper for PossibleAd class
 *
 * @author Ondřej Heřmánek (ondra.hermanek@gmail.com)
 */
public class PossibleAdPanel extends JPanel {
    private PossibleAd possibleAd;
    private JLabel labelStart, labelEnd;
    private JTextField textStart, textEnd;
    private JButton play;
    private JCheckBox advertisement, cutFromVideo;

    public PossibleAdPanel(PossibleAd possibleAd)
    {
        this.possibleAd = possibleAd;
    }

    public PossibleAd getAd()
    {
        setInfoFromGUI();
        return possibleAd;
    }

    /**
     * Sets the time from textFields
     * If not in right form the Program crashes!!!
     */
    public void setInfoFromGUI()
    {
        String t = textStart.getText();
        String[] startTime = t.split(":");
        possibleAd.setStartingHour(Integer.parseInt(startTime[0]));
        possibleAd.setStartingMinute(Integer.parseInt(startTime[1]));
        possibleAd.setStartingSecond(Integer.parseInt(startTime[2]));
        possibleAd.setStartingFrame(Integer.parseInt(startTime[3]));

        t = textEnd.getText();
        String[] endTime = t.split(":");
        possibleAd.setEndingHour(Integer.parseInt(endTime[0]));
        possibleAd.setEndingMinute(Integer.parseInt(endTime[1]));
        possibleAd.setEndingSecond(Integer.parseInt(endTime[2]));
        possibleAd.setEndingFrame(Integer.parseInt(endTime[3]));

        possibleAd.setIsAd(advertisement.isSelected());
        possibleAd.setToCut(cutFromVideo.isSelected());
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
        textStart.setText(possibleAd.getStartingTime());
        textStart.setToolTipText("You can change time when ad begins.");

        //Text label for ending item
        labelEnd = new JLabel("End:");

        //Field where you can change end of advertisement
        textEnd = new JTextField(11);
        textEnd.setText(possibleAd.getEndingTime());
        textEnd.setToolTipText("You can change time when ad ends.");

        //Button for playing video from start time
        play = new JButton("Play");
        play.setToolTipText("Play video from start time - NOT IMPLEMENTED");
        //TODO implement Action Listener
        //play.addActionListener(this);

        //CheckBox for selecting if it is advertisement
        advertisement = new JCheckBox("Is it an Ad?");
        advertisement.setSelected(possibleAd.getIsAd());
        advertisement.setToolTipText("If it is not an advertisement uncheck.");

        //CheckBox for selecting if it will be cut from video
        cutFromVideo = new JCheckBox("Crop");
        cutFromVideo.setSelected(possibleAd.getToCut());
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
