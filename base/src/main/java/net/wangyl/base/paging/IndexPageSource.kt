package net.wangyl.base.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import net.wangyl.base.base.FetchAction
import retrofit2.HttpException
import java.io.IOException

class PageSource<K: Any, T: Any>(val page: K?, val fetchAction: FetchAction<K, T>): PagingSource<K, T>() {
    override fun getRefreshKey(state: PagingState<K, T>): K? {
        return null
    }

    override suspend fun load(params: LoadParams<K>): LoadResult<K, T> {
        val position = params.key ?: page
        return try {
            val dataList = fetchAction.fetchData(position, params) ?: emptyList()
            LoadResult.Page(
                data = dataList,
                prevKey = fetchAction.prevKey(position, dataList),
                nextKey = fetchAction.nextKey(position, dataList)
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

//    fun getPrevKey(position: K?): K? {
//        return when (page) {
//            is Int -> (if (position == page) null else (position as Int) - 1) as K
//            else -> null
//        }
//    }
//    fun getNextKey(dataList: List<T>, position: K?): K? {
//        return when (page) {
//            is Int -> (if (dataList.isNotEmpty()) (position as Int) + 1 else null) as K
//            else -> null
//        }
//    }
}