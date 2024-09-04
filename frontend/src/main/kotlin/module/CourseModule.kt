// src/main/kotlin/module/CourseModule.kt
package module

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.google.gson.Gson
import data.UserSession
import network.NettyClientProvider
import view.component.DialogManager

class CourseModule {
    private val nettyClient = NettyClientProvider.nettyClient

    var id by mutableStateOf("")
    var name by mutableStateOf("")
    var classCount by mutableStateOf("")
    var type by mutableStateOf("")
    var credits by mutableStateOf("")
    var searchResults by mutableStateOf(listOf<Course>())
    var courses by mutableStateOf(listOf<Course>())
    var courseDetails by mutableStateOf(listOf<CourseDetail>())

    fun searchCourses(query: String) {
        val request = mapOf("role" to UserSession.role, "userId" to UserSession.userId, "query" to query)
        nettyClient.sendRequest(request, "course/search") { response: String ->
            handleResponseSearch(response)
        }
    }

    private fun handleResponseSearch(response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        if (responseJson["status"] == "success") {
            val num = responseJson["number"] as String
            if (num != "0") {
                val courses = mutableListOf<Course>()
                for (i in 0 until num.toInt()) {
                    val courseIndex = responseJson["c$i"] as String
                    val courseJson = Gson().fromJson(courseIndex, MutableMap::class.java) as MutableMap<String, Any>
                    println("Course name: ${courseJson["name"]}")
                    val course = Course(
                        id = courseJson["id"] as String,
                        name = courseJson["name"] as String,
                        classCount = courseJson["classCount"] as String,
                        type = courseJson["type"] as String,
                        credits = courseJson["credits"] as String
                    )
                    courses.add(course)
                }
                searchResults = courses
            } else {
                DialogManager.showDialog(responseJson["reason"] as String)
            }
        } else if (responseJson["status"] == "fail") {
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    }

    fun fetchCourseDetails(courseId: String) {
        val request = mapOf("courseId" to courseId)
        nettyClient.sendRequest(request, "course/details") { response: String ->
            handleResponseFetchDetails(response)
        }
    }

    private fun handleResponseFetchDetails(response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        if (responseJson["status"] == "success") {
            val details = mutableListOf<CourseDetail>()
            val num = responseJson["number"] as String
            for (i in 0 until num.toInt()) {
                val detailIndex = responseJson["d$i"] as String
                val detailJson = Gson().fromJson(detailIndex, MutableMap::class.java) as MutableMap<String, Any>
                val detail = CourseDetail(
                    teacherName = detailJson["teacherName"] as String,
                    week = detailJson["week"] as String,
                    beginTime = detailJson["beginTime"] as String,
                    endTime = detailJson["endTime"] as String,
                    capacity = detailJson["capacity"] as String,
                    selectedCount = detailJson["selectedCount"] as String
                )
                details.add(detail)
            }
            courseDetails = details
        } else {
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    }

    fun selectCourse() {
        val request = mapOf(
            "id" to id,
            "name" to name,
            "classCount" to classCount,
            "type" to type,
            "credits" to credits
        )
        nettyClient.sendRequest(request, "course/select") { response: String ->
            handleResponseAdd(response)
        }
    }

    private fun handleResponseAdd(response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        if (responseJson["status"] == "success") {
            DialogManager.showDialog("添加成功")
        } else {
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    }

    fun modifyCourse(onModifySuccess: () -> Unit) {
        val request = mapOf(
            "id" to id,
            "name" to name,
            "classCount" to classCount,
            "type" to type,
            "credits" to credits
        )
        nettyClient.sendRequest(request, "course/modify") { response: String ->
            handleResponseModify(response, onModifySuccess)
        }
    }

    private fun handleResponseModify(response: String, onModifySuccess: () -> Unit) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        if (responseJson["status"] == "success") {
            onModifySuccess()
        } else {
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    }

    fun deleteCourse(onDeleteSuccess: () -> Unit) {
        val request = mapOf("id" to id)
        nettyClient.sendRequest(request, "course/delete") { response: String ->
            val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
            if (responseJson["status"] == "success") {
                onDeleteSuccess()
            } else {
                DialogManager.showDialog(responseJson["reason"] as String)
            }
        }
    }
    fun listCourse() {
        val request = mapOf("role" to UserSession.role, "studentId" to UserSession.userId)
        nettyClient.sendRequest(request, "course/listAll") { response: String ->
            handleResponseList(response)
        }
    }
    private fun handleResponseList(response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        if (responseJson["status"] == "success") {
            val num = responseJson["number"] as String
            if (num != "0") {
                val courses = mutableListOf<Course>()
                for (i in 0 until num.toInt()) {
                    val courseIndex = responseJson["c$i"] as String
                    val courseJson = Gson().fromJson(courseIndex, MutableMap::class.java) as MutableMap<String, Any>
                    println("Course name: ${courseJson["name"]}")
                    val course = Course(
                        id = courseJson["id"] as String,
                        name = courseJson["name"] as String,
                        classCount = courseJson["classCount"] as String,
                        type = courseJson["type"] as String,
                        credits = courseJson["credits"] as String
                    )
                    courses.add(course)
                }
                this.courses = courses
            } else {
                DialogManager.showDialog(responseJson["reason"] as String)
            }
        } else if (responseJson["status"] == "fail") {
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    }
}

data class Course(
    var id: String,
    var name: String,
    var classCount: String,
    var type: String,
    var credits: String
)

data class CourseDetail(
    var teacherName: String,
    var week: String,
    var beginTime: String,
    var endTime: String,
    var capacity: String,
    var selectedCount: String
) {
    val classTime: String
        get() = "$week $beginTime-$endTime"
}