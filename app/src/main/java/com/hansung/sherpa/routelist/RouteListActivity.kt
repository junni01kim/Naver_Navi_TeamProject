package com.hansung.sherpa.routelist


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextClock
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.hansung.sherpa.R
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.itemsetting.RouteDetailAdapter
import com.hansung.sherpa.itemsetting.RouteDetailItem

/**
 * 경로 검색창(Activity) 내비게이션을 조회 할 출발지와 목적지를 입력한다.
 * 'route_list.xml'을 이용한다.
 */
class RouteListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.route_list)

        // RouteListActivity 내부의 Button
        val backActivityImageButton = findViewById<ImageButton>(R.id.back_activity_image_button)
        val searchImageButton = findViewById<ImageButton>(R.id.search_image_button)
        val changeImageButton = findViewById<ImageButton>(R.id.change_image_button)

        // RouteListActivity 내부의 TextView
        val destinationTextView = findViewById<EditText>(R.id.destination_edit_text)
        val departureTextView = findViewById<EditText>(R.id.departure_edit_text)
        
        // 현재는 미구현
        val departureTimeTextView = findViewById<TextClock>(R.id.departure_time_text_clock)
        val routeSortingTextView = findViewById<TextView>(R.id.route_sorting_text_view)

        // RecyclerView 동작 코드
        val recyclerView = findViewById<RecyclerView>(R.id.route_list_recycler_View)
        var routeListAdapter = RouteListAdapter(mutableListOf(),this)

        routeListAdapter.notifyDataSetChanged()
        recyclerView.adapter = routeListAdapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // HomeActivity의 destinationEditText 값 전달
        destinationTextView.setText(intent.getStringExtra("destination"))

        // MainActivity로 이동하는 리스너(뒤로가기)
        backActivityImageButton.setOnClickListener{
            finish()
        }

        // 출발지와 목적지의 경로 리스트를 보여주는 리스너
        searchImageButton.setOnClickListener{
            if(departureTextView.text.toString() == "") {
                departureTextView.requestFocus()
                return@setOnClickListener
            }

            if(destinationTextView.text.toString() == "") {
                destinationTextView.requestFocus()
                return@setOnClickListener
            }

            val navigation = StaticValue.navigation
            val routeList = navigation.getTransitRoutes(departureTextView.text.toString(), destinationTextView.text.toString())

            val tempRouteList:MutableList<ExpandableRouteListModel> = mutableListOf()

            for (i in routeList){
                val detailRouteList:MutableList<Route.DetailRoute> = mutableListOf()
                for (j in i){
                    detailRouteList.add(Route.DetailRoute(j.pathType, "대중교통 번호", "대기시간"))
                }
                tempRouteList.add(ExpandableRouteListModel(ExpandableRouteListModel.PARENT, Route("소요시간","도착시간", i, detailRouteList)))            }

            routeListAdapter = RouteListAdapter(tempRouteList, this)
            recyclerView.adapter = routeListAdapter

            routeListAdapter.notifyDataSetChanged()
        }

        // 출발지와 목적지 text를 바꿔주는 버튼(리스너)
        changeImageButton.setOnClickListener {
            val temp = departureTextView.text.toString()
            departureTextView.setText(destinationTextView.text.toString())
            destinationTextView.setText(temp)
        }
    }
}