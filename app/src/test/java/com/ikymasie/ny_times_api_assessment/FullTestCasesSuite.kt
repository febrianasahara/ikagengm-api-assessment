package com.ikymasie.ny_times_api_assessment

import Results
import android.content.Context
import android.content.Intent
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.test.core.app.ApplicationProvider
import com.google.common.reflect.TypeToken
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.gson.Gson
import com.ikymasie.api_lib.ApiClient
import com.ikymasie.ny_times_api_assessment.presenters.NewsListPresenter
import com.ikymasie.ny_times_api_assessment.views.ArticleDetailActivity
import com.ikymasie.ny_times_api_assessment.views.HomeActivity
import com.ikymasie.ny_times_api_assessment.views.ITEM_DETAIL_PARAM
import kotlinx.android.synthetic.main.activity_article_detail.*
import kotlinx.android.synthetic.main.activity_home.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.Shadows.shadowOf
import org.robolectric.fakes.RoboMenuItem
import org.robolectric.shadows.ShadowActivity

@RunWith(RobolectricTestRunner::class)
class FullTestCasesSuite {

    /**
     * Usind Robolectic to test App Implementation
     * */
    var home_controller = Robolectric.buildActivity(HomeActivity::class.java)
    var articles_controller = Robolectric.buildActivity(ArticleDetailActivity::class.java)
    val context = ApplicationProvider.getApplicationContext<Context>()
    lateinit var list :ArrayList<Results>
    lateinit var homeStub: HomeActivity
    lateinit var stub: ArticleDetailActivity
    lateinit var adapter : NewsListPresenter
    @Before
    fun setUp() {
        // init firebase
        Firebase.initialize(context)
        list = ArrayList()

    }
    /**
     * Test cases covering HomeActivity
     * */
    @Test
    fun Home_getApiUtil() {
        homeStub=  home_controller.create().resume().get()
        var util = homeStub.apiUtil
        assertNull(util)
    }

    @Test
    fun Home_setApiUtil() {
        homeStub=  home_controller.create().resume().get()
        var util = ApiClient("https://some-url", "some-api-key")
        homeStub.apiUtil = util
        assertNotNull(homeStub.apiUtil)
    }

    @Test
    fun Home_getArticles() {
        homeStub=  home_controller.create().resume().get()
        var articles = homeStub.articles
        assertEquals(true, articles.isEmpty())
    }

    @Test
    fun Home_setArticles() {
        homeStub=  home_controller.create().resume().get()
        var mockDataSet = SAMPLE_LIST
        val myType = object : TypeToken<ArrayList<Results>>() {}.type
        var mockData = Gson().fromJson<ArrayList<Results>>(mockDataSet, myType)
        homeStub.articles = mockData
        assertEquals(2, homeStub.articles.size)
    }

    @Test
    fun Home_getFilterIndex() {
        homeStub=  home_controller.create().resume().get()
        assertEquals(1, homeStub.filterIndex)
    }

    @Test
    fun Home_setFilterIndex() {
        homeStub=  home_controller.create().resume().get()
        homeStub.filterIndex = 7
        assertEquals(7, homeStub.filterIndex)
    }

    @Test
    fun Home_onCreate() {
        homeStub=  home_controller.create().get()
    }

    @Test
    fun Home_onCreateOptionsMenu() {
        homeStub=  home_controller.create().resume().visible().get()
        var toolbar = homeStub.findViewById<Toolbar>(R.id.toolbar)
        var homeShadow = shadowOf(homeStub)
        homeStub.onCreateOptionsMenu(toolbar.menu)
        assertEquals(homeShadow.getOptionsMenu().findItem(R.id.appSearchBar).isVisible(), true);
    }

    @Test
    fun Home_onBind() {
        homeStub=  home_controller.create().resume().get()
        homeStub.onBind()
        var lm = homeStub.list.layoutManager
        assertNotNull(lm)
    }

    @Test
    fun Home_toggleLoadingUI() {
        homeStub=  home_controller.create().resume().get()
        homeStub.toggleLoadingUI(true)
        assertEquals(true, homeStub.refresher.isRefreshing)
    }

    @Test
    fun Home_initConfig() {
        homeStub=  home_controller.create().resume().get()

        var task = homeStub.initConfig()
//        task.addOnCompleteListener {
//            assertNotNull(homeStub.apiUtil)
//        }
        assertEquals(false, task.isComplete)
    }

    @Test
    fun Home_onConfigLoaded() {
        homeStub=  home_controller.create().resume().get()
        homeStub.onConfigLoaded("some-key", "https://some-url")
        assertNotNull(homeStub.apiUtil!!.apiService)
    }

