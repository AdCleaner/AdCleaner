package cz.cuni.adcleaner.utilities;

import java.io.File;

import com.xuggle.xuggler.IContainer;

public final class VideoUtils {

	private VideoUtils() {
		//
	}

	/**
	 * 
	 * @param video
	 * @return Video duration in micro seconds
	 */
	public static long getVideoDuration(File video) {
		IContainer container = null;
		try {
			container = IContainer.make();
			if (container.open(video.getAbsolutePath(), IContainer.Type.READ, null) < 0) {
				throw new IllegalArgumentException("Could not open file: " + video.toString());
			}
			return container.getDuration();
		} finally {
			if (container != null) {
				container.close();
			}
		}
	}
}
