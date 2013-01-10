package cz.cuni.adcleaner.audio;

import java.awt.image.BufferedImage;
import java.io.File;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.MediaToolAdapter;
import com.xuggle.mediatool.ToolFactory;

public class MediaToolApplier {
        private final IMediaReader reader;

	public MediaToolApplier(File inputVideo) {
                this.reader = ToolFactory.makeReader(inputVideo.toString());
                reader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
	}

	public void apply(MediaToolAdapter mediaTool) {
		apply(null, mediaTool);
	}

	public void apply(File outputVideo, MediaToolAdapter mediaTool) {
		reader.addListener(mediaTool);

                // Leave
		if (outputVideo != null) {
			IMediaWriter writer = ToolFactory.makeWriter(outputVideo.toString(), reader);
			mediaTool.addListener(writer);
		}
	}

        public  void run()
        {
            while (reader.readPacket() == null)
          			;
        }
}
