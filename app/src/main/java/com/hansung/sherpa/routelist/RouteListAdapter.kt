package com.hansung.sherpa.routelist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hansung.sherpa.R

data class RouteItem(val remainingTime: String, val arrivalTime: String, var isExpanded:Boolean)

class RouteListAdapter(val itemList: List<RouteItem>) :
    RecyclerView.Adapter<RouteListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(routeItem: RouteItem){
            val remainingTime = itemView.findViewById<TextView>(R.id.remaining_time)
            val arrivalTime = itemView.findViewById<TextView>(R.id.arrival_time)
            val expandButton = itemView.findViewById<ImageButton>(R.id.expand_button)
            val layoutExpand = itemView.findViewById<LinearLayout>(R.id.expand_layout)

            remainingTime.text = routeItem.remainingTime
            arrivalTime.text = routeItem.arrivalTime
            expandButton.setOnClickListener{
                val show = toggleLayout(!routeItem.isExpanded, it, layoutExpand)
                routeItem.isExpanded = show
            }
        }
    }

    private fun toggleLayout(isExpanded: Boolean, view: View, layoutExpand:LinearLayout):Boolean{
        ToggleAnimation.toggleArrow(view, isExpanded)
        if(isExpanded){
            ToggleAnimation.expand(layoutExpand)
        } else{
            ToggleAnimation.collapse(layoutExpand)
        }
        return isExpanded
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.route_item,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener
}