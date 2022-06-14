package net.wangyl.life.data

import androidx.room.TypeConverter
import net.wangyl.base.util.fromJson
import net.wangyl.base.util.json
import net.wangyl.life.model.Attachment
import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter

object DBTypeConverters {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    private val dayOfWeekValues by lazy(LazyThreadSafetyMode.NONE) { DayOfWeek.values() }

    @TypeConverter
    @JvmStatic
    fun toOffsetDateTime(value: String?) = value?.let { formatter.parse(value, OffsetDateTime::from) }

    @TypeConverter
    @JvmStatic
    fun fromOffsetDateTime(date: OffsetDateTime?): String? = date?.format(formatter)

    @TypeConverter
    @JvmStatic
    fun toDayOfWeek(value: Int?): DayOfWeek? {
        return if (value != null) {
            dayOfWeekValues.firstOrNull { it.value == value }
        } else null
    }

    @TypeConverter
    @JvmStatic
    fun fromDayOfWeek(day: DayOfWeek?) = day?.value

    @TypeConverter
    @JvmStatic
    fun toInstant(value: Long?) = value?.let { Instant.ofEpochMilli(it) }

    @TypeConverter
    @JvmStatic
    fun fromInstant(date: Instant?) = date?.toEpochMilli()

    @TypeConverter
    @JvmStatic
    fun toZoneId(value: String?) = value?.let { ZoneId.of(it) }

    @TypeConverter
    @JvmStatic
    fun fromZoneId(value: ZoneId?) = value?.id

    @TypeConverter
    @JvmStatic
    fun toLocalTime(value: String?) = value?.let { LocalTime.parse(value) }

    @TypeConverter
    @JvmStatic
    fun fromLocalTime(value: LocalTime?) = value?.format(DateTimeFormatter.ISO_LOCAL_TIME)


    @TypeConverter
    @JvmStatic
    fun toAttachment(value: String?) = value?.let { fromJson<List<Attachment>>(it) }

    @TypeConverter
    @JvmStatic
    fun fromAttachment(value: List<Attachment>?) = value?.json

    @TypeConverter
    @JvmStatic
    fun toList(value: String?) = value?.let { fromJson<List<String>>(it) }

    @TypeConverter
    @JvmStatic
    fun fromList(value: List<String>?) = value?.json

    @TypeConverter
    @JvmStatic
    fun toList2(value: String?) = value?.let { fromJson<List<List<String>>>(it) }

    @TypeConverter
    @JvmStatic
    fun fromList2(value: List<List<String>>?) = value?.json

}