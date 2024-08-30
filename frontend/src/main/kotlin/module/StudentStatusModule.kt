// 修改 StudentStatusModule.kt
package module

import com.google.gson.Gson
import data.UserSession
import utils.NettyClientProvider

class StudentStatusModule {
    private val nettyClient = NettyClientProvider.nettyClient

    fun searchStudentStatus() {
        val request = mapOf("role" to UserSession.role, "userId" to UserSession.userId, "info" to "viewstudent")
        nettyClient.sendRequest(request, "searchStudentStatus") { response: String ->
            handleResponse(response)
        }
    }

    private fun handleResponse(response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        // 处理后端返回的信息
        println("Student status info: ${responseJson["info"]}")
    }
}