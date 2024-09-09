// src/main/kotlin/view/AffairsScene.kt
package view

import WebViewDialog
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight


@Composable
fun MultiColumnTable(headers: List<String>, data: List<List<String>>, onCellClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        headers.forEachIndexed { index, header ->
            // Table Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray)
            ) {
                Text(
                    text = header,
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.Center),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            // Table Rows
            data[index].chunked(2).forEach { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Gray),
                    horizontalArrangement = Arrangement.Center
                ) {
                    row.forEach { cell ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, Color.Gray)
                        ) {
                            ClickableText(
                                text = AnnotatedString(cell),
                                onClick = { onCellClick(cell) },
                                modifier = Modifier
                                    .padding(8.dp)
                                    .align(Alignment.Center),
                                style = TextStyle(fontSize = 14.sp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AffairsScene(onNavigate: (String) -> Unit, role: String) {
    val headers = listOf("学生平台", "教师平台", "公共服务")
    val data = listOf(
        listOf("全校课表查询", "选课", "学生在线（成绩查询、考试安排）", ""),
        listOf("2017级：教师课表    ", "2018级及以后：教师课表   ", "质量工程管理系统", "校外专家管理系统(外教/企业教师授课)"),
        listOf("大学生创新创业训练项目管理系统", "学科竞赛管理系统", "本科生课外研学学分管理系统", "毕业设计管理系统")
    )

    var showDialog by remember { mutableStateOf(false) }
    var url by remember { mutableStateOf("") }

    fun navigateToWebView(cellData: String) {
        url = when (cellData) {
            "全校课表查询" -> "http://ehall.seu.edu.cn/jwapp/sys/kcbcx/*default/index.do#/bjkcb"
            "选课" -> "http://newxk.urp.seu.edu.cn/"
            "学生在线（成绩查询、考试安排）" -> "https://i.seu.edu.cn/default/index.html#/jwcapps"
            "2017级：教师课表    " -> "http://xk.urp.seu.edu.cn/jw_service/service/lookTeacherCurriculum.action"
            "2018级及以后：教师课表   " -> "http://ehall.seu.edu.cn/appShow?appId=4770397878132218"
            "质量工程管理系统" -> "https://zlgc.seu.edu.cn/#/account/login"
            "校外专家管理系统(外教/企业教师授课)" -> "http://xwzj.seu.edu.cn/"
            "大学生创新创业训练项目管理系统" -> "http://cxcy.seu.edu.cn/"
            "学科竞赛管理系统" -> "http://srtp.seu.edu.cn/xkjs/"
            "本科生课外研学学分管理系统" -> "https://srtp.seu.edu.cn/pmp/user/login/"
            "毕业设计管理系统" -> "https://bysj.seu.edu.cn/"
            else -> ""
        }
        showDialog = true
    }

    if (showDialog) {
        WebViewDialog(url = url, onDismiss = { showDialog = false })
    }

    MultiColumnTable(headers = headers, data = data, onCellClick = { cellData ->
        navigateToWebView(cellData)
    })
}