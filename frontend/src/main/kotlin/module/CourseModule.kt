// src/main/kotlin/module/CourseModule.kt
package module

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.google.gson.Gson
import data.UserSession
import network.NettyClientProvider
import view.component.DialogManager

data class Course(
    val courseId: String,
    val courseIdPrefix: String,
    val courseName: String,
    val credit: String,
    val teacher: String,
    val time: String,
    val timeSlots: List<TimeSlot>,
    val location: String,
    val capacity: String,
    var validCapacity: String, // Change to var to allow modification
    val property: String,
    val teacherNumber: String
)

data class GroupedCourse(
    val courseIdPrefix: String,
    val courseName: String,
    val credit: String,
    val property: String,
    val courses: List<Course>
)

data class TimeSlot(
    val week: String,
    val begin: String,
    val end: String
)

class CourseModule {
    private val nettyClient = NettyClientProvider.nettyClient

    var courses by mutableStateOf(listOf<GroupedCourse>())
    var selectedCourses by mutableStateOf(listOf<Course>())
    private val _searchResults = MutableStateFlow(listOf<GroupedCourse>())
    val searchResults: StateFlow<List<GroupedCourse>> get() = _searchResults

    fun listCourse() {
        val request = mapOf("role" to UserSession.role, "studentId" to UserSession.userId)
        nettyClient.sendRequest(request, "course/listAll") { response: String ->
            handleResponseList(response)
        }
    }

    fun searchCourses(query: String) {
        _searchResults.value = courses.filter { groupedCourse ->
            groupedCourse.courses.any { it.courseName.contains(query) }
        }
    }

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
                        courseJson["valid_capacity"] as String,
                        courseJson["property"] as String,
                        teacherNumber
                    )
                    coursesMap.computeIfAbsent(courseIdPrefix) { mutableListOf() }.add(course)
                }
                this.courses = coursesMap.map {
                    val firstCourse = it.value.first()
                    GroupedCourse(it.key, firstCourse.courseName, firstCourse.credit, firstCourse.property, it.value)
                }
                _searchResults.value = this.courses
            } else {
                DialogManager.showDialog(responseJson["reason"] as String)
            }
        } else if (responseJson["status"] == "fail") {
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    }

    fun selectCourse(course: Course): Boolean {
        val request = mapOf("role" to UserSession.role, "studentId" to UserSession.userId, "courseId" to course.courseId)
        var success = false
        nettyClient.sendRequest(request, "course/select") { response: String ->
            success = handleResponseSelect(response, course)
        }
        return success
    }

    private fun handleResponseSelect(response: String, course: Course): Boolean {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        return if (responseJson["status"] == "success") {
            course.validCapacity = (course.validCapacity.toInt() + 1).toString() // Increment validCapacity
            DialogManager.showDialog("选课成功")
            true
        } else {
            DialogManager.showDialog(responseJson["reason"] as String)
            false
        }
    }
    fun unselectCourse(course: Course): Boolean {
        val request = mapOf("role" to UserSession.role, "studentId" to UserSession.userId, "courseId" to course.courseId)
        var success = false
        nettyClient.sendRequest(request, "course/unselect") { response: String ->
            success = handleResponseUnselect(response, course)
        }
        return success
    }

    private fun handleResponseUnselect(response: String, course: Course): Boolean {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        return if (responseJson["status"] == "success") {
            course.validCapacity = (course.validCapacity.toInt() - 1).toString() // Decrease validCapacity
            DialogManager.showDialog("退选成功")
            true
        } else {
            DialogManager.showDialog(responseJson["reason"] as String)
            false
        }
    }
}