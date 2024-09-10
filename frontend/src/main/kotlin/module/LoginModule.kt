package module

import com.google.gson.Gson
import data.UserSession
import data.Course
import utils.NettyClientProvider
import view.component.DialogManager

class LoginModule(
    private val onLoginSuccess: () -> Unit,
    private val onLogout: () -> Unit
) {
    private val nettyClient = NettyClientProvider.nettyClient
    fun onLoginClick(username: String, password: String) {
        val request = mapOf("username" to username, "password" to password)
        nettyClient.sendRequest(request, "login") { response: String ->
            handleResponse(response)
        }
    }

    private fun handleResponse(response: String) {
    println("Received response: $response")
    val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
    if (responseJson["status"] == "success" || responseJson["status"] == "noemail") {
        nettyClient.setRole(responseJson["role"] as String)
        UserSession.userId = responseJson["userId"] as String
        UserSession.role = responseJson["role"] as String
        UserSession.status = responseJson["status"] as String
        UserSession.userName = responseJson["username"] as String
        val courses = mutableListOf<Course>()
        val numberOfCourses = responseJson["number"]?.toString()?.toIntOrNull() ?: 0
        if (numberOfCourses == 0) {
            courses.add(Course(name = "今天暂无课程", time = "", classroom = ""))
        } else {
            for (i in 0 until numberOfCourses) {
                val courseJson = responseJson["course$i"] as? Map<String, String> ?: continue
                val time = courseJson["time"] ?: "Unknown"
                val modifiedTime = time.substringAfter("-")
                val course = Course(
                    name = courseJson["courseName"] ?: "Unknown",
                    time = modifiedTime,
                    classroom = courseJson["location"] ?: "Unknown"
                )
                courses.add(course)
            }
        }
        UserSession.courses = courses
        onLoginSuccess()
    } else {
        DialogManager.showDialog(responseJson["message"] as String)
    }
}

    fun sendVerificationCode(email: String, userId: String) {
        val request = mapOf("email" to email, "userId" to userId)
        nettyClient.sendRequest(request, "sendCode") { response: String ->
            handleResponseSendVerificationCode(response)
        }
    }

    private fun handleResponseSendVerificationCode(response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        if (responseJson["status"] == "success") {
           UserSession.code = responseJson["code"] as String
            DialogManager.showDialog(responseJson["message"] as String)
        } else {
            DialogManager.showDialog(responseJson["message"] as String)
        }
    }

    fun verifyCode(inputCode: String): Boolean {
        return inputCode == UserSession.code
    }

    fun updatePassword(userId: String, newPassword: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val request = mapOf("userId" to userId, "newPassword" to newPassword)
        nettyClient.sendRequest(request, "updatePassword") { response: String ->
            handleResponseUpdatePassword(response, onSuccess, onError)
        }
    }

    private fun handleResponseUpdatePassword(response: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        if (responseJson["status"] == "success") {
            onSuccess()
        } else {
            onError(responseJson["message"] as String)
        }
    }
    fun updateEmail(userId: String, email: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val request = mapOf("userId" to userId, "email" to email)
        nettyClient.sendRequest(request, "updateEmail") { response: String ->
            handleResponseUpdateEmail(response, onSuccess, onError)
        }
    }

    private fun handleResponseUpdateEmail(response: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        if (responseJson["status"] == "success") {
            onSuccess()
            DialogManager.showDialog(responseJson["message"] as String)

        } else {
            onError(responseJson["message"] as String)
        }
    }
    fun logout(){
        onLogout()
    }
}