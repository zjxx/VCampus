package module

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.google.gson.Gson
import data.UserSession
import network.NettyClientProvider

class StudentStatusModule {
    private val nettyClient = NettyClientProvider.nettyClient

    var name by mutableStateOf("")
    var gender by mutableStateOf("")
    var race by mutableStateOf("")
    var nativePlace by mutableStateOf("")
    var studentId by mutableStateOf("")
    var major by mutableStateOf("")
    var academy by mutableStateOf("")

    fun searchStudentStatus() {
        val request = mapOf("role" to UserSession.role, "userId" to UserSession.userId)
        nettyClient.sendRequest(request, "searchStudentStatus") { response: String ->
            handleResponse(response)
        }
    }

    private fun handleResponse(response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, String>
        name = responseJson["name"] ?: ""
        gender = responseJson["gender"] ?: ""
        race = responseJson["race"] ?: ""
        nativePlace = responseJson["nativePlace"] ?: ""
        studentId = responseJson["studentId"] ?: ""
        major = responseJson["major"] ?: ""
        academy = responseJson["academy"] ?: ""
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
            handleResponse(response)
        }
    }

    fun modifyStudentStatus() {
        val request = mapOf("role" to UserSession.role, "userId" to UserSession.userId)
        nettyClient.sendRequest(request, "modifyStudentStatus") { response: String ->
            handleResponse(response)
        }
    }
    fun searchAdmin(keyword: String) {
        val request = mapOf("role" to UserSession.role, "userId" to UserSession.userId, "keyword" to keyword)
        nettyClient.sendRequest(request, "student/search") { response: String ->
            handleResponse(response)
        }
    }
}


