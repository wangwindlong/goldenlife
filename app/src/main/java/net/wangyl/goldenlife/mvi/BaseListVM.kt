package net.wangyl.goldenlife.mvi

import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import net.wangyl.goldenlife.api.Repository
import net.wangyl.goldenlife.api.Status
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class BaseListVM<DataClass : Parcelable>(
    val loader: suspend () -> Status<List<DataClass>>,
    savedStateHandle: SavedStateHandle, private val repository: Repository? = null,
    exceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("BaseListVM", "orbit caught the exception ", throwable)
    }
) :
    ViewModel(), ContainerHost<BaseState<DataClass>, Event> {
//    BaseEvent<PostData>

    init {
        Log.d("BaseListVM", "repository=$repository")
    }

    override val container = container<BaseState<DataClass>, Event>(
        initialState = BaseState(),
        savedStateHandle = savedStateHandle,
        settings = Container.Settings(exceptionHandler = exceptionHandler)
    ) {
        if (it.values.isEmpty()) {
            loadList()
        }
    }

    private fun loadList() = intent {
        val datas = loader()

        reduce {
            state.copy(values = when(datas) {
                is Status.Success -> datas.data
                is Status.Failure -> emptyList()
            })
        }
    }

    fun onItemClicked(post: DataClass) = intent {
        postSideEffect(DertailEvent(post))
    }

}