package com.hansung.sherpa.routelist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.hansung.sherpa.R

class RouteList:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.route_list);

        val routeList = findViewById<RecyclerView>(R.id.recyclerView)
        val itemList = ArrayList<RouteItem>()

        itemList.add(RouteItem("1"))
        itemList.add(RouteItem("2"))
        itemList.add(RouteItem("3"))
        itemList.add(RouteItem("4"))

        val routeListAdapter = RouteListAdapter(itemList)
        routeListAdapter.notifyDataSetChanged()

        routeList.adapter = routeListAdapter
    }
}