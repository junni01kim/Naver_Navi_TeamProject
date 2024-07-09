package com.hansung.sherpa.itemsetting

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hansung.sherpa.R
import com.hansung.sherpa.databinding.SpecificRouteItemBinding
import java.lang.Exception

class RouteDetailAdapter(private val listData:MutableList<RouteDetailItem>, val context:Context): RecyclerView.Adapter<RouteHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteHolder {
        return RouteHolder(LayoutInflater.from(parent.context).inflate(R.layout.specific_route_item,parent,false))
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: RouteHolder, position: Int) {
        val item = listData.get(position)
        holder.setItem(item)
    }
}

class RouteHolder(val view: View):RecyclerView.ViewHolder(view){
    fun setItem(item:RouteDetailItem){
        try {
            var image:ImageView
            var specificItemFrom = view.findViewById<TextView>(R.id.specific_item_from_text)
            var specificItemTo = view.findViewById<TextView>(R.id.specific_item_to_text)
            var specificItemSummary = view.findViewById<TextView>(R.id.specific_item_summary)
            var specificItemTime = view.findViewById<TextView>(R.id.specific_item_time)

            if(item.summary.contains("도보")){
                image = view.findViewById<ImageView>(R.id.specific_item_image)
                image.setImageResource(R.drawable.pedestrianrouteimage)
            }
            else if(item.summary.contains("정류장")){
                image = view.findViewById<ImageView>(R.id.specific_item_image)
                image.setImageResource(R.drawable.greenbusrouteimage)
            }

            specificItemFrom.text = item.fromName
            specificItemTo.text = item.toName
            specificItemSummary.text = item.summary
            specificItemTime.text = item.time
        }catch (e:Exception){
            println("No Specific Image")
        }
    }
}