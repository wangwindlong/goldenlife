package net.wangyl.life.compose.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.wangyl.life.compose.AppTheme
import net.wangyl.life.R

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Home() {
    Column(modifier = Modifier.background(Color.Green).fillMaxSize()) {
        val pagerState = rememberPagerState(1)
        HorizontalPager(
            count = 4, Modifier.weight(1f),
            pagerState
        ) { page ->
            when(page) {
                0 -> ContactList()
                else -> ContactList()
            }
        }
        // 不显示 viewModel.selectedTab，改为 pagerState.currentPage
        HomeBottomBar(pagerState.currentPage) { page ->
            // 点击页签后，在协程里翻页
            launch {
                pagerState.animateScrollToPage(page)
            }
        }
    }
}


@Composable
fun HomeBottomBar(selected: Int, onSelectedChanged: CoroutineScope.(Int) -> Unit) {
    val scope = rememberCoroutineScope() // 创建 CoroutineScope
    Row(Modifier.background(AppTheme.colors.bottomBar).navigationBarsPadding()) {
        TabItem(
            if (selected == 0) R.drawable.ic_reddit_filled else R.drawable.ic_reddit_outlined,
            "新闻",
            if (selected == 0) AppTheme.colors.iconSelected else AppTheme.colors.icon,
            Modifier
                .weight(1f)
                .clickable {
                    scope.onSelectedChanged(0)
                }
        )
        TabItem(
            if (selected == 1) R.drawable.ic_book_filled else R.drawable.ic_book_outlined, "书架",
            if (selected == 1) AppTheme.colors.iconSelected else AppTheme.colors.icon,
            Modifier
                .weight(1f)
                .clickable {
                    scope.onSelectedChanged(1)
                }
        )

        TabItem(
            if (selected == 2) R.drawable.ic_discovery_filled else R.drawable.ic_discovery_outlined,
            "发现",
            if (selected == 2) AppTheme.colors.iconSelected else AppTheme.colors.icon,
            Modifier
                .weight(1f)
                .clickable {
                    scope.onSelectedChanged(2)
                }
        )
        TabItem(
            if (selected == 3) R.drawable.ic_me_filled else R.drawable.ic_me_outlined, "我",
            if (selected == 3) AppTheme.colors.iconSelected else AppTheme.colors.icon,
            Modifier
                .weight(1f)
                .clickable {
                    scope.onSelectedChanged(3)
                }
        )
    }
}

@Composable
fun TabItem(@DrawableRes iconId: Int, title: String, tint: Color, modifier: Modifier = Modifier) {
    Column(
        modifier.padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(painterResource(iconId), title, Modifier.size(24.dp), tint = tint)
        Text(title, fontSize = 11.sp, color = tint)
    }
}

@Preview(showBackground = true)
@Composable
fun BottomBarPreview() {
    AppTheme(AppTheme.Theme.Light) {
        var selectedTab by remember { mutableStateOf(0) }
        HomeBottomBar(selectedTab) { selectedTab = it }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomBarPreviewDark() {
    AppTheme(AppTheme.Theme.Dark) {
        var selectedTab by remember { mutableStateOf(0) }
        HomeBottomBar(selectedTab) { selectedTab = it }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomBarPreviewCustom() {
    AppTheme(AppTheme.Theme.Custom) {
        var selectedTab by remember { mutableStateOf(0) }
        HomeBottomBar(selectedTab) { selectedTab = it }
    }
}

@Preview(showBackground = true)
@Composable
fun TabItemPreview() {
    TabItem(iconId = R.drawable.ic_chat_outlined, title = "聊天", tint = AppTheme.colors.icon)
}