package com.hansung.sherpa.itemsetting

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.hansung.sherpa.R
import java.lang.Exception

class RouteDetailAdapter(private val listData:MutableList<SectionInfo>): RecyclerView.Adapter<RouteHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteHolder {
        return RouteHolder(LayoutInflater.from(parent.context).inflate(R.layout.specific_route_item,parent,false))
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: RouteHolder, position: Int) {
        val item = listData.get(position)
        holder.setItem(item)
    }
}

class RouteHolder(val view: View):RecyclerView.ViewHolder(view){
    fun setItem(item:SectionInfo){
        try {
            val imageButton = view.findViewById<ImageButton>(R.id.expand_button)
            val expandContent = view.findViewById<MaterialCardView>(R.id.specific_route_expand)

            imageButton.setOnClickListener{
                if(expandContent.visibility==View.VISIBLE){
                    expandContent.visibility = View.GONE
                    imageButton.animate().apply {
                        duration = 300
                        rotation(0f)
                    }
                }
                else{
                    expandContent.visibility = View.VISIBLE
                    imageButton.animate().apply {
                        duration = 300
                        rotation(180f)
                    }
                }
            }


            val image:ImageView
            val specificItemFrom = view.findViewById<TextView>(R.id.specific_item_from_text)
            val specificItemTo = view.findViewById<TextView>(R.id.specific_item_to_text)
            val specificItemSummary = view.findViewById<TextView>(R.id.specific_item_summary)
            val specificItemTime = view.findViewById<TextView>(R.id.specific_item_time)

            val expandImage:ImageView = view.findViewById(R.id.expand_route_color_image)

            // 해당 부분은 대중교통에서만 사용
            val numberOfTransitRapper = view.findViewById<LinearLayout>(R.id.number_of_transit_wrapper)
            val transitNum = view.findViewById<TextView>(R.id.number_of_transit)
            val transitWaitingTime = view.findViewById<TextView>(R.id.transit_waiting_time)
            val nextTransitTime = view.findViewById<TextView>(R.id.next_transit_time)

            // 전체 경로 표시 -> 보행자 : 100m앞 우회전... 대중교통 : 정류장 이름들
            //val layover = view.findViewById<TextView>(R.id.layover)
            val stations = view.findViewById<RecyclerView>(R.id.stations)

            when(item){
                is PedestrianSectionInfo -> {
                    image = view.findViewById<ImageView>(R.id.specific_item_image)
                    image.setImageResource(R.drawable.pedestrianrouteimage)

                    specificItemSummary.text = "도보 " + item.distance!!.toInt().toString() + "m 이동"

                    expandImage.setImageResource(R.drawable.pedestrianrouteimage)
                    numberOfTransitRapper.visibility = View.GONE
                    //layover.text = mutableToString(item.contents)
                    stations.adapter = ExpandStationsAdapter(item)
                    stations.layoutManager = LinearLayoutManager(stations.context)//????
                }
                is BusSectionInfo -> {
                    image = view.findViewById<ImageView>(R.id.specific_item_image)
                    image.setImageResource(R.drawable.greenbusrouteimage)

                    specificItemSummary.text = item.stationCount.toString() + "개 정류장"

                    expandImage.setImageResource(R.drawable.greenbusrouteimage) // TODO 색상 구별 추가
                    transitNum.text = item.lane[0].busNo // 버스 번호

                    transitWaitingTime.text = "5분 후 도착" // 해당부분은 명준 API이용
                    nextTransitTime.text = "다음 예정 10분" // 해당 부분은 명준 API이용
                    //layover.text = mutableToString(item.stationNames)
                    stations.adapter = ExpandStationsAdapter(item)
                    stations.layoutManager = LinearLayoutManager(stations.context)
                }
                is SubwaySectionInfo -> {
                    /*TODO 아직 지하철은 이미지 안정해짐*/
                }
            }

            specificItemFrom.text = item.startName
            specificItemTo.text = item.endName

            specificItemTime.text = item.sectionTime.toString() + "분"

        }catch (e:Exception){
            Log.d("NOIMAGE","이미지없음")
        }
    }
}