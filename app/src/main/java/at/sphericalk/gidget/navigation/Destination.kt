package at.sphericalk.gidget.navigation

import androidx.navigation.NavController

sealed class Destination(val route: String) {
    object Welcome : Destination("welcome")
    object Feed : Destination("feed")
    object Release : Destination("release")
}

class AppActions(navController: NavController) {
    val navigateToFeed: () -> Unit = {
        navController.navigate(Destination.Feed.route) {
            popUpTo(Destination.Welcome.route) { inclusive = true }
        }
    }
}