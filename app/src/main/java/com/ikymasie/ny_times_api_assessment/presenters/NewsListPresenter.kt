package com.ikymasie.ny_times_api_assessment.presenters

import Results
import android.app.ActivityOptions
import android.content.Intent
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.Gson
import com.ikymasie.ny_times_api_assessment.R
import com.ikymasie.ny_times_api_assessment.views.ArticleDetailActivity
import java.util.*
import kotlin.collections.ArrayList

class NewsListPresenter(private val dataSet: List<Results>, private val context: AppCompatActivity?) :
    RecyclerView.Adapter<NewsListPresenter.ViewHolder>() {
    val ITEM_DETAIL_PARAM: String = "articleDetailParam"

    var resultFilterList: List<Results>
    init {
        resultFilterList = dataSet
    }
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView
        val subtitle: TextView
        val extraText: TextView
        val image: ImageView

        init {
            // Define click listener for the ViewHolder's View.
            title = view.findViewById(R.id.txt_title)
            subtitle = view.findViewById(R.id.txt_subtitle)
            extraText = view.findViewById(R.id.txt_extra)
            image = view.findViewById(R.id.imageView)

        }
    }
     fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    resultFilterList = dataSet
                } else {
                    val resultList = ArrayList<Results>()
                    for (row in dataSet) {
                        if (row.title!!.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(row)
                        }

                    }
                    resultFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = resultFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

            }

        }
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.news_list_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val item = resultFilterList[position]
        viewHolder.title.text = item.title
        viewHolder.subtitle.text = item.byline
        viewHolder.extraText.text = item.published_date
        val image =item.media
        if(image!!.isNotEmpty()){
            val photoUrl =item.media!!.get(0).mediametadata.get(0).url
            val url = if ( photoUrl != null) "$photoUrl" else null

            //using glide library to elegantly load and cache images
            Glide.with(viewHolder.itemView)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.ic_broken_image)
                .fallback(R.drawable.ic_broken_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.image)
        }



        viewHolder.itemView.setOnClickListener{
            var selectedItem = resultFilterList[position]
            var gson = Gson()
            val jsonPayload = gson.toJson(selectedItem)
            var detailIntent  = Intent(context, ArticleDetailActivity::class.java)
            detailIntent.putExtra(ITEM_DETAIL_PARAM,jsonPayload!!)

            // Apply activity transition
            val options = ActivityOptions.makeSceneTransitionAnimation(context,
                Pair.create(viewHolder.title, "title"),
                Pair.create(viewHolder.subtitle, "byline"),
                Pair.create(viewHolder.image, "image"),
                Pair.create(viewHolder.extraText, "date"))

            context!!.startActivity(detailIntent,options.toBundle())

        }
    }



    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = resultFilterList.size
}