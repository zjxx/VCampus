package app.vcampus.domain;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "enroll")
public class Enrollment {

    @Column(length=13)
    private String studentid;

    @Id
    @Column(unique = true, nullable = false)
    private UUID recordid = UUID.randomUUID();
    @Column(length=8)
    private String courseid;
    @Column
    private String time;

    public Enrollment() {
        this.studentid = "";
        this.recordid = UUID.randomUUID();
        this.courseid = "";
        this.time = "";
    }

    public String getstudentid() {
        return studentid;
    }
    public void setstudentid(String studentid) {
        this.studentid = studentid;
    }
    public UUID getrecordid() {
        return recordid;
    }
    public void setrecordid(UUID recordid) {
        this.recordid = recordid;
    }
    public String getcourseid() {
        return courseid;
    }
    public void setcourseid(String courseid) {
        this.courseid = courseid;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

}
