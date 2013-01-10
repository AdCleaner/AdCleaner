package cz.cuni.adcleaner.audio;

import java.awt.image.BufferedImage;
import java.io.File;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.MediaToolAdapter;
import com.xuggle.mediatool.ToolFactory;

public class MediaToolApplyer {

	private final File inputVideo;

	public MediaToolApplyer(File inputVideo) {
		this.inputVideo = inputVideo;
	}

	public void apply(MediaToolAdapter mediaTool) {
		apply(null, mediaTool);
	}

	public void apply(File outputVideo, MediaToolAdapter mediaTool) {
		IMediaReader reader = ToolFactory.makeReader(inputVideo.toString());
		reader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
		reader.addListener(mediaTool);
		if (outputVideo != null) {
			IMediaWriter writer = ToolFactory.makeWriter(outputVideo.toString(), reader);
			mediaTool.addListener(writer);
		}
		while (reader.readPacket() == null)
			;
	}
}
