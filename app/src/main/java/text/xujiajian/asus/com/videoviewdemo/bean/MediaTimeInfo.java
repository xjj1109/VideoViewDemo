package text.xujiajian.asus.com.videoviewdemo.bean;

/**
 * Created by zhiyuan on 17/1/8.
 */

public class MediaTimeInfo {

    private int duration;
    private int currentPosition;
    private String totalTime;
    private String currentTime;

    public MediaTimeInfo(int duration, int currentPosition, String totalTime, String currentTime) {
        this.duration = duration;
        this.currentPosition = currentPosition;
        this.totalTime = totalTime;
        this.currentTime = currentTime;
    }

    public MediaTimeInfo() {
    }

    public int getDuration() {

        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }
}
