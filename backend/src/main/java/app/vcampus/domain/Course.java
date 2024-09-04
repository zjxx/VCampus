package app.vcampus.domain;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "course")
public class Course {
    @Id
    @Column(unique = true, nullable = false)
    private UUID uuid = UUID.randomUUID();
    @Column(length = 8)
    private String course_id;
    @Column(length = 20)
    private String course_name;
    @Column(length = 8)
    private String teacher_name;
    @Column(length = 13)
    private String teacher_id;
    @Column(length = 4)
    private String semester;
    @Column
    private Integer credit;
    @Column(length = 7)
    private String location;
    @Column(length =20)
    private String time;
    @Column
    private Integer capacity;
    @Column
    private Integer valid_capacity;
    @Column(length = 2)
    private String valid_grade;
    @Column
    private Integer major;
    @Column(length = 2)
    private String property;

    public Course() {
        this.uuid = UUID.randomUUID();
        this.course_id = "";
        this.course_name = "";
        this.teacher_name = "";
        this.teacher_id = "";
        this.semester = "";
        this.credit = 0;
        this.location = "";
        this.time = "";
        this.capacity = 0;
        this.valid_capacity = 0;
        this.valid_grade = "";
        this.major = 0;
        this.property = "";
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getValid_capacity() {
        return valid_capacity;
    }

    public void setValid_capacity(Integer valid_capacity) {
        this.valid_capacity = valid_capacity;
    }

    public String getValid_grade() {
        return valid_grade;
    }

    public void setValid_grade(String valid_grade) {
        this.valid_grade = valid_grade;
    }

    public Integer getMajor() {return major;}

    public void setMajor(Integer major) {this.major = major;}

    public String getProperty() {return property;}

    public void setProperty(String property) {this.property = property;}
}