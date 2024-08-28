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
    var ethnicity by mutableStateOf("")
    var origin by mutableStateOf("")
    var studentId by mutableStateOf("")
    var major by mutableStateOf("")
    var college by mutableStateOf("")

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
        ethnicity = responseJson["race"] ?: ""
        origin = responseJson["nativePlace"] ?: ""
        studentId = responseJson["studentId"] ?: ""
        major = responseJson["major"] ?: ""
        college = responseJson["academy"] ?: ""
    }
}