package net.wangyl.life.utils

import net.wangyl.base.extension.getK
import net.wangyl.life.data.RSSDatabase

object DaoManager {
    val db by lazy { getK<RSSDatabase>() }
    val categoryDao by lazy {
        db.categoryDao()
    }
    val feedsDao by lazy {
        db.feedsDao()
    }
}