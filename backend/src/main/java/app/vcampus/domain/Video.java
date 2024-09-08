package app.vcampus.domain;

import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "video")
public class Video {
    @Id
    @Column(unique = true, nullable = false,length = 30)
    private String videoId;

    @Column(length = 8)
    private String courseId;

    @Column(length=13)
    private String teacherId;

    @Column(length=30)
    private String videoName;

    @Column
    private Date Upload_Date;

    public Date getUpload_Date() {
        return Upload_Date;
    }

    public void setUpload_Date(Date upload_Date) {
        Upload_Date = upload_Date;
    }

    public Video()
    {
        this.videoId ="";
        this.courseId="";
        this.teacherId="";
        this.videoName="";
    }

    public String getCourseId(){ return courseId; };
    public String getTeacherId(){ return teacherId; };
    public String getVideoName(){ return videoName; };

    public void setCourseId(String courseId){ this.courseId = courseId; };
    public void setTeacherId(String teacherId){ this.teacherId = teacherId; };
    public void setVideoName(String videoName){ this.videoName = videoName; };

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
