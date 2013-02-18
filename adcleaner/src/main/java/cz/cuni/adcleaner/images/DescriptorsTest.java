package cz.cuni.adcleaner.images;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DescriptorsTest {

    public static void run() {

        final String dir = "E:\\DropBox\\DEV\\AdCleaner\\adcleaner\\~\\.adcelaner\\screens\\";
        ImageSimilarityComparer comparer = new ImageSimilarityComparer();

        ArrayList<File> files = new ArrayList<File>() {{
            add(new File(dir + "video1_10.png"));
            add(new File(dir + "video1_30.png"));
            add(new File(dir + "video1_60.png"));
            add(new File(dir + "video1_70.png"));
            add(new File(dir + "video1_100.png"));
            add(new File(dir + "video1_105.png"));
            add(new File(dir + "video1_110.png"));
            add(new File(dir + "video1_115.png"));
            //add(new File(dir + "video1_out_135.png"));
            //add(new File(dir + "video1_out_180.png"));
            //add(new File(dir + "video1_out_170.png"));
            //add(new File("img/cac1.jpg"));
            //add(new File("img/cac2.jpg"));
            //add(new File("img/cac3.jpg"));
            //add(new File("img/dexter1.jpg"));
        }};

        int length = 0;
        HashMap<String, HashMap<String, List<double[]>>> descriptors = new HashMap<>();
        for(File from : files){
            for (ImageDescriptor descriptor : comparer.getAllDescriptors())
            {
               String key = descriptor.getDescriptorName();

               if (!descriptors.containsKey(key))
                   descriptors.put(key, new HashMap<String, List<double[]>>());

                descriptors.get(key).put(from.getName(), descriptor.run(from));
            }
        }

        for(File from : files){
            for (File to : files){
                comparer.areImagesSimilar(from, to);
            }
        }
    }
}
