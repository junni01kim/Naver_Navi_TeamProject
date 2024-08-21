package com.hansung.sherpa.ui.specificroute

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hansung.sherpa.itemsetting.BusLane
import com.hansung.sherpa.itemsetting.BusSectionInfo
import com.hansung.sherpa.itemsetting.PedestrianSectionInfo
import com.hansung.sherpa.itemsetting.SectionInfo
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.hansung.sherpa.compose.chart.typeOfColor
import com.hansung.sherpa.itemsetting.SubwaySectionInfo
import com.hansung.sherpa.itemsetting.TransportRoute

/**
 * 경로의 이동 수단에 따른 전체 내용
 *
 * 버스, 지하철 : 총 몇개의 정류장 이동
 *
 * 보행자 : 총 몇m 이동
 * @param showRouteDetails 명준 UI에서 선택된 경로
 */
@Composable
fun SpecificList(showRouteDetails:TransportRoute){
    val toTransport = 1000f // 교통수단의 경우 연결된 선으로 그려야 함
    val toPedestrian = 10f // 보행자의 경우 점선으로 그려야 함 -> 점선 하나의 선 길이

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(1.dp, color = Color.LightGray)
    ) {
        items(items = showRouteDetails.subPath){item->
            var value = item.sectionInfo
            if(value.distance==0.0 || value.sectionTime==0){ // 이동 수단간 바로 환승 하는 경우(UI로 표시X)
                return@items
            }
            when(value){ // 업 다운 케스팅을 통한 이동 수단에 따른 화면 UI 자동 생성
                is PedestrianSectionInfo ->{
                    SpecificListItem(toPedestrian, value.startName, value.endName, value.distance?.toInt(), value.sectionTime, value)
                }
                is BusSectionInfo ->{
                    SpecificListItem(toTransport, value.startName, value.endName, value.stationCount, value.sectionTime, value, typeOfColor(item))
                }
                is SubwaySectionInfo ->{
                    SpecificListItem(toTransport, value.startName, value.endName, value.stationCount, value.sectionTime, value, typeOfColor(item))
                }
            }
        }

    }
}

/**
 * @param drawType 점선 or 직선
 * @param fromName 출발지 이름
 * @param toName 도착지 이름
 * @param total 총걸리는 비용 (도보 : 150m, 대중교통 : 6개 정류장)
 * @param totalTime 총걸리는 시간
 * @param origin 원본 데이터 (현재 그려질 UI의 원본 내용)
 * @param lineColor 버스 or 지하철 호선 색상
 */
@Composable
fun SpecificListItem(
    drawType:Float,
    fromName:String?,
    toName: String?,
    total:Int? = 0,
    totalTime:Int? = 0,
    origin:SectionInfo,
    lineColor:Color = Color.Black
){
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .wrapContentHeight()
            .bottomBorder(strokeWidth = 1.dp, color = if(expanded){ Color.Transparent } else { Color.LightGray })
            .clickable { expanded = !expanded }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(// 좌측 부분 (전체 이동 시간, 대중교통 : 이동 정류장 수(보행자 : 이동 거리), 출발지 이름)
                modifier = Modifier.wrapContentSize()
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DrawTransitLine(drawType,lineColor)

                    Column (
                        modifier = Modifier
                            .wrapContentHeight()
                            .width(150.dp)
                            .padding(start = 6.dp)
                    ){
                        Text(text = cuttingString(fromName ?: "정보를 불러올 수 없음"), fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val totalText = if (lineColor==Color.Black) "${total}m" else "${total}개 정류장"
                            Text(text = "${totalText}", fontSize = 18.sp, fontWeight = FontWeight.W300)
                            Text(text = "${totalTime}분", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            Box {// 우측 부분 (도착지 이름 및 세부 내용 확장 버튼)
                Row(
                    modifier = Modifier.wrapContentSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = cuttingString(toName ?: "정보를 불러올 수 없음"), fontWeight = FontWeight.Bold, fontSize = 22.sp)
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
    // expand 버튼을 눌렀을 때 보여질 세부 내용들 (어디 앞 우회전, ○○역...)
    SpecificContents(origin, lineColor, expanded)
}

/**
 * 보여질 문자열이 너무 긴 경우 잘라내기 작업
 * @param target 잘라낼 문자열
 */
fun cuttingString(target:String):String{
    val to = 8
    if(target.length<=to){
        return target
    }

    return target.substring(0 until (to-1)) + "..."
}

/**
 * 밑에만 border를 주기위함
 * */
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


/**
* 이동 수단에 따라 점선 or 직선
 * 대중 교통의 경우 해당 색상 적용하여 선을 그림
 * @param drawType 점선 or 직선
 * @param lineNum 버스, 지하철 호선 색상
* */
@Composable
fun DrawTransitLine(drawType: Float, lineNum:Color){
    Canvas(
        modifier = Modifier
            .width(20.dp)
            .height(60.dp)
            .padding(2.dp)
            .background(Color.White),
        onDraw = {
            drawLine(
                color = lineNum,
                start = Offset(10.dp.toPx(), 0.dp.toPx()),
                end = Offset(10.dp.toPx(), 60.dp.toPx()),
                strokeWidth = 5.dp.toPx(),
                cap = StrokeCap.Round,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(drawType, 20f), 0f)// 점선 옵션
            )
        }
    )
}

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
    //SpecificList(showRouteDetails)
}