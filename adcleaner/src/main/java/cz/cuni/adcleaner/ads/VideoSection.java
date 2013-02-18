package cz.cuni.adcleaner.ads;

import java.util.concurrent.TimeUnit;

public class VideoSection implements Comparable<VideoSection> {

	private Long start;
	private Long end;
	private TimeUnit timeUnit;
        private boolean cut;

	public VideoSection(Long start, Long end) {
		this(start, end, TimeUnit.MILLISECONDS);
	}

	public VideoSection(Long start, Long end, TimeUnit timeUnit) {
		this.start = Math.min(start, end);
		this.end = Math.max(start, end);
		this.timeUnit = timeUnit;
	}
        
        public void setTime(Long startTime, Long endTime) {
            start = startTime;
            end = endTime;
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

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}
	
	public boolean isInSection(long time) {
		return start <= time && time <= end;
	}

        public void setCut(boolean cut) {
            this.cut = cut;
        }

        public boolean getCut() {
            return this.cut;
        }

	@Override
	public int compareTo(VideoSection o) {
		int startCompare = this.start.compareTo(o.start);
		return startCompare != 0 ? startCompare : this.end.compareTo(o.end);
	}

	@Override
	public String toString() {
		return "VideoSection [start=" + start + ", end=" + end + ", timeUnit=" + timeUnit.toString() + "]";
	}
}
