package net.wangyl.life.compose.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import net.wangyl.base.interf.dataFlow
import net.wangyl.life.R
import net.wangyl.life.compose.MyComposeTheme
import net.wangyl.life.compose.util.rememberStateWithLifecycle
import net.wangyl.life.model.User
import net.wangyl.life.vm.RSSViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.LayoutDirection
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.SnapOffsets
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import net.wangyl.life.compose.util.Layout
import net.wangyl.life.compose.util.rememberFlowWithLifecycle
import net.wangyl.life.data.entity.CategoryEntity
import net.wangyl.life.model.Article


@Composable
fun HeadLinesListItem(
    modifier: Modifier = Modifier,
) {
    val navController = LocalNavController.current

}

@Composable
fun HeadlinesList() {
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
        val viewState by viewModel.dataFlow.collectAsState()
        Column() {
            Box(contentModifier
            ) {
//                HeadlinesList(viewState)

            }
        }
    }

}

//
//@OptIn(ExperimentalSnapperApi::class, ExperimentalFoundationApi::class)
//@Composable
//private fun  EntryShowCarousel(
//    items: List<Article>,
//    onItemClick: (TiviShow) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val lazyListState = rememberLazyListState()
//    val contentPadding = PaddingValues(horizontal = Layout.bodyMargin, vertical = Layout.gutter)
//
//    LazyRow(
//        state = lazyListState,
//        modifier = modifier,
//        flingBehavior = rememberSnapperFlingBehavior(
//            lazyListState = lazyListState,
//            snapOffsetForItem = SnapOffsets.Start,
//            endContentPadding = contentPadding.calculateEndPadding(LayoutDirection.Ltr),
//        ),
//        contentPadding = contentPadding,
//        horizontalArrangement = Arrangement.spacedBy(4.dp),
//    ) {
//        items(
//            items = items,
//            key = { it.show.id },
//        ) { item ->
//            PosterCard(
//                show = item.show,
//                poster = item.poster,
//                onClick = { onItemClick(item.show) },
//                modifier = Modifier
//                    .animateItemPlacement()
//                    .fillParentMaxHeight()
//                    .aspectRatio(2 / 3f)
//            )
//        }
//    }
//}

@Composable
fun HeadlinesList(contacts: List<User>) {
    LazyColumn(
        Modifier
            .background(MyComposeTheme.colors.listItem)
            .fillMaxWidth()
    ) {
        val buttons = listOf(
            User("contact_add", "新的朋友", R.drawable.ic_chat_filled),
            User("contact_chat", "仅聊天", R.drawable.ic_chat_filled),
            User("contact_group", "群聊", R.drawable.ic_chat_filled),
            User("contact_tag", "标签", R.drawable.ic_chat_filled),
            User("contact_official", "公众号", R.drawable.ic_chat_filled),
        )
        itemsIndexed(buttons) { index, contact ->
            ContactListItem(contact)
            if (index < buttons.size - 1) {
                Divider(
                    startIndent = 56.dp,
                    color = MyComposeTheme.colors.chatListDivider,
                    thickness = 0.8f.dp
                )
            }
        }
        item {
            Text(
                "朋友",
                Modifier
                    .background(MyComposeTheme.colors.background)
                    .fillMaxWidth()
                    .padding(12.dp, 8.dp),
                fontSize = 14.sp,
                color = MyComposeTheme.colors.onBackground
            )
        }
        itemsIndexed(contacts) { index, contact ->
            ContactListItem(contact)
            if (index < contacts.size - 1) {
                Divider(
                    startIndent = 56.dp,
                    color = MyComposeTheme.colors.chatListDivider,
                    thickness = 0.8f.dp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HeadlinesItemPreview() {
    MyComposeTheme {
        Box {
            ContactListItem(
                User("gaolaoshi", "高老师", R.drawable.ic_chat_filled)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HeadlinesListPreview() {
    val contacts = listOf<User>(
        User("gaolaoshi", "高老师", R.drawable.ic_chat_filled),
        User("diuwuxian", "丢物线", R.drawable.ic_chat_filled),
    )
    ContactList(contacts)
}