package app.vcampus.domain;

import jakarta.persistence.*;
/**
 * Represents a student entity in the database.
 */
@Entity
@Table(name = "student")
public class Student {
    @Id
    @Column(length = 13)
    private String studentId;

    @Column(length = 25)
    private String username;

    private int gender;

    @Column(length = 10)
    private String nativePlace;

    @Column(length = 5)
    private String race;

    @Column(length = 10)
    private String major;

    @Column(length = 10)
    private String academy;

    @Column(length = 2)
    private String grade;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getAcademy() {
        return academy;
    }

    public void setAcademy(String academy) {
        this.academy = academy;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public String getGrade() {return grade;}

    public void setGrade(String grade) {this.grade = grade;}
}