package net.wangyl.life.compose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.wangyl.life.compose.AppTheme
import net.wangyl.life.data.entity.CategoryEntity

@Composable
fun CategoryListTopBar() {
    TopBar(title = "通讯录")
}

@Preview(showBackground = true)
@Composable
fun CategoryListTopBarPreview() {
    CategoryListTopBar()
}

@Composable
fun CategoryListItem(category: CategoryEntity, modifier: Modifier = Modifier, ) {
    val navController = LocalNavController.current
    Box() {
        Text(
            category.title,
            modifier = Modifier.padding(20.dp, 12.dp, 20.dp, 8.dp),
            fontSize = 20.sp,
            color = AppTheme.colors.textPrimary
        )
        if (!category.unread.isEmpty() && category.unread != "0")
            Text(text = category.unread, color = AppTheme.colors.onBadge, fontSize = 10.sp,
                modifier = Modifier.clip(RoundedCornerShape(14.dp))
                    .background(AppTheme.colors.badge)
                    .padding(6.dp, 1.dp)
                    .align(Alignment.TopEnd))
    }

}

@Preview(showBackground = true)
@Composable
fun previewCategoryItem() {
    val category = CategoryEntity(title = "军事", unread = "10")
    val navController = LocalNavController.current
    Box(modifier = Modifier.clickable { navController?.navigate("settings_dialog") }) {
        Text(
            category.title,
            modifier = Modifier.padding(16.dp, 8.dp),
            fontSize = 20.sp,
            color = AppTheme.colors.textPrimary
        )
        if (!category.unread.isEmpty() && category.unread != "0")
            Text(text = category.unread, color = AppTheme.colors.onBadge, fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clip(RoundedCornerShape(14.dp))
                    .background(AppTheme.colors.badge).padding(6.dp, 1.dp)
                    .align(Alignment.TopEnd))
    }
}
