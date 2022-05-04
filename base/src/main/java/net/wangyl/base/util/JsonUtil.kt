package net.wangyl.base.util

import com.google.gson.Gson
import com.squareup.moshi.Moshi
import net.wangyl.base.extension.getK


inline fun <reified T> fromJson(json: String): T? {
    val moshi = getK<Moshi>()
    println("fromJson moshi=$moshi json=$json")
    return moshi.adapter(T::class.java).fromJson(json)
}

inline val <reified T> T.json get() = getK<Moshi>().adapter(T::class.java).toJson(this)

inline fun <reified T> T.fromGson(json: String?) = getK<Gson>().fromJson(json, T::class.java)

inline val <reified T> T.gson get() = getK<Gson>().toJson(this)
