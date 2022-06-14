package net.wangyl.life.compose.state

import net.wangyl.base.interf.State
import net.wangyl.life.data.entity.CategoryEntity

data class NewsState(
    val categories: List<CategoryEntity> = emptyList(),
    val loading: Boolean = false,

    ) : State