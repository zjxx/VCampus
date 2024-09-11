package data

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color

object BackgroundColor {
    var colors = arrayOf(
        mutableStateOf(Color(0xFF006400)), // Navigation bar color
        mutableStateOf(Color(0xFF006400)),       // Background color
        mutableStateOf(Color(0xFF006400))  // Button color
    )
}