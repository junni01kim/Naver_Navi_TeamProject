package com.hansung.sherpa.routelist

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.hansung.sherpa.R

/**
 * 샘플 데이터
 */
val itemList = arrayListOf(
    RouteItem("12분", "09시 55분 도착", false,
        listOf(Transport(1,"16-1번","18분 뒤 도착"),Transport(2,"6호선","5분 뒤 도착"),Transport(3, "도보","5분 소요"))),
    RouteItem("15분","09시 56분 도착", false,
        listOf(Transport(1,"14-1번","23분 뒤 도착"),Transport(2,"1호선","1분 뒤 도착"),Transport(1, "1302번","곧 도착"))),
    RouteItem("21분","10시 02분 도착", false,
        listOf(Transport(1,"32번","5분 뒤 도착"),Transport(2,"3호선","3분 뒤 도착"),Transport(1, "1302번","곧 도착"))),
    RouteItem("34분","10시 15분 도착", false,
        listOf(Transport(1,"56번","한시간 뒤 도착"),Transport(2,"2호선","5분 뒤 도착"),Transport(1, "1302번","곧 도착"))),
    RouteItem("1시간","10시 41분 도착", false,
        listOf(Transport(1,"2번","12분 뒤 도착"),Transport(1,"12번","7분 뒤 도착"),Transport(1, "1302번","곧 도착")))
)

/**
 * 경로 검색창(Activity) 내비게이션을 조회 할 출발지와 목적지를 입력한다.
 * 'route_list.xml'을 이용한다.
 */
class RouteListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.route_list)

        val backActivityImageButton = findViewById<ImageButton>(R.id.back_activity_image_button)
        val searchImageButton = findViewById<ImageButton>(R.id.search_image_button)
        val changeImageButton = findViewById<ImageButton>(R.id.change_image_button)

        val destinationTextView = findViewById<EditText>(R.id.destination_edit_text)
        val departureTextView = findViewById<EditText>(R.id.departure_edit_text)

        val string = intent.getStringExtra("destination")
        Log.d("explain", string.toString())
        destinationTextView.setText(string)

        val recyclerView = findViewById<RecyclerView>(R.id.route_list_recycler_View)

        val routeListAdapter = RouteListAdapter(itemList, this)
        routeListAdapter.notifyDataSetChanged()

        recyclerView.adapter = routeListAdapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        routeListAdapter.setItemClickListener(object : RouteListAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                TODO("클릭 시 이벤트 작성")
            }
        })

        backActivityImageButton.setOnClickListener{
            finish()
        }

        searchImageButton.setOnClickListener{
            TODO("검색 버튼 기능 구현")
        }

        changeImageButton.setOnClickListener {
            val temp = departureTextView.text.toString()
            departureTextView.setText(destinationTextView.text.toString())
            destinationTextView.setText(temp)
        }
    }
}