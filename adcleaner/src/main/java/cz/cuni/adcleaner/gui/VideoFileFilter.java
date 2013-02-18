package cz.cuni.adcleaner.gui;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
 *
 * @author Twister
 */
public class VideoFileFilter extends FileFilter
{
    /**
     * Filter the files and directories
     * 
     * @param f - File or directory to process
     * @return true let the file in list
     *         false file will not be in list
     */
    @Override
    public boolean accept(File f)
    {
        //we want all directories
        if (f.isDirectory())
        {
            return true;
        }

        //get the file name and extension (if it got extension)
        String name = f.getName();
        String extension = "";
        if (name.indexOf(".") != -1)
        {
            extension = name.substring(name.lastIndexOf("."), name.length());
            extension = extension.toLowerCase();
            extension = extension.replace(".", "");
        }

        if (extension != null)
        {
            if (extension.equals("mkv") ||
                extension.equals("avi") ||
                extension.equals("mp4") ||
                extension.equals("mov") ||
                extension.equals("wmv") ||
                extension.equals("flv") ||
                extension.equals("ogm") ||
                extension.equals("mpeg") ||
                extension.equals("mpg"))
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        return false;
    }

    /**
     * The name in File Type
     */
    @Override
    public String getDescription()
    {
        return "Video Files Only";
    }
}
