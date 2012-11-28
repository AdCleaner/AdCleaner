package cz.cuni.adcleaner;

import javax.swing.*;

import cz.cuni.adcleaner.descriptors.*;
import cz.cuni.adcleaner.gui.MainWindow;

/**
 * @author Ondřej Heřmánek (ondra.hermanek@gmail.com)
 */
public class Main {

    /**
     * Create JFrame and sets it
     */
    private static void createAndShowGUI()
    {
        //Create and set up the window
        JFrame frame = new JFrame("AdCleaner");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window
        frame.add(new MainWindow());

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
        //gui();

        //CaptureScreenToFile.run(args);

        DescriptorsTest.run();
    }

    private static void gui() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();
            }
        });
    }
}
