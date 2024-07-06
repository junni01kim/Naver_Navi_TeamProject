package com.hansung.sherpa.routelist

import android.content.Context
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ExpandableListAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.hansung.sherpa.R


val itemList = arrayListOf(
    RouteItem("12분", "09시 55분 도착", false, listOf(Transport(1,"16-1번","18분 뒤 도착"),Transport(2,"6호선","5분 뒤 도착"),Transport(1, "1302번","곧 도착"))),
    RouteItem("15분","09시 56분 도착", false, listOf(Transport(1,"14-1번","23분 뒤 도착"),Transport(2,"1호선","1분 뒤 도착"),Transport(1, "1302번","곧 도착"))),
    RouteItem("21분","10시 02분 도착", false, listOf(Transport(1,"32번","5분 뒤 도착"),Transport(2,"3호선","3분 뒤 도착"),Transport(1, "1302번","곧 도착"))),
    RouteItem("34분","10시 15분 도착", false, listOf(Transport(1,"56번","한시간 뒤 도착"),Transport(2,"2호선","5분 뒤 도착"),Transport(1, "1302번","곧 도착"))),
    RouteItem("1시간","10시 41분 도착", false, listOf(Transport(1,"2번","12분 뒤 도착"),Transport(1,"12번","7분 뒤 도착"),Transport(1, "1302번","곧 도착")))
)

class RouteListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.route_list);

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_View)

        val routeListAdapter = RouteListAdapter(itemList, this)
        routeListAdapter.notifyDataSetChanged()

        recyclerView.adapter = routeListAdapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        routeListAdapter.setItemClickListener(object : RouteListAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                // 클릭 시 이벤트 작성
                Log.d("explain", "클릭")

            }
        })
    }

    fun createLayout(transport: Transport) : View{
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.expand_item, null) as LinearLayout

        val expandIcon = layout.findViewById<ImageView>(R.id.icon)
        val expandName = layout.findViewById<TextView>(R.id.name)
        val expandRemainingTime = layout.findViewById<TextView>(R.id.expand_remaining_time)

        val icon = when(transport.type){ // 샘플
            1 -> R.drawable.add
            2 -> R.drawable.cancel_widget
            else -> {R.drawable.add}
        }
        expandIcon.setImageResource(icon)
        expandName.text = transport.name
        expandRemainingTime.text = transport.remainingTime

        return layout
    }
}