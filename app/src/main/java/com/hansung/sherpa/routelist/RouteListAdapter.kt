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
import com.hansung.sherpa.convert.LegRoute
import com.hansung.sherpa.convert.PathType

/**
 * 'expand_item.xml'에 작성할 내용 sample parameter
 */
data class Transport(var type:Int, var name: String, var remainingTime: String)

/**
 * searchLocation에서 받아올 데이터 sample parameter
 * 대중교통 단위로 나눠진 경로 리스트이다.
 * 
 * @param remainingTime 소요시간
 * @param arrivalTime 도착시간
 * @param isExpanded 세부사항 확장 여부
 * @param transportList 대중교통 이용 정보(변경 가능)
 */
data class RouteItem(val remainingTime: String, val arrivalTime: String, var isExpanded:Boolean, val transportList:List<Transport>)

data class TempClass(val legRouteList: MutableList<LegRoute>, var isExpanded: Boolean)

/**
 * RouteListRecyclerView를 이용하기 위한 Adapter이다.
 * @param itemList 출발지에서 목적지까지 이동할 수 잇는 경우의 수 리스트
 * @param context
 */
class RouteListAdapter(var routeList: MutableList<MutableList<LegRoute>>, val context: Context) :
    RecyclerView.Adapter<RouteListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(legRouteList: MutableList<LegRoute>){
            val remainingTime = itemView.findViewById<TextView>(R.id.remaining_time)
            val arrivalTime = itemView.findViewById<TextView>(R.id.arrival_time)
            val expandButton = itemView.findViewById<ImageButton>(R.id.expand_button)
            val layoutExpand = itemView.findViewById<LinearLayout>(R.id.expand_layout)

            val tempClass = TempClass(legRouteList, false)

            for (i in tempClass.legRouteList) layoutExpand.addView(createLayout(i.pathType))

            remainingTime.text = "전체 소요시간"
            arrivalTime.text = "전체 도착 시간"

            expandButton.setOnClickListener{
                val show = toggleLayout(!tempClass.isExpanded, it, layoutExpand)
                tempClass.isExpanded = show
            }
        }
    }

    /**
     * 각각의 세부경로를 표현하기 위함
     * @param transport 단일 대중교통 정보
     */
    fun createLayout(transport: PathType) : View{
        // 리스트가 늘어나는 오류 발생
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.expand_route_item, null) as LinearLayout

        val expandIcon = layout.findViewById<ImageView>(R.id.expand_icon)
        val expandName = layout.findViewById<TextView>(R.id.expand_name)
        val expandRemainingTime = layout.findViewById<TextView>(R.id.expand_remaining_time)

        // 타입 단위로 이미지를 설정한다.
        val icon = when(transport){
            PathType.BUS -> R.drawable.directions_bus
            PathType.SUBWAY -> R.drawable.train
            PathType.WALK -> R.drawable.walk
            else -> {R.drawable.cancel_widget} // 예외처리: 없는 타입
        }
        expandIcon.setImageResource(icon)
        expandName.text = "대중교통 번호"
        expandRemainingTime.text = "소요 시간"

        return layout
    }

    /**
     * 세부 사항 확장 및 애니메이션 기능
     * @param isExpanded 현재 세부 경로창이 확장되어 있는지 여부
     * @param view
     * @param layoutExpand 확장시킬 레이아웃 주체
     */
    private fun toggleLayout(isExpanded: Boolean, view: View, layoutExpand:LinearLayout):Boolean{
        ToggleAnimation.toggleArrow(view, isExpanded)
        if(isExpanded){
            ToggleAnimation.expand(layoutExpand)
        } else{
            ToggleAnimation.collapse(layoutExpand)
        }
        return isExpanded
    }

    // ---------- 이후는 일반적인 코드 로직이다. ----------
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.route_item,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(routeList[position])

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        return routeList.count()
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener
}