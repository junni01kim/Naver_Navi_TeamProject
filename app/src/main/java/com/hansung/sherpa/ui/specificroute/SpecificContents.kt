package com.hansung.sherpa.ui.specificroute

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.hansung.sherpa.R
import com.hansung.sherpa.itemsetting.BusLane
import com.hansung.sherpa.itemsetting.BusSectionInfo
import com.hansung.sherpa.itemsetting.PedestrianSectionInfo
import com.hansung.sherpa.itemsetting.SectionInfo
import com.hansung.sherpa.itemsetting.SubwaySectionInfo

@Composable
fun SpecificContents(sectionInfo:SectionInfo,lineColor:Color, expand:Boolean){
    AnimatedVisibility(
        visible = expand,
        enter = slideInVertically(),
        exit = shrinkVertically(),
        modifier = Modifier
            .background(Color.White)
            .border(2.dp, Color.Transparent)
            .bottomBorder(2.dp, Color.Black)
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .background(Color.White)
        ){
            when(sectionInfo){
                is PedestrianSectionInfo ->{
                    SpecificRouteContentsUI(sectionInfo.contents.toList(), lineColor)
                }
                is BusSectionInfo ->{
                    SpecificRouteContentsUI(sectionInfo.stationNames, lineColor)
                }
                is SubwaySectionInfo ->{
                    SpecificRouteContentsUI(sectionInfo.stationNames, lineColor)
                }
            }
        }
    }
}

@Composable
fun SpecificRouteContentsUI(contents:List<String>, lineColor: Color){
    var composableSize by remember { mutableStateOf(0f) }

    Row {
        DrawTransitLineContents(lineColor, composableSize)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .onGloballyPositioned { layoutCoordinates ->
                    composableSize = layoutCoordinates.size.height.toFloat()
                },
        ) {
            for(content in contents){
                Row(
                    modifier = Modifier.padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if(lineColor==Color.Black){
                        SetContentsImage(content)
                    }
                    Text(
                        text = content,
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(4.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun DrawTransitLineContents(lineColor:Color, contentsSize:Float){
    var drawType = 100f
    var spacing = 0f
    if (lineColor==Color.Black){
        drawType = 15f
        spacing = 20f
    }

    Canvas(
        modifier = Modifier
            .width(50.dp)
            .padding(2.dp)
            .background(Color.White),
        onDraw = {
            drawLine(
                color = lineColor,
                start = Offset(10.dp.toPx(), 0.dp.toPx()),
                end = Offset(10.dp.toPx(), contentsSize-10.dp.toPx()),
                strokeWidth = 5.dp.toPx(),
                cap = StrokeCap.Round,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(drawType, spacing), 0f)
            )
        }
    )
}

@Composable
fun SetContentsImage(text:String){
    val settingImageID:Int
    val imagedesc:String = text

    if(text.contains("우회전")){
        settingImageID = R.drawable.right_arrow
    }
    else if(text.contains("좌회전")){
        settingImageID = R.drawable.left_arrow
    }
    else if(text.contains("횡단보도")){
        settingImageID = R.drawable.cross_walk
    }
    else if(text.contains("공사현장")){
        settingImageID = R.drawable.warning_zone
    }
    else{
        return
    }
    Image(
        painter = painterResource(settingImageID),
        contentDescription = imagedesc,
        modifier = Modifier
            .width(25.dp)
            .height(25.dp)
            .padding(2.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun SpecificRouteContentsPreview(){
    var showRouteDetails:MutableList<SectionInfo> = mutableListOf(
        PedestrianSectionInfo(200.0, 20, "한성대공학관", "한성대학교 정문",0.0,0.0,0.0,0.0,mutableListOf("200m 직진후 횡단보도", "500m 우회전", "50m 앞 공사현장", "200m 직진")),
        BusSectionInfo(1600.0, 30, "한성대학교정문", "한성대입구역",0.0,0.0,0.0,0.0, listOf(
            BusLane("","성북02",0,0,"0",0)
        ), 6, 0,0,0,"null",0,0,0,"null",mutableListOf("한성대입구역", "화정역", "은평구", "어쩌구 저쩌구", "등등")),
        PedestrianSectionInfo(200.0, 5, "한성대입구역", "한성대입구역2번출구",0.0,0.0,0.0,0.0,mutableListOf("200m 직진", "500m 우회전","200m 좌회전", "500m 로롤","200m 직진", "500m 우회전"))
    )
    SpecificContents(showRouteDetails[0], Color.Black, true)
}