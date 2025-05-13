package com.example.superahorro.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.superahorro.R


val arialBlackFont = FontFamily(Font(R.font.ariblk))

val AppBarTitleStyle = TextStyle(
    fontFamily = arialBlackFont,
    fontWeight = FontWeight.Black,
    fontSize = 20.sp
)

val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp
    )
)