package app.vcampus.domain;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "enrollment")
public class Enrollment {
    @Id
    @Column(length=13)
    private String student_id;
    @Column(unique = true, nullable = false)
    private UUID record_id = UUID.randomUUID();
    @Column(length=8)
    private String course_id;
    @Column
    private String time;

    public Enrollment() {
        this.student_id = "";
        this.record_id = UUID.randomUUID();
        this.course_id = "";
        this.time = "";
    }

    public String getStudent_id() {
        return student_id;
    }
    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }
    public UUID getRecord_id() {
        return record_id;
    }
    public void setRecord_id(UUID record_id) {
        this.record_id = record_id;
    }
    public String getCourse_id() {
        return course_id;
    }
    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

}
