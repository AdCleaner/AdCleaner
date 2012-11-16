package cz.cuni.adcleaner;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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
    private JButton openButton;
    private JTextArea text;
    private JFileChooser fc;
   

    /**
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

        //Add the buttons and the text to this panel
        add(buttonPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Process the action of choosing a file
     * 
     * @param e - action event like mouse click or enter
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        //Cleaning variables for new input (URL or file)
        URL = "";
        file = null;
        
        //Handle open button action
        if (e.getSource() == openButton)
        {
            openButtonAction();
        }
        else if (e.getSource() == pathText)
        {
            pathTextEnterPressed();
        }
        
        //if file != null then file contains existing file
        
        //if URL != "" then URL contains http://
        //validation of URL is needed (also if URL exists)
    }
    
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
