package net.wangyl.goldenlife.obj

import com.tencent.mmkv.MMKV

object PrefManager {

    fun get(): MMKV {
        return  MMKV.defaultMMKV()
    }
}