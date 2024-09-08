package app.vcampus.domain;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "video")
public class Video {
    @Id
    @Column(unique = true, nullable = false)
    private UUID videoId;

    @Column(length = 8)
    private String courseId;

    @Column(length=13)
    private String teacherId;

    @Column(length=30)
    private String videoName;

    @Column
    private int clickTimes;

    public Video()
    {
        this.videoId = UUID.randomUUID();
        this.courseId="";
        this.teacherId="";
        this.videoName="";
        this.clickTimes=0;
    }

    public UUID getVideoId(){ return videoId; };
    public String getCourseId(){ return courseId; };
    public String getTeacherId(){ return teacherId; };
    public String getVideoName(){ return videoName; };

    public int getClickTimes(){ return clickTimes; };

    public void setCourseId(String courseId){ this.courseId = courseId; };
    public void setTeacherId(String teacherId){ this.teacherId = teacherId; };
    public void setVideoName(String videoName){ this.videoName = videoName; };
    public void setVideoId(UUID videoId){ this.videoId = videoId; };
    public void setClickTimes(int clickTimes){ this.clickTimes = clickTimes; };

}
