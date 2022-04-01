package net.wangyl.base

object Configs {
    var API_EXPIRE = if (BuildConfig.DEBUG) 10L else 60 * 60L //api接口缓存时间 单位 s 一小时
    var API_DELAY = 600L //api调用延时时间 单位 ms
    var API_MAX = 30 //最多缓存api个数


}