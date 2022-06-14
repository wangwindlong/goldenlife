package net.wangyl.life.obj

import kotlinx.coroutines.Dispatchers

object AppDispatchers {
    val io = Dispatchers.IO
    val computation = Dispatchers.Default
    val main = Dispatchers.Main
}