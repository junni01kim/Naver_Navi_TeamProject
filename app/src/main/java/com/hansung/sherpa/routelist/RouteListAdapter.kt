package com.hansung.sherpa.routelist

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.hansung.sherpa.R
import com.hansung.sherpa.convert.PathType

/**
 * RouteListRecyclerView의 Adapter를 정의한 클래스
 * ExpandableRecyclerView이다.
 * @param routeListModelList 'ExpandableRouteListModel' 참고 할 것
 */
class RouteListAdapter (var routeListModelList:MutableList<ExpandableRouteListModel>, var context: Context) :  RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val row = routeListModelList[position]
        when(row.type){
            // 요약 정보 영역
            ExpandableRouteListModel.PARENT -> {
                (holder as RouteListParentViewHolder).remainingtime.text = row.parent.remainingTime
                holder.arrivalTime.text = row.parent.arrivalTime

                // TODO: 아이콘 회전이 작동하지 않는다.
                // 확장 버튼 기능
                holder.closeImage.setOnClickListener{
                    if (row.isExpanded) {
                        row.isExpanded = false
                        collapseRow(position)
                        holder.upArrowImage.visibility = View.GONE
                        holder.closeImage.visibility = View.VISIBLE
                    }else{
                        row.isExpanded = true
                        holder.upArrowImage.visibility = View.VISIBLE
                        holder.closeImage.visibility = View.GONE
                        expandRow(position)
                    }
                }
                holder.upArrowImage.setOnClickListener{
                    if(row.isExpanded){
                        row.isExpanded = false
                        collapseRow(position)
                        holder.upArrowImage.visibility = View.GONE
                        holder.closeImage.visibility = View.VISIBLE
                    }
                }
                holder.layout.setOnClickListener{
                    Log.d("explain", "요약 정보 클릭")
                }
            }
            // 세부 정보 영역
            ExpandableRouteListModel.CHILD -> {
                (holder as RouteListChildViewHolder).transportNumber.text = row.child.transportNumber
                holder.watingTime.text = row.child.watingTime
                when(row.child.iconType){
                    PathType.BUS -> holder.transportIcon.setImageResource(R.drawable.express_bus)
                    PathType.SUBWAY -> holder.transportIcon.setImageResource(R.drawable.subway)
                    PathType.EXPRESSBUS -> holder.transportIcon.setImageResource(R.drawable.express_bus)
                    PathType.TRAIN -> holder.transportIcon.setImageResource(R.drawable.subway)
                    else -> holder.transportIcon.setImageResource(R.drawable.walk)
                }
                holder.layout.setOnClickListener{
                    Log.d("explain", "세부 정보 클릭")
                }
            }
        }
    }

    // item 확장을 위한 함수
    private fun expandRow(position: Int){
        val row = routeListModelList[position]
        var nextPosition = position
        when (row.type) {
            ExpandableRouteListModel.PARENT -> {
                for(child in row.parent.detailRoute){
                    routeListModelList.add(++nextPosition, ExpandableRouteListModel(ExpandableRouteListModel.CHILD, child))
                }
                notifyDataSetChanged()
            }
            ExpandableRouteListModel.CHILD -> {
                notifyDataSetChanged()
            }
        }
    }

    // item 축소를 위한 함수
    private fun collapseRow(position: Int){
        val row = routeListModelList[position]
        var nextPosition = position + 1
        when (row.type) {
            ExpandableRouteListModel.PARENT -> {
                outerloop@ while (true) {
                    if (nextPosition == routeListModelList.size || routeListModelList[nextPosition].type == ExpandableRouteListModel.PARENT) {
                        break@outerloop
                    }
                    routeListModelList.removeAt(nextPosition)
                }
                notifyDataSetChanged()
            }
        }
    }

    // RecyclerView 뷰 홀더 항목은 parent 항목과 child 항목으로 구분된다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            ExpandableRouteListModel.PARENT -> {RouteListParentViewHolder(LayoutInflater.from(parent.context).inflate(
                R.layout.route_list_parent_item, parent, false))}

            ExpandableRouteListModel.CHILD -> { RouteListChildViewHolder(LayoutInflater.from(parent.context).inflate(
                R.layout.route_list_child_item, parent, false))  }

            else -> {RouteListParentViewHolder(LayoutInflater.from(parent.context).inflate(
                R.layout.route_list_parent_item, parent, false))}
        }
    }

    override fun getItemViewType(position: Int): Int = routeListModelList[position].type
    override fun getItemCount(): Int = routeListModelList.size

    inner class RouteListParentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var layout = itemView.findViewById<ConstraintLayout>(R.id.route_list_parent_container)
        internal var remainingtime = itemView.findViewById<TextView>(R.id.remaining_time)
        internal var arrivalTime = itemView.findViewById<TextView>(R.id.arrival_time)
        internal var remainingBar = itemView.findViewById<ProgressBar>(R.id.remaining_bar)
        internal var closeImage = itemView.findViewById<ImageView>(R.id.close_arrow)
        internal var upArrowImage = itemView.findViewById<ImageView>(R.id.up_arrow)
    }

    inner class RouteListChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var layout = itemView.findViewById<ConstraintLayout>(R.id.route_list_parent_container)
        internal var transportIcon = itemView.findViewById<ImageView>(R.id.transport_icon)
        internal var transportNumber = itemView.findViewById<TextView>(R.id.transport_number)
        internal var watingTime = itemView.findViewById<TextView>(R.id.wating_time)
    }
}