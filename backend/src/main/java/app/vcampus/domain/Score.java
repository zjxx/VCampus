package app.vcampus.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;
/**
 * Represents a score entity in the database.
 */
@Entity
@Table(name = "score")
public class Score {

    @Column(length=13)
    private String studentId;
    @Column(length=8)
    private String courseId;

    @Id
    @Column(unique = true, nullable = false)
    private UUID scoreId;

    @Column
    private int score;
    @Column
    private int participationScore;
    @Column
    private int midtermScore;
    @Column
    private int finalScore;
    @Column
    private String semester;
    @Column
    private int credit;
    @Column(length = 5)
    private String status;
    @Column(length = 13)
    private String teacherId;

    public Score() {
        this.studentId = "";
        this.courseId = "";
        this.scoreId = UUID.randomUUID();
        this.score = 0;
        this.participationScore = 0;
        this.midtermScore = 0;
        this.finalScore = 0;
        this.semester = "";
        this.credit = 0;
        this.status = "";
        this.teacherId = "";
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public UUID getScoreId() {  return scoreId;}

    public void setScoreId(UUID scoreId) {  this.scoreId = scoreId; }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getParticipationScore() {
        return participationScore;
    }

    public void setParticipationScore(int participationScore) {
        this.participationScore = participationScore;
    }

    public int getMidtermScore() {
        return midtermScore;
    }

    public void setMidtermScore(int midtermScore) {
        this.midtermScore = midtermScore;
    }

    public int getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(int finalScore) {
        this.finalScore = finalScore;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public int getCredit() { return credit; }

    public void setCredit(int credit) { this.credit = credit; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getTeacherId() { return teacherId; }

    public void setTeacherId(String teacherId) { this.teacherId = teacherId; }
}
