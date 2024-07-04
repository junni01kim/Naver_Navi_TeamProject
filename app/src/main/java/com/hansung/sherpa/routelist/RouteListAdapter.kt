package com.hansung.sherpa.routelist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hansung.sherpa.R

data class RouteItem(val remainingTime: String, val arrivalTime: String)

class RouteListAdapter(val itemList: ArrayList<RouteItem>) :
    RecyclerView.Adapter<RouteListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.route_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.remainingTime.text = itemList[position].remainingTime
        holder.arrivalTime.text = itemList[position].arrivalTime

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var remainingTime = itemView.findViewById<TextView>(R.id.remaining_time)
        var arrivalTime = itemView.findViewById<TextView>(R.id.arrival_time)
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener
}