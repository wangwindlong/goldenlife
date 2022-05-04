package net.wangyl.life.domain

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withTimeout
import net.wangyl.base.enums.StateError
import net.wangyl.base.enums.StateEvent
import net.wangyl.base.enums.StateIdle
import net.wangyl.base.enums.StateLoading
import java.util.concurrent.TimeUnit

//获取远程接口数据的封装，不直接获取返回值，增加加载、结束和错误状态发送，同时有超时机制
// 冷流 ui层collect才会执行dowork操作
abstract class Interactor<in P> {

    open fun load(params: P, timeoutMs: Long = defaultTimeoutMs) = flow {
        try {
            withTimeout(timeoutMs) {
                emit(StateLoading)
                doWork(params)
                emit(StateIdle)
            }
        } catch (t: TimeoutCancellationException) {
            emit(StateError(t))
        }
    }.catch { t -> emit(StateError(t)) }


    operator fun invoke(params: P, timeoutMs: Long = defaultTimeoutMs) = load(params, timeoutMs)

    suspend fun executeSync(params: P) = doWork(params)

    protected abstract suspend fun doWork(params: P)

    companion object {
        private val defaultTimeoutMs = TimeUnit.MINUTES.toMillis(25)
    }
}

//有返回值无加载状态的请求操作
abstract class ResultInteractor<in P, R> {
    operator fun invoke(params: P): Flow<R> = flow {
        emit(doWork(params))
    }

    suspend fun executeSync(params: P): R = doWork(params)

    protected abstract suspend fun doWork(params: P): R
}

abstract class PagingInteractor<P : PagingInteractor.Parameters<T>, T : Any> : DaoInteractor<P, PagingData<T>>() {
    interface Parameters<T : Any> {
        val pagingConfig: PagingConfig
    }
}

abstract class SuspendingWorkInteractor<P : Any, T> : DaoInteractor<P, T>() {
    override fun createFlow(params: P): Flow<T> = flow {
        emit(doWork(params))
    }

    abstract suspend fun doWork(params: P): T
}

//数据库数据操作封装，查看或者监听
abstract class DaoInteractor<P : Any, T> {
    //replay 1是表示可以存储一个值，以便collect的时候总能获取到最新的值
    //buffer 1是因为invoke 不能是挂起函数，所以需要在外部调用tryemit，需要有一个缓冲池来存储
    private val paramState = MutableSharedFlow<P>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val flow: Flow<T> = paramState
        .distinctUntilChanged()
        .flatMapLatest { createFlow(it) }
        .distinctUntilChanged()

    operator fun invoke(params: P) {
        load(params = params)
    }

    fun load(params: P) {
        paramState.tryEmit(params)
    }

    protected abstract fun createFlow(params: P): Flow<T>
}