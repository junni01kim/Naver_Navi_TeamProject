package com.hansung.sherpa.ui.specificroute

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hansung.sherpa.R
import com.hansung.sherpa.itemsetting.BusLane
import com.hansung.sherpa.itemsetting.BusSectionInfo
import com.hansung.sherpa.itemsetting.PedestrianSectionInfo
import com.hansung.sherpa.itemsetting.SectionInfo
import kotlin.math.roundToInt
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun SpecificList(showRouteDetails:MutableList<SectionInfo>){
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(0.5.dp, color = Color.Black)
    ) {
        items(items = showRouteDetails){item->
            when(item){
                is PedestrianSectionInfo ->{
                    SpecificListItem(R.drawable.pedestrianrouteimage, item.startName, item.endName, item.distance, item.sectionTime, item)
                }
                is BusSectionInfo ->{
                    SpecificListItem(R.drawable.greenbusrouteimage, item.startName, item.endName, item.distance, item.sectionTime, item)
                }
            }
        }

    }
}

/**
 * @param imageSource 경로에 따라 보여질 이미지
 * @param fromName 출발지 이름
 * @param toName 도착지 이름
 * @param total 총걸리는 비용 (도보 : 150m, 대중교통 : 6개 정류장)
 * @param totalTime 총걸리는 시간
 */
@Composable
fun SpecificListItem(imageSource: Int, fromName:String,toName: String ,total:Double, totalTime:Int, origin:SectionInfo){
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .wrapContentHeight()
            .bottomBorder(strokeWidth = 1.dp, color = if(expanded){ Color.Transparent }else{ Color.Black })
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(// 좌측 부분
                modifier = Modifier.wrapContentSize()
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(imageSource),
                        contentDescription = "이동 수단 이미지",
                        modifier = Modifier
                            .width(20.dp)
                            .height(40.dp)
                            .padding(2.dp)
                    )
                    Column (
                        modifier = Modifier
                            .wrapContentHeight()
                            .width(150.dp)
                            .padding(start = 6.dp)
                    ){
                        Text(text = "${cuttingString(fromName)}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "${total.roundToInt()}m", fontSize = 20.sp, fontWeight = FontWeight.W300)
                            Text(text = "${totalTime}분", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            Box {
                Row(
                    modifier = Modifier.wrapContentSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "${cuttingString(toName)}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                            contentDescription = "세부 경로 보이기 화살표"
                        )
                    }
                }
            }
        }
    }
    // expand 버튼을 눌렀을 때 보여질 이미지
    SpecificContents(origin, expanded)
}

fun cuttingString(target:String):String{
    val to = 8
    if(target.length<=to){
        return target
    }

    return target.substring(0 until (to-1)) + "..."
}

fun Modifier.bottomBorder(strokeWidth: Dp, color: Color) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawBehind {
            val width = size.width
            val height = size.height - strokeWidthPx/2

            drawLine(
                color = color,
                start = Offset(x = 0f, y = height),
                end = Offset(x = width , y = height),
                strokeWidth = strokeWidthPx
            )
        }
    }
)

@Preview(showBackground = true)
@Composable
fun previewSpecificList(){
    var showRouteDetails:MutableList<SectionInfo> = mutableListOf(
        PedestrianSectionInfo(200.0, 20, "한성대공학관", "한성대학교 정문",0.0,0.0,0.0,0.0,mutableListOf("200m 직진후 횡단보도", "500m 우회전", "50m 앞 공사현장", "200m 직진")),
        BusSectionInfo(1600.0, 30, "한성대학교정문", "한성대입구역",0.0,0.0,0.0,0.0, listOf(
            BusLane("","성북02",0,0,"0",0)
        ), 6, 0,0,0,"null",0,0,0,"null",mutableListOf("한성대입구역", "화정역", "은평구", "어쩌구 저쩌구", "등등")),
        PedestrianSectionInfo(200.0, 5, "한성대입구역", "한성대입구역2번출구",0.0,0.0,0.0,0.0,mutableListOf("200m 직진", "500m 우회전","200m 좌회전", "500m 로롤","200m 직진", "500m 우회전"))
    )
    SpecificList(showRouteDetails)
}