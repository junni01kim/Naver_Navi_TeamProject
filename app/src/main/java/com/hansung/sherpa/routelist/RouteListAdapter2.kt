package com.hansung.sherpa.routelist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hansung.sherpa.R
import com.hansung.sherpa.convert.LegRoute

object Constants{
    const val PARENT = 0
    const val CHILD = 1
}

data class ParentData(
    val parentTitle:String?=null,
    var type:Int = Constants.PARENT,
    var subList : MutableList<ChildData> = ArrayList(),
    var isExpanded:Boolean = false
)

data class ChildData(val childTitle:String)

val listData : MutableList<ParentData> = mutableListOf()

class RouteListAdapter2(var mContext: Context, val list: MutableList<ParentData>):
    RecyclerView.Adapter<RouteListAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(legRouteList: MutableList<LegRoute>){
            val remainingTime = itemView.findViewById<TextView>(R.id.remaining_time)
            val arrivalTime = itemView.findViewById<TextView>(R.id.arrival_time)
            val expandButton = itemView.findViewById<ImageButton>(R.id.expand_button)
            val layoutExpand = itemView.findViewById<LinearLayout>(R.id.expand_layout)

            val tempClass = TempClass(legRouteList, false)

            for (i in tempClass.legRouteList) layoutExpand.addView(createLayout(i.pathType))

            remainingTime.text = "전체 소요시간"
            arrivalTime.text = "전체 도착 시간"

            expandButton.setOnClickListener{
                val show = toggleLayout(!tempClass.isExpanded, it, layoutExpand)
                tempClass.isExpanded = show
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if(viewType== Constants.PARENT){
            val rowView: View = LayoutInflater.from(parent.context).inflate(R.layout.route_item, parent,false)
            GroupViewHolder(rowView)
        } else {
            val rowView: View = LayoutInflater.from(parent.context).inflate(R.layout.expand_route_item, parent,false)
            ChildViewHolder(rowView)
        }
    }

    override fun onBindViewHolder(holder: RouteListAdapter2.ViewHolder, position: Int) {

        val dataList = list[position]
        if (dataList.type == Constants.PARENT) {
            holder as GroupViewHolder
            holder.apply {
                parentTV?.text = dataList.parentTitle
                downIV?.setOnClickListener {
                    expandOrCollapseParentItem(dataList,position)
                }
            }
        } else {
            holder as ChildViewHolder

            holder.apply {
                val singleService = dataList.subList.first()
                childTV?.text =singleService.childTitle
            }
        }
    }
    override fun getItemCount(): Int = list.size

    private fun expandOrCollapseParentItem(singleBoarding: ParentData,position: Int) {

        if (singleBoarding.isExpanded) {
            collapseParentRow(position)
        } else {
            expandParentRow(position)
        }
    }

    private fun expandParentRow(position: Int){
        val currentBoardingRow = list[position]
        val services = currentBoardingRow.subList
        currentBoardingRow.isExpanded = true
        var nextPosition = position
        if(currentBoardingRow.type==Constants.PARENT){

            services.forEach { service ->
                val parentModel =  ParentData()
                parentModel.type = Constants.CHILD
                val subList : ArrayList<ChildData> = ArrayList()
                subList.add(service)
                parentModel.subList=subList
                list.add(++nextPosition,parentModel)
            }
            notifyDataSetChanged()
        }
    }

    private fun collapseParentRow(position: Int){
        val currentBoardingRow = list[position]
        val services = currentBoardingRow.subList
        list[position].isExpanded = false
        if(list[position].type==Constants.PARENT){
            services.forEach { _ ->
                list.removeAt(position + 1)
            }
            notifyDataSetChanged()
        }
    }

    override fun getItemViewType(position: Int): Int = list[position].type

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    class GroupViewHolder(row: View) : RecyclerView.ViewHolder(row) {
        val parentTV = row.findViewById(R.id.parent_Title) as TextView?
        val downIV  = row.findViewById(R.id.down_iv) as ImageView?
    }
    class ChildViewHolder(row: View) : RecyclerView.ViewHolder(row) {
        val childTV = row.findViewById(R.id.child_Title) as TextView?

    }
}