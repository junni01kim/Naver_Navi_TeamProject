package com.hansung.sherpa.testroutelist

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hansung.sherpa.R

class RouteListStateExpandableAdapter (var context: Context, var routeListModelList:MutableList<ExpandableRouteListModel>) :  RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            ExpandableRouteListModel.PARENT -> {RouteListParentViewHolder(LayoutInflater.from(parent.context).inflate(
                R.layout.expandable_parent_item, parent, false))}

            ExpandableRouteListModel.CHILD -> { RouteListChildViewHolder(LayoutInflater.from(parent.context).inflate(
                R.layout.expandable_child_item, parent, false))  }

            else -> {RouteListParentViewHolder(LayoutInflater.from(parent.context).inflate(
                R.layout.expandable_parent_item, parent, false))}
        }
    }

    override fun getItemCount(): Int = routeListModelList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val row = routeListModelList[position]
        when(row.type){
            ExpandableRouteListModel.PARENT -> {
                (holder as RouteListParentViewHolder).countryName.text = row.parent.country
                holder.closeImage.setOnClickListener {
                    if (row.isExpanded) {
                        row.isExpanded = false
                        collapseRow(position)
                        holder.layout.setBackgroundColor(Color.WHITE)


                    }else{
                        holder.layout.setBackgroundColor(Color.GRAY)
                        row.isExpanded = true
                        holder.upArrowImg.visibility = View.VISIBLE
                        holder.closeImage.visibility = View.GONE
                        expandRow(position)
                    }
                }
                holder.upArrowImg.setOnClickListener{
                    if(row.isExpanded){
                        row.isExpanded = false
                        collapseRow(position)
                        holder.layout.setBackgroundColor(Color.WHITE)
                        holder.upArrowImg.visibility = View.GONE
                        holder.closeImage.visibility = View.VISIBLE

                    }
                }
            }


            ExpandableRouteListModel.CHILD -> {
                (holder as RouteListChildViewHolder).stateName.text = row.chile.name
                holder.capitalImage.text = row.child.capital
            }
        }
    }

    override fun getItemViewType(position: Int): Int = routeListModelList[position].type

    private fun expandRow(position: Int){
        val row = routeListModelList[position]
        var nextPosition = position
        when (row.type) {
            ExpandableRouteListModel.PARENT -> {
                for(child in row.parent.states){
                    routeListModelList.add(++nextPosition, ExpandableRouteListModel(ExpandableRouteListModel.CHILD, child))
                }
                notifyDataSetChanged()
            }
            ExpandableRouteListModel.CHILD -> {
                notifyDataSetChanged()
            }
        }
    }

    private fun collapseRow(position: Int){
        val row = routeListModelList[position]
        var nextPosition = position + 1
        when (row.type) {
            ExpandableRouteListModel.PARENT -> {
                outerloop@ while (true) {
                    //  println("Next Position during Collapse $nextPosition size is ${shelfModelList.size} and parent is ${shelfModelList[nextPosition].type}")

                    if (nextPosition == routeListModelList.size || routeListModelList[nextPosition].type == ExpandableRouteListModel.PARENT) {

                        break@outerloop
                    }

                    routeListModelList.removeAt(nextPosition)
                }

                notifyDataSetChanged()
            }
        }
    }

    class RouteListParentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var layout = itemView.country_item_parent_container
        internal var countryName : TextView = itemView.country_name
        internal var closeImage = itemView.close_arrow
        internal var upArrowImg = itemView.up_arrow

    }

    class RouteListChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var layout = itemView.country_item_child_container
        internal var stateName : TextView = itemView.state_name
        internal var capitalImage = itemView.capital_name

    }
}