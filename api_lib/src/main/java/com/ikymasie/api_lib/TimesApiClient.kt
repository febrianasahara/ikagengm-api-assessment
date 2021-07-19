package com.ikymasie.api_lib
import PopularArticlesResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ikymasie.api_lib.services.ApiService
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient constructor(baseUrl: String, apiKey: String) {

    private val apiKEY : String = apiKey
    private val gson : Gson by lazy {
        GsonBuilder().setLenient().create()
    }

    private val httpClient : OkHttpClient by lazy {
        OkHttpClient.Builder().build()
    }

    private val retrofit : Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val apiService : ApiService by lazy{
        retrofit.create(ApiService::class.java)
    }

     fun getPopularArticles(period: Int): Call<PopularArticlesResponse> {
        return apiService.getArticles(period,apiKEY)
    }
}