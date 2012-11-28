package cz.cuni.adcleaner.descriptors;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.lmu.ifi.dbs.jfeaturelib.features.CEDD;
import de.lmu.ifi.dbs.jfeaturelib.features.FCTH;
import de.lmu.ifi.dbs.jfeaturelib.features.FeatureDescriptor;
import de.lmu.ifi.dbs.jfeaturelib.features.FuzzyHistogram;
import de.lmu.ifi.dbs.jfeaturelib.features.Gabor;
import de.lmu.ifi.dbs.jfeaturelib.features.Haralick;
import de.lmu.ifi.dbs.jfeaturelib.features.JCD;
import de.lmu.ifi.dbs.jfeaturelib.features.Moments;
import de.lmu.ifi.dbs.jfeaturelib.features.SURF;
import de.lmu.ifi.dbs.jfeaturelib.features.Tamura;
import de.lmu.ifi.dbs.jfeaturelib.shapeFeatures.CentroidBoundaryDistance;
import de.lmu.ifi.dbs.jfeaturelib.shapeFeatures.CentroidFeature;
import de.lmu.ifi.dbs.jfeaturelib.shapeFeatures.Compactness;
import de.lmu.ifi.dbs.jfeaturelib.shapeFeatures.Eccentricity;
import de.lmu.ifi.dbs.jfeaturelib.shapeFeatures.ExtremalPoints;
import de.lmu.ifi.dbs.jfeaturelib.shapeFeatures.Profiles;

public class DescriptorsTest {
	public static void run() {
		List<FeatureDescriptor> descriptors1 = getAllDescriptors();
		List<FeatureDescriptor> descriptors2 = getAllDescriptors();
		List<FeatureDescriptor> descriptors3 = getAllDescriptors();

		System.out.println("CAC vs CAC        CAC vs Dexter");

		for (int i = 0; i < descriptors1.size(); ++i) {
			List<double[]> featuresCac1 = runDescriptor("img/cac1.jpg", descriptors1.get(i));
			List<double[]> featuresCac2 = runDescriptor("img/cac2.jpg", descriptors2.get(i));
			List<double[]> featuresCac3 = runDescriptor("img/dexter1.jpg", descriptors3.get(i));

			System.out.println(hammingSimilarity(featuresCac1, featuresCac2) + "         " + hammingSimilarity(featuresCac1, featuresCac3));
		}

	}

	private static List<double[]> runDescriptor(String path, FeatureDescriptor descriptor) {
		descriptor.run(ImageProcessorFactory.newProcessor(new File(path)));
		return descriptor.getFeatures();
	}

	@SuppressWarnings("serial")
	private static ArrayList<FeatureDescriptor> getAllDescriptors() {
		return new ArrayList<FeatureDescriptor>() {
			{
				add(new CEDD());
				add(new CentroidBoundaryDistance());
				add(new CentroidFeature());
				add(new Compactness());
				add(new Eccentricity());
				add(new ExtremalPoints());
				add(new FCTH());
				add(new FuzzyHistogram());
				add(new Gabor());
				add(new Haralick());
				// Histogram e = new Histogram();
				// e.setProperties(LibProperties.get())
				// add(e);
				add(new JCD());
				add(new Moments());
				// add(new PHOG());
				// add(new PolygonEvolution());
				// add(new Moments());
				add(new Profiles());
				add(new SURF());
				// add(new Sift());
				add(new Tamura());
			}
		};
	}

	private static double hammingSimilarity(List<double[]> featuresCac1, List<double[]> featuresCac2) {
		int diff = 0;
		int sumAll = 0;
		for (int i = 0; i < featuresCac1.size(); ++i) {
			sumAll += featuresCac1.get(i).length;
			double[] ds1 = featuresCac1.get(i);
			double[] ds2 = featuresCac2.get(i);
			for (int j = 0; j < ds1.length; ++j) {
				diff += doubleEquals(ds1[j], ds2[j]) ? 0 : 1;
			}
		}
		return diff / (double) sumAll;
	}

	public static boolean doubleEquals(double d1, double d2) {
		return Math.abs(d1 - d2) < 0.5;
	}
}
