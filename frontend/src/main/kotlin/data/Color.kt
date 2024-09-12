
package data

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color

object ColorPack {
    var choose = mutableStateOf(0)
    var mainColor1 = arrayOf(
        mutableStateOf(Color(0xff21563a)), // 主题色：绿
        mutableStateOf(Color(0xffc85d50)), // Background color
    )
    var mainColor2 = arrayOf(
        mutableStateOf(Color(0xffdce812)), // 次主题色：黄
        mutableStateOf(Color(0xff5be0ff)), // Background color
    )
    var sideColor1 = arrayOf(
        mutableStateOf(Color(0xff9eb397)), // 点缀色：深绿
        mutableStateOf(Color(0xffb88372)), // Background color
    )
    var sideColor2 = arrayOf(
        mutableStateOf(Color(0xff373836)), // 次点缀色：墨绿
        mutableStateOf(Color(0xff340611)), // Background color
    )
    var backgroundColor1 = arrayOf(
        mutableStateOf(Color(0xfff4f7f1)), // 背景色：淡绿
        mutableStateOf(Color(0xfffce3e3)), // Background color
    )
    var backgroundColor2 = arrayOf(
        mutableStateOf(Color(0xffd4eae1)), // 次背景色：浅蓝
        mutableStateOf(Color(0xffdbc8f0)), // Background color
    )
}