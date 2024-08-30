package module

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.google.gson.Gson
import data.UserSession
import network.NettyClientProvider
import view.component.DialogManager

class StudentStatusModule {
    private val nettyClient = NettyClientProvider.nettyClient

    var name by mutableStateOf("")
    var gender by mutableStateOf("")
    var race by mutableStateOf("")
    var nativePlace by mutableStateOf("")
    var studentId by mutableStateOf("")
    var major by mutableStateOf("")
    var academy by mutableStateOf("")
    var number by mutableStateOf("")

    fun searchStudentStatus() {
        val request = mapOf("role" to UserSession.role, "userId" to UserSession.userId)
        nettyClient.sendRequest(request, "searchStudentStatus") { response: String ->
            handleResponseView(response)
        }
    }

    private fun handleResponseView(response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, String>
        name = responseJson["name"] ?: ""
        gender = responseJson["gender"] ?: ""
        race = responseJson["race"] ?: ""
        nativePlace = responseJson["nativePlace"] ?: ""
        studentId = responseJson["studentId"] ?: ""
        major = responseJson["major"] ?: ""
        academy = responseJson["academy"] ?: ""
        number = responseJson["number"] ?: ""

    }

    fun addStudentStatus() {
        var gender_int =0
        if(gender=="女"){
            gender_int=1
        }
        val request = mapOf(
            "username" to name,
            "gender" to gender_int,
            "race" to race,
            "nativePlace" to nativePlace,
            "studentId" to studentId,
            "major" to major,
            "academy" to academy
        )
        nettyClient.sendRequest(request, "addStudentStatus") { response: String ->
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
    fun modifyStudentStatus() {
        val request = mapOf("role" to UserSession.role, "userId" to UserSession.userId)
        nettyClient.sendRequest(request, "modifyStudentStatus") { response: String ->
            handleResponseView(response)
        }
    }
    fun searchAdmin(keyword: String) {
        val request = mapOf("role" to UserSession.role, "userId" to UserSession.userId, "keyword" to keyword)
        nettyClient.sendRequest(request, "student/search") { response: String ->
            handleResponseSearch(response)
        }
    }
    private fun handleResponseSearch(response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        println("Response message: ${responseJson["message"]}")
        println("Response status: ${responseJson["status"]}")
        if (responseJson["status"] == "success") {
        } else {
            DialogManager.showDialog("无相关学生")
        }
    }
    fun deleteStudentStatus() {
        val request = mapOf("role" to UserSession.role, "userId" to UserSession.userId, "studentId" to studentId)
        nettyClient.sendRequest(request, "deleteStudentStatus") { response: String ->
            handleResponseView(response)
        }
    }
}


