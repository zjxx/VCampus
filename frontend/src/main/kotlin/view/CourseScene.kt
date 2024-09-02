// src/main/kotlin/view/CourseScene.kt
package view

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun CourseScene(onNavigate: (String) -> Unit, role: String) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Title
        Text(
            text = "课程表",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )

        // Divider
        Divider(
            color = Color(0xFFFFD700), // Dark yellow color
            thickness = 4.dp,
            modifier = Modifier
                .fillMaxWidth(0.382f)
                .padding(vertical = 8.dp)
        )

        // Content
        // Add your course-related content here
    }
}