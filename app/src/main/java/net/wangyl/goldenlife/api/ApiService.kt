package net.wangyl.goldenlife.api

import net.wangyl.goldenlife.model.ListModel
import net.wangyl.goldenlife.model.PostData
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("posts/{id}")
    suspend fun post(@Path("id") id: Int): PostData

    @GET("posts")
    suspend fun posts(): List<PostData>

    companion object {
        operator fun invoke(retrofit: Retrofit) = retrofit.create<ApiService>()
    }
}