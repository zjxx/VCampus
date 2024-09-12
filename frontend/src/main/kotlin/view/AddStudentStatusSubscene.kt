package view

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import module.StudentStatusModule
import view.component.pageTitle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var filePath by remember { mutableStateOf("") }
    val genderOptions = listOf("男", "女")
    var expanded by remember { mutableStateOf(false) }
    val validMajors = listOf(
        "建筑学", "城乡规划", "风景园林", "机械工程", "能源与动力工程", "建筑环境与能源应用工程",
        "核工程与核技术", "新能源科学与工程", "环境工程", "自动化", "机器人工程", "电气工程及其自动化",
        "智能感知工程", "测控技术与仪器", "材料科学与工程", "土木工程", "给排水科学与工程", "工程管理",
        "智能建造", "交通工程", "交通运输", "港口航道与海岸工程", "城市地下空间工程", "道路桥梁与渡河工程",
        "智慧交通", "信息工程", "海洋信息工程", "电子科学与技术", "信息工程","计算机科学与技术","软件工程","物联网工程","人工智能"
    )
val validAcademies = listOf(
    "建筑学院", "机械工程学院", "能源与环境学院", "信息科学与工程学院", "土木工程学院",
    "电子科学与工程学院", "数学系", "自动化学院", "计算机科学与工程学院", "物理系",
    "生物科学与医学工程学院", "材料科学与工程学院", "人文学院", "经济管理学院", "电气工程学院",
    "外国语学院", "体育系", "化学化工学院", "交通学院", "仪器科学与工程学院", "艺术学院",
    "法学院", "医学院", "公共卫生学院", "吴健雄学院", "海外教育学院", "软件学院",
    "微电子学院", "马克思主义学院", "生命科学研究院"
)
    var majorError by remember { mutableStateOf(false) }
    var academyError by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(start = 16.dp)) {
        pageTitle(heading = "增加学籍信息", caption = "填写学籍信息")

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
                    onValueChange = { studentStatusModule.gender = it },
                    label = { Text("性别") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    genderOptions.forEach { gender ->
                        DropdownMenuItem(onClick = {
                            studentStatusModule.gender = gender
                            expanded = false
                        }) {
                            Text(text = gender)
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
                modifier = Modifier.weight(1f)
            )
        }

        // 第三行
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            OutlinedTextField(
                value = studentStatusModule.major,
                onValueChange = {
                    studentStatusModule.major = it
                    majorError = !validMajors.contains(it)
                },
                label = { Text("专业") },
                modifier = Modifier.weight(1f).padding(end = 16.dp),
                isError = majorError
            )
            if (majorError) {
                Text("未找到相关专业", color = MaterialTheme.colors.error, modifier = Modifier.padding(start = 16.dp))
            }
        }

        // 第四行
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            OutlinedTextField(
                value = studentStatusModule.academy,
                onValueChange = {
                    studentStatusModule.academy = it
                    academyError = !validAcademies.contains(it)
                },
                label = { Text("学院") },
                modifier = Modifier.weight(1f).padding(end = 16.dp),
                isError = academyError
            )
            if (academyError) {
                Text("未找到相关学院", color = MaterialTheme.colors.error, modifier = Modifier.padding(start = 16.dp))
            }
        }

        // 从文件导入和提交按钮
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
           Column{
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
                        studentStatusModule.students = newStudents
                        showDialog = true
                    }
                },
                modifier = Modifier.width(160.dp) // Set the width of the import button
            ) {
                Text("从文件导入")
            }
            Button(
                onClick = {
                },
                modifier = Modifier.width(160.dp) // Set the width of the download button
            ) {
                Text("下载示例文件")
            }
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
                Box(modifier = Modifier.height(800.dp)) { // Set a fixed height for the dialog
                    Column {
                        AddFromFileSubscene(studentStatusModule.students, onUpdateFile = { updatedStudents ->
                            studentStatusModule.students = updatedStudents
                        }, filePath = filePath)
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    scope.launch {

                        val studentList = studentStatusModule.students.map { student ->
                            module.Merchandise(
                                username = student.name,
                                gender = if (student.gender == "男") "0" else "1",
                                race = student.race,
                                nativePlace = student.nativePlace,
                                studentId = student.studentId,
                                major = student.major,
                                academy = student.academy
                            )
                        }
                        studentStatusModule.Studentfile(studentList)
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
    if (studentStatusModule.majorErrorDialog) {
        AlertDialog(
            onDismissRequest = { studentStatusModule.majorErrorDialog = false },
            title = { Text("错误") },
            text = { Text("未找到相关专业，请检查输入。") },
            confirmButton = {
                Button(onClick = { studentStatusModule.majorErrorDialog = false }) {
                    Text("确定")
                }
            }
        )
    }
    if (studentStatusModule.fieldErrorDialog) {
        AlertDialog(
            onDismissRequest = { studentStatusModule.fieldErrorDialog = false },
            title = { Text("错误") },
            text = { Text("所有字段均不能为空，请检查输入。") },
            confirmButton = {
                Button(onClick = { studentStatusModule.fieldErrorDialog = false }) {
                    Text("确定")
                }
            }
        )
    }
    if (studentStatusModule.cardNumberErrorDialog) {
        AlertDialog(
            onDismissRequest = { studentStatusModule.cardNumberErrorDialog = false },
            title = { Text("错误") },
            text = { Text("一卡通号必须以2开头且为9位，请检查输入。") },
            confirmButton = {
                Button(onClick = { studentStatusModule.cardNumberErrorDialog = false }) {
                    Text("确定")
                }
            }
        )
    }
    if (studentStatusModule.nameErrorDialog) {
        AlertDialog(
            onDismissRequest = { studentStatusModule.nameErrorDialog = false },
            title = { Text("错误") },
            text = { Text("姓名格式不正确，请检查输入。") },
            confirmButton = {
                Button(onClick = { studentStatusModule.nameErrorDialog = false }) {
                    Text("确定")
                }
            }
        )
    }
    if (studentStatusModule.raceErrorDialog) {
        AlertDialog(
            onDismissRequest = { studentStatusModule.raceErrorDialog = false },
            title = { Text("错误") },
            text = { Text("未找到相关民族，请检查输入。") },
            confirmButton = {
                Button(onClick = { studentStatusModule.raceErrorDialog = false }) {
                    Text("确定")
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