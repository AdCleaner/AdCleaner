package cz.cuni.adcleaner;

import java.util.concurrent.TimeUnit;

public final class VideoSection implements Comparable<VideoSection> {

	private final Long start;
	private final Long end;
	private final TimeUnit timeuUnit;

	public VideoSection(Long start, Long end) {
		this(start, end, TimeUnit.MILLISECONDS);
	}

	public VideoSection(Long start, Long end, TimeUnit timeuUnit) {
		this.start = Math.min(start, end);
		this.end = Math.max(start, end);
		this.timeuUnit = timeuUnit;
	}
        
	public long getStart() {
		return start;
	}

	public long getEnd() {
		return end;
	}

	public long getDuration() {
		return end - start;
	}

	public TimeUnit getTimeuUnit() {
		return timeuUnit;
	}
	
	public boolean isInSection(long time) {
		return start <= time && time <= end;
	}

	@Override
	public int compareTo(VideoSection o) {
		int startCompare = this.start.compareTo(o.start);
		return startCompare != 0 ? startCompare : this.end.compareTo(o.end);
	}

	@Override
	public String toString() {
		return "VideoSection [start=" + start + ", end=" + end + ", timeuUnit=" + timeuUnit + "]";
	}
}
