package com.hansung.sherpa.routelist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hansung.sherpa.R

/**
 * 'expand_item.xml'에 작성할 내용 sample
 */
data class Transport(var type:Int, var name: String, var remainingTime: String)

/**
 * searchLocation에서 받아올 데이터 sample
 */
data class RouteItem(val remainingTime: String, val arrivalTime: String, var isExpanded:Boolean, val transportList:List<Transport>)

class RouteListAdapter(val itemList: List<RouteItem>, val context: Context) :
    RecyclerView.Adapter<RouteListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(routeItem: RouteItem){
            val remainingTime = itemView.findViewById<TextView>(R.id.remaining_time)
            val arrivalTime = itemView.findViewById<TextView>(R.id.arrival_time)
            val expandButton = itemView.findViewById<ImageButton>(R.id.expand_button)
            val layoutExpand = itemView.findViewById<LinearLayout>(R.id.expand_layout)

            for (i in routeItem.transportList) layoutExpand.addView(createLayout(i))

            remainingTime.text = routeItem.remainingTime
            arrivalTime.text = routeItem.arrivalTime

            expandButton.setOnClickListener{
                val show = toggleLayout(!routeItem.isExpanded, it, layoutExpand)
                routeItem.isExpanded = show
            }
        }
    }

    fun createLayout(transport: Transport) : View{
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.expand_route_item, null) as LinearLayout

        val expandIcon = layout.findViewById<ImageView>(R.id.expand_icon)
        val expandName = layout.findViewById<TextView>(R.id.expand_name)
        val expandRemainingTime = layout.findViewById<TextView>(R.id.expand_remaining_time)

        val icon = when(transport.type){ // 샘플
            1 -> R.drawable.directions_bus
            2 -> R.drawable.train
            3 -> R.drawable.walk
            else -> {R.drawable.walk}
        }
        expandIcon.setImageResource(icon)
        expandName.text = transport.name
        expandRemainingTime.text = transport.remainingTime

        return layout
    }

    private fun toggleLayout(isExpanded: Boolean, view: View, layoutExpand:LinearLayout):Boolean{
        ToggleAnimation.toggleArrow(view, isExpanded)
        if(isExpanded){
            ToggleAnimation.expand(layoutExpand)
        } else{
            ToggleAnimation.collapse(layoutExpand)
        }
        return isExpanded
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.route_item,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener
}