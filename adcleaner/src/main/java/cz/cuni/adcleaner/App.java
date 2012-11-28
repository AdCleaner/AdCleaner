package cz.cuni.adcleaner;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;

/**
 * Runs application where you can choose file
 *
 */
public class App extends JPanel
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

    private ArrayList<PossibleAd> results = new ArrayList<PossibleAd>();


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
    public void addAdvertisement(PossibleAd Ad)
    {
        results.add(Ad);
    }

    /**
     * Constructor - creates window and its layout
     */
    public App()
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
        JPanel buttonPanel = new JPanel(); //use FlowLayout
        buttonPanel.add(label);
        buttonPanel.add(pathText);
        buttonPanel.add(openButton);
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(processButton);

        //For result layout purpos
        resultBox = new JPanel();
        resultBox.setLayout(new BoxLayout(resultBox, BoxLayout.Y_AXIS));

        //Add the buttons and the text to this panel
        this.add(buttonPanel, BorderLayout.PAGE_START);
        this.add(logScrollPane, BorderLayout.CENTER);
        this.add(resultBox, BorderLayout.PAGE_END);
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
        int returnVal = fc.showOpenDialog(App.this);

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
    }

    /**
     * Action performed when process button is pressed
     */
    private void processButtonAction()
    {
        if (results.isEmpty())
        {
            text.append(String.format("Nothing to process.%s", newline));
            return ;
        }

        text.append(String.format("--------------------------%s", newline));

        while (!results.isEmpty())
        {
            PossibleAd a = results.get(0);

            a.setInfoFromGUI();
            text.append(String.format("%s.%s", a.message(), newline));

            results.remove(0);
        }

        text.append(String.format("--------------------------%s", newline));

        showTimes();
    }

    /**
     * Temporary function for filling arraylist with fake advertisements
     */
    private void makeTestingTimes()
    {
        PossibleAd pointer;

        //00:01:20 - 00:01:50
        pointer = new PossibleAd(0, 1, 20, 0, 0, 1, 50, 0);
        addAdvertisement(pointer);

        //00:13:02 - 00:14:14
        pointer = new PossibleAd(0, 13, 2, 0, 0, 14, 14, 0);
        addAdvertisement(pointer);

        //00:20:20 - 00:22:22
        pointer = new PossibleAd(0, 20, 20, 0, 0, 22, 22, 0);
        addAdvertisement(pointer);
    }

    /**
     * Function displays results in bottom of window.
     * (Data is taken from arraylist result)
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

    /**
     * Create JFrame and sets it
     */
    private static void createAndShowGUI()
    {
        //Create and set up the window
        JFrame frame = new JFrame("AdCleaner");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window
        frame.add(new App());

        //Size the frame
        frame.pack();
        //Display the window
        frame.setVisible(true);
    }

    /**
     * Main function for program
     * 
     * @param args program doesn't use them
     */
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE); 
                createAndShowGUI();
            }
        });
    }
}
