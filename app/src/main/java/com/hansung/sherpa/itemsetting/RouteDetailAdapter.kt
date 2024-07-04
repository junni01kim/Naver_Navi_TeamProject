package com.hansung.sherpa.itemsetting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet.Layout
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.hansung.sherpa.R
import com.hansung.sherpa.databinding.ShowSpecificRouteBinding
import com.hansung.sherpa.databinding.SpecificRouteItemBinding
import java.lang.Exception

class RouteDetailAdapter(private val listData:MutableList<RouteDetailItem>): RecyclerView.Adapter<RouteHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteHolder {
        val binding = SpecificRouteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RouteHolder(binding)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: RouteHolder, position: Int) {
        val item = listData.get(position)
        holder.setItem(item)
    }
}

class RouteHolder(val binding:SpecificRouteItemBinding):RecyclerView.ViewHolder(binding.root){
    fun setItem(item:RouteDetailItem){
        try {
            if(item.summary.contains("도보")){
                binding.specificItemImage.setImageResource(R.drawable.pedestrianrouteimage)
            }
            else if(item.summary.contains("정류장")){
                binding.specificItemImage.setImageResource(R.drawable.greenbusrouteimage)
            }
            binding.specificItemFromText.text = item.fromName
            binding.specificItemToText.text = item.toName
            binding.specificItemSummary.text = item.summary
            binding.specificItemTime.text = item.time
        }catch (e:Exception){
            println("등록할 이미지가 없음")
        }
    }
}