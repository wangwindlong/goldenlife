package net.wangyl.base.enums


sealed class StateEvent

object StateIdle : StateEvent()
object StateLoading : StateEvent()
//object StateFinished : StateEvent()
data class StateError(val throwable: Throwable) : StateEvent()

fun StateEvent.isLoading() = this is StateLoading

enum class LoginState(val state: Int) {
    UNLOGIN(0),
    LOGEDIN(1),
    OUTDATED(2),
    KICKEDOFF(-1),
}