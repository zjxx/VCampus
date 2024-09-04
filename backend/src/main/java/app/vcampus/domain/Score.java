package app.vcampus.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "score")
public class Score {
    @Id
    @Column(length=13)
    private String student_id;
    @Column(length=8)
    private String course_id;
    @Column(length=16)
    private String grade_id;
    @Column
    private int grade;
    @Column
    private int participation_grade;
    @Column
    private int midterm_grade;
    @Column
    private int final_grade;
    @Column
    private int semester;
    @Column
    private int year;

    public Score() {
        this.student_id = "";
        this.course_id = "";
        this.grade_id = "";
        this.grade = 0;
        this.participation_grade = 0;
        this.midterm_grade = 0;
        this.final_grade = 0;
        this.semester = 0;
        this.year = 0;
    }

    public Score(String student_id, String course_id, String grade_id, int grade, int participation_grade, int midterm_grade, int final_grade, int semester, int year) {
        this.student_id = student_id;
        this.course_id = course_id;
        this.grade_id = grade_id;
        this.grade = grade;
        this.participation_grade = participation_grade;
        this.midterm_grade = midterm_grade;
        this.final_grade = final_grade;
        this.semester = semester;
        this.year = year;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getGrade_id() {
        return grade_id;
    }

    public void setGrade_id(String grade_id) {
        this.grade_id = grade_id;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getParticipation_grade() {
        return participation_grade;
    }

    public void setParticipation_grade(int participation_grade) {
        this.participation_grade = participation_grade;
    }

    public int getMidterm_grade() {
        return midterm_grade;
    }

    public void setMidterm_grade(int midterm_grade) {
        this.midterm_grade = midterm_grade;
    }

    public int getFinal_grade() {
        return final_grade;
    }

    public void setFinal_grade(int final_grade) {
        this.final_grade = final_grade;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

}
