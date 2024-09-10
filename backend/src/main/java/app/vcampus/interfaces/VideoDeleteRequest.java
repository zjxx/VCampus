package app.vcampus.interfaces;

public class VideoDeleteRequest {
    private String teacherId;
    private String videoId;


    public VideoDeleteRequest(String teacherId, String videoId) {
        this.teacherId = teacherId;
        this.videoId = videoId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
