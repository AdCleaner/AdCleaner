package cz.cuni.adcleaner;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Runs aplication where you can choose file
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
    
    public App()
    {
        super(new BorderLayout());

        //Create text area for writing path
        pathText = new JTextField(30);
        //Too lazy to write another class:
        //I don't know how to get rid of this warning:
        //Leaking this in constructor
        pathText.addActionListener(this);
        label = new JLabel("Open:");
        
        //Create the open button. If image wanted use:
        //createImageIcon("path to image") in JButton
        openButton = new JButton("Open a File...");
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

        //For layout purposes, put the buttons in a separate panel
        JPanel buttonPanel = new JPanel(); //use FlowLayout
        buttonPanel.add(label);
        buttonPanel.add(pathText);
        buttonPanel.add(openButton);

        //Add the buttons and the text to this panel
        add(buttonPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);
    }
    
   
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
                text.append("Opening: " + file.getName() + "." + newline);
            }
            else
            {
                text.append("Open command cancelled by user." + newline);
            }
            text.setCaretPosition(text.getDocument().getLength());
        }
        else if (e.getSource() == pathText)
        {
            String fileName = pathText.getText();
            text.append("Opening: " + fileName + "." + newline);
            //TODO!!
            //Try if its a file and assign it to file
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
            public void run()
            {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE); 
                createAndShowGUI();
            }
        });
    }
}
