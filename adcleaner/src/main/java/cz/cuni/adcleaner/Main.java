package cz.cuni.adcleaner;

import javax.swing.*;

import cz.cuni.adcleaner.descriptors.CaptureScreenToFile;
import cz.cuni.adcleaner.descriptors.DescriptorsTest;
import cz.cuni.adcleaner.gui.App;

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

        CaptureScreenToFile.run(args);

        DescriptorsTest.run();
    }
}
