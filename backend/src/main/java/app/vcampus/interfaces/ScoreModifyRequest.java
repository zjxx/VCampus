package app.vcampus.interfaces;

public class ScoreModifyRequest {
    private String teacherId;
    private String studentId;
    private String courseId;
    private String ParticipationScore;
    private String MidtermScore;
    private String FinalScore;
    private String Score;

    public ScoreModifyRequest(String teacherId, String studentId, String courseId, String ParticipationScore, String MidtermScore, String FinalScore, String Score) {
        this.teacherId = teacherId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.ParticipationScore = ParticipationScore;
        this.MidtermScore = MidtermScore;
        this.FinalScore = FinalScore;
        this.Score = Score;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
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

    public String getParticipationScore() {
        return ParticipationScore;
    }

    public void setParticipationScore(String ParticipationScore) {
        this.ParticipationScore = ParticipationScore;
    }

    public String getMidtermScore() {
        return MidtermScore;
    }

    public void setMidtermScore(String MidtermScore) {
        this.MidtermScore = MidtermScore;
    }

    public String getFinalScore() {
        return FinalScore;
    }

    public void setFinalScore(String FinalScore) {
        this.FinalScore = FinalScore;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String Score) {
        this.Score = Score;
    }
}
