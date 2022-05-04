package net.wangyl.life.compose.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.*
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.dialog
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import net.wangyl.base.manager.AppManager
import net.wangyl.base.manager.startFragment
import net.wangyl.life.ui.settings.SettingsFragment

@ExperimentalAnimationApi
@Composable
fun AppNavigation(navController: NavHostController,
                    modifier: Modifier = Modifier,) {
    AnimatedNavHost(
        navController = navController,
        startDestination = AppScreen.News.route,
        enterTransition = { defaultEnterTransition(initialState, targetState) },
        exitTransition = { defaultExitTransition(initialState, targetState) },
        popEnterTransition = { defaultPopEnterTransition() },
        popExitTransition = { defaultPopExitTransition() },
        modifier = modifier
    ) {
        addNewsTopLevel(navController) //添加新闻页面
        addShelfTopLevel(navController) //添加书架页面
        addWatchedTopLevel(navController) //添加发现页面
        addMineTopLevel(navController) //添加我的页面
        dialog(route = "settings_dialog") {
            Text(text = "测试",
                Modifier
                    .fillMaxSize()
                    .background(Color.Red))
        }

//        val accountsName = AppScreen.Home.name
//        composable(
//            route = "$accountsName/{name}",
//            arguments = listOf(
//                navArgument("name") {
//                    type = NavType.StringType
//                }
//            ),
//            deepLinks = listOf(
//                navDeepLink {
//                    uriPattern = "rally://$accountsName/{name}"
//                }
//            ),
//        ) { entry ->
//            val accountName = entry.arguments?.getString("name")
//            val account = UserData.getAccount(accountName)
//            SingleAccountBody(account = account)
//        }
    }

}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addNewsTopLevel(navController: NavHostController) {
    navigation(route = AppScreen.News.route,
        startDestination = LeafScreen.Feeds.createRoute(AppScreen.News)) {
        composable(LeafScreen.Feeds.createRoute(AppScreen.News)) {
            HeadlinesList()
        }
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addShelfTopLevel(navController: NavHostController) {
    navigation(route = AppScreen.Books.route,
        startDestination = LeafScreen.Books.createRoute(AppScreen.Books)) {

        composable(LeafScreen.Books.createRoute(AppScreen.Books)) {
            Text("添加剂", Modifier.fillMaxSize())
        }
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addWatchedTopLevel(navController: NavHostController) {
    navigation(route = AppScreen.Interests.route,
        startDestination = LeafScreen.Interests.createRoute(AppScreen.Interests)) {

        composable(LeafScreen.Interests.createRoute(AppScreen.Interests)) {
            Text("添加剂", Modifier.fillMaxSize())

        }
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addMineTopLevel(navController: NavHostController) {
    navigation(route = AppScreen.Mine.route,
        startDestination = LeafScreen.Mine.createRoute(AppScreen.Mine)) {

        composable(LeafScreen.Mine.createRoute(AppScreen.Mine)) {
            Button(onClick = {
                AppManager.startFragment(SettingsFragment::class.java)
            }, modifier = Modifier.fillMaxSize()) {
                Text("测试跳转设置页面", Modifier.systemBarsPadding())
            }
        }
    }
}


@ExperimentalAnimationApi
private fun NavGraphBuilder.addAccount(root: AppScreen,
    onOpenSettings: () -> Unit,
) {
//    dialog(
//        route = LeafScreen.Account.createRoute(root),
//        debugLabel = "AccountUi()",
//    ) {
//        AccountUi(
//            openSettings = onOpenSettings
//        )
//    }
}

private val NavDestination.hostNavGraph: NavGraph
    get() = hierarchy.first { it is NavGraph } as NavGraph

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultExitTransition(
    initial: NavBackStackEntry,
    target: NavBackStackEntry,
): ExitTransition {
    val initialNavGraph = initial.destination.hostNavGraph
    val targetNavGraph = target.destination.hostNavGraph
    // If we're crossing nav graphs (bottom navigation graphs), we crossfade
    if (initialNavGraph.id != targetNavGraph.id) {
        return fadeOut()
    }
    // Otherwise we're in the same nav graph, we can imply a direction
    return fadeOut() + slideOutOfContainer(AnimatedContentScope.SlideDirection.Start)
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultEnterTransition(
    initial: NavBackStackEntry,
    target: NavBackStackEntry,
): EnterTransition {
    val initialNavGraph = initial.destination.hostNavGraph
    val targetNavGraph = target.destination.hostNavGraph
    // If we're crossing nav graphs (bottom navigation graphs), we crossfade
    if (initialNavGraph.id != targetNavGraph.id) {
        return fadeIn()
    }
    // Otherwise we're in the same nav graph, we can imply a direction
    return fadeIn() + slideIntoContainer(AnimatedContentScope.SlideDirection.Start)
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultPopEnterTransition(): EnterTransition {
    return fadeIn() + slideIntoContainer(AnimatedContentScope.SlideDirection.End)
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultPopExitTransition(): ExitTransition {
    return fadeOut() + slideOutOfContainer(AnimatedContentScope.SlideDirection.End)
}


sealed class AppScreen(val route: String) {
    object News: AppScreen("news")
    object Books: AppScreen("books")
    object Interests: AppScreen("interests")
    object Mine: AppScreen("mine")
    object Dialog: AppScreen("settings_dialog")


    companion object {

        fun fromRoute(route: String?): AppScreen =
            when (route?.substringBefore("/")) {
                News.route -> News
                Books.route -> Books
                Interests.route -> Interests
                Mine.route -> Mine
                "settings_dialog" -> Dialog
                null -> News
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}

private sealed class LeafScreen(
    private val route: String,
) {
    fun createRoute(root: AppScreen) = "${root.route}/$route"

    object Feeds : LeafScreen("feeds")
    object Books : LeafScreen("books")
    object Interests : LeafScreen("interests")
    object Mine : LeafScreen("mine")

    object ShowDetails : LeafScreen("show/{showId}") {
        fun createRoute(root: AppScreen, showId: Long): String {
            return "${root.route}/show/$showId"
        }
    }
}

