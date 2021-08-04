package at.sphericalk.gidget

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import at.sphericalk.gidget.navigation.AppActions
import at.sphericalk.gidget.navigation.Destination
import at.sphericalk.gidget.ui.routes.Feed
import at.sphericalk.gidget.ui.routes.FeedViewModel
import at.sphericalk.gidget.ui.routes.Release
import at.sphericalk.gidget.ui.routes.Welcome
import at.sphericalk.gidget.ui.theme.GidgetTheme
import at.sphericalk.gidget.utils.Constants
import at.sphericalk.gidget.utils.LanguageColors
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

val Context.dataStore by preferencesDataStore(name = "settings")
val LocalActivity = compositionLocalOf<Activity> { error("No context found!") }

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: FeedViewModel by viewModels()
    private lateinit var navController: NavHostController
    @Inject
    lateinit var languageColors: LanguageColors

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
                        Home(viewModel, navController, languageColors)
                    }
                }
            }
        }
        // This must be called after setContent
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    @Composable
    fun Home(
        viewModel: FeedViewModel,
        navController: NavHostController,
        languageColors: LanguageColors
    ) {
        val dataStore = LocalActivity.current.dataStore

        val token = runBlocking { dataStore.data.first()[Constants.API_KEY] }
        val startDestination =
            if (token == null) Destination.Welcome.route else Destination.Feed.route
        val appActions = remember(navController) { AppActions(navController) }

        NavHost(navController = navController, startDestination = startDestination) {
            composable(Destination.Welcome.route) { Welcome(viewModel, appActions.navigateToFeed) }
            composable(Destination.Feed.route) { Feed(navController, viewModel, languageColors) }
            composable(Destination.Release.route) { Release(navController, viewModel) }
        }
    }
}
