package at.sphericalk.gidget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import at.sphericalk.gidget.ui.routes.Feed
import at.sphericalk.gidget.ui.routes.Welcome
import at.sphericalk.gidget.ui.theme.GidgetTheme
import com.google.accompanist.insets.ProvideWindowInsets

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ProvideWindowInsets {
                GidgetTheme {
                    Home()
                }
            }
        }
    }
}

@Composable
fun Home() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") { Welcome() }
        composable("feed") { Feed() }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GidgetTheme {
        Home()
    }
}