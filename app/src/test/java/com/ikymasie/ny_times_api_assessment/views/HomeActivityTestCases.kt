package com.ikymasie.ny_times_api_assessment.views

import Results
import android.content.Context
import androidx.appcompat.widget.Toolbar
import androidx.test.core.app.ApplicationProvider
import com.google.common.reflect.TypeToken
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.gson.Gson
import com.ikymasie.api_lib.ApiClient
import com.ikymasie.ny_times_api_assessment.R
import com.ikymasie.ny_times_api_assessment.SAMPLE_LIST
import com.ikymasie.ny_times_api_assessment.presenters.NewsListPresenter
import kotlinx.android.synthetic.main.activity_home.*
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class HomeActivityTestCases {

    /**
     * Usind Robolectic to test App Implementation
     * */
    var home_controller = Robolectric.buildActivity(HomeActivity::class.java)
    val context = ApplicationProvider.getApplicationContext<Context>()
    lateinit var list :ArrayList<Results>
    lateinit var homeStub: HomeActivity
    lateinit var adapter : NewsListPresenter
    @Before
    fun setUp() {
        // init firebase
        Firebase.initialize(context)
        list = ArrayList()

    }
    @Test
    fun getApiUtil() {
        homeStub=  home_controller.create().resume().get()
        var util = homeStub.apiUtil
        assertNull(util)
    }

    @Test
    fun setApiUtil() {
        homeStub=  home_controller.create().resume().get()
        var util = ApiClient("","some-api-key")
        homeStub.apiUtil = util
        assertNotNull(homeStub.apiUtil)
    }

    @Test
    fun getArticles() {
        homeStub=  home_controller.create().resume().get()
        var articles = homeStub.articles
        assertEquals(true,articles.isEmpty())
    }

    @Test
    fun setArticles() {
        homeStub=  home_controller.create().resume().get()
        var mockDataSet = SAMPLE_LIST
        val myType = object : TypeToken<ArrayList<Results>>() {}.type
        var mockData = Gson().fromJson<ArrayList<Results>>(mockDataSet, myType)
        homeStub.articles = mockData
        assertEquals(2,homeStub.articles.size)
    }

    @Test
    fun getFilterIndex() {
        homeStub=  home_controller.create().resume().get()
        assertEquals(1,homeStub.filterIndex)
    }

    @Test
    fun setFilterIndex() {
        homeStub=  home_controller.create().resume().get()
        homeStub.filterIndex = 7
        assertEquals(7,homeStub.filterIndex)
    }

    @Test
    fun onCreate() {
        homeStub=  home_controller.create().get()
    }

    @Test
    fun onCreateOptionsMenu() {
        homeStub=  home_controller.create().resume().visible().get()
        var toolbar = homeStub.findViewById<Toolbar>(R.id.toolbar)
        var homeShadow = shadowOf(homeStub)
        homeStub.onCreateOptionsMenu(toolbar.menu)
        assertEquals(homeShadow.getOptionsMenu().findItem(R.id.appSearchBar).isVisible(), true);
    }

    @Test
    fun onBind() {
        homeStub=  home_controller.create().resume().get()
        homeStub.onBind()
        var lm = homeStub.list.layoutManager
        assertNotNull(lm)
    }

    @Test
    fun toggleLoadingUI() {
        homeStub=  home_controller.create().resume().get()
        homeStub.toggleLoadingUI(true)
        assertEquals(true,homeStub.refresher.isRefreshing)
    }

    @Test
    fun initConfig() {
        homeStub=  home_controller.create().resume().get()

        var task = homeStub.initConfig()
//        task.addOnCompleteListener {
//            assertNotNull(homeStub.apiUtil)
//        }
        assertEquals(false,task.isComplete)
    }

    @Test
    fun onConfigLoaded() {
        homeStub=  home_controller.create().resume().get()
        homeStub.onConfigLoaded("some-key","https://some-url")
        assertNotNull(homeStub.apiUtil!!.apiService)
    }

    @Test
    fun onDataSuccess() {
        homeStub=  home_controller.create().resume().get()
        var mockDataSet = SAMPLE_LIST
        val myType = object : TypeToken<ArrayList<Results>>() {}.type
        var mockData = Gson().fromJson<ArrayList<Results>>(mockDataSet, myType)
        homeStub.onDataSuccess(mockData)
        assertEquals(2,homeStub.articles.size)

    }

    @Test
    fun onErrorCallback() {
        homeStub=  home_controller.create().resume().get()
        homeStub.onErrorCallback("some-error")
        assertEquals("some-error",homeStub.errorMessage)
    }

    @Test
    fun onRefresh() {
        homeStub=  home_controller.create().resume().get()

        var task = homeStub.initConfig()
        task.addOnCompleteListener {
            homeStub.onRefresh()
            assertEquals(true,homeStub.refresher.isRefreshing)
        }
    }

    @Test
    fun onPositionChanged() {
        homeStub=  home_controller.create().resume().get()
        val positionToChangeTo =1
        homeStub.onPositionChanged(positionToChangeTo)
      assertEquals("Past Week",  homeStub.btn_filter.getButton(positionToChangeTo).text)

    }
}