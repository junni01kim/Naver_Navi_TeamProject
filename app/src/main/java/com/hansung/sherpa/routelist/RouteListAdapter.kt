package com.hansung.sherpa.routelist

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.hansung.sherpa.R
import com.hansung.sherpa.convert.LegRoute
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
                initStackBarChart(holder.remainingBar)
                setData(holder.remainingBar, row.parent.legRouteList)

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
        internal var remainingBar = itemView.findViewById<HorizontalBarChart>(R.id.chart)
        internal var closeImage = itemView.findViewById<ImageView>(R.id.close_arrow)
        internal var upArrowImage = itemView.findViewById<ImageView>(R.id.up_arrow)
    }

    inner class RouteListChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var layout = itemView.findViewById<ConstraintLayout>(R.id.route_list_parent_container)
        internal var transportIcon = itemView.findViewById<ImageView>(R.id.transport_icon)
        internal var transportNumber = itemView.findViewById<TextView>(R.id.transport_number)
        internal var watingTime = itemView.findViewById<TextView>(R.id.wating_time)
    }

    // setDrawLabels 필요
    fun initStackBarChart(barChart: HorizontalBarChart) {
        // 변수 설정
        val xAxis: XAxis = barChart.xAxis
        val axisLeft: YAxis = barChart.axisLeft
        val axisRight: YAxis = barChart.axisRight

        // 두께 및 길이 설정
        axisLeft.axisMinimum = 0f // 좌우 최소 길이
        axisLeft.axisMaximum = 100f // 좌우 최대 길이
        xAxis.axisMaximum = 1.5f // bar 두께

        // 축 선 설정 (default = true)
        xAxis.setDrawAxisLine(false)
        axisLeft.setDrawAxisLine(false)
        axisRight.setDrawAxisLine(false)

        // 격자선 설정 (default = true)
        xAxis.setDrawGridLines(false)
        axisLeft.setDrawGridLines(false)
        axisRight.setDrawGridLines(false)

        // 라벨 설정 (default = true)
        xAxis.setDrawLabels(false)
        axisLeft.setDrawLabels(false)
        axisRight.setDrawLabels(false)
    }

    // 차트 데이터 설정
    private fun setData(barChart: HorizontalBarChart, legRouteList: MutableList<LegRoute>) {
        // 막대(범례?)가 하나이므로 리스트에는 하나만 추가
        val valueList = ArrayList<BarEntry>()

        // TODO: 경로 개수만큼 경로 분할
        val transportList:MutableList<Float> = mutableListOf()
        for (i in legRouteList) {
            transportList.add(100f/legRouteList.size)
        }
        valueList.add(BarEntry(0f, transportList.toFloatArray()))


        // 바 색상 설정 (ColorTemplate.LIBERTY_COLORS) 리스트 별로 1대1 매칭
        val barDataSet = BarDataSet(valueList, "") // 값 리스트와 타이틀 이름("")을 인자로 함

        // TODO: 타입 별로 색 지정
        val colorList:MutableList<Int> = mutableListOf()

        for(i in legRouteList){
            val color = when(i.pathType){
                PathType.BUS -> Color.GREEN
                PathType.SUBWAY -> Color.BLUE
                PathType.EXPRESSBUS -> Color.RED
                PathType.TRAIN -> Color.DKGRAY
                else -> Color.GRAY
            }
            colorList.add(color)
        }
        barDataSet.colors = colorList

        val data = BarData(barDataSet)
        data.setDrawValues(false)

        barChart.data = data
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.setScaleEnabled(false)
        barChart.invalidate()
    }
}