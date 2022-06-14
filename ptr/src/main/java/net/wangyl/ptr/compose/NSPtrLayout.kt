package net.wangyl.ptr.compose

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animate
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.InspectorValueInfo
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.NoInspectorInfo
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.wangyl.ptr.*
import net.wangyl.ptr.State
import kotlin.math.max
import kotlin.math.roundToInt

sealed class PtrComponent {
    object PtrContent : PtrComponent()
    object PtrHeader : PtrComponent()
}

@Stable
class NSPtrState(
    val contentInitPosition: Dp = 0.dp,// 初始位置
    val contentRefreshPosition: Dp = 54.dp,// 刷新位置
    val pullFriction: Float = 0.4f,// 拖动的摩擦力参数
    coroutineScope: CoroutineScope,
    onRefresh: CoroutineScope.(NSPtrState) -> Unit,// 触发刷新的回调
) {
    // 当前 content view 所处的位置
    var contentPositionPx: Float by mutableStateOf(0f)

    var contentRefreshPositionPx: Float = 0f

    var contentInitPositionPx: Float = 0f
    // 最近一次状态转变的对象
    var lastTransition: StateMachine.Transition<State, Event, SideEffect>? = null

    fun isContentAtInitPosition(): Boolean {
        return contentPositionPx == contentInitPositionPx
    }
    // 内部用来处理状态转变逻辑的状态机
    private val _stateMachine = createNSPtrFSM {
        coroutineScope.launch {
            lastTransition = it
            if (it is StateMachine.Transition.Valid) {
                state = it.toState

                when (it.sideEffect) {
                    is SideEffect.OnReleaseToIdle,
                    is SideEffect.OnComplete -> {
                        animateContentTo(contentInitPositionPx)
                    }
                    is SideEffect.OnRefreshing -> {
                        animateContentTo(contentRefreshPositionPx)
                        coroutineScope.onRefresh(this@NSPtrState)
                    }
                    else -> {

                    }
                }
            } else {
                if (it.event is Event.ReleaseToRefreshing
                    && it.fromState == State.REFRESHING
                ) {
                    animateContentTo(contentRefreshPositionPx)
                }
            }
        }
    }
    // 当前的下拉刷新状态
    var state: State by mutableStateOf(_stateMachine.state)
    // 触发状态转变事件
    fun dispatchPtrEvent(event: Event) {
        _stateMachine.transition(event)
    }

    fun isContentOverRefreshPosition(): Boolean {
        return contentPositionPx > contentRefreshPositionPx
    }

    fun pullProgress(): Float {
        return contentPositionPx * 1.0f / contentRefreshPositionPx
    }
    // content view 的位移方法
    private suspend fun animateContentTo(
        value: Float,
        animationSpec: AnimationSpec<Float> = SpringSpec()
    ) {
        var prevValue = 0f
        animate(
            0f,
            (value - contentPositionPx),
            animationSpec = animationSpec
        ) { currentValue, _ ->
            contentPositionPx += currentValue - prevValue
            prevValue = currentValue
        }
    }
}

private class NSPtrNestedScrollConnection(val ptrState: NSPtrState) : NestedScrollConnection {
    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        if (available.y < 0f
            && !ptrState.isContentAtInitPosition()
            && source == NestedScrollSource.Drag
        ) {
            available.y.minus(available.y)
            ptrState.contentPositionPx += available.y
            ptrState.contentPositionPx = max(0f, ptrState.contentPositionPx)
            return Offset(0f, available.y)
        }
        return super.onPreScroll(available, source)
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        super.onPostScroll(consumed, available, source)
        return internalNestedScroll(available, source)
    }

    private fun internalNestedScroll(
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        if (available.y > 0f && source == NestedScrollSource.Drag) {
            ptrState.contentPositionPx += available.y.times(ptrState.pullFriction)
        }
        return Offset.Zero
    }
}

