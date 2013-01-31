package cz.cuni.adcleaner;

import javax.swing.*;

import cz.cuni.adcleaner.audio.TestAudio;
import cz.cuni.adcleaner.descriptors.CaptureScreenToFile;
import cz.cuni.adcleaner.descriptors.CaptureVideoFrame;
import cz.cuni.adcleaner.descriptors.DescriptorsTest;
import cz.cuni.adcleaner.gui.MainWindow;

/**
 * @author Ondřej Heřmánek (ondra.hermanek@gmail.com)
 */
public class Main {

    /**
     * Entry point to the AdCleaner application.
     *
     * Initializes GUI, and Mediator for communication between GUI and Video processors
     *
     * @param args program doesn't use them
     */
    public static void main( String[] args )
    {
        try
        {
            IMediator mediator = new Mediator();
            final MainWindow window = new MainWindow();
            IAdFinder adFinder = new AdFinder();

            mediator.registerWindow(window);
            mediator.registerAdFinder(adFinder);

            ///*
            // Show MainWindow
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    //Turn off metal's use of bold fonts
                    UIManager.put("swing.boldMetal", Boolean.FALSE);
                    window.createAndShowGUI();
                }
            });
            //*/

            //DescriptorsTest.run();

            /*
            CaptureScreenToFile.run(args);



            CaptureVideoFrame.run();

            TestAudio.run(args);
            */

        }
        catch (Exception ex)
        {
            System.out.println("Unhandled exception: " + ex.getMessage());
            System.out.println("Stack trace:\n");
            ex.printStackTrace();
        }
    }
}
