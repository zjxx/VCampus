package app.vcampus.interfaces;

public class CourseAddRequest {
    private String courseName;
    private String courseId;
    private String teacherName;
    private String teacherId;
    private String time;
    private String location;
    private String credit;
    private String capacity;
    private String grade;
    private String major;
    private String semester;
    private String property;


    public CourseAddRequest(String courseName, String courseId, String teacherName, String teacherId, String time, String location, String credit, String capacity, String grade, String major, String semester, String property) {
        this.courseName = courseName;
        this.courseId = courseId;
        this.teacherName = teacherName;
        this.teacherId = teacherId;
        this.time = time;
        this.location = location;
        this.credit = credit;
        this.capacity = capacity;
        this.grade = grade;
        this.major = major;
        this.semester = semester;
        this.property = property;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }
}
