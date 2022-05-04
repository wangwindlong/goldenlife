package net.wangyl.life.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import net.wangyl.life.data.entity.CategoryEntity
import net.wangyl.life.data.entity.LastRequest


@Database(
    entities = [
        CategoryEntity::class,
        LastRequest::class,
    ],
    version = 1,
    exportSchema = false,

    )
@TypeConverters(DBTypeConverters::class)
abstract class RSSDatabase : RoomDatabase(), IDatabase

interface IDatabase {
    fun categoryDao(): CategoryDao
    fun lastRequestDao(): LastRequestDao
}