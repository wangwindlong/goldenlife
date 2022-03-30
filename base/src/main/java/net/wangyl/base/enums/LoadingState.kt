package net.wangyl.base.enums

enum class LoadingState(val status: Int = 0, val message: String = "") {
    LOADING(-1,"加载中"),
    IDLE(0,""),
    FINISHED(1,"加载完成"),
}

enum class LoginState(val state: Int) {
    UNLOGIN(0),
    LOGEDIN(1),
    OUTDATED(2),
    KICKEDOFF(-1),
}