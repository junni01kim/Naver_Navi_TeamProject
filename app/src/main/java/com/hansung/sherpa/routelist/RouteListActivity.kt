package com.hansung.sherpa.routelist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextClock
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hansung.sherpa.R
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.convert.LegRoute
import com.hansung.sherpa.navigation.Navigation

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
        val routeListAdapter = RouteListAdapter( mutableListOf(), this)
        routeListAdapter.notifyDataSetChanged()
        recyclerView.adapter = routeListAdapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // 경로 세부 리스트로 진입하는 리스너(RecyclerView Item)
        routeListAdapter.setItemClickListener(object : RouteListAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                //TODO("클릭 시 이벤트 작성(김재호 팀원 파트로 이동)")
            }
        })

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

            routeListAdapter.routeList = routeList
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