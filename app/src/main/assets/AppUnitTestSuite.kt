import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.core.app.ApplicationProvider
import com.addisonelliott.segmentedbutton.SegmentedButtonGroup
import com.google.common.reflect.TypeToken
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.gson.Gson
import com.ikymasie.api_lib.ApiClient
import com.ikymasie.ny_times_api_assessment.presenters.NewsListPresenter
import com.ikymasie.ny_times_api_assessment.views.ArticleDetailActivity
import com.ikymasie.ny_times_api_assessment.views.HomeActivity
import kotlinx.android.synthetic.main.activity_home.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.fakes.RoboMenuItem


@RunWith(RobolectricTestRunner::class)
class AppUnitTestSuite {
    /**
     * Usind Robolectic to test App Implementation
     * */
    var home_controller = Robolectric.buildActivity(HomeActivity::class.java)
    var detail_controller = Robolectric.buildActivity(ArticleDetailActivity::class.java)
    val context = ApplicationProvider.getApplicationContext<Context>()
    lateinit var list :ArrayList<Results>
    lateinit var homeStub: HomeActivity
    lateinit var detailStub: ArticleDetailActivity
    lateinit var adapter : NewsListPresenter
    @Before
    fun setUp() {
        // init firebase
        Firebase.initialize(context)
        list = ArrayList()

    }

    @Test
    fun Presenter_GivenValidActivity_GetFilterWillReturnObjectOfType() {
        homeStub=  home_controller.create().resume().get()
        var mockDataSet = SAMPLE_LIST
        val myType = object : TypeToken<ArrayList<Results>>() {}.type
        var mockData = Gson().fromJson<ArrayList<Results>>(mockDataSet, myType)
        adapter = NewsListPresenter(mockData,homeStub)
        var filter =adapter!!.getFilter()
        Assert.assertNotNull(filter)
        Assert.assertNotNull(adapter.resultFilterList)
        Assert.assertEquals("articleDetailParam",adapter.ITEM_DETAIL_PARAM)
        Assert.assertEquals(2,adapter.itemCount)
    }

    @Test
    fun Given_valid_key_When_service_executes_Then_apiServiceIsCreatedSuccessfully() {
        val apiClient = ApiClient("https://some-url.com","some-key")

        Assert.assertNotNull(apiClient.apiService)
    }

    @Test
    fun HomeActivity_GivenBeforeActivity_WhenArticlesDeclared_ThenObjectArrayIsEmpty() {
        homeStub=  home_controller.get()

        Assert.assertEquals(true, homeStub.articles.isEmpty())
        Assert.assertNull(homeStub.apiUtil)
    }
    @Test
    fun HomeActivity_Given_ValidCacheDuration_WhenInitCalled_ThenBodyWillBeReturn() {
        homeStub=  home_controller.create().resume().get()
        var mockDataSet = SAMPLE_LIST
        val myType = object : TypeToken<ArrayList<Results>>() {}.type
        var mockData = Gson().fromJson<ArrayList<Results>>(mockDataSet, myType)
        homeStub.articles = mockData
        var task = homeStub.initConfig()
        task.addOnCompleteListener {
            Assert.assertNotNull(homeStub.apiUtil)
            var response = homeStub.apiUtil!!.getPopularArticles(1).execute()
            Assert.assertNotNull(response.body())
            var res = homeStub.refresh()
            Assert.assertNull(res)
            Assert.assertEquals(true, homeStub.refresher.isRefreshing)
        }


    }

    @Test
    fun HomeActivity_OnCreate_Test() {
        homeStub=  home_controller.create().resume().get()
        homeStub.onErrorCallback("some-error")
        var mockDataSet = SAMPLE_LIST
        val myType = object : TypeToken<ArrayList<Results>>() {}.type
        var mockData = Gson().fromJson<ArrayList<Results>>(mockDataSet, myType)
        homeStub.articles = mockData
        homeStub.initConfig()
        var btnFilter =  homeStub.findViewById<SegmentedButtonGroup>(R.id.btn_filter)

        var toolbar = homeStub.supportActionBar
        Assert.assertEquals("Today", btnFilter.getButton(0).text)
        Assert.assertEquals("Ikageng Masie", toolbar!!.title)
    }

