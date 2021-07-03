package at.sphericalk.gidget.ui.routes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.insets.statusBarsPadding
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Feed(navController: NavController, viewModel: FeedViewModel) {
    val auth = FirebaseAuth.getInstance()

    // check if current user is signed in
    if (auth.currentUser == null) {
        navController.navigate("welcome")
    }

    viewModel.fetchEvents()

    Scaffold(topBar = {
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
    }) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = viewModel.events.joinToString { it.repo.name })
        }
    }
}
