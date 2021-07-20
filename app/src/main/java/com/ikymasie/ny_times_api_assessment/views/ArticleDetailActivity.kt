package com.ikymasie.ny_times_api_assessment.views

import Results
import android.os.Bundle
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.Gson
import com.ikymasie.ny_times_api_assessment.BaseActivity
import com.ikymasie.ny_times_api_assessment.R
import kotlinx.android.synthetic.main.activity_article_detail.*

const val ITEM_DETAIL_PARAM: String = "articleDetailParam"

class ArticleDetailActivity : BaseActivity() {
    // static instance of current activity
    val instance = this
    // result item to be loaded into views
    private  lateinit var article: Results
    // defined param to receive intent payload

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_detail)
        onBind()
    }
    /**
     * Method to handle all view instantiation when activity is first created
     * */
    private fun onBind(){
        setSupportActionBar(detail_toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        supportActionBar!!.title = "Abstract..."

        val jsonString = intent.getStringExtra(ITEM_DETAIL_PARAM)
        if(jsonString!=null) {
            var gson = Gson()
            article = gson.fromJson(jsonString, Results::class.java)
            if (article != null) {
                onLoadData(article)
            } else {
                this.finish()
            }
        }else{
            this.finish()
        }

    }

    /**
     * Method to load data once it has been received from the activity intent
     * */
    fun onLoadData(data: Results){
        article_title.text = data.title

        byline.text = data.byline
        scroll_text.text = data.abstract
        date.text = data.published_date
        main_text.text = "${data.section} ${data.type}"
        // build keyword data if available
        var category = ""
        if(data.des_facet!!.isNotEmpty() && data.des_facet!!.size>2){
            category = "${data.des_facet!![0]} | ${data.des_facet!![1]}"
        }else{
            category = data.source!!
        }
        source_text.text = category
        val image =data.media
        if(image!!.isNotEmpty()){
            val photoUrl =data.media!!.get(0).mediametadata.get(2 ).url
            val url = if ( photoUrl != null) "$photoUrl" else null

            //using glide library to elegantly load and cache images
            Glide.with(instance)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.ic_broken_image)
                .fallback(R.drawable.ic_broken_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(detail_image)
        }
    }

    /**
     * onOptionsItemSelected - called when top back button is called to close the current activity
     * */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
             android.R.id.home -> {
                 this.onBackPressed()
                 return true
             }
        }
        return super.onOptionsItemSelected(item)
    }
}