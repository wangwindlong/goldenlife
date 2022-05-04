package net.wangyl.life.compose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import net.wangyl.life.compose.MyComposeTheme
import net.wangyl.life.obj.Global
import net.wangyl.life.R

@Composable
fun TopBar(title: String, onBack: (() -> Unit)? = null) {
    Box(Modifier.background(MyComposeTheme.colors.background).statusBarsPadding().fillMaxWidth()) {
        Row(Modifier.height(48.dp)) {
            if (onBack != null) {
                Icon(
                    painterResource(net.wangyl.base.R.drawable.base_ic_arrow_back_ios),
                    null,
                  Modifier
                    .clickable(onClick = onBack)
                    .align(Alignment.CenterVertically)
                    .size(36.dp)
                    .padding(8.dp),
                    tint = MyComposeTheme.colors.icon
                )
            }
            Spacer(Modifier.weight(1f))
          val navControll = LocalNavController.current

          Icon(
            painterResource(R.drawable.ic_reddit_outlined),
            "设置",
            Modifier
              .clickable {
                navControll?.navigate("settings_dialog")
              }
              .align(Alignment.CenterVertically)
              .size(36.dp)
              .padding(8.dp),
            tint = MyComposeTheme.colors.icon
          )
            Icon(
                painterResource(R.drawable.ic_palette),
                "切换主题",
              Modifier
                .clickable {
                  Global.theme = when (Global.theme) {
                    MyComposeTheme.Theme.Light -> MyComposeTheme.Theme.Dark
                    MyComposeTheme.Theme.Dark -> MyComposeTheme.Theme.Custom
                    MyComposeTheme.Theme.Custom -> MyComposeTheme.Theme.Light
                  }
                }
                .align(Alignment.CenterVertically)
                .size(36.dp)
                .padding(8.dp),
                tint = MyComposeTheme.colors.icon
            )
        }
        Text(title, Modifier.align(Alignment.Center), color = MyComposeTheme.colors.textPrimary)
    }
}