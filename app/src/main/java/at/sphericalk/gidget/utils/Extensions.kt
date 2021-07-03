package at.sphericalk.gidget.utils

import androidx.compose.ui.graphics.Color

fun <T> List<T>.destructure() = Pair(get(0), get(1))

fun String.toColor() = Color(android.graphics.Color.parseColor(this))