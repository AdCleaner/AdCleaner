package cz.cuni.adcleaner.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;

import javax.swing.*;

import cz.cuni.adcleaner.IMediator;
import cz.cuni.adcleaner.IWindow;
import cz.cuni.adcleaner.ads.VideoSection;

/**
 * Runs application where you can choose file
 *
 */
public class MainWindow implements ActionListener, IWindow
{
    private IMediator mediator;
    private State currentState = State.INITIAL;
    private final String newline = "\n";
    private final double MAX_HEIGHT = 600.0;
    private String url = "";
    private java.util.List<VideoSection> videoSections;
    private ArrayList<VideoSectionPanel> results = new ArrayList<>();

    private File currentFile;
    private JLabel label;
    private JProgressBar progressBar;
    private JTextField pathText;
    private JButton openButton, processButton, stopButton, cutButton;
    private JTextArea text;
    private VideoFileDialog videoFileDialog = new VideoFileDialog();
    private JPanel resultBox, mainWindow;
    private JFrame frame;

    @Override
    public void registerMediator(IMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public void processResults(java.util.List<VideoSection> videoSections) {
        processingDone(videoSections);
    }

    @Override
    public void setProgress(int progress) {
        progressBar.setValue(progress);
    }

    @Override
    public void processActionFailed(String reason) {
        text.append(reason);
        setStateInitial();
    }

    @Override
    public void cuttingAdsFromVideoFinished(File outputFile) {
        if (outputFile == null)
        {
            text.append(
                String.format(
                    "Unable to generate output video file: %s.%s",
                    outputFile.getAbsolutePath(),
                    newline));
        }
        else {
            text.append(
                String.format(
                    "Output video generated: %s.%s",
                    outputFile.getAbsolutePath(),
                    newline));
        }

        //remove VideoSectionPanels for listing
        results.clear();

        //remove VideoSection as result of finding advertisements
        videoSections.clear();

        text.append(String.format("--------------------------%s", newline));

        //revalidate, repaint and remove result lines
        showTimes();

        setStateInitial();
    }

    /**
     * Constructor - creates window and its layout
     */
    public MainWindow() {
        // Create main Window
        mainWindow = new JPanel();
        mainWindow.setLayout(new BorderLayout());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Handle open button action
        if (e.getSource() == openButton)
        {
            openButtonAction();
        }
        else if ((e.getSource() == pathText))
        {
            if (currentState != State.PROCESSING)
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
     * Create JFrame and show it
     */
    public void createAndShowGUI() {
        // Create and set up the window
        frame = new JFrame("AdCleaner");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(600, 600));

        // Add content to the window
        mainWindow.add(createWindowContent());

        frame.add(mainWindow);
        frame.pack();
        frame.setVisible(true);

        //sets starting state of application
        this.setStateInitial();
    }

    private void setStateInitial() {
        this.currentState = State.INITIAL;
        openButton.setEnabled(true);
        processButton.setEnabled(false);
        stopButton.setEnabled(false);
        cutButton.setEnabled(false);

        currentFile = null;
        pathText.setText("");
        progressBar.setValue(0);
    }

    private void setStatePrepared() {
        this.currentState = State.PREPARED;
        openButton.setEnabled(true); //you can still choose another file
        processButton.setEnabled(true);
        stopButton.setEnabled(false);
        cutButton.setEnabled(false);

        progressBar.setValue(0);
    }

    private void setStateProcessing() {
        this.currentState = State.PROCESSING;
        openButton.setEnabled(false);
        processButton.setEnabled(false);
        stopButton.setEnabled(true);
        cutButton.setEnabled(false);
        //Path is disabled in method: actionPerformed(ActionEvent e)

        progressBar.setValue(0);
    }

    private void setStateProcessed() {
        this.currentState = State.PROCESSED;
        openButton.setEnabled(false);
        processButton.setEnabled(false);
        stopButton.setEnabled(false);
        cutButton.setEnabled(true);
    }

    private void setStateFinishing() {
        this.currentState = State.FINISHING;
        openButton.setEnabled(false);
        processButton.setEnabled(false);
        stopButton.setEnabled(true);
        cutButton.setEnabled(false);

        progressBar.setValue(0);
    }
    
    /**
     * Creates contents of window
     */
    private Component createWindowContent() {
        JPanel window = new JPanel();
        window.setLayout(new BorderLayout());

        //Create text area for writing path
        pathText = new JTextField(30);
        pathText.setToolTipText("Stream selection under construction");
        pathText.setEnabled(false);
        //TODO: URL
        // pathText.addActionListener(this); //Too lazy to write another class:

        label = new JLabel("File or stream URL:");

        //Create the open button. If image wanted use:
        //createImageIcon("path to image") in JButton
        openButton = new JButton("Browse");
        openButton.setToolTipText("Browse your PC for a video file.");

        //Too lazy to write another class:
        openButton.addActionListener(this);

        //Create the start button.
        processButton = new JButton("Process");
        processButton.setToolTipText("Starts searching for advertisements in video file.");
        processButton.addActionListener(this);

        //Create the stop button.
        stopButton = new JButton("Stop");
        stopButton.setToolTipText("Stops searching for advertisements in video file.");
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

        progressBar = new JProgressBar(0, 100);
        progressBar.setToolTipText("Progress");

        JPanel pageStartPanel = new JPanel();
        pageStartPanel.setLayout(new BorderLayout());
        pageStartPanel.add(navigationBar, BorderLayout.PAGE_START);
        pageStartPanel.add(progressBar, BorderLayout.PAGE_END);

        //Add the buttons and the text to window panel
        window.add(pageStartPanel, BorderLayout.PAGE_START);
        window.add(logScrollPane, BorderLayout.CENTER);
        window.add(resultBox, BorderLayout.PAGE_END);

        // Makes it scrollable
        return new JScrollPane(window);
    }

    /**
     * Clears the URL and file for new selection
     */
    private void clearOldData() {
        //Cleaning variables for new input (URL or file)
        url = "";
        currentFile =  null;
        videoFileDialog.cancelSelection();
    }

    /**
     * Action performed when open button is pressed
     */
    private void openButtonAction() {
        clearOldData();

        // Show VideoFileDialog and wait for selected file
        if (videoFileDialog.showOpenDialog(mainWindow) == JFileChooser.APPROVE_OPTION)
        {
            // Get result of dialog window choosing
            if ((currentFile = videoFileDialog.getSelectedFile()).exists())
            {
                text.append(String.format("Selected file: %s.%s", currentFile.getAbsolutePath(), newline));

                // Put the path to file into text field
                pathText.setText(currentFile.getAbsolutePath());
                this.setStatePrepared();
            }
            else
            {
                text.append(String.format("File %s doesn't exist.%s", currentFile.getAbsolutePath(), newline));

                // File doesn't exist so set file to null
                this.setStateInitial();
            }
        }
        else
        {
            // File chooser was canceled
            text.append(String.format("Open command cancelled by user.%s", newline));
            this.setStateInitial();
        }
    }

    /**
     * TODO: this functionality is disabled now
     * Action performed when enter is pressed (while pathText has focus)
     */
    private void pathTextEnterPressed() {
        clearOldData();

        //Processing string from text field
        String source = pathText.getText();
        text.append(String.format("Processing: %s.%s", source, newline));

        if (source.contains("http://") || source.contains("http://"))
        {
            //It's URL
            url = source;
            text.append(String.format("URL: %s selected.%s", url, newline));
            //verification of URL during PROCESSING state
            this.setStatePrepared();
        }
        else
        {
            //It's file
            currentFile = new File(source);
            if (currentFile.exists())
            {
                text.append(String.format("Opening file: %s.%s", currentFile.getAbsolutePath(), newline));
                this.setStatePrepared();
            }
            else
            {
                text.append(String.format("File %s doesn't exist.%s", source, newline));
                currentFile = null;
                this.setStateInitial();
            }
        }
    }

    /**
     * Action performed when start button is pressed
     */
    private void processButtonAction() {
        if (currentFile != null) {
            if (!currentFile.exists()) {
                text.append("Can't process not-existing file: " + currentFile.getAbsolutePath());
                return;
            }

            text.append(String.format(
                "Started to process file: %s.%s",
                currentFile.getAbsolutePath(),
                newline)
            );

            mediator.startVideoProcessing(currentFile);
            this.setStateProcessing();
            return;
        }

        if (!url.equals("")) //URL contains http://
        {
            //validation of URL is needed (also if URL exists)
            text.append(String.format("URL is not supported right now."));
            this.setStateInitial();
        }
    }

    /**
     * Action performed when stop button is pressed
     */
    private void stopButtonAction() {
        text.append(String.format("Stopping current action.%s", newline));
        // Remember the state before stopping
        State state = currentState;

        // Set to initial
        this.setStateInitial();

        //remove buttons
        if (!results.isEmpty())
        {
            text.append(String.format("Removing results.%s", newline));
            results.clear();
            showTimes();
        }

        if (state == State.PROCESSING) {
            if (!this.mediator.stopVideoProcessing())
            {
                text.append(String.format("Failed to stop the processing.%s", newline));
                return;
            }

            text.append(String.format("Video processing stopped.%s", newline));
        }

        if (state == State.FINISHING) {
            if (!this.mediator.stopCuttingAds())
            {
                text.append(String.format("Failed to stop the ads cutting.%s", newline));
                return;
            }

            text.append(String.format("Ads cutting stopped.%s", newline));
        }
    }

    /**
     * Action performed when process button is pressed
     */
    private void cutButtonAction()
    {
        // No ads found
        if (results.isEmpty())
        {
            text.append(String.format("No ads found.%s", newline));
            setStateInitial();

            return;
        }

        // Validate specified cut times
        if (!validateCutTimes())
        {
            return;
        }

        // Print cut info and disable panels
        for(VideoSectionPanel panel : results)
        {
            text.append(String.format("%s%s", panel.getInfo(), newline));
            panel.setEnabled(false);
        }

        // Initialize progress and start generating output video
        mediator.startCuttingAds(videoSections, currentFile);
        setStateFinishing();
    }
    
    private boolean validateCutTimes()
    {
        for(VideoSectionPanel panel : results)
        {
            if (!panel.validateTimes())
            {
                //warning window
                JOptionPane.showMessageDialog(frame,
                    panel.errorMessage(),
                    "Warning - wrong time format",
                    JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        
        return true;
    }

    /**
     * Method to displays results in bottom of window.
     * (Data is taken from array list result)
     */
    private void showTimes()
    {
        // Removing old data
        resultBox.removeAll();

        // Prepare data for showing
        JPanel resultLine;
        for (int i = 0; i < results.size(); ++i)
        {
            resultLine = new JPanel();
            results.get(i).putIntoPanel(resultLine);
            resultBox.add(resultLine);
        }
        
        frame.pack();
        resultBox.invalidate();

        //make them visible
        mainWindow.revalidate();
        mainWindow.repaint();
    }

    /**
     * Function for transforming results into panels
     * VideoSection => VideoSectionPanel
     */
    private void showResults()
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

    /**
     * Method, which is called when are all advertisements found in file
     */
    private void processingDone(java.util.List<VideoSection> videoResults)
    {
        //save results
        videoSections = videoResults;
        
        //change results
        showResults();

        //set state to done
        setStateProcessed();
    }
}
