// src/main/kotlin/module/CourseModule.kt
package module

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.google.gson.Gson
import data.UserSession
import utils.NettyClientProvider
import view.component.DialogManager
import java.lang.Class

data class Course(
    var courseId: String,
    var courseIdPrefix: String,
    var courseName: String,
    var credit: String,
    var teacher: String,
    var time: String,
    var timeSlots: List<TimeSlot>,
    var location: String,
    var capacity: String,
    var validCapacity: String, // Change to var to allow modification
    var property: String,
    var teacherNumber: String,
    var isSelect: Boolean
)

data class GroupedCourse(
    var courseIdPrefix: String,
    var courseName: String,
    var credit: String,
    var property: String,
    var courses: List<Course>
)

data class TimeSlot(
    var week: String,
    var begin: String,
    var end: String
)
data class CourseData(
    var courseName: String,
    var courseId: String,
    var credit: String,
    var capacity: String,
    var grade: String,
    var major: String,
    var semester: String,
    var property: String,
    var time: String,
    var location: String,
    var timeAndLocationCards: List<TimeAndLocationCardData>,
    var teacher: String, // New field
    var teacherId: String, // New field
    var courseIdPrefix: String
)

data class TimeAndLocationCardData(
    var dayOfWeek: String,
    var startPeriod: String,
    var endPeriod: String,
    var location: String
){fun getDayOfWeekNumber(): String {
    return when (dayOfWeek) {
        "星期一" -> "1"
        "星期二" -> "2"
        "星期三" -> "3"
        "星期四" -> "4"
        "星期五" -> "5"
        "星期六" -> "6"
        "星期日" -> "7"
        else -> dayOfWeek
    }
  }
}
data class CourseScheduleItem(
    val courseName: String,
    val dayOfWeek: String,
    val startPeriod: Int,
    val endPeriod:  Int,
    val location: String,
    val teacherName:String
)
data class Student(
    val studentId: String,
    val name: String,
    val gender: String,
    var score: String,
    val isScored:String,
    var ParticipationScore: String,
    var MidtermScore: String,
    var FinalScore: String
)
data class Class(
    val courseName: String,
    val courseId: String,
    val time: String,
    val location: String,
    val students: List<Student>,
    val timeAndLocationCards: List<TimeAndLocationCardData> // New property
)
data class StudentScore(
    val studentId: String,
    val courseId: String,
    var regularGrade: String,
    var midtermGrade: String,
    var finalGrade: String,
    var overallGrade: String,
)

data class ScoreStatus(
    val courseId: String,
    val classStatus: String

)
class CourseModule {
    private val nettyClient = NettyClientProvider.nettyClient

    var courses by mutableStateOf(listOf<GroupedCourse>())
    var selectedCourses by mutableStateOf(listOf<Course>())
    private val _searchResults = MutableStateFlow(listOf<GroupedCourse>())
    val searchResults: StateFlow<List<GroupedCourse>> get() = _searchResults
    private val _course = MutableStateFlow<List<CourseData>>(emptyList())
    val course: StateFlow<List<CourseData>> get() = _course
    private val _courseSchedule = MutableStateFlow<List<CourseScheduleItem>>(emptyList())
    val courseSchedule: StateFlow<List<CourseScheduleItem>> get() = _courseSchedule

fun mapDayOfWeekNumberToChinese(dayOfWeekNumber: String): String {
    return when (dayOfWeekNumber) {
        "1" -> "星期一"
        "2" -> "星期二"
        "3" -> "星期三"
        "4" -> "星期四"
        "5" -> "星期五"
        "6" -> "星期六"
        "7" -> "星期日"
        else -> dayOfWeekNumber
    }
}
    fun listCourse() {
        val request = mapOf("role" to UserSession.role, "studentId" to UserSession.userId)
        nettyClient.sendRequest(request, "course/listAll") { response: String ->
            handleResponseList(response)
        }
    }//显示所有课程

    fun searchCourses(query: String) {
        val request = mapOf("role" to UserSession.role, "studentId" to UserSession.userId, "query" to query)
        nettyClient.sendRequest(request, "course/search") { response: String ->
            handleResponseList(response)
        }
    }//搜索


