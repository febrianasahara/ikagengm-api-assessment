package com.ikymasie.api_lib.services

import PopularArticlesResponse
import okhttp3.Response
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("/svc/mostpopular/v2/viewed/{period}.json?api-key={key}")
    suspend fun getArticles(@Path("period") period : Int,
                            @Path("key")apiKey : String): Call<PopularArticlesResponse>

}