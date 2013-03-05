package cz.cuni.adcleaner.audio;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cz.cuni.adcleaner.ads.VideoSection;

public class TestAudio {

	public static void main(String[] args) throws FileNotFoundException {
		File videoOut = new File("data/video1_out.mp4");
		File videoIn = new File("data/video1.mp4");
		
		// Vytvorim si testovacie video
//		List<VideoSection> louderSections = new ArrayList<>();
//		louderSections.add(new VideoSection(45L, 120L, TimeUnit.SECONDS));
//		louderSections.add(new VideoSection(160L, 200L, TimeUnit.SECONDS));
//		new VolumeRandomizer(louderSections, 0.2).randomize(videoIn, videoOut);
//		System.out.println("LouderSections:");
//		for (VideoSection videoSection : louderSections) {
//			System.out.println("Start: " + videoSection.getStart() + " End:" + videoSection.getEnd());
//		}
//
//		// Vykreslim grafy
		//new AmplitudeGraph(videoOut).show();
		//new AmplitudeMaxesGraph(videoOut, 5).show();
		
		// Detekcia hlasnejsich casti
		VolumeAdDetector volumeAdDetector = new VolumeAdDetector(videoOut);
		List<VideoSection> louderSectionsFound = volumeAdDetector.run();
		System.out.println("Video sections found:");
		for (VideoSection videoSection : louderSectionsFound) {
			System.out.println(videoSection.toString());
		}
	}
}
