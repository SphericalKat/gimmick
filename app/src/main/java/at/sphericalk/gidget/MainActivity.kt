package at.sphericalk.gidget

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.core.view.WindowCompat
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import at.sphericalk.gidget.ui.routes.Feed
import at.sphericalk.gidget.ui.routes.FeedViewModel
import at.sphericalk.gidget.ui.routes.Welcome
import at.sphericalk.gidget.ui.theme.GidgetTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

val Context.dataStore by preferencesDataStore(name = "settings")
val LocalActivity = compositionLocalOf<Activity> { error("No context found!") }

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: FeedViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ProvideWindowInsets {
                GidgetTheme {
                    CompositionLocalProvider(LocalActivity provides this) {
                        Home(viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun Home(viewModel: FeedViewModel) {
    val navController = rememberNavController()
    val startDestination = if (FirebaseAuth.getInstance().currentUser != null) "feed" else "welcome"
    NavHost(navController = navController, startDestination = startDestination) {
        composable("welcome") { Welcome(navController) }
        composable("feed") { Feed(navController, viewModel) }
    }
}
