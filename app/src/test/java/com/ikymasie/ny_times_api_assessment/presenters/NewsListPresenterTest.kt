package com.ikymasie.ny_times_api_assessment.presenters

import Results
import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import androidx.test.core.app.ApplicationProvider
import com.google.common.reflect.TypeToken
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.gson.Gson
import com.ikymasie.ny_times_api_assessment.R
import com.ikymasie.ny_times_api_assessment.SAMPLE_LIST
import com.ikymasie.ny_times_api_assessment.views.HomeActivity
import kotlinx.android.synthetic.main.activity_home.*
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NewsListPresenterTest {
    var home_controller = Robolectric.buildActivity(HomeActivity::class.java)
    val context = ApplicationProvider.getApplicationContext<Context>()
    lateinit var list :ArrayList<Results>
    lateinit var homeStub: HomeActivity
    lateinit var adapter : NewsListPresenter
    @Before
    fun setUp() {
        Firebase.initialize(context)
        var mockDataSet = SAMPLE_LIST
        val myType = object : TypeToken<ArrayList<Results>>() {}.type
        list = Gson().fromJson(mockDataSet, myType)
        homeStub=  home_controller.create().resume().get()

    }

    @After
    fun tearDown() {
    }

    @Test
    fun getITEM_DETAIL_PARAM() {
        adapter = NewsListPresenter(list,homeStub)
        assertEquals("articleDetailParam",adapter.ITEM_DETAIL_PARAM)
    }



    @Test
    fun getResultFilterList() {
        adapter = NewsListPresenter(ArrayList(),homeStub)
        assertEquals(0,adapter.resultFilterList.size)
    }

    @Test
    fun setResultFilterList() {
        adapter = NewsListPresenter(ArrayList(),homeStub)
        adapter.resultFilterList = list
        assertEquals(2,adapter.resultFilterList.size)
    }

    @Test
    fun getFilter() {
        adapter = NewsListPresenter(list,homeStub)
        adapter.getFilter().filter("Dolly Parton")
        assertNotNull(adapter.getFilter())
        assertEquals(1,adapter.resultFilterList.size)
    }


    @Test
    fun getItemCount() {
        adapter = NewsListPresenter(list,homeStub)
        assertEquals(2,adapter.itemCount)
    }
}