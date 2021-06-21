package at.sphericalk.gidget.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = DarkColors.primaryColor,
    primaryVariant = DarkColors.primaryColor,
    secondary = DarkColors.primaryColor,
    secondaryVariant = DarkColors.primaryColor,
    surface = DarkColors.backgroundElevated,
    onBackground = DarkColors.background,
    onSurface = DarkColors.textPrimary,
    background = DarkColors.background,
    onPrimary = DarkColors.background,
    onSecondary = DarkColors.background,
)

private val LightColorPalette = lightColors(
    primary = LightColors.primaryColor,
    primaryVariant = LightColors.primaryColor,
    secondary = LightColors.primaryColor,
    secondaryVariant = LightColors.primaryColor,
    surface = LightColors.backgroundSecondary,
    onBackground = LightColors.background,
    onSurface = LightColors.textPrimary,
    background = LightColors.background,
    onPrimary = LightColors.background,
    onSecondary = LightColors.background,
)

@Composable
fun GidgetTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val colorType = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colors = colors,
        typography = buildTypography(colorType),
        shapes = Shapes,
        content = content
    )
}