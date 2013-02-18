package cz.cuni.adcleaner.images;

import de.lmu.ifi.dbs.jfeaturelib.features.FeatureDescriptor;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import cz.cuni.adcleaner.images.samples.ImageProcessorFactory;

/**
 * Wraps FeatureDescriptor and its similarity treshold.
 * The treshold is used when comparing two feature vectors to decide about image similarity.
 *
 * @author Ondřej Heřmánek (ondra.hermanek@gmail.com)
 */
public class ImageDescriptor {
    private FeatureDescriptor descriptor;
    private double treshold;

    public ImageDescriptor(FeatureDescriptor descriptor, double treshold)
    {
        this.descriptor = descriptor;
        this.treshold = treshold;
    }

    /**
     * Runs descriptor for given image.
     *
     * @param image Image to desribe
     * @return Returns feature vector.
     */
    public List<double[]> run(File image) {
        if (!image.exists())
            return new LinkedList<double[]>();

        descriptor.run(ImageProcessorFactory.newProcessor(image));
        return descriptor.getFeatures();
    }

    public double getTreshold() { return this.treshold; }

    public String getDescriptorName() { return descriptor.getClass().getName(); }
}
