package at.sphericalk.gidget.ui.routes

import android.app.Activity
import android.util.Log
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
import androidx.datastore.preferences.core.edit
import androidx.navigation.NavController
import at.sphericalk.gidget.LocalActivity
import at.sphericalk.gidget.dataStore
import at.sphericalk.gidget.utils.Constants
import com.google.accompanist.insets.statusBarsPadding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.auth.OAuthProvider
import kotlinx.coroutines.runBlocking

private lateinit var auth: FirebaseAuth

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
fun Welcome(navController: NavController) {
    auth = FirebaseAuth.getInstance()

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
            val activity = LocalActivity.current
            Text(text = "Welcome to Gidget")
            Button(onClick = {
                handleLogin(activity) {
                    navController.navigate("feed")
                }
            }) {
                Text(text = "Login")
            }
        }
    }
}

fun handleLogin(
    activity: Activity,
    onLoggedIn: () -> Unit,
) {
    val provider = OAuthProvider.newBuilder("github.com")
    provider.scopes = arrayListOf("read:user", "user:repo")

    val pendingResult = auth.pendingAuthResult
    if (pendingResult != null) {
        pendingResult.addOnSuccessListener {
            onLoggedIn()
        }.addOnFailureListener {
            Log.e("LOGIN", "Failure logging in", it)
        }
    } else {
        auth.startActivityForSignInWithProvider(activity, provider.build())
            .addOnSuccessListener {
                val credential = it.credential as OAuthCredential
                runBlocking {
                    activity.dataStore.edit { prefs ->
                        prefs[Constants.API_KEY] = credential.accessToken!!
                    }
                }
                onLoggedIn()
            }.addOnFailureListener {
                Log.e("LOGIN", "Failure logging in", it)
            }
    }
}