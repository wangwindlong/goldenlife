package net.wangyl.life.obj

object Mappers {

    val categoryMapper = object : Mapper<Int, String> {
        override suspend fun map(from: Int): String {
            return "$from"
        }
    }
}

fun interface Mapper<F, T> {
    suspend fun map(from: F): T
}

fun interface IndexedMapper<F, T> {
    suspend fun map(index: Int, from: F): T
}
