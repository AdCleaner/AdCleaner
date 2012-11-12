package cz.cuni.adcleaner;

import java.io.File;

import ij.ImagePlus;
import ij.io.Opener;
import ij.process.ImageProcessor;

public abstract class ImageProcessorFactory {

	private ImageProcessorFactory() {
		//
	}

	public static ImageProcessor newProcessor(File file) {
		ImageProcessor ip = null;
		if (file != null) {
			ImagePlus iplus = new Opener().openImage(file.getAbsolutePath());
			ip = iplus.getProcessor();
		}
		return ip;
	}
}
