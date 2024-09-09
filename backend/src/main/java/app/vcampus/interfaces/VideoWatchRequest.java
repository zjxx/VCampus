package app.vcampus.interfaces;

public class VideoWatchRequest {
    private String studentId;
    private String videoId;


    public VideoWatchRequest(String studentId, String videoId) {
        this.studentId = studentId;
        this.videoId = videoId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getVideoId() {
        return videoId;
    }

}
