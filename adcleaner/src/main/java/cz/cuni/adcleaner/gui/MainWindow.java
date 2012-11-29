package cz.cuni.adcleaner.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;

import cz.cuni.adcleaner.PossibleAd;

/**
 * Runs application where you can choose file
 *
 */
public class MainWindow extends JPanel
                             implements ActionListener
{
    static private final String newline = "\n";
    private String URL = "";
    private File file;

    private JLabel label;
    private JTextField pathText;
    private JButton openButton, startButton, stopButton, processButton;
    private JTextArea text;
    private JFileChooser fc;
    private JPanel resultBox;

    private ArrayList<PossibleAdPanel> results = new ArrayList<PossibleAdPanel>();


    /**
     * The selected file is return, but user must check if it's a video file
     * 
     * @return Returns selected file or null if file doesn't exist
     */
    public File getSelectedFile()
    {
        if (file.exists())
        {
            return file;
        }

        return null;
    }

    /**
     * User must check if URL exist and is valid
     * 
     * @return Returns string URL or "" if URL is empty
     */
    public String getURL()
    {
        return URL;
    }

    /**
     * You put here results (setter)
     * 
     * @param Ad - Possible advertisement
     */
    public void addAdvertisement(PossibleAdPanel Ad)
    {
        results.add(Ad);
    }

    /**
     * Constructor - creates window and its layout
     */
    public MainWindow()
    {
        super(new BorderLayout());

        //Create text area for writing path
        pathText = new JTextField(30);
        //Too lazy to write another class:
        pathText.addActionListener(this);
        label = new JLabel("File or stream URL:");

        //Create the open button. If image wanted use:
        //createImageIcon("path to image") in JButton
        openButton = new JButton("Browse");
        openButton.setToolTipText("Browse your PC for a video file.");

        //Too lazy to write another class:
        openButton.addActionListener(this);

        //Create a file chooser
        fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setCurrentDirectory(new File("C:/"));

        //Create the start button.
        startButton = new JButton("Start");
        startButton.setToolTipText("Starts search for advertisments in video file.");
        startButton.addActionListener(this);

        //Create the stop button.
        stopButton = new JButton("Stop");
        stopButton.setToolTipText("Stops search for advertisments in video file.");
        stopButton.addActionListener(this);

        //Create the process button.
        processButton = new JButton("Process");
        processButton.setToolTipText("Cuts advertisements from video.");
        processButton.addActionListener(this);

        //Create the text area so action listeners can refer to it
        //15 lines, 50 chars:
        text = new JTextArea(15,50);
        text.setMargin(new Insets(5,5,5,5));
        text.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(text);

        //For layout purposes, put the buttons in a separate panel
        JPanel navigationBar = new JPanel(); //use FlowLayout
        navigationBar.add(label);
        navigationBar.add(pathText);
        navigationBar.add(openButton);
        navigationBar.add(startButton);
        navigationBar.add(stopButton);
        navigationBar.add(processButton);

        //For result layout purpose
        resultBox = new JPanel();
        resultBox.setLayout(new BoxLayout(resultBox, BoxLayout.Y_AXIS));

        //Add the buttons and the text to this panel
        this.add(navigationBar, BorderLayout.PAGE_START);
        this.add(logScrollPane, BorderLayout.CENTER);
        this.add(resultBox, BorderLayout.PAGE_END);

        startButton.setEnabled(false);
        stopButton.setEnabled(false);
        processButton.setEnabled(false);
    }

    /**
     * Process the actions from buttons
     * 
     * @param e - action event like mouse click or enter
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        //Handle open button action
        if (e.getSource() == openButton)
        {
            clearOldData();
            openButtonAction();
        }
        else if (e.getSource() == pathText)
        {
            clearOldData();
            pathTextEnterPressed();
        }
        else if (e.getSource() == startButton)
        {
            startButtonAction();
        }
        else if (e.getSource() == stopButton)
        {
            stopButtonAction();
        }
        else if (e.getSource() == processButton)
        {
            processButtonAction();
        }
    }

    /**
     * Clears the URL and file for new selection
     */
    private void clearOldData()
    {
        //Cleaning variables for new input (URL or file)
        URL = "";
        file = null;
    }

    /**
     * Action performed when open button is pressed
     */
    private void openButtonAction()
    {
        //show Dialog window
        int returnVal = fc.showOpenDialog(MainWindow.this);

        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            //Get result of dialog window choosing
            file = fc.getSelectedFile();

            if (file.exists())
            {
                text.append(String.format("Opening file: %s.%s", file.getAbsolutePath(), newline));
                //Put the path to file into text field
                pathText.setText(file.getAbsolutePath());
            }
            else
            {
                text.append(String.format("File %s doesn't exist.%s", file.getAbsolutePath(), newline));
                //File doesn't exist so set file to null
                file = null;
            }
        }
        else
        {
            //File chooser was exited
            text.append(String.format("Open command cancelled by user.%s", newline));
        }

        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        processButton.setEnabled(false);
    }

    /**
     * Action performed when enter is pressed (while pathText has focus)
     */
    private void pathTextEnterPressed()
    {
        //Processing string from text field
        String source = pathText.getText();
        text.append(String.format("Processing: %s.%s", source, newline));

        if (source.contains("http://"))
        {
            //It's URL
            URL = source;
            text.append(String.format("Connecting to URL: %s.%s", URL, newline));
        }
        else
        {
            //It's file
            file = new File(source);
            if (file.exists())
            {
                text.append(String.format("Opening file: %s.%s", file.getAbsolutePath(), newline));
            }
            else
            {
                text.append(String.format("File %s doesn't exist.%s", source, newline));
                file = null;
            }
        }
    }

    /**
     * Action performed when start button is pressed
     */
    private void startButtonAction()
    {
        if (file != null) //file contains existing file
        {
            //temporary behaviour
            //---------------START--------------------------
            text.append(String.format("Scanning file: %s.%s", file.getName(), newline));
            if (file.getName().contains(".txt"))
            {
                text.append(String.format("Adding buttons.%s", newline));

                makeTestingTimes();

                //add buttons
                showTimes();
            }
            else
            {
                text.append(String.format("It's not a txt file.%s", newline));
            }
            //---------------END---------------------------
        }

        if (!URL.equals("")) //URL contains http://
        {
            //validation of URL is needed (also if URL exists)
            text.append(String.format("Downloading stream: %s.%s", URL, newline));
        }

        if((file == null) && URL.equals(""))
        {
            text.append(String.format("No file or URL selected.%s", newline));
        }

        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        processButton.setEnabled(true);
    }

    /**
     * Action performed when stop button is pressed
     */
    private void stopButtonAction()
    {
        //Kill procesing thread.

        text.append(String.format("Stopping current action.%s", newline));

        //remove buttons
        if (!results.isEmpty())
        {
            text.append(String.format("Removing buttons.%s", newline));
            results.clear();
            showTimes();
        }

        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        processButton.setEnabled(false);
    }

    /**
     * Action performed when process button is pressed
     */
    private void processButtonAction()
    {
        if (results.isEmpty())
        {
            text.append(String.format("Nothing to process.%s", newline));
            return;
        }

        text.append(String.format("--------------------------%s", newline));

        for(PossibleAdPanel panel : results)
        {
            text.append(String.format("%s.%s", panel.getAd().message(), newline));
        }

        results.clear();

        text.append(String.format("--------------------------%s", newline));

        showTimes();

        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        processButton.setEnabled(false);
    }

    /**
     * Temporary function for filling array list with fake advertisements
     */
    private void makeTestingTimes()
    {
        PossibleAd pointer;

        //00:01:20 - 00:01:50
        pointer = new PossibleAd(0, 1, 20, 0, 0, 1, 50, 0, true, false);
        addAdvertisement(new PossibleAdPanel(pointer));

        //00:13:02 - 00:14:14
        pointer = new PossibleAd(0, 13, 2, 0, 0, 14, 14, 0, false, true);
        addAdvertisement(new PossibleAdPanel(pointer));

        //00:20:20 - 00:22:22
        pointer = new PossibleAd(0, 20, 20, 0, 0, 22, 22, 0, true, false);
        addAdvertisement(new PossibleAdPanel(pointer));
    }

    /**
     * Function displays results in bottom of window.
     * (Data is taken from array list result)
     */
    private void showTimes()
    {
        //removing old data
        resultBox.removeAll();

        //prepare data for showing
        JPanel resultLine;
        for (int i = 0; i < results.size(); ++i)
        {
            resultLine = new JPanel();
            results.get(i).putIntoPanel(resultLine);
            resultBox.add(resultLine);
        }
        resultBox.invalidate();

        //make them visible
        this.revalidate();
        this.repaint();
    }
}
