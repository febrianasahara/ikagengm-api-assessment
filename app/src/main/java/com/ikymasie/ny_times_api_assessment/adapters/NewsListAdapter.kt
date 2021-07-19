package com.ikymasie.ny_times_api_assessment.adapters

import Results
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ikymasie.ny_times_api_assessment.R
import com.ikymasie.ny_times_api_assessment.interfaces.OnItemClick
import kotlinx.android.synthetic.main.news_list_item.view.*

class NewsListAdapter (private val dataSet: Array<Results>, private val click: OnItemClick) :
    RecyclerView.Adapter<NewsListAdapter.ViewHolder>() {

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
            subtitle = view.findViewById(R.id.txt_title)
            extraText = view.findViewById(R.id.txt_title)
            image = view.findViewById(R.id.txt_title)

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
        val item = dataSet[position]
        viewHolder.title.text = item.title
        viewHolder.subtitle.text = item.abstract
        viewHolder.extraText.text = item.byline
        val photoUrl =item.media.get(0).mediametadata.get(0).url
        val url = if ( photoUrl != null) "$photoUrl" else null //1
        Glide.with(viewHolder.itemView)  //2
            .load(url) //3
            .centerCrop() //4
            .placeholder(R.drawable.ic_image_placeholder) //5
            .error(R.drawable.ic_broken_image) //6
            .fallback(R.drawable.ic_broken_image) //7
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(viewHolder.image) //8


        viewHolder.itemView.setOnClickListener{
            click.onItemClick(position)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}