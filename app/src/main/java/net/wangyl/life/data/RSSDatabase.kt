package net.wangyl.life.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import net.wangyl.life.data.entity.CategoryEntity
import net.wangyl.life.data.entity.LastRequest
import net.wangyl.life.model.Article
import net.wangyl.life.model.Feed


@Database(
    entities = [
        CategoryEntity::class,
        LastRequest::class,
        Feed::class,
        Article::class,
    ],
    version = 2,
    exportSchema = false,

    )
@TypeConverters(DBTypeConverters::class)
abstract class RSSDatabase : RoomDatabase(), IDatabase

interface IDatabase {
    fun categoryDao(): CategoryDao
    fun lastRequestDao(): LastRequestDao
    fun feedsDao(): FeedsDao
    fun articleDao(): ArticleDao
}