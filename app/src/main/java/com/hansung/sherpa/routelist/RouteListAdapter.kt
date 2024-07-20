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
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
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
                initStackBarChart(holder.remainingBar)
                setData(holder.remainingBar)


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
        internal var remainingBar = itemView.findViewById<BarChart>(R.id.chart)
        internal var closeImage = itemView.findViewById<ImageView>(R.id.close_arrow)
        internal var upArrowImage = itemView.findViewById<ImageView>(R.id.up_arrow)
    }

    inner class RouteListChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var layout = itemView.findViewById<ConstraintLayout>(R.id.route_list_parent_container)
        internal var transportIcon = itemView.findViewById<ImageView>(R.id.transport_icon)
        internal var transportNumber = itemView.findViewById<TextView>(R.id.transport_number)
        internal var watingTime = itemView.findViewById<TextView>(R.id.wating_time)
    }

    fun initStackBarChart(barChart: BarChart) {
        // 차트 회색 배경 설정 (default = false)
        barChart.setDrawGridBackground(false)
        // 막대 그림자 설정 (default = false)
        barChart.setDrawBarShadow(false)
        // 차트 테두리 설정 (default = false)
        barChart.setDrawBorders(false)

        val description = Description()
        // 오른쪽 하단 모서리 설명 레이블 텍스트 표시 (default = false)
        description.isEnabled = false
        barChart.description = description

        // X, Y 바의 애니메이션 효과
        barChart.animateY(0)
        barChart.animateX(0)

        // 바텀 좌표 값
        val xAxis: XAxis = barChart.xAxis
        // x축 위치 설정
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        // 그리드 선 수평 거리 설정
        xAxis.granularity = 1f
        // x축 텍스트 컬러 설정
        xAxis.textColor = Color.RED
        // x축 선 설정 (default = true)
        xAxis.setDrawAxisLine(false)
        // 격자선 설정 (default = true)
        xAxis.setDrawGridLines(false)

        val leftAxis: YAxis = barChart.axisLeft
        // 좌측 선 설정 (default = true)
        leftAxis.setDrawAxisLine(false)
        // 좌측 텍스트 컬러 설정
        leftAxis.textColor = Color.BLUE

        val rightAxis: YAxis = barChart.axisRight
        // 우측 선 설정 (default = true)
        rightAxis.setDrawAxisLine(false)
        // 우측 텍스트 컬러 설정
        rightAxis.textColor = Color.GREEN

        // 바차트의 타이틀
        val legend: Legend = barChart.legend
        // 범례 모양 설정 (default = 정사각형)
        legend.form = Legend.LegendForm.LINE
        // 타이틀 텍스트 사이즈 설정
        legend.textSize = 20f
        // 타이틀 텍스트 컬러 설정
        legend.textColor = Color.BLACK
        // 범례 위치 설정
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        // 범례 방향 설정
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        // 차트 내부 범례 위치하게 함 (default = false)
        legend.setDrawInside(false)
    }

    // 차트 데이터 설정
    private fun setData(barChart: BarChart) {

        // Zoom In / Out 가능 여부 설정
        barChart.setScaleEnabled(false)

        val valueList = ArrayList<BarEntry>()
        val title = "걸음 수"

        // 임의 데이터
        for (i in 0 until 5) {
            valueList.add(BarEntry(i.toFloat(), i * 100f))
        }

        val barDataSet = BarDataSet(valueList, title)
        // 바 색상 설정 (ColorTemplate.LIBERTY_COLORS)
        barDataSet.setColors(
            Color.rgb(207, 248, 246), Color.rgb(148, 212, 212), Color.rgb(136, 180, 187),
            Color.rgb(118, 174, 175), Color.rgb(42, 109, 130))

        val data = BarData(barDataSet)
        barChart.data = data
        barChart.invalidate()
    }
}