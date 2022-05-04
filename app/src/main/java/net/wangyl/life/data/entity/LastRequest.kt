package net.wangyl.life.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import net.wangyl.base.data.IdEntity


@Entity(
    tableName = "last_requests",
    indices = [Index(value = ["request", "entity_id"], unique = true)]
)
data class LastRequest(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "localid") override val localid: Long,
    @ColumnInfo(name = "id") override val id: Long = 0,
    @ColumnInfo(name = "request") val request: String,
    @ColumnInfo(name = "entity_id") val entityId: Long,
    @ColumnInfo(name = "timestamp") val timestamp: Long,
): IdEntity