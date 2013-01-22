package cz.cuni.adcleaner;

import javax.swing.*;

import cz.cuni.adcleaner.descriptors.*;
import cz.cuni.adcleaner.gui.MainWindow;

/**
 * @author Ondřej Heřmánek (ondra.hermanek@gmail.com)
 */
public class Mediator 
{
    private static MainWindow window = new MainWindow();

    /**
     * Mediator function for program
     *
     * @param args program doesn't use them
     */
    public static void main( String[] args )
    {
        gui();

        //CaptureScreenToFile.run(args);

        //DescriptorsTest.run();

        //CaptureVideoFrame.run();
    }

    private static void gui() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                window.createAndShowGUI();
            }
        });
    }
}
