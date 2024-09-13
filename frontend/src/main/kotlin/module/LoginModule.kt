package module

import com.google.gson.Gson
import data.Course
import data.UserSession
import utils.NettyClientProvider
import view.component.DialogManager

/**
 * Module for handling login-related operations.
 *
 * @property onLoginSuccess Callback function to be called on successful login.
 * @property onLogout Callback function to be called on logout.
 */
class LoginModule(
    private val onLoginSuccess: () -> Unit,
    private val onLogout: () -> Unit
) {
    private val nettyClient = NettyClientProvider.nettyClient

    /**
     * Handles the login click event.
     *
     * @param username The username entered by the user.
     * @param password The password entered by the user.
     */
    fun onLoginClick(username: String, password: String) {
        val request = mapOf("username" to username, "password" to password)
        nettyClient.sendRequest(request, "login") { response: String ->
            handleResponse(response)
        }
    }

    /**
     * Handles the response from the login request.
     *
     * @param response The response string from the server.
     */
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

    /**
     * Sends a verification code to the specified email.
     *
     * @param email The email address to send the verification code to.
     * @param userId The user ID associated with the email.
     */
    fun sendVerificationCode(email: String, userId: String) {
        val request = mapOf("email" to email, "userId" to userId)
        nettyClient.sendRequest(request, "sendCode") { response: String ->
            handleResponseSendVerificationCode(response)
        }
    }

    /**
     * Handles the response from the send verification code request.
     *
     * @param response The response string from the server.
     */
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

    /**
     * Verifies the input code against the stored verification code.
     *
     * @param inputCode The code entered by the user.
     * @return True if the input code matches the stored code, false otherwise.
     */
    fun verifyCode(inputCode: String): Boolean {
        return inputCode == UserSession.code
    }

    /**
     * Updates the password for the specified user.
     *
     * @param userId The user ID whose password is to be updated.
     * @param newPassword The new password to be set.
     * @param onSuccess Callback function to be called on successful password update.
     * @param onError Callback function to be called on error, with the error message as a parameter.
     */
    fun updatePassword(userId: String, newPassword: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val request = mapOf("userId" to userId, "newPassword" to newPassword)
        nettyClient.sendRequest(request, "updatePassword") { response: String ->
            handleResponseUpdatePassword(response, onSuccess, onError)
        }
    }

    /**
     * Handles the response from the update password request.
     *
     * @param response The response string from the server.
     * @param onSuccess Callback function to be called on successful password update.
     * @param onError Callback function to be called on error, with the error message as a parameter.
     */

    private fun handleResponseUpdatePassword(response: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        if (responseJson["status"] == "success") {
            onSuccess()
        } else {
            onError(responseJson["message"] as String)
        }
    }

    /**
     * Updates the email for the specified user.
     *
     * @param userId The user ID whose email is to be updated.
     * @param email The new email to be set.
     * @param onSuccess Callback function to be called on successful email update.
     * @param onError Callback function to be called on error, with the error message as a parameter.
     */
    fun updateEmail(userId: String, email: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val request = mapOf("userId" to userId, "email" to email)
        nettyClient.sendRequest(request, "updateEmail") { response: String ->
            handleResponseUpdateEmail(response, onSuccess, onError)
        }
    }

    /**
     * Handles the response from the update email request.
     *
     * @param response The response string from the server.
     * @param onSuccess Callback function to be called on successful email update.
     * @param onError Callback function to be called on error, with the error message as a parameter.
     */
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

    /**
     * Logs out the user.
     */
    fun logout(){
        onLogout()
    }
}