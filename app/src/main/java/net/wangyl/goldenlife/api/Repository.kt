package net.wangyl.goldenlife.api

import net.wangyl.goldenlife.model.PostData
import retrofit2.HttpException
import java.io.IOException

class Repository(private val apiService: ApiService) {

    suspend fun getPost(id: Int): Status<PostData> {
        return try {
            Status.Success(apiService.post(id))
        } catch (e: IOException) {
            Status.Failure(e)
        } catch (e: HttpException) {
            Status.Failure(e)
        }
    }

    suspend fun getPosts(): List<PostData> {
        return try {
            apiService.posts().sortedBy { it.title }
        } catch (ignore: IOException) {
            emptyList()
        } catch (ignore: HttpException) {
            emptyList()
        }
    }
}