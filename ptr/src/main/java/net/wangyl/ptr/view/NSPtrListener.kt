package net.wangyl.ptr.view

import net.wangyl.ptr.Event
import net.wangyl.ptr.SideEffect
import net.wangyl.ptr.State
import net.wangyl.ptr.StateMachine

interface NSPtrListener {
    /**
     * perform dragging the content view
     *
     * @param ptrLayout
     */
    fun onDrag(ptrLayout: NSPtrLayout) {}

    /**
     * perform refreshing UI
     */
    fun onRefreshing(ptrLayout: NSPtrLayout) {}

    /**
     * perform UI after refresh
     */
    fun onComplete(ptrLayout: NSPtrLayout) {}

    /**
     * when state transition happen
     */
    fun onTransition(ptrLayout: NSPtrLayout, transition: StateMachine.Transition.Valid<State, Event, SideEffect>) {
        when (transition.toState) {
            is State.IDLE -> {
                if (transition.event == Event.RefreshComplete) {
                    onComplete(ptrLayout)
                }
            }
            is State.REFRESHING -> {
                onRefreshing(ptrLayout)
            }
            is State.DRAG -> {
                onDrag(ptrLayout)
            }
        }
    }

    /**
     * drag or animate the content view and move it
     */
    fun onPositionChange(ptrLayout: NSPtrLayout, offset: Int) {}
}