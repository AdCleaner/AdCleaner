package cz.cuni.adcleaner;

import de.lmu.ifi.dbs.jfeaturelib.Progress;
import de.lmu.ifi.dbs.jfeaturelib.features.AbstractFeatureDescriptor;
import java.util.logging.Logger;

import ij.process.ImageProcessor;

/**
 * This is a sample descriptor
 *
 * @author Franz
 */
public class SampleDescriptor extends AbstractFeatureDescriptor {

    static final Logger log = Logger.getLogger(SampleDescriptor.class.getName());

    @Override
    public String getDescription() {
        return "A sample descript that just returns some arbitrary numbers";
    }

    @Override
    public void run(ImageProcessor ip) {
        firePropertyChange(Progress.START);

        // if you want to extract multiple features for one image just add as much 
        // feature vectors as you want
        double[] featureVector = {22d, 03d, 1980d};
        addData(featureVector);

        firePropertyChange(Progress.END);
    }
}
