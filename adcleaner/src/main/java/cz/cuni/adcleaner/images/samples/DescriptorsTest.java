package cz.cuni.adcleaner.images.samples;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.lmu.ifi.dbs.jfeaturelib.features.*;
import de.lmu.ifi.dbs.jfeaturelib.shapeFeatures.*;

public class DescriptorsTest {

    public static void run() {

        final String dir = "E:\\DropBox\\DEV\\AdCleaner\\adcleaner\\~\\.adcelaner\\screens\\";

        ArrayList<File> files = new ArrayList<File>() {{
            add(new File(dir + "video1_out_10.png"));
            add(new File(dir + "video1_out_115.png"));
            add(new File(dir + "video1_out_135.png"));
            add(new File(dir + "video1_out_180.png"));
            add(new File(dir + "video1_out_170.png"));
            add(new File("img/cac1.jpg"));
            //add(new File("img/cac2.jpg"));
            //add(new File("img/dexter.jpg"));
        }};

        int length = 0;
        HashMap<String, ArrayList<FeatureDescriptor>> descriptors = new HashMap<>();
        for(File from : files){
            ArrayList<FeatureDescriptor> d = getAllDescriptors();
            length = d.size();

            descriptors.put(from.getName(), d);
        }


        for(int i = 0; i < length; ++i)
        {
            for(File from : files){
                for (File to : files){
                    if (from == to)
                        continue;

                    List<double[]> fromFeatures = runDescriptor(from, descriptors.get(from.getName()).get(i));
                    List<double[]> toFeatures = runDescriptor(to, descriptors.get(to.getName()).get(i));

                    double hamming = hammingSimilarity(fromFeatures, toFeatures);

                    if(hamming >= 0) {
                        System.out.print(String.format(
                            "%s:\n%s vs %s:",
                            descriptors.get(to.getName()).get(i).getClass().getName(),
                            from.getName(),
                            to.getName()
                            )
                        );

                        System.out.println(
                            String.format("%s\n", hamming)
                        );
                    }
                }
            }
        }
    }

    private static List<double[]> runDescriptor(File image, FeatureDescriptor descriptor) {
        descriptor.run(ImageProcessorFactory.newProcessor(image));
        return descriptor.getFeatures();
    }

    @SuppressWarnings("serial")
    private static ArrayList<FeatureDescriptor> getAllDescriptors() {
        return new ArrayList<FeatureDescriptor>() {
            {
                add(new CEDD());
                //add(new CentroidBoundaryDistance());
                add(new CentroidFeature());
                add(new Compactness());
                //add(new Eccentricity());
                add(new ExtremalPoints());
                add(new FCTH());
                add(new FuzzyHistogram());
                add(new Gabor());
                add(new Haralick());
                add(new JCD());
                add(new Moments());
                //add(new PHOG());
                //add(new PolygonEvolution());
                add(new Profiles());
                add(new SURF());
                //add(new Sift());
                add(new Tamura());
            }
                // Histogram e = new Histogram();
                // e.setProperties(LibProperties.get())
                // add(e);
        };
    }

    private static double hammingSimilarity(List<double[]> featuresCac1,
                                            List<double[]> featuresCac2) {
        int diff = 0;
        int sumAll = 0;

        if (featuresCac1.size() != featuresCac2.size())
            return -1;

        for (int i = 0; i < featuresCac1.size(); ++i) {
            sumAll += featuresCac1.get(i).length;
            double[] ds1 = featuresCac1.get(i);
            double[] ds2 = featuresCac2.get(i);

            if (ds1.length != ds2.length)
                return -1;

            for (int j = 0; j < ds1.length; ++j) {
                diff += doubleEquals(ds1[j], ds2[j]) ? 0 : 1;
            }
        }
        return diff / (double) sumAll;
    }

    public static boolean doubleEquals(double d1, double d2) {
        return Math.abs(d1 - d2) < 0.05;
    }
}
