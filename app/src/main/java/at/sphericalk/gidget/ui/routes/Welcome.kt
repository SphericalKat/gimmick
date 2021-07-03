package at.sphericalk.gidget.ui.routes

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.navigation.NavController
import at.sphericalk.gidget.LocalActivity
import at.sphericalk.gidget.R
import at.sphericalk.gidget.dataStore
import at.sphericalk.gidget.utils.Constants
import com.google.accompanist.insets.statusBarsPadding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.auth.OAuthProvider
import kotlinx.coroutines.runBlocking

private lateinit var auth: FirebaseAuth

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
                        Text(text = "Gimmick")
                    }
                },
            )
        },
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(30.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()

            ) {
                val activity = LocalActivity.current
                Text(
                    text = "Welcome to Gidget,\n your new and improved GitHub feed",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h6
                )
                Button(onClick = {
                    handleLogin(activity) {
                        navController.navigate("feed")
                    }
                }) {
                    Icon(painter = painterResource(id = R.drawable.github_icon), contentDescription = null)
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(text = "Sign in ", color = Color.Black)
                }
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
                        prefs[Constants.USERNAME] = it.additionalUserInfo?.username!!
                    }
                }
                onLoggedIn()
            }.addOnFailureListener {
                Log.e("LOGIN", "Failure logging in", it)
            }
    }
}