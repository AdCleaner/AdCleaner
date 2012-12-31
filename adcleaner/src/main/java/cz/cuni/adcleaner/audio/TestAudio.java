package cz.cuni.adcleaner.audio;

import java.io.File;
import java.util.List;

import cz.cuni.adcleaner.VideoSection;

public class TestAudio {

	public static void main(String[] args) {
		File videoOut = new File("data/video1_out.mp4");
		File videoIn = new File("data/video1.mp4");
		List<VideoSection> randomize = new VolumeRandomizer(1).randomize(videoIn, videoOut);
		for (VideoSection videoSection : randomize) {
			System.out.println("Start: " + videoSection.getStart() + " End:" + videoSection.getEnd());
		}
		new AmplitudeGraph(videoOut).show();
		new AmplitudeMaxesGraph(videoOut, 5).show();

	}

}
