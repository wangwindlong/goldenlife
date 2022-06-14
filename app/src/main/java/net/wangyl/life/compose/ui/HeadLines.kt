package net.wangyl.life.compose.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import net.wangyl.life.R
import net.wangyl.life.compose.AppTheme
import net.wangyl.life.model.User
import net.wangyl.life.vm.RSSViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.*
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.SnapOffsets
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.wangyl.life.compose.util.Layout
import net.wangyl.life.compose.util.rememberStateWithLifecycle
import net.wangyl.life.data.entity.CategoryEntity
import net.wangyl.ptr.Event
import net.wangyl.ptr.State
import net.wangyl.ptr.compose.NSPtrEZHeader
import net.wangyl.ptr.compose.NSPtrLayout
import net.wangyl.ptr.compose.NSPtrScope
import net.wangyl.ptr.compose.NSPtrState
import timber.log.Timber


@Composable
fun HeadLinesListItem(
    modifier: Modifier = Modifier,
) {
    val navController = LocalNavController.current

}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CategoriesPager() {
    val viewModel = viewModel<RSSViewModel>()
//    val contacts = viewModel.headLines {  }
//    val drawer = LocalDrawer.current
    val scope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = rememberScaffoldState(),
        topBar = {
            TopBar(title = stringResource(id = R.string.app_name)) {
//                scope.launch { drawer?.open() }
            }
        },
    ) { innerPadding ->
        val contentModifier = Modifier.padding(innerPadding)
        val uistate by rememberStateWithLifecycle(stateFlow = viewModel.categoryFlow)
        val coroutine = rememberCoroutineScope()
        val pagerState = rememberPagerState(1)
        val categories = uistate.categories
//        uistate.loading = true
        val nsPtrState = remember {
            NSPtrState(
                coroutineScope = coroutine,
                contentInitPosition = 0.dp,
                contentRefreshPosition = 60.dp
            ) {
                launch {
                    delay(3000)
                    it.dispatchPtrEvent(Event.RefreshComplete)
                }
            }
        }
        Column(modifier = Modifier.fillMaxSize()) {
            Timber.d("CategoriesPager uistate=${categories}")
            CategoriesHorizontalList(categories, pagerState = pagerState, loading = uistate.loading) { page ->
                coroutine.launch {
                    pagerState.animateScrollToPage(page)
                }
            }
            NSPtrLayout( nsPtrState = nsPtrState, modifier = Modifier
                .fillMaxSize()
                .weight(1f),
                isRefreshing = uistate.loading,) {
                NSPtrEZHeader(
                    modifier = Modifier.offset(0.dp, 16.dp),
                    nsPtrState = nsPtrState
                )
//        if (categories.isEmpty()) return@NSPtrLayout
                HorizontalPager(count = categories.size,
                    Modifier
                        .fillMaxSize()
                        .ptrContent(), pagerState) { page ->
                    ArticleList(categories[page])
                }
            }
        }
        LaunchedEffect(uistate) {
            coroutine.launch {
                delay(3000)
//                uistate.loading = false
            }
        }
    }
}


@OptIn(ExperimentalSnapperApi::class)
@Composable
fun CategoryPage(category: CategoryEntity, modifier: Modifier = Modifier) {
    val coroutine = rememberCoroutineScope()
    val nsPtrState = remember {
        NSPtrState(
            coroutineScope = coroutine,
            contentInitPosition = 0.dp,
            contentRefreshPosition = 60.dp
        ) {
            launch {
                delay(3000)
                it.dispatchPtrEvent(Event.RefreshComplete)
            }
        }
    }
    NSPtrLayout(nsPtrState = nsPtrState, modifier = modifier) {
        NSPtrEZHeader(
            modifier = Modifier.offset(0.dp, 16.dp),
            nsPtrState = nsPtrState
        )
//        if (categories.isEmpty()) return@NSPtrLayout

    }


}


/**
 * 文章列表
 */
