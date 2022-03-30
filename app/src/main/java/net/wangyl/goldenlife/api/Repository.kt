package net.wangyl.goldenlife.api

import net.wangyl.goldenlife.model.PostData

class Repository(private val apiService: ApiService) {

//    suspend fun getPost(id: Int): Status<PostData> {
//        return try {
//            Status.Success(apiService.post(id))
//        } catch (e: IOException) {
//            Status.Failure(e)
//        } catch (e: HttpException) {
//            Status.Failure(e)
//        }
//    }
//
//    suspend fun getPosts(): Status<List<PostData>> {
//        return try {
//            Status.Success()
//        } catch (ignore: IOException) {
//            Status.Failure(ignore)
//        } catch (ignore: HttpException) {
//            Status.Failure(ignore)
//        }
//    }

    suspend fun getPosts(): List<PostData> {
        return apiService.posts().sortedBy { it.title }
    }
}