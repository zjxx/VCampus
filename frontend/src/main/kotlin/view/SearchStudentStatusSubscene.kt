package view

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import view.component.pageTitle

@Composable
fun SearchStudentStatusSubscene() {
    Column {
        pageTitle(heading = "个人学籍信息", caption = "查找个人学籍信息")
        // 其他UI组件

    }
}