@OptIn(ExperimentalSnapperApi::class)
@Composable
fun ArticleList(category: CategoryEntity, modifier:Modifier=Modifier) {
    LazyColumn(
        modifier
            .background(AppTheme.colors.listItem)
            .fillMaxWidth()
    ) {
        itemsIndexed(buttons) { index, contact ->
            ContactListItem(contact)
            if (index < buttons.size - 1) {
                Divider(
                    startIndent = 56.dp,
                    color = AppTheme.colors.chatListDivider,
                    thickness = 0.8f.dp
                )
            }
        }
        item {
            Text(
                "朋友",
                Modifier
                    .background(AppTheme.colors.background)
                    .fillMaxWidth()
                    .padding(12.dp, 8.dp),
                fontSize = 14.sp,
                color = AppTheme.colors.onBackground
            )
        }
//        itemsIndexed(contacts) { index, contact ->
//            ContactListItem(contact)
//            if (index < contacts.size - 1) {
//                Divider(
//                    startIndent = 56.dp,
//                    color = AppTheme.colors.chatListDivider,
//                    thickness = 0.8f.dp
//                )
//            }
//        }
    }
}



@OptIn(ExperimentalSnapperApi::class)
@Composable
fun CategoryTab(
    index: Int, category: CategoryEntity, selected: Int, totalCount: Int,
    onTabSelected: (CoroutineScope.(Int) -> Unit)? = null
) {
    val coroutine = rememberCoroutineScope()
    Tab(
        selected = selected == index,
        onClick = {
            onTabSelected?.invoke(coroutine, index)
        },
        Modifier.background(AppTheme.colors.background)
    ) {
        Row(Modifier.height(IntrinsicSize.Min)) {
            CategoryListItem(category, Modifier.height(48.dp))
            if (index < totalCount - 1) {
                Divider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(0.dp, 10.dp)
                        .width(1.dp),
                    color = AppTheme.colors.chatListDivider,
                    thickness = 1f.dp,
                )
            }
        }
    }
}


@OptIn(ExperimentalSnapperApi::class, ExperimentalPagerApi::class)
@Composable
fun CategoriesHorizontalList(
    categories: List<CategoryEntity>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    onSelectedChanged: ((Int) -> Unit)? = null
) {
    if (categories.isEmpty()) return
//    val lazyListState = rememberLazyListState()
//    val contentPadding = PaddingValues(horizontal = Layout.bodyMargin, vertical = Layout.gutter)
    Row(
        modifier
            .fillMaxWidth()
            .background(AppTheme.colors.background)) {
//        LazyRow(
//            modifier = Modifier
//                .background(AppTheme.colors.listItem)
//                .fillMaxWidth()
//                .weight(1f),
//            state = lazyListState,
//            flingBehavior = rememberSnapperFlingBehavior(
//                lazyListState = lazyListState,
//                snapOffsetForItem = SnapOffsets.Start,
//                endContentPadding = contentPadding.calculateEndPadding(LayoutDirection.Ltr),
//            ),
//            contentPadding = contentPadding,
//            horizontalArrangement = Arrangement.spacedBy(4.dp),
//        ) {
//            itemsIndexed(categories) { index, category ->
//                Row(Modifier.height(IntrinsicSize.Min)) {
//                    CategoryListItem(category, Modifier.height(48.dp))
//                    if (index < categories.size - 1) {
//                        Divider(
//                            modifier = Modifier
//                                .fillMaxHeight()
//                                .padding(0.dp, 10.dp)
//                                .width(1.dp),
//                            color = AppTheme.colors.chatListDivider,
//                            thickness = 1f.dp,
//                        )
//                    }
//                }
//            }
//        }

        ScrollableTabRow(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = AppTheme.colors.background,
            // Override the indicator, using the provided pagerTabIndicatorOffset modifier
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(Modifier.pagerTabIndicatorOffset(pagerState, tabPositions))
            }
        ) {
            categories.forEachIndexed { index, category ->
                CategoryTab(index, category, pagerState.currentPage, categories.size) { page ->
                    onSelectedChanged?.invoke(page)
                }
            }
        }
        AnimatedVisibility(visible = loading) {
            AutoSizedCircularProgressIndicator(
                color = AppTheme.colors.more,
                modifier = Modifier
                    .padding(8.dp)
                    .size(16.dp)
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun HeadlinesItemPreview() {
    AppTheme {
        Box {
            ContactListItem(
                User("gaolaoshi", "高老师", R.drawable.ic_chat_filled)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HeadlinesListPreview(modifier: Modifier = Modifier) {
    val contacts = listOf<User>(
        User("gaolaoshi", "高老师", R.drawable.ic_chat_filled),
        User("diuwuxian", "丢物线", R.drawable.ic_chat_filled),
    )
    ContactList(contacts, modifier)
}