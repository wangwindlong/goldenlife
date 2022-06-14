package net.wangyl.life.compose.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import net.wangyl.life.R
import net.wangyl.life.compose.AppTheme
import net.wangyl.life.obj.Global
import net.wangyl.life.vector.Filled
import net.wangyl.life.vector.Outlined

@Composable
fun MainScreen() {
    AppTheme(Global.theme) {
        val systemUiController = rememberSystemUiController()
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = !Global.theme.isDark()
        )
        systemUiController.setNavigationBarColor(
            color = AppTheme.colors.bottomBar
        )
        MainContent()
//                TextButton(onClick = {}) {
//
//                }
//                Button(onClick = {
//                    viewModel.login {
//                        Timber.d(" $it")
//                        Global.updateUserData(it)
////                    .launchMain()
////                navigationActions.navigateToHome
//                    }
//                }, Modifier.fillMaxWidth()) {
//                    Text(text = "登录", modifier = Modifier.size(100.dp))
//                }

    }
}


val LocalNavController = staticCompositionLocalOf<NavHostController?> { null }
//val LocalDrawer = staticCompositionLocalOf<DrawerState?> { null }


@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun MainContent() {
    val navController = rememberAnimatedNavController()
    val backstack by navController.currentBackStackEntryAsState()
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    CompositionLocalProvider(
        LocalNavController provides navController,
//        LocalDrawer provides drawerState
    ) {
        Scaffold(
            bottomBar = {
                val currentSelectedItem by navController.currentScreenAsState()
                HomeBottomNavigation(
                    selectedNavigation = currentSelectedItem,
                    onNavigationSelected = { selected ->
                        val dest = navController.graph.findStartDestination()
                        if (currentSelectedItem.route == selected.route) return@HomeBottomNavigation
                        navController.navigate(selected.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(dest.id) {
                                saveState = true
                            }
                        }
                    },
                    modifier = Modifier.navigationBarsPadding().fillMaxWidth()
                )
            },
            scaffoldState = rememberScaffoldState(drawerState),
            drawerContent = {
                AppDrawer(
                    currentRoute = backstack?.destination?.route ?: AppScreen.News.route,
                    closeDrawer = { coroutineScope.launch { drawerState.close() } },
                    modifier = Modifier
                        .statusBarsPadding()
                        .navigationBarsPadding()
                )
            },
            drawerGesturesEnabled = true,
            modifier = Modifier
        ) { padding ->
            AppNavigation(navController, Modifier.fillMaxSize().padding(padding))
        }
    }

}

@Composable
private fun HomeNavigationItemIcon(item: HomeNavigationItem, selected: Boolean) {
    val tint = if (selected) AppTheme.colors.iconSelected else AppTheme.colors.icon
    val painter = when (item) {
        is HomeNavigationItem.ResourceIcon -> painterResource(item.iconResId)
        is HomeNavigationItem.ImageVectorIcon -> rememberVectorPainter(item.iconImageVector)
    }
    val selectedPainter = when (item) {
        is HomeNavigationItem.ResourceIcon -> item.selectedIconResId?.let { painterResource(it) }
        is HomeNavigationItem.ImageVectorIcon -> item.selectedImageVector?.let {
            rememberVectorPainter(it)
        }
    }

    if (selectedPainter != null) {
        Crossfade(targetState = selected) {
            Icon(
                painter = if (it) selectedPainter else painter,
                contentDescription = stringResource(item.contentDescriptionResId),
                tint = tint, modifier = Modifier.height(24.dp)
            )
        }
    } else {
        Icon(
            painter = painter,
            contentDescription = stringResource(item.contentDescriptionResId),
            tint = tint, modifier = Modifier.height(24.dp)
        )
    }
}

@Composable
internal fun HomeBottomNavigation(
    selectedNavigation: AppScreen,
    onNavigationSelected: (AppScreen) -> Unit,
    modifier: Modifier = Modifier,
) {
    BottomNavigation(
        backgroundColor = AppTheme.colors.bottomBar.copy(alpha = 1f),
//        contentColor = contentColorFor(MyComposeTheme.colors.iconSelected),
        modifier = modifier
    ) {
        HomeNavigationItems.forEach { item ->
            BottomNavigationItem(
                icon = {
                    HomeNavigationItemIcon(
                        item = item,
                        selected = selectedNavigation == item.screen
                    )
                },
                label = { Text(text = stringResource(item.labelResId)) },
                selected = selectedNavigation == item.screen,
                onClick = { onNavigationSelected(item.screen) },
                selectedContentColor = AppTheme.colors.iconSelected,
                unselectedContentColor = AppTheme.colors.icon,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * Adds an [NavController.OnDestinationChangedListener] to this [NavController] and updates the
 * returned [State] which is updated as the destination changes.
 */
@Stable
@Composable
private fun NavController.currentScreenAsState(): State<AppScreen> {
    val selectedItem = remember { mutableStateOf<AppScreen>(AppScreen.News) }

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            println("listener destination=$destination")
            selectedItem.value = AppScreen.fromRoute(destination.route)
        }
        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return selectedItem
}


private sealed class HomeNavigationItem(
    val screen: AppScreen,
    @StringRes val labelResId: Int,
    @StringRes val contentDescriptionResId: Int,
) {
    class ResourceIcon(
        screen: AppScreen,
        @StringRes labelResId: Int,
        @StringRes contentDescriptionResId: Int,
        @DrawableRes val iconResId: Int,
        @DrawableRes val selectedIconResId: Int? = null,
    ) : HomeNavigationItem(screen, labelResId, contentDescriptionResId)

    class ImageVectorIcon(
        screen: AppScreen,
        @StringRes labelResId: Int,
        @StringRes contentDescriptionResId: Int,
        val iconImageVector: ImageVector,
        val selectedImageVector: ImageVector? = null,
    ) : HomeNavigationItem(screen, labelResId, contentDescriptionResId)
}

private val HomeNavigationItems = listOf(
    HomeNavigationItem.ResourceIcon(
        screen = AppScreen.News,
        labelResId = R.string.news,
        contentDescriptionResId = R.string.news,
        iconResId = R.drawable.ic_discovery_outlined,
        selectedIconResId = R.drawable.ic_discovery_filled,
    ),
    HomeNavigationItem.ImageVectorIcon(
        screen = AppScreen.Books,
        labelResId = R.string.shelfs,
        contentDescriptionResId = R.string.shelfs,
        iconImageVector = Outlined.Book,
        selectedImageVector = Filled.Book,
    ),
    HomeNavigationItem.ImageVectorIcon(
        screen = AppScreen.Interests,
        labelResId = R.string.interests,
        contentDescriptionResId = R.string.interests,
        iconImageVector = Icons.Default.FavoriteBorder,
        selectedImageVector = Icons.Default.Favorite,
    ),
    HomeNavigationItem.ResourceIcon(
        screen = AppScreen.Mine,
        labelResId = R.string.mine,
        contentDescriptionResId = R.string.mine,
        iconResId = R.drawable.ic_me_outlined,
        selectedIconResId = R.drawable.ic_me_filled,
    ),
)