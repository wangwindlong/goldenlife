package net.wangyl.base.enums

enum class HttpError(val code: Int, val message: String){
    UNKNOWN(-100,"未知错误"),
    NETWORK_ERROR(1000, "服务器异常"),
    TIMEOUT_ERROR(10000, "网络连接超时，请检查网络"),
    CONNECT_ERROR(10001, "连接错误，请检查网络"),
    JSON_PARSE_ERROR(1001, "Json 解析失败")
    //······
}