package at.sphericalk.gidget.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import at.sphericalk.gidget.R

val ManropeLight = FontFamily(Font(R.font.manrope_light))
val ManropeRegular = FontFamily(Font(R.font.manrope_regular))
val ManropeBold = FontFamily(Font(R.font.manrope_bold))

const val fontFeatures = "calt, liga"

fun buildTypography(colors: ColorType) = Typography(
    h1 = TextStyle(
        fontSize = 96.sp,
        letterSpacing = (-1.5).sp,
        color = colors.textPrimary,
        fontFamily = ManropeLight,
        fontFeatureSettings = fontFeatures
    ),
    h2 = TextStyle(
        fontSize = 60.sp,
        letterSpacing = (-0.5).sp,
        color = colors.textPrimary,
        fontFamily = ManropeLight,
        fontFeatureSettings = fontFeatures
    ),
    h3 = TextStyle(
        fontSize = 48.sp,
        color = colors.textPrimary,
        fontFamily = ManropeRegular,
        fontFeatureSettings = fontFeatures
    ),
    h4 = TextStyle(
        fontSize = 34.sp,
        letterSpacing = .25.sp,
        color = colors.textPrimary,
        fontFamily = ManropeRegular,
        fontFeatureSettings = fontFeatures
    ),
    h5 = TextStyle(
        fontSize = 24.sp,
        color = colors.textPrimary,
        fontFamily = ManropeRegular,
        fontFeatureSettings = fontFeatures
    ),
    h6 = TextStyle(
        fontSize = 20.sp,
        letterSpacing = .15.sp,
        color = colors.textPrimary,
        fontFamily = ManropeBold,
        fontFeatureSettings = fontFeatures
    ),
    subtitle1 = TextStyle(
        fontSize = 16.sp,
        letterSpacing = .1.sp,
        color = colors.textSecondary,
        fontFamily = ManropeBold,
        fontFeatureSettings = fontFeatures
    ),
    subtitle2 = TextStyle(
        fontSize = 14.sp,
        letterSpacing = .5.sp,
        color = colors.textPrimary,
        fontFamily = ManropeRegular,
        fontFeatureSettings = fontFeatures
    ),
    body1 = TextStyle(
        fontSize = 16.sp,
        letterSpacing = .5.sp,
        color = colors.textPrimary,
        fontFamily = ManropeRegular,
        fontFeatureSettings = fontFeatures
    ),
    body2 = TextStyle(
        fontSize = 14.sp,
        letterSpacing = .25.sp,
        color = colors.textPrimary,
        fontFamily = ManropeRegular,
        fontFeatureSettings = fontFeatures
    ),
    button = TextStyle(
        fontSize = 14.sp,
        letterSpacing = 1.5.sp,
        color = colors.textPrimary,
        fontFamily = ManropeBold,
        fontFeatureSettings = fontFeatures
    ),
    caption = TextStyle(
        fontSize = 12.sp,
        letterSpacing = .4.sp,
        color = colors.textPrimary,
        fontFamily = ManropeRegular,
        fontFeatureSettings = fontFeatures
    ),
    overline = TextStyle(
        fontSize = 10.sp,
        letterSpacing = 2.5.sp,
        color = colors.textPrimary,
        fontFamily = ManropeRegular,
        fontFeatureSettings = fontFeatures
    ),
)