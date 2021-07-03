package at.sphericalk.gidget.ui.routes

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import at.sphericalk.gidget.LocalActivity
import at.sphericalk.gidget.utils.Constants
import com.google.accompanist.insets.statusBarsPadding
import com.google.firebase.auth.FirebaseAuth

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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
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
//    val provider = OAuthProvider.newBuilder("github.com")
//    provider.scopes = arrayListOf("read:user", "user:repo")
//
//    val pendingResult = auth.pendingAuthResult
//    if (pendingResult != null) {
//        pendingResult.addOnSuccessListener {
//            onLoggedIn()
//        }.addOnFailureListener {
//            Log.e("LOGIN", "Failure logging in", it)
//        }
//    } else {
//        auth.startActivityForSignInWithProvider(activity, provider.build())
//            .addOnSuccessListener {
//                val credential = it.credential as OAuthCredential
//                runBlocking {
//                    activity.dataStore.edit { prefs ->
//                        prefs[Constants.API_KEY] = credential.accessToken!!
//                        prefs[Constants.USERNAME] = it.additionalUserInfo?.username!!
//                    }
//                }
//                onLoggedIn()
//            }.addOnFailureListener {
//                Log.e("LOGIN", "Failure logging in", it)
//            }
//    }
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.OAUTH_URL))
    activity.startActivity(intent)
}