    private fun handleResponseList(response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        if (responseJson["status"] == "success") {
            val num = responseJson["number"] as String
            if (num != "0") {
                val coursesMap = mutableMapOf<String, MutableList<Course>>()
                for (i in 0 until num.toInt()) {
                    val courseJson = responseJson["course$i"] as Map<String, Any>
                    val courseId = courseJson["courseId"] as String
                    val credit = courseJson["credit"] as String
                    val courseIdPrefix = courseId.substring(0, 7)
                    val teacherNumber = courseId[7].toString()
                    val time = courseJson["time"] as String
                    val timeSlots = time.split(";").map {
                        val parts = it.split("-")
                        TimeSlot(parts[0], parts[1], parts[2])
                    }
                    val course = Course(
                        courseId,
                        courseIdPrefix,
                        courseJson["courseName"] as String,
                        credit,
                        courseJson["teacher"] as String,
                        time,
                        timeSlots,
                        courseJson["location"] as String,
                        courseJson["capacity"] as String,
                        courseJson["validCapacity"] as String,
                        courseJson["property"] as String,
                        teacherNumber,
                        courseJson["isSelected"] as Boolean
                    )
                    coursesMap.computeIfAbsent(courseIdPrefix) { mutableListOf() }.add(course)
                }
                this.courses = coursesMap.map {
                    val firstCourse = it.value.first()
                    GroupedCourse(it.key, firstCourse.courseName, firstCourse.credit, firstCourse.property, it.value)
                }
                _searchResults.value = this.courses
            }
        } else if (responseJson["status"] == "failed") {
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    }

    fun selectCourse(course: Course, onSuccess: (Boolean) -> Unit) {
        val request = mapOf("role" to UserSession.role, "studentId" to UserSession.userId, "courseId" to course.courseId)
        nettyClient.sendRequest(request, "course/select") { response: String ->
            val success = handleResponseSelect(response, course)
            onSuccess(success)
        }
    }//选课

    private fun handleResponseSelect(response: String, course: Course): Boolean {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        return if (responseJson["status"] == "success") {
            course.validCapacity = (course.validCapacity.toInt() - 1).toString() // Decrement validCapacity
            DialogManager.showDialog("选课成功")
            true
        } else {
            DialogManager.showDialog(responseJson["reason"] as String)
            false
        }
    }

    fun unselectCourse(course: Course, onSuccess: (Boolean) -> Unit) {
        val request = mapOf("role" to UserSession.role, "studentId" to UserSession.userId, "courseId" to course.courseId)
        nettyClient.sendRequest(request, "course/unselect") { response: String ->
            val success = handleResponseUnselect(response, course)
            onSuccess(success)
        }
    }//退选

    private fun handleResponseUnselect(response: String, course: Course): Boolean {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        return if (responseJson["status"] == "success") {
            course.validCapacity = (course.validCapacity.toInt() + 1).toString() // Increment validCapacity
            DialogManager.showDialog("退选成功")
            true
        } else {
            DialogManager.showDialog(responseJson["reason"] as String)
            false
        }
    }

    fun addCourse(courseData: CourseData, onSuccess: (Boolean) -> Unit) {
        val time = courseData.timeAndLocationCards.joinToString(";") { "${it.getDayOfWeekNumber()}-${it.startPeriod}-${it.endPeriod}" }
        val location = courseData.timeAndLocationCards.joinToString(";") { it.location }
        var grade_int ="20"
        if(courseData.grade=="大一"){
            grade_int="24"
        }
        else if(courseData.grade=="大二"){
            grade_int="23"
        }
        else if(courseData.grade=="大三"){
            grade_int="22"
        }
        else if(courseData.grade=="大四"){
            grade_int="21"
        }
        val request = mapOf(
            "courseName" to courseData.courseName,
            "courseId" to courseData.courseId,
            "credit" to courseData.credit,
            "capacity" to courseData.capacity,
            "grade" to grade_int,
            "major" to courseData.major,
            "semester" to courseData.semester,
            "property" to courseData.property,
            "time" to time,
            "location" to location,
            "teacherName" to courseData.teacher,
            "teacherId" to courseData.teacherId// New field
        )
        nettyClient.sendRequest(request, "course/add") { response: String ->
            val success = handleResponseAdd(response)
            onSuccess(success)
        }
    }//教务增加课程

    private fun handleResponseAdd(response: String): Boolean {
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        return if (responseJson["status"] == "success") {
            DialogManager.showDialog("添加课程成功")
            true
        } else {
            DialogManager.showDialog(responseJson["reason"] as String)
            false
        }
    }
    // src/main/kotlin/module/CourseModule.kt
// In `CourseModule.kt`

   private fun handleResponseView(response: String, onClassesReceived: (List<module.Class>) -> Unit) {
    println("Received response: $response")
    val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
    if (responseJson["status"] == "success") {
        val num = responseJson["number"] as String
        if (num != "0") {
            val classesMap = mutableMapOf<String, MutableList<module.Class>>()
            for (i in 0 until num.toInt()) {
                val courseJson = responseJson["course$i"] as Map<String, Any>
                val courseName = courseJson["courseName"] as String
                val courseId = courseJson["courseId"] as String
                val time = courseJson["time"] as String
                val location = courseJson["location"] as String
                val timeAndLocationCards = time.split(";").zip(location.split(";")).map {
                    val (timeSlot, loc) = it
                    val parts = timeSlot.split("-")
                    TimeAndLocationCardData(
                        dayOfWeek = parts[0],
                        startPeriod = parts[1],
                        endPeriod = parts[2],
                        location = loc
                    )
                }
                val studentsJson = courseJson["students"] as Map<String, Any>
                val numberOfStudents = (studentsJson["number"] as String).toInt()
                val students = mutableListOf<Student>()
                for (j in 0 until numberOfStudents) {
                    val studentJson = studentsJson["student$j"] as Map<String, Any>
                    val studentId = studentJson["studentId"] as String
                    val name = studentJson["name"] as String
                    val gender = studentJson["gender"] as String
                    val score = studentJson["score"] as String
                    val isScored = studentJson["isScored"] as String
                    val ParticipationScore = studentJson["ParticipationScore"] as String
                    val MidtermScore = studentJson["MidtermScore"] as String
                    val FinalScore = studentJson["FinalScore"] as String
                    students.add(Student(studentId, name, gender, score,isScored,ParticipationScore,MidtermScore,FinalScore))
                }
                val classItem = Class(courseName, courseId,time, location, students, timeAndLocationCards)
                classesMap.computeIfAbsent(courseName) { mutableListOf() }.add(classItem)
            }
            val classes = classesMap.map {
                val firstClass = it.value.first()
                Class(firstClass.courseName,firstClass.courseId, firstClass.time, firstClass.location, firstClass.students, firstClass.timeAndLocationCards)
            }
            onClassesReceived(classes)
        }
    } else if (responseJson["status"] == "failed") {
        DialogManager.showDialog(responseJson["reason"] as String)
    }
}
    fun viewMyclass(onClassesReceived: (List<module.Class>) -> Unit) {
    val request = mapOf("role" to UserSession.role, "teacherId" to UserSession.userId)
    nettyClient.sendRequest(request, "course/getCoursesByTeacherId") { response: String ->
        handleResponseView(response, onClassesReceived)
    }
}

    fun deleteCourse(course: CourseData,onDeleteSuccess: () -> Unit) {
        val request = mapOf("role" to UserSession.role, "courseId" to course.courseId, "teacherId" to course.teacherId)
        nettyClient.sendRequest(request, "course/delete") { response: String ->
            println("Received response: $response")
            val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
             if (responseJson["status"] == "success") {
                DialogManager.showDialog("删除课程成功")
                 onDeleteSuccess()
            } else {
                DialogManager.showDialog(responseJson["reason"] as String)
            }

        }
    }
    fun ShowAllCourse()
    {
        val request = mapOf("role" to UserSession.role, "UserId" to UserSession.userId)
        nettyClient.sendRequest(request, "course/showAll") { response: String ->
            handleResponseShowAll(response)
        }
    }
   private fun handleResponseShowAll(response: String) {
    println("Received response: $response")
    val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
    if (responseJson["status"] == "success") {
        val num = responseJson["number"] as String
        if (num != "0") {
            val coursesMap = mutableMapOf<String, MutableList<CourseData>>()
            for (i in 0 until num.toInt()) {
                val courseJson = responseJson["course$i"] as Map<String, Any>
                val courseId = courseJson["courseId"] as String
                val courseIdPrefix = courseId.substring(0, 7)
                val timeAndLocationCards = courseJson["time"].toString().split(";").zip(courseJson["location"].toString().split(";")).map {
    val (time, location) = it
    val parts = time.split("-")
    TimeAndLocationCardData(
        dayOfWeek = mapDayOfWeekNumberToChinese(parts[0]),
        startPeriod = parts[1],
        endPeriod = parts[2],
        location = location
    )
}
                val courseData = CourseData(
                    courseName = courseJson["courseName"] as String,
                    courseId = courseId,
                    credit = courseJson["credit"] as String,
                    capacity = courseJson["capacity"] as String,
                    grade = courseJson["validGrade"] as String,
                    major = courseJson["major"] as String,
                    semester = courseJson["semester"] as String,
                    property = courseJson["property"] as String,
                    time = courseJson["time"] as String,
                    location = courseJson["location"] as String,
                    timeAndLocationCards = timeAndLocationCards,
                    teacher = courseJson["teacherName"] as String,
                    teacherId = courseJson["teacherId"] as String,
                    courseIdPrefix = courseIdPrefix
                )
                coursesMap.computeIfAbsent(courseIdPrefix) { mutableListOf() }.add(courseData)
            }
            _course.value = coursesMap.values.flatten()
        } else {
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    } else {
        DialogManager.showDialog(responseJson["reason"] as String)
    }
}
    fun modifyCourse(courseData: CourseData)
    {
        val time = courseData.timeAndLocationCards.joinToString(";") { "${it.getDayOfWeekNumber()}-${it.startPeriod}-${it.endPeriod}" }
        val location = courseData.timeAndLocationCards.joinToString(";") { it.location }
        val request = mapOf(
            "courseName" to courseData.courseName,
            "courseId" to courseData.courseId,
            "credit" to courseData.credit,
            "capacity" to courseData.capacity,
            "grade" to courseData.grade,
            "major" to courseData.major,
            "semester" to courseData.semester,
            "property" to courseData.property,
            "time" to time,
            "location" to location,
            "teacherName" to courseData.teacher,
            "teacherId" to courseData.teacherId// New field
        )
        nettyClient.sendRequest(request, "course/modify") { response: String ->
            handleResponseModify(response)
        }
    }
    private fun handleResponseModify(response: String) {
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        if (responseJson["status"] == "success") {
            DialogManager.showDialog("修改课程成功")
        } else {
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    }
    fun classTable()
    {
        val request = mapOf("role" to UserSession.role, "studentId" to UserSession.userId)
        nettyClient.sendRequest(request, "course/table") { response: String ->
            handleResponseTable(response)
        }
    }//课表
    private fun handleResponseTable(response: String) {
    println("Received response: $response")
    val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
    if (responseJson["status"] == "success") {
        val courses = mutableListOf<CourseScheduleItem>()
        val num = responseJson["number"] as String
        for (i in 0 until num.toInt()) {
            val courseJson = responseJson["course$i"] as Map<String, Any>
            val courseName = courseJson["courseName"] as String
            val time = courseJson["time"] as String
            val location = courseJson["location"] as String
            val teacherName = courseJson["teacherName"] as String
            val timeSlots = time.split(";")
            val locations = location.split(";")
            for ((index, timeSlot) in timeSlots.withIndex()) {
                val parts = timeSlot.split("-")
                val dayOfWeek = mapDayOfWeekNumberToChinese(parts[0])
                val startPeriod = parts[1].toInt()
                val endPeriod = parts[2].toInt()
                val slotLocation = if (locations.size > 1) locations[index] else locations[0]
                courses.add(CourseScheduleItem(courseName, dayOfWeek, startPeriod, endPeriod, slotLocation,teacherName))
            }
        }
        _courseSchedule.value = courses
    } else {
        DialogManager.showDialog(responseJson["reason"] as String)
    }
}
   fun giveScore(studentScore: StudentScore) {
    val request = mapOf(
        "role" to UserSession.role,
        "teacherId" to UserSession.userId,
        "studentId" to studentScore.studentId,
        "courseId" to studentScore.courseId,
        "ParticipationScore" to studentScore.regularGrade,
        "MidtermScore" to studentScore.midtermGrade,
        "FinalScore" to studentScore.finalGrade,
        "Score" to studentScore.overallGrade
    )
    nettyClient.sendRequest(request, "score/give") { response: String ->
        handleResponseScore(response)
    }
}
    fun handleResponseScore(response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        if (responseJson["status"] == "success") {
            DialogManager.showDialog("打分成功")
        } else {
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    }
    fun viewScore() {
    val request = mapOf("role" to UserSession.role, "studentId" to UserSession.userId)
    nettyClient.sendRequest(request, "score/view") { response: String ->
        handleResponseViewScore(response)
    }
    }
    private fun handleResponseViewScore(response: String) {
    println("Received response: $response")
    }
    fun ModifyScore(studentScore: StudentScore) {
    val request = mapOf(
        "role" to UserSession.role,
        "teacherId" to UserSession.userId,
        "studentId" to studentScore.studentId,
        "courseId" to studentScore.courseId,
        "ParticipationScore" to studentScore.regularGrade,
        "MidtermScore" to studentScore.midtermGrade,
        "FinalScore" to studentScore.finalGrade,
        "Score" to studentScore.overallGrade
    )
    nettyClient.sendRequest(request, "score/modify") { response: String ->
        handleResponseModifyScore(response)
       }
    }
    private fun handleResponseModifyScore(response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        if (responseJson["status"] == "success") {
            DialogManager.showDialog("修改成功")
        } else {
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    }
    fun ConfirmGrade(onClassesReceived: (List<module.Class>) -> Unit) {
        val request = mapOf("role" to UserSession.role, "teacherId" to UserSession.userId)
        nettyClient.sendRequest(request, "score/list") { response: String ->
            handleResponseConfirm(response, onClassesReceived)
        }
    }
    private fun handleResponseConfirm(response: String, onClassesReceived: (List<module.Class>) -> Unit) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        if (responseJson["status"] == "success") {
            val num = responseJson["number"] as String
            if (num != "0") {
                val classesMap = mutableMapOf<String, MutableList<module.Class>>()
                for (i in 0 until num.toInt()) {
                    val courseJson = responseJson["course$i"] as Map<String, Any>
                    val courseName = courseJson["courseName"] as String
                    val courseId = courseJson["courseId"] as String
                    val time = courseJson["time"] as String
                    val location = courseJson["location"] as String
                    val timeAndLocationCards = time.split(";").zip(location.split(";")).map {
                        val (timeSlot, loc) = it
                        val parts = timeSlot.split("-")
                        TimeAndLocationCardData(
                            dayOfWeek = parts[0],
                            startPeriod = parts[1],
                            endPeriod = parts[2],
                            location = loc
                        )
                    }
                    val studentsJson = courseJson["students"] as Map<String, Any>
                    val numberOfStudents = (studentsJson["number"] as String).toInt()
                    val students = mutableListOf<Student>()
                    for (j in 0 until numberOfStudents) {
                        val studentJson = studentsJson["student$j"] as Map<String, Any>
                        val studentId = studentJson["studentId"] as String
                        val name = studentJson["name"] as String
                        val gender = studentJson["gender"] as String
                        val score = studentJson["score"] as String
                        val isScored = studentJson["isScored"] as String
                        val ParticipationScore = studentJson["ParticipationScore"] as String
                        val MidtermScore = studentJson["MidtermScore"] as String
                        val FinalScore = studentJson["FinalScore"] as String
                        students.add(Student(studentId, name, gender, score,isScored,ParticipationScore,MidtermScore,FinalScore))
                    }
                    val classItem = Class(courseName, courseId,time, location, students, timeAndLocationCards)
                    classesMap.computeIfAbsent(courseName) { mutableListOf() }.add(classItem)
                }
                val classes = classesMap.map {
                    val firstClass = it.value.first()
                    Class(firstClass.courseName,firstClass.courseId, firstClass.time, firstClass.location, firstClass.students, firstClass.timeAndLocationCards)
                }
                onClassesReceived(classes)
            }
        } else if (responseJson["status"] == "failed") {
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    }

    fun CheckGrade(scoreStatus: ScoreStatus)
    {
        val request = mapOf(
            "role" to UserSession.role,
            "userId" to UserSession.userId,
            "courseId" to scoreStatus.courseId,
            "classStatus" to scoreStatus.classStatus
        )
        nettyClient.sendRequest(request, "score/check") { response: String ->
            handleResponseCheck(response)
        }
    }
    private fun handleResponseCheck(response:String)
    {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        if (responseJson["status"] == "success") {
            DialogManager.showDialog("提交成功")
        } else {
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    }
}

