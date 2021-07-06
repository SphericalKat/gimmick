package at.sphericalk.gidget.ui.routes

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import at.sphericalk.gidget.LocalActivity
import at.sphericalk.gidget.R
import at.sphericalk.gidget.ui.composables.Loading
import at.sphericalk.gidget.utils.Constants
import com.google.accompanist.insets.statusBarsPadding


@Composable
fun Welcome(navController: NavController) {
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
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(30.dp),
                modifier = Modifier
                    .padding(24.dp)

            ) {
                val activity = LocalActivity.current
                var isLoading by remember { mutableStateOf(false) }
                Image(painter = painterResource(id = R.drawable.ic_gimmick_icon), contentDescription = "Github Icon")
                Text(
                    text = "Welcome to your new and improved GitHub feed.",
                    style = MaterialTheme.typography.h6
                )
                // TODO (sphericalkat): add gimmick logo / icon
                if (isLoading) {
                    Loading()
                } else {
                    Button(
                        onClick = {
                            isLoading = true
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.OAUTH_URL))
                            activity.startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.github_icon),
                            contentDescription = null
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(text = "Sign in ", color = Color.Black)
                    }
                }
            }
        }
    }
}