    @Test
    fun Home_onDataSuccess() {
        homeStub=  home_controller.create().resume().get()
        var mockDataSet = SAMPLE_LIST
        val myType = object : TypeToken<ArrayList<Results>>() {}.type
        var mockData = Gson().fromJson<ArrayList<Results>>(mockDataSet, myType)
        homeStub.onDataSuccess(mockData)
        assertEquals(2, homeStub.articles.size)

    }

    @Test
    fun Home_onErrorCallback() {
        homeStub=  home_controller.create().resume().get()
        homeStub.onErrorCallback("some-error")
        assertEquals("some-error", homeStub.errorMessage)
    }

    @Test
    fun Home_onRefresh() {
        //given
        homeStub=  home_controller.create().resume().get()
        //when
        var task = homeStub.initConfig()
        task.addOnCompleteListener {
            homeStub.onRefresh()
            //then
            assertEquals(true, homeStub.refresher.isRefreshing)
        }
    }



    @Test
    fun Home_onPositionChanged() {
        //given
        homeStub=  home_controller.create().resume().get()
        val positionToChangeTo =1
        //when
        homeStub.onPositionChanged(positionToChangeTo)
        //then
      assertEquals("Past Week", homeStub.btn_filter.getButton(positionToChangeTo).text)

    }

    /**
     * Test cases covering ArticleDetailActivity
     * */
    @Test
    fun Detail_getInstance() {
        //given
        stub = articles_controller.create().resume().get()
        //then
        assertNotNull(stub.instance)
    }


    @Test
    fun Detail_onLoadData() {
        val mIntent = Intent(
            ApplicationProvider.getApplicationContext(),
            ArticleDetailActivity::class.java
        )
        var mockDataSet = SAMPLE_DATA_ITEM
        val myType = object : TypeToken<Results>() {}.type
        var mockData = Gson().fromJson<Results>(mockDataSet, myType)
        mIntent.putExtra(ITEM_DETAIL_PARAM, mockDataSet)
        stub = articles_controller.create().newIntent(mIntent).resume().get()
        stub.onLoadData(mockData)
        assertEquals("By Margaret Renkl",stub.byline.text)

    }

    @Test
    fun Detail_onOptionsItemSelected() {
        stub = articles_controller.create().resume().get()

        val menuItem: MenuItem = RoboMenuItem(android.R.id.home)
        stub.onOptionsItemSelected(menuItem)
        val shadowActivity: ShadowActivity = Shadows.shadowOf(stub)
        assertTrue(shadowActivity.isFinishing)
    }

    /**
     * PRESENTER TEST CASES
     * */

    @Test
    fun getITEM_DETAIL_PARAM() {
        homeStub=  home_controller.create().resume().get()
        adapter = NewsListPresenter(list,homeStub)
        assertEquals("articleDetailParam",adapter.ITEM_DETAIL_PARAM)
    }



    @Test
    fun getResultFilterList() {
        homeStub=  home_controller.create().resume().get()
        adapter = NewsListPresenter(ArrayList(),homeStub)
        assertEquals(0,adapter.resultFilterList.size)
    }

    @Test
    fun setResultFilterList() {
        homeStub=  home_controller.create().resume().get()
        var mockDataSet = SAMPLE_LIST
        val myType = object : TypeToken<ArrayList<Results>>() {}.type
        list = Gson().fromJson(mockDataSet, myType)
        adapter = NewsListPresenter(ArrayList(),homeStub)
        adapter.resultFilterList = list
        assertEquals(2,adapter.resultFilterList.size)
    }

    @Test
    fun getFilter() {
        homeStub=  home_controller.create().resume().get()
        var mockDataSet = SAMPLE_LIST
        val myType = object : TypeToken<ArrayList<Results>>() {}.type
        list = Gson().fromJson(mockDataSet, myType)
        adapter = NewsListPresenter(list,homeStub)
        adapter.getFilter().filter("Dolly Parton")
        assertNotNull(adapter.getFilter())
        assertEquals(1,adapter.resultFilterList.size)
    }


    @Test
    fun getItemCount() {
        homeStub=  home_controller.create().resume().get()
        var mockDataSet = SAMPLE_LIST
        val myType = object : TypeToken<ArrayList<Results>>() {}.type
        list = Gson().fromJson(mockDataSet, myType)
        adapter = NewsListPresenter(list,homeStub)
        assertEquals(2,adapter.itemCount)
    }
}