package net.wangyl.life.compose.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.wangyl.life.R
import net.wangyl.life.compose.MyComposeTheme
import net.wangyl.life.model.User

@Composable
fun ContactListTopBar() {
    TopBar(title = "通讯录")
}

@Preview(showBackground = true)
@Composable
fun ContactListTopBarPreview() {
    ContactListTopBar()
}

@Composable
fun ContactListItem(
    contact: User,
    modifier: Modifier = Modifier,
) {
    val navController = LocalNavController.current
    Row(
        Modifier
            .fillMaxWidth()
            .clickable {
                navController?.navigate(AppScreen.Mine.route)
            }
    ) {
        Image(
            painterResource(contact.avatar), "avatar", Modifier
                .padding(12.dp, 8.dp, 8.dp, 8.dp)
                .size(36.dp)
                .clip(RoundedCornerShape(4.dp))
        )
        Text(
            contact.name,
            Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            fontSize = 17.sp,
            color = MyComposeTheme.colors.textPrimary
        )
    }
}

@Composable
fun ContactList() {
    val contacts by remember {
        mutableStateOf(
            listOf(
                User("gaolaoshi", "高老师", R.drawable.ic_chat_filled),
                User("diuwuxian", "丢物线", R.drawable.ic_chat_filled),
                User("diuwuxian", "丢物线", R.drawable.ic_chat_filled),
                User("diuwuxian", "丢物线", R.drawable.ic_chat_filled),
                User("diuwuxian", "丢物线", R.drawable.ic_chat_filled),
                User("diuwuxian", "丢物线", R.drawable.ic_chat_filled),
                User("diuwuxian", "丢物线", R.drawable.ic_chat_filled),
                User("diuwuxian", "丢物线", R.drawable.ic_chat_filled),
                User("diuwuxian", "丢物线", R.drawable.ic_chat_filled),
                User("diuwuxian", "丢物线", R.drawable.ic_chat_filled),
                User("diuwuxian", "丢物线", R.drawable.ic_chat_filled),
                User("diuwuxian", "丢物线", R.drawable.ic_chat_filled),
                User("diuwuxian", "丢物线", R.drawable.ic_chat_filled),
                User("diuwuxian", "丢物线", R.drawable.ic_chat_filled),
                User("diuwuxian", "丢物线", R.drawable.ic_chat_filled),
                User("diuwuxian", "丢物线", R.drawable.ic_chat_filled)
            )
        )
    }

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

        Column() {
            Box(contentModifier

            ) {
                ContactList(contacts)
            }
        }
    }

}

@Composable
fun ContactList(contacts: List<User>) {
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
fun ContactListItemPreview() {
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
fun ContactListPreview() {
    val contacts = listOf<User>(
        User("gaolaoshi", "高老师", R.drawable.ic_chat_filled),
        User("diuwuxian", "丢物线", R.drawable.ic_chat_filled),
    )
    ContactList(contacts)
}