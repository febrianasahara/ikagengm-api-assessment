package com.ikymasie.ny_times_api_assessment.adapters

import Results
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ikymasie.ny_times_api_assessment.R
import com.ikymasie.ny_times_api_assessment.interfaces.OnItemClick
import kotlinx.android.synthetic.main.news_list_item.view.*
import java.util.*
import kotlin.collections.ArrayList

class NewsListAdapter (private val dataSet: List<Results>, private val click: OnItemClick) :
    RecyclerView.Adapter<NewsListAdapter.ViewHolder>() {
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
                        if (row.title.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                        if (row.abstract.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                        if (row.byline.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                        if (row.adx_adx_keywordswords.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
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

    fun getItemAtPosition(position: Int): Results {
       return resultFilterList[position]
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
        val item = dataSet[position]
        viewHolder.title.text = item.title
        viewHolder.subtitle.text = item.abstract
        viewHolder.extraText.text = item.section
        val image =item.media
        if(image.isNotEmpty()){
            val photoUrl =item.media!!.get(0).mediametadata!!.get(0).url
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
            click.onItemClick(position)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}