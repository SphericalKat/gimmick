package at.sphericalk.gidget.utils

import android.text.format.DateUtils
import android.text.format.DateUtils.FORMAT_ABBREV_RELATIVE
import android.util.Log
import androidx.compose.ui.graphics.Color
import java.text.SimpleDateFormat
import java.util.*

fun <T> List<T>.destructure() = Pair(get(0), get(1))

fun String.toColor() = Color(android.graphics.Color.parseColor(this))

fun String.timeAgo(): String {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val date = sdf.parse(this)
        DateUtils.getRelativeTimeSpanString(
            date.time,
            Date().time,
            DateUtils.MINUTE_IN_MILLIS,
            FORMAT_ABBREV_RELATIVE
        ).toString()
    } catch (e: Exception) {
        Log.e("DATE", "Something went wrong while getting relative time: ", e)
        ""
    }
}