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
    private JLabel label;
    private JTextField pathText;
    private JButton openButton;
    private JTextArea text;
    
    private JFileChooser fc;
    //Maybe public can be
    private File file;

    /**
     *
     * @return Returns selected file or null if file doesn't exist
     */
    public File getSelectedFile()
    {
        if (file.exists())
            return file;

        return null;
    }
    
    public App()
    {
        super(new BorderLayout());

        //Create text area for writing path
        pathText = new JTextField(30);
        //Too lazy to write another class:
        //I don't know how to get rid of this warning:
        //Leaking this in constructor
        pathText.addActionListener(this);
        label = new JLabel("File or stream URL:");
        
        //Create the open button. If image wanted use:
        //createImageIcon("path to image") in JButton
        openButton = new JButton("Browse");
        openButton.setToolTipText("Browse your PC for a video file.");

        //Too lazy to write another class:
        //I don't know how to get rid of this warning:
        //Leaking this in constructor
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

        // :-D
        pathText.setSize(pathText.getWidth(), pathText.getHeight() + 10);

        //For layout purposes, put the buttons in a separate panel
        JPanel buttonPanel = new JPanel(); //use FlowLayout
        buttonPanel.add(label);
        buttonPanel.add(pathText);
        buttonPanel.add(openButton);

        //Add the buttons and the text to this panel
        add(buttonPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);
    }
    
   
    @Override
    public void actionPerformed(ActionEvent e)
    {
        //Handle open button action
        if (e.getSource() == openButton)
        {
            int returnVal = fc.showOpenDialog(App.this);

            if (returnVal == JFileChooser.APPROVE_OPTION)
            {
                file = fc.getSelectedFile();
                //Here you got file in variable file and you can do what you
                //need with it
                pathText.setText(file.getAbsolutePath());
                text.append("Opening file: " + file.getAbsolutePath() + "." + newline);
                //text.append(String.format("Opening file: %s.%s", file.getAbsolutePath(), newline));
            }
            else
            {
                text.append("Open command cancelled by user." + newline);
            }
            text.setCaretPosition(text.getDocument().getLength());
        }
        else if (e.getSource() == pathText)
        {
            String source = pathText.getText();
            text.append("Processing: " + source + "." + newline);

            if (source.contains("http://"))
            {
                file = null;
                text.append("Connecting to URL: " + source + "."+ newline);
            }
            else
            {
                file = new File(source);
                if (file.exists())
                {
                    text.append("Opening file: " + file.getAbsolutePath() + "." + newline);
                }
                else
                {
                    file = null;
                    text.append("File " + source + " doesn't exist."+ newline);
                }
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

        //Display the window
        frame.pack();
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
