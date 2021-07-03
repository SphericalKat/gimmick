package at.sphericalk.gidget.ui.routes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import at.sphericalk.gidget.LocalActivity
import at.sphericalk.gidget.dataStore
import at.sphericalk.gidget.R
import at.sphericalk.gidget.utils.Constants
import coil.transform.CircleCropTransformation
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.insets.statusBarsPadding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

@Composable
fun Feed(navController: NavController, viewModel: FeedViewModel) {
    val auth = FirebaseAuth.getInstance()

    // check if current user is signed in
    if (auth.currentUser == null) {
        navController.navigate("welcome")
    }
    val datastore = LocalActivity.current.dataStore
    val username = runBlocking { datastore.data.map { it[Constants.USERNAME] ?: "" }.first() }

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
                    Text(text = "@$username's feed")
                }
            },
        )
    }) {
        LazyColumn(Modifier.padding(horizontal = 24.dp)) {
            items(viewModel.events) { event ->
                Row(
                    modifier = Modifier.padding(vertical = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = rememberCoilPainter(
                            request = event.actor.avatar_url,
                            requestBuilder = {
                                transformations(CircleCropTransformation())
                            },
                            previewPlaceholder = R.drawable.ic_launcher_background,
                            fadeIn = true
                        ),
                        contentDescription = event.actor.login,
                        modifier = Modifier.size(32.dp, 32.dp),
                    )
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(event.actor.login)
                            }
                            append(" ")
                            append(event.type.toString())
                            append(" ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(event.repo.name)
                            }
                        },
                    )
                }
            }
        }
    }
}
