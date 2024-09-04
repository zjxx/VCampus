package app.vcampus.interfaces;

public class CourseStudentShowRequest {
    private String courseId;
    private String teacherId;

    public CourseStudentShowRequest(String courseId, String teacherId) {
        this.courseId = courseId;
        this.teacherId = teacherId;
    }


    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }
}
