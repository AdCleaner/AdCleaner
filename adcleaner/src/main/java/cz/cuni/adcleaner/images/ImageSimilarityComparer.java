package cz.cuni.adcleaner.images;

import de.lmu.ifi.dbs.jfeaturelib.features.CEDD;
import de.lmu.ifi.dbs.jfeaturelib.features.FCTH;
import de.lmu.ifi.dbs.jfeaturelib.features.FuzzyHistogram;
import de.lmu.ifi.dbs.jfeaturelib.features.JCD;
import de.lmu.ifi.dbs.jfeaturelib.features.SURF;
import de.lmu.ifi.dbs.jfeaturelib.features.Tamura;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Compares two images for similarity. Uses 6 descriptors to decide about image similarity.
 *
 * @author Ondřej Heřmánek (ondra.hermanek@gmail.com)
 */
public class ImageSimilarityComparer {

    /**
     * Compares two images for similarity using multiple descriptors.
     * When descriptor evaluates the images to be similar, one "similarity hit" is counted.
     * There is a certain treshold for similarity hits to pronounce the images as similar.
     *
     * The descriptors returns a vector of features. Vectors of both images are compared using euclid distance.
     * Every descriptor has defined a certain treshold to consider image features similar.
     *
     * For example: when comparing images using 6 descriptors, 4 similar hits are required.
     *
     * @param image1
     * @param image2
     * @return
     */
    public boolean areImagesSimilar(File image1, File image2) {
        if ( image1 == null || image2 == null ||
             !image1.exists() || !image2.exists() ||
             image1.getAbsolutePath().equals(image2.getAbsolutePath()))
            return false;

        // Descriptors have to be loaded before every use (can't be reused for another image)
        List<ImageDescriptor> image1Descriptors = getAllDescriptors();
        List<ImageDescriptor> image2Descriptors = getAllDescriptors();

        int similarityHits = 0;
        for(int i = 0; i < image1Descriptors.size(); ++i){
            List<double[]> image1Features = image1Descriptors.get(i).run(image1);
            List<double[]> image2Features = image2Descriptors.get(i).run(image2);

            double euklid = euklidSimilarity(image1Features, image2Features);

            if (euklid < image1Descriptors.get(i).getTreshold())
            {
                System.out.println(image1Descriptors.get(i).getDescriptorName());
                similarityHits++;
            }
        }

        System.out.print(String.format(
            "%s and %s has similarity score: %s: ",
            image1.getName(),
            image2.getName(),
            similarityHits
            )
        );
        System.out.println();

        return similarityHits > 4;
    }

    /**
     * Computes euklid distance of two feature vectors. The distance is commutative.
     * When dimensions of vectors don't match, the missing values are supplied with 0s.
     *
     * @param vector1 First feature vector.
     * @param vector2 Second feature vector
     * @return Return euklid distance of supplied vectors
     */
    private static double euklidSimilarity(List<double[]> vector1, List<double[]> vector2) {
        double diff = 0;
        int listSize = Math.max(vector1.size(), vector2.size());
        for (int i = 0; i < listSize; ++i) {
            double[] ds1 = vector1.size() <= i ? new double[0] : vector1.get(i);
            double[] ds2 = vector2.size() <= i ? new double[0] : vector2.get(i);

            int arrayLength = Math.max(ds1.length, ds2.length);
            for (int j = 0; j < arrayLength; ++j) {
                double d1 = ds1.length <= j ? 0.0 : ds1[j];
                double d2 = ds2.length <= j ? 0.0 : ds2[j];

                diff += Math.pow(d1 - d2, 2);
            }
        }

        return Math.sqrt(diff);
    }

    /**
     * List of all vectors to describe images
     * @return List of all vectors to describe images
     */
    public List<ImageDescriptor> getAllDescriptors() {
        return new ArrayList<ImageDescriptor>() {
            {
                add(new ImageDescriptor(new CEDD(), 7.5));
                add(new ImageDescriptor(new FCTH(), 5.0));
                add(new ImageDescriptor(new FuzzyHistogram(), 250.0));
                add(new ImageDescriptor(new JCD(), 7.5));
                add(new ImageDescriptor(new SURF(), 3000.0));
                add(new ImageDescriptor(new Tamura(), 100.0));
            }
        };
    }
}
