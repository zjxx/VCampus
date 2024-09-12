package module

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.google.gson.Gson
import data.UserSession
import utils.NettyClientProvider
import view.component.DialogManager

data class Merchandise(
    val username: String,
    val gender: String,
    val race: String,
    val nativePlace: String,
    val studentId: String,
    val major: String,
    val academy: String
)

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
    var searchResults by mutableStateOf(listOf<StudentStatusModule>())
    var students by mutableStateOf(listOf<StudentStatusModule>())
    var majorErrorDialog by mutableStateOf(false)
    var fieldErrorDialog by mutableStateOf(false)
    var cardNumberErrorDialog by mutableStateOf(false)
    var nameErrorDialog by mutableStateOf(false)
    var raceErrorDialog by mutableStateOf(false)
    val validMajors = listOf(
        "建筑学", "城乡规划", "风景园林", "机械工程", "能源与动力工程", "建筑环境与能源应用工程",
        "核I程与核技术", "新能源科学与工程", "环境工程", "自动化", "机器人工程", "电气工程及其自动化",
        "智能感知工程", "测控技术与仪器", "材料科学与工程", "土木工程", "给排水科学与工程", "工程管理",
        "智能建造", "交通工程", "交通运输", "港口航道与海岸工程", "城市地下空间工程", "道路桥梁与渡河工程",
        "智慧交通", "信息工程", "海洋信息工程", "电子科学与技术", "信息工程","计算机科学与技术","软件工程","物联网工程","人工智能"
    )
    val validRaces = listOf(
        "汉", "蒙古", "回", "藏", "维吾尔", "苗", "彝", "壮", "布依", "朝鲜",
        "满", "侗", "瑶", "白", "土家", "哈尼", "哈萨克", "傣", "黎", "傈僳",
        "佤", "畲", "高山", "拉祜", "水", "东乡", "纳西", "景颇", "柯尔克孜", "土",
        "达斡尔", "仫佬", "羌", "布朗", "撒拉", "毛南", "仡佬", "锡伯", "阿昌", "普米",
        "塔吉克", "怒", "乌孜别克", "俄罗斯", "鄂温克", "德昂", "保安", "裕固", "京", "塔塔尔",
        "独龙", "鄂伦春", "赫哲", "门巴", "珞巴", "基诺"
    )

    fun searchStudentStatus() {
        val request = mapOf("role" to UserSession.role, "userId" to UserSession.userId)
        nettyClient.sendRequest(request, "arc/view") { response: String ->
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
    private val nameRegex = Regex("^[\\u4e00-\\u9fa5]+(·[\\u4e00-\\u9fa5]+)*$|^[\\u4e00-\\u9fa5]{2,20}$")

    fun addStudentStatus() {
        if (name.isEmpty() || gender.isEmpty() || race.isEmpty() || nativePlace.isEmpty() || studentId.isEmpty() || major.isEmpty() || academy.isEmpty()) {
            fieldErrorDialog = true // Set the error dialog state to true if any field is empty
            return
        }
        if (!nameRegex.matches(name)) {
            nameErrorDialog = true // Set the error dialog state to true if the name is invalid
            return
        }
        if (!validRaces.contains(race)) {
            raceErrorDialog = true
            return
        }
        if (!validMajors.contains(major)) {
            majorErrorDialog = true // Set the error dialog state to true
            return
        }
        if (studentId.length != 9 || !studentId.startsWith("2")) {
            cardNumberErrorDialog = true // Set the error dialog state to true if the card number is invalid
            return
        }
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
        nettyClient.sendRequest(request, "arc/add") { response: String ->
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
    fun modifyStudentStatus(onModifySuccess: () -> Unit) {
        var gender_int ="0"
        if(gender=="女"){
            gender_int="1"
        }
        val request = mapOf(
            "role" to UserSession.role,
            "userId" to UserSession.userId,
            "username" to name,
            "gender" to gender_int,
            "race" to race,
            "nativePlace" to nativePlace,
            "studentId" to studentId,
            "major" to major,
            "academy" to academy
        )
        nettyClient.sendRequest(request, "arc/modify") { response: String ->
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

    fun searchAdmin(keyword: String, updateSearchResults: (List<StudentStatusModule>) -> Unit) {
        val request = mapOf("role" to UserSession.role, "userId" to UserSession.userId, "keyword" to keyword)
        nettyClient.sendRequest(request, "arc/search") { response: String ->
            handleResponseSearch(response, updateSearchResults)
        }
    }
    private fun handleResponseSearch(response: String, updateSearchResults: (List<StudentStatusModule>) -> Unit) {
    println("Received response: $response")
    val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
    if (responseJson["status"] == "success") {
        val num = responseJson["number"] as String
        if (num != "0") {
            val students = mutableListOf<StudentStatusModule>()
            for (i in 0 until num.toInt()) {
                val studentIndex = responseJson["s$i"] as String
                val studentJson = Gson().fromJson(studentIndex, MutableMap::class.java) as MutableMap<String, Any>
                println("studentId: ${studentJson["name"]}")
                val student = StudentStatusModule().apply {
                    name = studentJson["name"] as String
                    gender = studentJson["gender"] as String
                    race = studentJson["race"] as String
                    nativePlace = studentJson["nativePlace"] as String
                    studentId = studentJson["studentId"] as String
                    major = studentJson["major"] as String
                    academy = studentJson["academy"] as String
                }
                students.add(student)
            }
            updateSearchResults(students)
        } else {
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    } else if (responseJson["status"] == "fail") {
        DialogManager.showDialog(responseJson["reason"] as String)
    }
}
    fun deleteStudentStatus(onDeleteSuccess: () -> Unit) {
        val request = mapOf("role" to UserSession.role, "userId" to UserSession.userId, "studentId" to studentId)
        nettyClient.sendRequest(request, "arc/delete") { response: String ->
            val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
            if (responseJson["status"] == "success") {
                onDeleteSuccess()
            } else {
                DialogManager.showDialog(responseJson["reason"] as String)
            }
        }
    }
    fun onclickModifyStudentStatus(updateSearchResults: (List<StudentStatusModule>) -> Unit) {
        val request = mapOf("role" to UserSession.role, "userId" to UserSession.userId)
        nettyClient.sendRequest(request, "arc/clickmodify") { response: String ->
            handleResponseModify(response, updateSearchResults)
        }
    }
    private fun handleResponseModify(response: String, updateSearchResults: (List<StudentStatusModule>) -> Unit) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, String>
        if (responseJson["status"] == "success") {
            val num = responseJson["number"] as String
            if (num != "0") {
                val students = mutableListOf<StudentStatusModule>()
                for (i in 0 until num.toInt()) {
                    val studentIndex = responseJson["s$i"] as String
                    val studentJson = Gson().fromJson(studentIndex, MutableMap::class.java) as MutableMap<String, Any>
                    println("studentId: ${studentJson["name"]}")
                    val student = StudentStatusModule().apply {
                        name = studentJson["name"] as String
                        gender = studentJson["gender"] as String
                        race = studentJson["race"] as String
                        nativePlace = studentJson["nativePlace"] as String
                        studentId = studentJson["studentId"] as String
                        major = studentJson["major"] as String
                        academy = studentJson["academy"] as String
                    }
                    students.add(student)
                }
                updateSearchResults(students)
            }
        }
    }
    fun modifyPassword(oldPassword: String, newPassword: String) {
        if (oldPassword.isEmpty() || newPassword.isEmpty()) {
            DialogManager.showDialog("旧密码和新密码不能为空")
            return
        }
        val request = mapOf(
            "role" to UserSession.role,
            "userId" to UserSession.userId,
            "oldPassword" to oldPassword,
            "newPassword" to newPassword
        )
        nettyClient.sendRequest(request, "arc/modifyPassword") { response: String ->
            handleResponseModifyPassword(response)
        }
    }

    private fun handleResponseModifyPassword(response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        if (responseJson["status"] == "success") {
            DialogManager.showDialog("密码修改成功")
        }
        else if(responseJson["status"] == "fail") {
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    }
    fun Studentfile (itemList: List<module.Merchandise>) {
        val request = mapOf("userId" to UserSession.userId.toString(), "length" to (itemList.size).toString(), "items" to Gson().toJson(itemList))
        nettyClient.sendRequest(request, "arc/file") { response: String ->
            handleAddfile(response)
        }
    }
    private fun handleAddfile(response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        if (responseJson["status"] == "success") {
            DialogManager.showDialog("添加成功")
        } else {
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    }
}

