package com.ikymasie.ny_times_api_assessment.views

import Results
import android.content.Context
import android.view.MenuItem
import androidx.test.core.app.ApplicationProvider
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import kotlinx.android.synthetic.main.activity_home.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.fakes.RoboMenuItem
import org.robolectric.shadows.ShadowActivity


@RunWith(RobolectricTestRunner::class)
class ArticleDetailActivityTest {
    var controller = Robolectric.buildActivity(ArticleDetailActivity::class.java)
    val context = ApplicationProvider.getApplicationContext<Context>()
    lateinit var dataItem :Results
    lateinit var stub: ArticleDetailActivity
    @Before
    fun setUp() {
        // init firebase
        Firebase.initialize(context)



    }
    @Test
    fun getInstance() {
        stub = controller.create().resume().get()
        assertNotNull(stub.instance)
    }

    @Test
    fun onCreate() {
    }

    @Test
    fun onLoadData() {
        stub = controller.create().resume().get()

    }

    @Test
    fun onOptionsItemSelected() {
        stub = controller.create().resume().get()

        val menuItem: MenuItem = RoboMenuItem(android.R.id.home)
        stub.onOptionsItemSelected(menuItem)
        val shadowActivity: ShadowActivity = Shadows.shadowOf(stub)
        assertTrue(shadowActivity.isFinishing)
    }
}