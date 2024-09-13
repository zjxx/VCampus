package app.vcampus.domain;

import jakarta.persistence.*;

import java.util.UUID;
/**
 * Represents a course entity in the database.
 */
@Entity
@Table(name = "course")
public class Course {
    @Id
    @Column(unique = true, nullable = false)
    private UUID uuid = UUID.randomUUID();
    @Column(length = 8)
    private String courseId;
    @Column(length = 20)
    private String courseName;
    @Column(length = 8)
    private String teacherName;
    @Column(length = 13)
    private String teacherId;
    @Column(length = 4)
    private String semester;
    @Column
    private Integer credit;
    @Column(length = 39)
    private String location;
    @Column(length =39)
    private String time;
    @Column
    private Integer capacity;
    @Column
    private Integer validCapacity;
    @Column(length = 2)
    private String validGrade;
    @Column(length = 10)
    private String major;
    @Column(length = 2)
    private String property;

    public Course() {
        this.uuid = UUID.randomUUID();
        this.courseId = "";
        this.courseName = "";
        this.teacherName = "";
        this.teacherId = "";
        this.semester = "";
        this.credit = 0;
        this.location = "";
        this.time = "";
        this.capacity = 0;
        this.validCapacity = 0;
        this.validGrade = "";
        this.major = "";
        this.property = "";
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getcourseId() {
        return courseId;
    }

    public void setcourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getcourseName() {
        return courseName;
    }

    public void setcourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getteacherName() {
        return teacherName;
    }

    public void setteacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getteacherId() {
        return teacherId;
    }

    public void setteacherId(String teacherId) {
        this.teacherId = teacherId;
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

    public Integer getvalidCapacity() {
        return validCapacity;
    }

    public void setvalidCapacity(Integer validCapacity) {
        this.validCapacity = validCapacity;
    }

    public String getvalidGrade() {
        return validGrade;
    }

    public void setvalidGrade(String validGrade) {
        this.validGrade = validGrade;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getProperty() {return property;}

    public void setProperty(String property) {this.property = property;}
}