    @Test
    fun HomeActivity_FilterButton_Test() {
        homeStub=  home_controller.create().resume().get()
        homeStub.onErrorCallback("some-error")
        var mockDataSet = SAMPLE_LIST
        val myType = object : TypeToken<ArrayList<Results>>() {}.type
        var mockData = Gson().fromJson<ArrayList<Results>>(mockDataSet, myType)
        homeStub.articles = mockData
        var task = homeStub.initConfig()
        task.addOnCompleteListener {
            var btnFilter =  homeStub.findViewById<SegmentedButtonGroup>(R.id.btn_filter)
            btnFilter.setPosition(1,false)
            Assert.assertEquals("Past Week", btnFilter.getButton(1).text)
        }

    }

    @Test
    fun HomeActivity_OnDataSuccess_Test() {
        homeStub=  home_controller.create().resume().get()
        homeStub.onBind()
        var mockDataSet = SAMPLE_LIST
        val myType = object : TypeToken<ArrayList<Results>>() {}.type
        var mockData = Gson().fromJson<ArrayList<Results>>(mockDataSet, myType)
        homeStub.onDataSuccess(mockData)
        var list = homeStub.findViewById<RecyclerView>(R.id.list)


        Assert.assertEquals(2,homeStub.articles.size)
        Assert.assertNotNull(list.layoutManager)
    }

    @Test
    fun HomeActivity_ToggleLoading_Test() {
        homeStub=  home_controller.create().resume().get()
        homeStub.toggleLoadingUI(true)
        homeStub.onConfigLoaded("some-key","https://some-url")
        Assert.assertEquals(true,homeStub.refresher.isRefreshing)
        Assert.assertNotNull(homeStub.apiUtil)
    }


    @Test
    fun HomeActivity_InitConfig_WillLoadInBackground() {
        homeStub=  home_controller.create().resume().get()
        var mockDataSet = SAMPLE_LIST
        val myType = object : TypeToken<ArrayList<Results>>() {}.type
        var mockData = Gson().fromJson<ArrayList<Results>>(mockDataSet, myType)
        homeStub.articles = mockData
        homeStub.initConfig()
        var refresher =  homeStub.findViewById<SwipeRefreshLayout>(R.id.refresher)
        Assert.assertEquals(true, refresher.isRefreshing)

    }

    @Test
    fun ArticleDetailActivity_OnCreate_Then_Toolbar_TitleWillBeApp_Name() {
        var selectedItem = detail_controller.create().get().getString(R.string.test_data_item)

        //add item to mock intent
        var mockIntent = Intent()
        Log.d("EST_DATA", selectedItem)
        mockIntent.putExtra("articleDetailParam", selectedItem)
        detailStub  = detail_controller.newIntent(mockIntent).resume().get()

        var toolbar = detailStub.supportActionBar
        Assert.assertEquals("Abstract...", toolbar!!.title)
    }

    @Test
    fun ArticleDetailActivity_OnBackPressed_ReturnsSuccess_ThenClosesActivity() {
        var selectedItem = detail_controller.create().resume().get().getString(R.string.test_data_item)

        //add item to mock intent
        var mockIntent = Intent()
        Log.d("EST_DATA", selectedItem)
        mockIntent.putExtra("articleDetailParam", selectedItem)
        detailStub  = detail_controller.newIntent(mockIntent).resume().get()


        val menuItem: MenuItem = RoboMenuItem(android.R.id.home)
        var selectedSuccessfully = detailStub.onMenuItemSelected(0, menuItem)

        Assert.assertEquals(true, selectedSuccessfully)
    }

    @Test
    fun ArticleDetailActivity_OnLoadData_DetailsAreSetCorrectly() {
        var selectedItem = SAMPLE_DATA_ITEM

        //add item to mock intent
        var mockIntent = Intent()
        Log.d("EST_DATA", selectedItem)
        mockIntent.putExtra("articleDetailParam", selectedItem)
        detailStub  = detail_controller.newIntent(mockIntent).create().resume().get()
        var result = Gson().fromJson(selectedItem, Results::class.java)
        detailStub.onLoadData(result)
        var title = detailStub.findViewById<TextView>(R.id.article_title)
        var image = detailStub.findViewById<ImageView>(R.id.detail_image)

        Assert.assertEquals(result.title, title.text)
        Assert.assertNotNull(image)

    }

}