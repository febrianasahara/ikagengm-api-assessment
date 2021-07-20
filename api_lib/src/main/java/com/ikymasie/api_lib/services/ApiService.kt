package com.ikymasie.api_lib.services

import PopularArticlesResponse
import okhttp3.Response
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("/svc/mostpopular/v2/viewed/{period}.json")
    fun getArticles(@Path("period") period : Int,
                            @Query("api-key")apiKey : String): Call<PopularArticlesResponse>

}