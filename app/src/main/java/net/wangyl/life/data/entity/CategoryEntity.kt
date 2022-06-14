package net.wangyl.life.data.entity

import androidx.room.*
import net.wangyl.base.data.BaseEntity
import net.wangyl.base.data.IdEntity

//对应服务端ttrss_feed_categories表
@Entity(
    tableName = "categories",
    indices = [
        Index(value = ["id"], unique = true),
        Index(value = ["title"]),
    ]
)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "localid") override val localid: Long = 0,
    @ColumnInfo(name = "id") override val id: Long = 0, //对应服务端表id
    @ColumnInfo(name = "order_id") val order_id: Long = 0, //对应服务端排序
    @ColumnInfo(name = "title") val title: String = "",//对应服务端表title
    @ColumnInfo(name = "unread") val unread: String = "",//未读条数
    @ColumnInfo(name = "isshow") val isshow: Boolean = true,//是否显示

    ) : BaseEntity, IdEntity {
}