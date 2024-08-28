package view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import module.StudentStatusModule
import view.component.CombinedStudentStatusCard

@Composable
fun ModifyStudentStatusSubscene() {
    val studentStatusModule = remember { StudentStatusModule() }
    CombinedStudentStatusCard(studentStatusModule)
}