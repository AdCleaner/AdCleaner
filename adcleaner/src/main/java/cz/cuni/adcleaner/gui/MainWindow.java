package cz.cuni.adcleaner.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;

import cz.cuni.adcleaner.IMediator;
import cz.cuni.adcleaner.IWindow;
import cz.cuni.adcleaner.VideoSection;

/**
 * Runs application where you can choose file
 *
 */
public class MainWindow implements ActionListener, IWindow
{
    private IMediator mediator;
    private State ActiveState = State.INITIAL;
    static private final String newline = "\n";
    private String URL = "";
    private File file;
    java.util.List<VideoSection> videoSections;
    private ArrayList<VideoSectionPanel> results = new ArrayList<>();

    private JLabel label;
    private JTextField pathText;
    private JButton openButton, processButton, stopButton, cutButton;
    private JTextArea text;
    private JFileChooser fc;
    private JPanel resultBox;
    private JPanel mainWindow;

    /**
     * The selected file is return, but user must check if it's a video file
     * 
     * @return Returns selected file or null if file doesn't exist
     */
    private File getSelectedFile()
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
     * @param ad - Possible advertisement
     */
    public void addAdvertisement(VideoSectionPanel ad)
    {
        results.add(ad);
    }

    /**
     * 4 methods for setting up the state of application (also enables/disables
     * buttons)
     */
    private void setStateInitial()
    {
        this.ActiveState = State.INITIAL;
        openButton.setEnabled(true);
        processButton.setEnabled(false);
        stopButton.setEnabled(false);
        cutButton.setEnabled(false);
    }

    private void setStatePrepared()
    {
        this.ActiveState = State.PREPARED;
        openButton.setEnabled(true); //you can still choose another file
        processButton.setEnabled(true);
        stopButton.setEnabled(false);
        cutButton.setEnabled(false);
    }

    private void setStateProcessing()
    {
        this.ActiveState = State.PROCESSING;
        openButton.setEnabled(false);
        processButton.setEnabled(false);
        stopButton.setEnabled(true);
        cutButton.setEnabled(false);
        //Path is disabled in method: actionPerformed(ActionEvent e)
    }

    private void setStateFinish()
    {
        this.ActiveState = State.FINISH;
        openButton.setEnabled(true);
        openButton.setEnabled(false);
        processButton.setEnabled(false);
        stopButton.setEnabled(false);
        cutButton.setEnabled(true);
    }

    /**
     * Constructor - creates window and its layout
     */
    public MainWindow()
    {
        //create main Window
        mainWindow = new JPanel();
        mainWindow.setLayout(new BorderLayout());

        //Creates and fills the with content
        JPanel window = new JPanel();
        window.setLayout(new BorderLayout());
        this.MainWindowContent(window);

        //makes it scrollable
        JScrollPane mainWindowContent = new JScrollPane(window);

        //put it into window
        mainWindow.add(mainWindowContent);

        //sets starting state of application
        this.setStateInitial();
    }
    
    /**
     * Creates contents of window
     */
    private void MainWindowContent(JPanel window)
    {
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
        processButton = new JButton("Process");
        processButton.setToolTipText("Starts search for advertisments in video file.");
        processButton.addActionListener(this);

        //Create the stop button.
        stopButton = new JButton("Stop");
        stopButton.setToolTipText("Stops search for advertisments in video file.");
        stopButton.addActionListener(this);

        //Create the process button.
        cutButton = new JButton("Cut");
        cutButton.setToolTipText("Cuts advertisements from video.");
        cutButton.addActionListener(this);

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
        navigationBar.add(processButton);
        navigationBar.add(stopButton);
        navigationBar.add(cutButton);

        //For result layout purpose
        resultBox = new JPanel();
        resultBox.setLayout(new BoxLayout(resultBox, BoxLayout.Y_AXIS));

        //Add the buttons and the text to window panel
        window.add(navigationBar, BorderLayout.PAGE_START);
        window.add(logScrollPane, BorderLayout.CENTER);
        window.add(resultBox, BorderLayout.PAGE_END);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        //Handle open button action
        if (e.getSource() == openButton)
        {
            openButtonAction();
        }
        else if ((e.getSource() == pathText) &&
                 (ActiveState != State.PROCESSING))
        {
            pathTextEnterPressed();
        }
        else if (e.getSource() == processButton)
        {
            processButtonAction();
        }
        else if (e.getSource() == stopButton)
        {
            stopButtonAction();
        }
        else if (e.getSource() == cutButton)
        {
            cutButtonAction();
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
        clearOldData();
        //show Dialog window
        int returnVal = fc.showOpenDialog(mainWindow);

        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            //Get result of dialog window choosing
            file = fc.getSelectedFile();

            if (file.exists())
            {
                text.append(String.format("Opening file: %s.%s", file.getAbsolutePath(), newline));
                //Put the path to file into text field
                pathText.setText(file.getAbsolutePath());
                this.setStatePrepared();
            }
            else
            {
                text.append(String.format("File %s doesn't exist.%s", file.getAbsolutePath(), newline));
                //File doesn't exist so set file to null
                file = null;
                this.setStateInitial();
            }
        }
        else
        {
            //File chooser was exited
            text.append(String.format("Open command cancelled by user.%s", newline));
            this.setStateInitial();
        }
    }

