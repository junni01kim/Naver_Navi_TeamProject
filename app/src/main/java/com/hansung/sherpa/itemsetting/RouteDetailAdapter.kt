package com.hansung.sherpa.itemsetting

import android.content.Context
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.hansung.sherpa.R
import java.lang.Exception

class RouteDetailAdapter(private val listData:MutableList<SectionInfo>, val context:Context): RecyclerView.Adapter<RouteHolder>() {

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
            val transitNum = view.findViewById<TextView>(R.id.number_of_transit)
            val layover = view.findViewById<TextView>(R.id.layover)

            when(item){
                is PedestrianSectionInfo -> {
                    image = view.findViewById<ImageView>(R.id.specific_item_image)
                    image.setImageResource(R.drawable.pedestrianrouteimage)

                    specificItemSummary.text = "도보 " + item.distance.toInt().toString() + "m 이동"

                    expandImage.setImageResource(R.drawable.pedestrianrouteimage)
                    transitNum.visibility = View.GONE
                    layover.text = mutableToString(item.contents)
                }
                is BusSectionInfo -> {
                    image = view.findViewById<ImageView>(R.id.specific_item_image)
                    image.setImageResource(R.drawable.greenbusrouteimage)

                    specificItemSummary.text = item.stationCount.toString() + "개 정류장"

                    expandImage.setImageResource(R.drawable.greenbusrouteimage) // TODO 색상 구별 추가
                    transitNum.text = item.lane[0].name // 버스 번호
                    layover.text = mutableToString(item.stationNames)
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

    private fun mutableToString(ml:MutableList<String>):String{
        var ret = ""
        for(i in ml){
            ret += (i+"\n")
        }
        ret.trimEnd()
        return ret
    }
}