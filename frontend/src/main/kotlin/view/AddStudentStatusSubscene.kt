// src/main/kotlin/view/AddStudentStatusSubscene.kt
package view

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import module.StudentStatusModule
import view.component.pageTitle
import java.awt.Desktop
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.DataFormatter

@Composable
fun AddStudentStatusSubscene() {
    val studentStatusModule = remember { StudentStatusModule() }
    var name by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var race by remember { mutableStateOf("") }
    var nativePlace by remember { mutableStateOf("") }
    var studentId by remember { mutableStateOf("") }
    var major by remember { mutableStateOf("") }
    var academy by remember { mutableStateOf("") }
    var students by remember { mutableStateOf(listOf<StudentStatusModule>()) }
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var filePath by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(start = 16.dp)) {
        pageTitle(heading = "增加学籍信息", caption = "填写学籍信息")

        // 第一行
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("姓名") },
                modifier = Modifier.weight(1f).padding(end = 16.dp)
            )
            OutlinedTextField(
                value = gender,
                onValueChange = { gender = it },
                label = { Text("性别") },
                modifier = Modifier.weight(1f).padding(end = 16.dp)
            )
            OutlinedTextField(
                value = race,
                onValueChange = { race = it },
                label = { Text("民族") },
                modifier = Modifier.weight(1f)
            )
        }

        // 第二行
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            OutlinedTextField(
                value = nativePlace,
                onValueChange = { nativePlace = it },
                label = { Text("籍贯") },
                modifier = Modifier.weight(1f).padding(end = 16.dp)
            )
            OutlinedTextField(
                value = studentId,
                onValueChange = { studentId = it },
                label = { Text("一卡通") },
                modifier = Modifier.weight(1f)
            )
        }

        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            OutlinedTextField(
                value = major,
                onValueChange = { major = it },
                label = { Text("专业") },
                modifier = Modifier.weight(1f).padding(end = 16.dp)
            )
            OutlinedTextField(
                value = academy,
                onValueChange = { academy = it },
                label = { Text("学院") },
                modifier = Modifier.weight(1f)
            )
        }

        // 从文件导入和提交按钮
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(
                onClick = {
                    val fileChooser = JFileChooser().apply {
                        fileFilter = FileNameExtensionFilter("Excel Files", "xls", "xlsx")
                        isAcceptAllFileFilterUsed = false
                    }
                    val result = fileChooser.showOpenDialog(null)
                    if (result == JFileChooser.APPROVE_OPTION) {
                        val selectedFile = fileChooser.selectedFile
                        filePath = selectedFile.absolutePath
                        val newStudents = readExcelFile(selectedFile)
                        students = newStudents
                        showDialog = true
                    }
                },
                modifier = Modifier.width(120.dp) // Set the width of the import button
            ) {
                Text("从文件导入")
            }
            Button(
                onClick = {
                    scope.launch {
                        studentStatusModule.addStudentStatus()
                    }
                },
                modifier = Modifier.width(120.dp) // Set the width of the submit button
            ) {
                Text("提交")
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            text = {
                AddFromFileSubscene(students, onUpdateFile = { updatedStudents ->
                    students = updatedStudents
                }, filePath = filePath)
            },
            confirmButton = {
                Button(onClick = {
                    scope.launch {
                        students.forEach { student ->
                            student.addStudentStatus()
                        }
                        showDialog = false
                    }
                }) {
                    Text("导入")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("取消")
                }
            }
        )
    }
}

fun readExcelFile(file: File): List<StudentStatusModule> {
    val students = mutableListOf<StudentStatusModule>()
    val workbook = WorkbookFactory.create(file)
    val sheet = workbook.getSheetAt(0)
    for (row in sheet) {
        if (row.rowNum == 0) continue // 跳过表头行
        val student = StudentStatusModule().apply {
            name = getCellValueAsString(row.getCell(0))
            gender = getCellValueAsString(row.getCell(1))
            race = getCellValueAsString(row.getCell(2))
            nativePlace = getCellValueAsString(row.getCell(3))
            studentId = getCellValueAsString(row.getCell(4))
            major = getCellValueAsString(row.getCell(5))
            academy = getCellValueAsString(row.getCell(6))
        }
        students.add(student)
    }
    workbook.close()
    return students
}

fun getCellValueAsString(cell: Cell?): String {
    return when (cell?.cellType) {
        CellType.STRING -> cell.stringCellValue
        CellType.NUMERIC -> {
            val formatter = DataFormatter()
            formatter.formatCellValue(cell)
        }
        CellType.BOOLEAN -> cell.booleanCellValue.toString()
        CellType.FORMULA -> cell.cellFormula
        else -> ""
    }
}