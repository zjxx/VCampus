package view

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import module.Class
import module.CourseModule
import module.videoClass
import view.component.CameraComponent
import view.component.VideoPlayerScreen
import view.component.classCard
import view.component.classVideoCard

@Composable
fun StudentCourseSubscene(classes: List<videoClass>) {
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        classes.forEach { classItem: videoClass ->
            classVideoCard(
                courseName = classItem.courseName,
                courseId = classItem.courseId,
                timeAndLocationCards = classItem.timeAndLocationCards,
                videoCount = classItem.videos.size,
                videos = classItem.videos,
                onRecordNewVideo = {
                    // No action needed for students
                }
            )
        }
    }
}