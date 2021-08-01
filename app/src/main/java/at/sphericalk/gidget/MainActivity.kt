package at.sphericalk.gidget

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import at.sphericalk.gidget.ui.routes.Feed
import at.sphericalk.gidget.ui.routes.FeedViewModel
import at.sphericalk.gidget.ui.routes.Release
import at.sphericalk.gidget.ui.routes.Welcome
import at.sphericalk.gidget.ui.theme.GidgetTheme
import at.sphericalk.gidget.utils.Constants
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

val Context.dataStore by preferencesDataStore(name = "settings")
val LocalActivity = compositionLocalOf<Activity> { error("No context found!") }

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: FeedViewModel by viewModels()
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProvideWindowInsets {
                GidgetTheme {
                    val systemUiController = rememberSystemUiController()
                    val useDarkIcons = MaterialTheme.colors.isLight
                    val bgColor = MaterialTheme.colors.background

                    SideEffect {
                        systemUiController.setStatusBarColor(
                            color = Color.Transparent,
                            darkIcons = useDarkIcons
                        )
                        systemUiController.setNavigationBarColor(
                            color = bgColor,
                            darkIcons = useDarkIcons
                        )
                    }

                    CompositionLocalProvider(LocalActivity provides this) {
                        navController = rememberNavController()
                        Home(viewModel, navController)
                    }
                }
            }
        }
        // This must be called after setContent
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    override fun onResume() {
        super.onResume()
        val uri = intent.data

        if (uri.toString().startsWith(Constants.REDIRECT_URI)) {
            viewModel.getAccessToken(
                BuildConfig.CLIENT_ID,
                BuildConfig.CLIENT_SECRET,
                uri?.getQueryParameter("code").toString(),
                Constants.REDIRECT_URI
            ).observe(this) {
                if (it != null) {
                    runBlocking {
                        dataStore.edit { prefs ->
                            prefs[Constants.API_KEY] = it.first
                            prefs[Constants.USERNAME] = it.second
                        }
                    }
                    navController.navigate("feed") {
                        popUpTo("welcome") { inclusive = true }
                    }
                }
            }
        }
    }
}

@Composable
fun Home(viewModel: FeedViewModel, navController: NavHostController) {
    val dataStore = LocalActivity.current.dataStore

    val token = runBlocking { dataStore.data.first()[Constants.API_KEY] }
    val startDestination = if (token == null) "welcome" else "feed"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("welcome") { Welcome(navController) }
        composable("feed") { Feed(navController, viewModel) }
        composable("release") { Release(navController, viewModel) }
    }
}
