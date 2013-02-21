package cz.cuni.adcleaner.utilities;

import java.io.File;

import com.xuggle.xuggler.IContainer;

public final class VideoUtils {

	private VideoUtils() {
		//
	}

	/**
	 * Calculates video duration in seconds.
         *
	 * @param video
	 * @return Video duration in seconds
	 */
	public static long getVideoDurationInSeconds(File video) {
		IContainer container = null;
		try {
			container = IContainer.make();
			if (container.open(video.getAbsolutePath(), IContainer.Type.READ, null) < 0) {
				throw new IllegalArgumentException("Could not open file: " + video.toString());
			}
			return container.getDuration() / 1000000; // Seconds
		} finally {
			if (container != null) {
				container.close();
			}
		}
	}
}