@Composable
fun NSPtrLayout(
    nsPtrState: NSPtrState,
    modifier: Modifier,
    isRefreshing: Boolean = false,
    content: @Composable NSPtrScope.() -> Unit
) {

    nsPtrState.contentRefreshPositionPx = with(LocalDensity.current) {
        nsPtrState.contentRefreshPosition.toPx()
    }
    nsPtrState.contentInitPositionPx = with(LocalDensity.current) {
        val initPx = nsPtrState.contentInitPosition.toPx()
        nsPtrState.contentPositionPx = initPx
        initPx
    }

    val nestedScrollConnection = remember {
        NSPtrNestedScrollConnection(nsPtrState)
    }
    LaunchedEffect(key1 = isRefreshing) {
        if (isRefreshing) {
            nsPtrState.dispatchPtrEvent(Event.AutoRefresh)
        }
    }

    val measurePolicy = rememberPtrMeasurePolicy(nsPtrState)

    Layout(
        content = { NSPtrScopeInstance.content() },
        modifier
            .pointerInput(Unit) {
                detectDownAndUp(
                    {
                        nsPtrState.dispatchPtrEvent(Event.Pull)
                    },
                    {
                        if (nsPtrState.isContentOverRefreshPosition()) {
                            nsPtrState.dispatchPtrEvent(Event.ReleaseToRefreshing)
                        } else {
                            nsPtrState.dispatchPtrEvent(Event.ReleaseToIdle)
                        }
                    }
                )
            }
            .nestedScroll(nestedScrollConnection),
        measurePolicy
    )
}

@Composable
internal fun rememberPtrMeasurePolicy(nsPtrState: NSPtrState) = remember {
    ptrMeasurePolicy(nsPtrState)
}

/**
 * like custom onLayout
 */
internal fun ptrMeasurePolicy(nsPtrState: NSPtrState) = MeasurePolicy { measurables, constraints ->
    if (measurables.isEmpty()) {
        return@MeasurePolicy layout(constraints.minWidth, constraints.minHeight) {}
    } else {
        val layoutWidth: Int = constraints.maxWidth
        val layoutHeight: Int = constraints.maxHeight
        val placeables = arrayOfNulls<Placeable>(measurables.size)

        measurables.forEachIndexed { index, measurable ->
            placeables[index] = measurable.measure(constraints)
        }

        layout(layoutWidth, layoutHeight) {
            placeables.forEachIndexed { index, placeable ->
                placeable?.let {
                    placeInPtr(it, measurables[index], nsPtrState)
                }
            }
        }
    }
}

private fun Placeable.PlacementScope.placeInPtr(
    placeable: Placeable,
    measurable: Measurable,
    nsPtrState: NSPtrState
) {

    when (measurable.ptrComponent) {
        PtrComponent.PtrContent -> {
            placeable.place(0, nsPtrState.contentPositionPx.roundToInt())
        }
        else -> {
            placeable.place(0, 0)
        }
    }
}

/**
 * custom measurable, like custom LayoutParams
 */
interface NSPtrScope {

    @Stable
    fun Modifier.ptrContent(): Modifier

    @Stable
    fun Modifier.ptrHeader(): Modifier
}

internal object NSPtrScopeInstance : NSPtrScope {

    override fun Modifier.ptrContent() = this.then(
        NSPtrChildData(PtrComponent.PtrContent)
    )

    override fun Modifier.ptrHeader() = this.then(
        NSPtrChildData(PtrComponent.PtrHeader)
    )
}

private val Measurable.ptrComponent: PtrComponent?
get() = (parentData as? NSPtrChildData)?.ptrComponent

class NSPtrChildData(
    val ptrComponent: PtrComponent,
    inspectorInfo: InspectorInfo.() -> Unit = NoInspectorInfo
) : ParentDataModifier, InspectorValueInfo(inspectorInfo) {

    override fun Density.modifyParentData(parentData: Any?): Any = this@NSPtrChildData

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NSPtrChildData

        if (ptrComponent != other.ptrComponent) return false

        return true
    }

    override fun hashCode(): Int {
        return ptrComponent.hashCode()
    }
}