    /**
     * Action performed when enter is pressed (while pathText has focus)
     */
    private void pathTextEnterPressed()
    {
        clearOldData();
        //Processing string from text field
        String source = pathText.getText();
        text.append(String.format("Processing: %s.%s", source, newline));

        if (source.contains("http://"))
        {
            //It's URL
            URL = source;
            text.append(String.format("URL: %s selected.%s", URL, newline));
            //verification of URL during PROCESSING state
            this.setStatePrepared();
        }
        else
        {
            //It's file
            file = new File(source);
            if (file.exists())
            {
                text.append(String.format("Opening file: %s.%s", file.getAbsolutePath(), newline));
                this.setStatePrepared();
            }
            else
            {
                text.append(String.format("File %s doesn't exist.%s", source, newline));
                file = null;
                this.setStateInitial();
            }
        }
    }

    /**
     * Action performed when start button is pressed
     */
    private void processButtonAction()
    {
        this.setStateProcessing();
        File selectedFile = getSelectedFile();

        if (selectedFile != null) //file contains existing file
        {
            // TODO: threading!!!
            //HERE will be calling of method from mediator to start processing
            this.processingDone(mediator.processVideo(selectedFile));
        }

        if (!URL.equals("")) //URL contains http://
        {
            //validation of URL is needed (also if URL exists)
            text.append(String.format("URL is not supported right now."));
        }
        this.setStateFinish();
    }

    /**
     * Action performed when stop button is pressed
     */
    private void stopButtonAction()
    {
        // TODO: Kill processing thread - Stream processing/File Processing
        //HERE will be calling of method from mediator to stop processing

        text.append(String.format("Stopping current action.%s", newline));

        this.setStatePrepared();

        //remove buttons
        if (!results.isEmpty())
        {
            text.append(String.format("Removing buttons.%s", newline));
            results.clear();
            showTimes();
        }
    }

    /**
     * Action performed when process button is pressed
     */
    private void cutButtonAction()
    {
        this.setStatePrepared();
        //TODO maybe enabled Stop button if processing takes too long

        if (results.isEmpty())
        {
            text.append(String.format("Nothing to process.%s", newline));
            return;
        }

        text.append(String.format("--------------------------%s", newline));

        for(VideoSectionPanel panel : results)
        {
            text.append(String.format("%s.%s", panel.message(), newline));
        }

        //remove VideoSectionPanels for listing
        results.clear();
        
        //remove VideoSection as result of finding advertisements
        videoSections.clear();

        text.append(String.format("--------------------------%s", newline));

        //revalidate, repaint and remove result lines
        showTimes();
    }

    /**
     * Method to displays results in bottom of window.
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
        mainWindow.revalidate();
        mainWindow.repaint();
    }

    /**
     * Create JFrame and sets it
     */
    public void createAndShowGUI()
    {
        //Create and set up the window
        JFrame frame = new JFrame("AdCleaner");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window
        frame.add(mainWindow);

        //Size the frame
        frame.pack();
        //Display the window
        frame.setVisible(true);
    }

    /**
     * Function for transforming results into panels
     * VideoSection => VideoSectionPanel
     */
    private void prepareResultsForShowing()
    {
        //delete old data
        results.clear();

        //make panels from result
        for (int i = 0; i < videoSections.size(); ++i)
        {
            results.add(new VideoSectionPanel(videoSections.get(i)));
        }

        //show them
        showTimes();
    }

    @Override
    public void registerMediator(IMediator mediator) {
        this.mediator = mediator;
    }

    /**
     * Method, which is called when are all advertisements found in file
     */
    public void processingDone(java.util.List<VideoSection> results)
    {
        //save results
        videoSections = results;

        //change results
        prepareResultsForShowing();

        //set state to done
        setStateFinish();
    }
}
