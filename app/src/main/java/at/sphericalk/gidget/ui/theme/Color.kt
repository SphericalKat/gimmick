package at.sphericalk.gidget.ui.theme

import androidx.compose.ui.graphics.Color


interface ColorType {
    val background: Color
    val backgroundElevated: Color
    val backgroundSecondary: Color

    val primaryColor: Color

    val textPrimary: Color
    val textSecondary: Color

    val textFieldBackground: Color
}

object LightColors : ColorType {
    override val background = Color(0xfff0f0f0)
    override val backgroundElevated = Color(0xfff5f5f5)
    override val backgroundSecondary = Color.White

    override val primaryColor = Color(0xff1f99b7)

    override val textPrimary = Color(0xff141414)
    override val textSecondary = Color(0xff444444)

    override val textFieldBackground = Color(0x14212121)
}

object DarkColors : ColorType {
    override val background = Color(0xff111517)
    override val backgroundElevated = Color(0xff1F2022)
    override val backgroundSecondary = Color(0xff020202)

    override val primaryColor = Color(0xff4dd0e1)

    override val textPrimary = Color(0xfff9f9f9)
    override val textSecondary = Color(0xffcccccc)

    override val textFieldBackground = Color(0x14ffffff)
}