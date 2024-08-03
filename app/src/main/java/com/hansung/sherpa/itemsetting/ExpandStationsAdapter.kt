package com.hansung.sherpa.itemsetting

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hansung.sherpa.R
import java.lang.Exception

class ExpandStationsAdapter(private val listData:SectionInfo): RecyclerView.Adapter<ExpandHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpandHolder {
        return ExpandHolder(LayoutInflater.from(parent.context).inflate(R.layout.expand_station_names,parent,false))
    }

    override fun getItemCount(): Int {
        when(listData){
            is PedestrianSectionInfo->{
                return listData.contents.size
            }
            is BusSectionInfo->{
                return listData.stationNames.size
            }
            is SubwaySectionInfo->{
                //TODO 미정
            }
        }

        return 0
    }

    override fun onBindViewHolder(holder: ExpandHolder, position: Int) {
        when(listData){
            is PedestrianSectionInfo->{
                holder.setItem(listData, listData.contents.get(position))
            }
            is BusSectionInfo->{
                holder.setItem(listData, listData.stationNames.get(position))
            }
            is SubwaySectionInfo->{
                //TODO 미정
            }
        }
    }
}

class ExpandHolder(val view: View):RecyclerView.ViewHolder(view){
    fun setItem(itemGroup:SectionInfo, item:String){

        try {
            val image = view.findViewById<ImageView>(R.id.expand_pedestrian_image)
            val stationText = view.findViewById<TextView>(R.id.expand_station_names)

            when(itemGroup){
                is PedestrianSectionInfo->{
                    if(item.contains("우회전")){
                        image.setImageResource(R.drawable.right_arrow)
                    }
                    else if(item.contains("좌회전")){
                        image.setImageResource(R.drawable.left_arrow)
                    }
                    else if(item.contains("횡단보도")){
                        image.setImageResource(R.drawable.cross_walk)
                    }
                    else if(item.contains("공사현장")){
                        image.setImageResource(R.drawable.warning_zone)
                    }
                    else{
                        image.visibility = View.GONE
                    }
                    stationText.text = item
                }
                is BusSectionInfo->{
                    image.visibility = View.GONE
                    stationText.text = item
                }
                is SubwaySectionInfo->{
                    image.visibility = View.GONE
                    stationText.text = item
                }
            }
        }
        catch (E:Exception){
            Log.d("ExpandSpecificImage", "이미지 없음")
        }

    }
}