package com.ikymasie.ny_times_api_assessment.views

import PopularArticlesResponse
import Results
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.addisonelliott.segmentedbutton.SegmentedButtonGroup
import com.google.android.gms.tasks.Task
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.ikymasie.api_lib.ApiClient
import com.ikymasie.ny_times_api_assessment.BaseActivity
import com.ikymasie.ny_times_api_assessment.R
import com.ikymasie.ny_times_api_assessment.presenters.NewsListPresenter
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "HomeActivity"

/**
 * HomeActivity is the MASTER view
 * Displays results from new repository
 * */
class HomeActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener, SegmentedButtonGroup.OnPositionChangedListener {

    var apiUtil: ApiClient? = null
    var errorMessage: String? = null
    var filterIndex = 1
    var articles = ArrayList<Results>()

    private lateinit var presenter: NewsListPresenter
    private val instance = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        onBind()
        initConfig()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)

        val search = menu.findItem(R.id.appSearchBar)
        val searchView = search.actionView as SearchView
        searchView.queryHint = "Search Articles"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                presenter.getFilter().filter(newText)
                presenter.notifyDataSetChanged()
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }
    /**
     * onBind - Instanties view children and adds appropriate listners to handle user interaction
     * */
    fun onBind(){
        setSupportActionBar(toolbar)
        supportActionBar!!.title = getString(R.string.my_name)
        LinearLayoutManager(instance).also { list.layoutManager = it }
        refresher.setOnRefreshListener(this)
        btn_filter.onPositionChangedListener = this
    }
    /**
     * toggleLoadingUI - Show/Hide list loading
     * */
    fun toggleLoadingUI(loading:Boolean){
        refresher.isRefreshing = loading
    }
    /**
     * initConfig - Fetch config which contains SENSITIVE DATA
     * */
    fun initConfig(): Task<Boolean> {
       toggleLoadingUI(true)

        // using Firebase Remote Config to fetch sensitive data
        var remoteConfig  = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(1)
            .build()

        remoteConfig.setConfigSettingsAsync(configSettings)

        return  remoteConfig.fetchAndActivate().addOnCompleteListener { subtask ->
                // load api key and API Url into API utility
                onConfigLoaded(remoteConfig.getString("apiKey"),
                    remoteConfig.getString("baseUrl"))
        }
    }
    /**
     * refresh - call news Repository using API Client
     * */
    fun refresh(){
            // fetch articles from NY Times API using a filterIndex (period)
          return  apiUtil!!.getPopularArticles(filterIndex).clone().enqueue(object: Callback<PopularArticlesResponse> {
            override fun onFailure(call: Call<PopularArticlesResponse>?, t: Throwable?) {
                // unknown error handler
                toggleLoadingUI(false)
                onErrorCallback(t!!.message!!)
            }

            override fun onResponse(
                call: Call<PopularArticlesResponse>?,
                response: Response<PopularArticlesResponse>?
            ) {
                onDataSuccess(response!!.body()!!.results as ArrayList<Results>)
            }
        })
    }
    /**
     * onConfigLoaded - creates new instance of API client once configuration has successfully loaded
     * */
    fun onConfigLoaded(key: String, url: String){
        apiUtil = ApiClient(url, key)
        refresher.isRefreshing = true
        refresh()
    }
    /**
     * onDataSuccess - creates instance of presenter once results are returned from the api
     * */
    fun onDataSuccess(body:ArrayList<Results>){
        articles = body
        presenter = NewsListPresenter(articles, instance)

        list.adapter = presenter
        toggleLoadingUI(false)
    }
    /**
     * onErrorCallback - generic function to handle app errors
     * */
    fun onErrorCallback(message: String){
        // log error
        Toast.makeText(instance,message,Toast.LENGTH_LONG).show()
        Log.e(TAG,message)
        toggleLoadingUI(false)
        errorMessage = message
        // show error dialog
    }
    /**
     * onRefresh - refreshes UI for Search Activity
     * */
    override fun onRefresh() {
        if(apiUtil!=null) {
            toggleLoadingUI(true)
            refresh()
        }
    }
    /**
     * onPositionChanged - handles button filter (switching periods between accepted values)
     * */
    override fun onPositionChanged(position: Int) {
        filterIndex = when (position) {
            0 -> 1
            1 -> 7
            2 -> 30
            else -> 1
        }

        if(apiUtil!=null){
            toggleLoadingUI(true)
            refresh()
        }
    }

}