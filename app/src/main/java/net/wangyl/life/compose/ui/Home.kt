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
import kotlinx.coroutines.launch
import net.wangyl.life.compose.MyComposeTheme
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
        val scope = rememberCoroutineScope() // 创建 CoroutineScope
        // 不显示 viewModel.selectedTab，改为 pagerState.currentPage
        HomeBottomBar(pagerState.currentPage) { page ->
            // 点击页签后，在协程里翻页
            scope.launch {
                pagerState.animateScrollToPage(page)
            }
        }
    }
}


@Composable
fun HomeBottomBar(selected: Int, onSelectedChanged: (Int) -> Unit) {
    Row(Modifier.background(MyComposeTheme.colors.bottomBar).navigationBarsPadding()) {
        TabItem(
            if (selected == 0) R.drawable.ic_reddit_filled else R.drawable.ic_reddit_outlined,
            "新闻",
            if (selected == 0) MyComposeTheme.colors.iconSelected else MyComposeTheme.colors.icon,
            Modifier
                .weight(1f)
                .clickable {
                    onSelectedChanged(0)
                }
        )
        TabItem(
            if (selected == 1) R.drawable.ic_book_filled else R.drawable.ic_book_outlined, "书架",
            if (selected == 1) MyComposeTheme.colors.iconSelected else MyComposeTheme.colors.icon,
            Modifier
                .weight(1f)
                .clickable {
                    onSelectedChanged(1)
                }
        )

        TabItem(
            if (selected == 2) R.drawable.ic_discovery_filled else R.drawable.ic_discovery_outlined,
            "发现",
            if (selected == 2) MyComposeTheme.colors.iconSelected else MyComposeTheme.colors.icon,
            Modifier
                .weight(1f)
                .clickable {
                    onSelectedChanged(2)
                }
        )
        TabItem(
            if (selected == 3) R.drawable.ic_me_filled else R.drawable.ic_me_outlined, "我",
            if (selected == 3) MyComposeTheme.colors.iconSelected else MyComposeTheme.colors.icon,
            Modifier
                .weight(1f)
                .clickable {
                    onSelectedChanged(3)
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
    MyComposeTheme(MyComposeTheme.Theme.Light) {
        var selectedTab by remember { mutableStateOf(0) }
        HomeBottomBar(selectedTab) { selectedTab = it }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomBarPreviewDark() {
    MyComposeTheme(MyComposeTheme.Theme.Dark) {
        var selectedTab by remember { mutableStateOf(0) }
        HomeBottomBar(selectedTab) { selectedTab = it }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomBarPreviewCustom() {
    MyComposeTheme(MyComposeTheme.Theme.Custom) {
        var selectedTab by remember { mutableStateOf(0) }
        HomeBottomBar(selectedTab) { selectedTab = it }
    }
}

@Preview(showBackground = true)
@Composable
fun TabItemPreview() {
    TabItem(iconId = R.drawable.ic_chat_outlined, title = "聊天", tint = MyComposeTheme.colors.icon)
}