package app.vcampus.interfaces;

public class ScoreGiveRequest {
    private String teacherId;
    private String studentId;
    private String courseId;
    private String ParticipationScore;
    private String MidtermScore;
    private String FinalScore;
    private String Score;

    public ScoreGiveRequest(String teacherId, String studentId, String courseId, String ParticipationScore, String MidtermScore, String FinalScore, String Score) {
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

    public String getStudentId() {
        return studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getParticipationScore() {
        return ParticipationScore;
    }

    public String getMidtermScore() {
        return MidtermScore;
    }

    public String getFinalScore() {
        return FinalScore;
    }

    public String getScore() {
        return Score;
    }

}
