// src/main/kotlin/view/component/FileCard.kt
package view.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import module.StudentStatusModule
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ArrowDropDown
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.DataFormatter
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@Composable
fun FileCard(
    studentStatusModule: StudentStatusModule,
    onDeleteSuccess: () -> Unit,
    onUpdateSuccess: (StudentStatusModule) -> Unit,
    filePath: String
) {
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var genderExpanded by remember { mutableStateOf(false) }
    val genderOptions = listOf("男", "女")

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("确认删除") },
            text = { Text("你确定要删除这个学生信息吗？") },
            confirmButton = {
                Button(onClick = {
                    // Perform file delete operation
                    deleteStudentFromFile(filePath, studentStatusModule)
                    onDeleteSuccess()
                    showDialog = false
                }) {
                    Text("确认")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("取消")
                }
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "学生姓名: ${studentStatusModule.name}")
                    Text(text = "一卡通: ${studentStatusModule.studentId}")
                }
                if (!expanded) {
                    IconButton(onClick = { showDialog = true }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "删除")
                    }
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(start = 16.dp)) {
                    // 第一行
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                        OutlinedTextField(
                            value = studentStatusModule.name,
                            onValueChange = { studentStatusModule.name = it },
                            label = { Text("姓名") },
                            modifier = Modifier.weight(1f).padding(end = 16.dp)
                        )
                        Box(modifier = Modifier.weight(1f).padding(end = 16.dp)) {
                            OutlinedTextField(
                                value = studentStatusModule.gender,
                                onValueChange = {},
                                label = { Text("性别") },
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = true,
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = null,
                                        modifier = Modifier.clickable { genderExpanded = true }
                                    )
                                }
                            )
                            DropdownMenu(
                                expanded = genderExpanded,
                                onDismissRequest = { genderExpanded = false },
                                modifier = Modifier.align(Alignment.TopStart)
                            ) {
                                genderOptions.forEach { option ->
                                    DropdownMenuItem(onClick = {
                                        studentStatusModule.gender = option
                                        genderExpanded = false
                                    }) {
                                        Text(text = option)
                                    }
                                }
                            }
                        }
                        OutlinedTextField(
                            value = studentStatusModule.race,
                            onValueChange = { studentStatusModule.race = it },
                            label = { Text("民族") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // 第二行
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                        OutlinedTextField(
                            value = studentStatusModule.nativePlace,
                            onValueChange = { studentStatusModule.nativePlace = it },
                            label = { Text("籍贯") },
                            modifier = Modifier.weight(1f).padding(end = 16.dp)
                        )
                        OutlinedTextField(
                            value = studentStatusModule.studentId,
                            onValueChange = { studentStatusModule.studentId = it },
                            label = { Text("一卡通") },
                            readOnly = true,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                        OutlinedTextField(
                            value = studentStatusModule.major,
                            onValueChange = { studentStatusModule.major = it },
                            label = { Text("专业") },
                            modifier = Modifier.weight(1f).padding(end = 16.dp)
                        )
                        OutlinedTextField(
                            value = studentStatusModule.academy,
                            onValueChange = { studentStatusModule.academy = it },
                            label = { Text("学院") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Button(onClick = {
                            // Perform file update operation
                            updateStudentInFile(filePath, studentStatusModule)
                            onUpdateSuccess(studentStatusModule)
                            expanded = false // 确认修改后收起卡片
                        }) {
                            Text("确认修改")
                        }
                    }
                }
            }
        }
    }
}


fun deleteStudentFromFile(filePath: String, student: StudentStatusModule) {
    val file = File(filePath)
    val workbook = FileInputStream(file).use { WorkbookFactory.create(it) }
    val sheet = workbook.getSheetAt(0)
    val rowIterator = sheet.iterator()
    while (rowIterator.hasNext()) {
        val row = rowIterator.next()
        if (getCellValueAsString(row.getCell(4)) == student.studentId) {
            sheet.removeRow(row)
            break
        }
    }
    FileOutputStream(file).use { workbook.write(it) }
    workbook.close()
}

fun updateStudentInFile(filePath: String, student: StudentStatusModule) {
    val file = File(filePath)
    val workbook = FileInputStream(file).use { WorkbookFactory.create(it) }
    val sheet = workbook.getSheetAt(0)
    for (row in sheet) {
        if (getCellValueAsString(row.getCell(4)) == student.studentId) {
            row.getCell(0).setCellValue(student.name)
            row.getCell(1).setCellValue(student.gender)
            row.getCell(2).setCellValue(student.race)
            row.getCell(3).setCellValue(student.nativePlace)
            row.getCell(5).setCellValue(student.major)
            row.getCell(6).setCellValue(student.academy)
            break
        }
    }
    FileOutputStream(file).use { workbook.write(it) }
    workbook.close()
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