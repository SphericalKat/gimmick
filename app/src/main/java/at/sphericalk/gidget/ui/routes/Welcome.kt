package at.sphericalk.gidget.ui.routes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding

class PrefixTransformation(val prefix: String, val color: Color) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return prefixFilter(text, prefix, color)
    }
}

fun prefixFilter(text: AnnotatedString, prefix: String, color: Color): TransformedText {
    val prefixOffset = prefix.length
    val out = with(AnnotatedString.Builder()) {
        append(prefix)
        append(text)
        addStyle(SpanStyle(color = color), 0, 1)
        toAnnotatedString()
    }

    val numberOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            return offset + prefixOffset
        }

        override fun transformedToOriginal(offset: Int): Int {
            if (offset <= prefixOffset - 1) return prefixOffset
            return offset - prefixOffset
        }
    }

    return TransformedText(out, numberOffsetTranslator)
}

@Composable
fun Welcome() {
    Scaffold(
        topBar = {
            TopAppBar(
                elevation = 0.dp,
                modifier = Modifier
                    .background(MaterialTheme.colors.background)
                    .statusBarsPadding(),
                backgroundColor = MaterialTheme.colors.background,
                title = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(text = "Gidget")
                    }
                },
            )
        },
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            var username by remember { mutableStateOf("") }
            Text(text = "Welcome to Gidget")
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text(text = "Enter username") },
                maxLines = 1,
                visualTransformation = PrefixTransformation("@", Color.DarkGray)
            )
        }
    }
}