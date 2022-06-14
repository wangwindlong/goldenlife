package net.wangyl.life.vm

import androidx.lifecycle.ViewModel
import net.wangyl.base.interf.State
import net.wangyl.base.interf.StateContainer
import net.wangyl.base.interf.StateHost
import net.wangyl.base.interf.stateOf
import net.wangyl.life.compose.state.NewsState

class ArticleVM: ViewModel(), StateHost<State> {
    override val stateContainer: StateContainer<State> by stateOf(object : State {

    })
}