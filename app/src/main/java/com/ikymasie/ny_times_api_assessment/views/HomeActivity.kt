package com.ikymasie.ny_times_api_assessment.views

import PopularArticlesResponse
import Results
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.ikymasie.api_lib.ApiClient
import com.ikymasie.ny_times_api_assessment.BaseActivity
import com.ikymasie.ny_times_api_assessment.R
import com.ikymasie.ny_times_api_assessment.adapters.NewsListAdapter
import com.ikymasie.ny_times_api_assessment.interfaces.OnItemClick
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.toTypedArray as toTypedArray

class HomeActivity : BaseActivity(), OnItemClick  {
    private var apiUtil: ApiClient? = null
    private val instance = this
    private val TAG = "HomeActivity"
    private lateinit var articles: List<Results>;
    private lateinit var adapter: NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        onBind()
    }

    fun onBind(){
        initConfig()
        refresher.setOnRefreshListener {
            refresh()                    // refresh your list contents somehow
              // reset the SwipeRefreshLayout (stop the loading spinner)
        }
    }

    private fun initConfig() {
        refresher.isRefreshing = true
        var remoteConfig  = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(10)
            .build()

        remoteConfig.setConfigSettingsAsync(configSettings)
        val cacheExpiration: Long =3600
        remoteConfig.fetch(cacheExpiration)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    remoteConfig.fetchAndActivate().addOnCompleteListener(){subtask ->
                        if(subtask.isSuccessful){
                            var baseUrl = remoteConfig.getString("baseUrl")
                            var apiKey  = remoteConfig.getString("apiKey")
                            apiUtil = ApiClient(baseUrl, apiKey)
                            refresh()
                        }
                    }

                }
            }
    }
    private fun refresh(){
        if(apiUtil!=null){
            refresher.isRefreshing = true
            apiUtil!!.getPopularArticles(1).clone().enqueue(object: Callback<PopularArticlesResponse> {
            override fun onFailure(call: Call<PopularArticlesResponse>?, t: Throwable?) {
                // network error
                onErrorCallback(t!!.message!!)
                refresher.isRefreshing = false
            }

            override fun onResponse(
                call: Call<PopularArticlesResponse>?,
                response: Response<PopularArticlesResponse>?
            ) {
                if (response?.isSuccessful!!) {
                    if(response.body()!!.status == "OK"){
                        // handle response and attach to view handler
                        articles = response.body()!!.results
                        adapter = NewsListAdapter(articles,instance)
                        list.layoutManager =  LinearLayoutManager(instance)
                        list.adapter = adapter
                        refresher.isRefreshing = false
                    }
                } else {
                   // api error
                    onErrorCallback("Error: ${response.body()!!.status}")
                    refresher.isRefreshing = false
                }
            }
        })
        }
    }
    private fun onErrorCallback(message: String){
        // log error

        // show error dialog
    }

    override fun onItemClick(position: Int) {
        var selectedItem = adapter.getItemAtPosition(position)
        Log.d(TAG,"SELECTED: ${selectedItem.id}")

    }

}