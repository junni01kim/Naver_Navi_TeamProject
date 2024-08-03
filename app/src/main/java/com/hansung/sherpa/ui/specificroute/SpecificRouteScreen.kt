package com.hansung.sherpa.ui.specificroute


import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.itemsetting.SectionInfo

enum class DragValue { Start, Center, End }

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SpecificRouteScreen(){
    /*var showRouteDetails:MutableList<SectionInfo> = mutableListOf(
        PedestrianSectionInfo(200.0, 20, "한성대공학관", "한성대학교 정문",0.0,0.0,0.0,0.0,mutableListOf("200m 직진후 횡단보도", "500m 우회전", "50m 앞 공사현장")),
        BusSectionInfo(1600.0, 30, "한성대학교정문", "한성대입구역",0.0,0.0,0.0,0.0, listOf(BusLane("","성북02",0,0,"0",0)), 6, 0,0,0,"null",0,0,0,"null",mutableListOf("한성대입구역", "화정역", "은평구", "어쩌구 저쩌구", "등등")),
        PedestrianSectionInfo(200.0, 5, "한성대입구역", "한성대입구역2번출구",0.0,0.0,0.0,0.0,mutableListOf("200m 직진", "500m 우회전","200m 좌회전", "500m 로롤","200m 직진", "500m 우회전"))
    )*/

    val showRouteDetails = StaticValue.navigation.getDetailTransitRoutes("", "")?.subPath?.map { it.sectionInfo }?.toMutableList()
        ?: emptyList<SectionInfo>().toMutableList()

    val progress by remember { mutableStateOf(0.5f) }


    val density = LocalDensity.current // 화면 밀도
    val screenHeightSizeDp = LocalConfiguration.current.screenHeightDp.dp // 현재 화면 높이 DpSize
    val screenSizePx = with(density) {screenHeightSizeDp.toPx()} // 화면 밀도에 따른 화면 크기 PxSize
    val anchors = remember {
        DraggableAnchors{
            DragValue.Start at 0f
            DragValue.End at 300f
        }
    }

    val state = remember {
        AnchoredDraggableState(
            initialValue = DragValue.Start,
            anchors = anchors,  // 생성자에 anchors 전달
            positionalThreshold = { distance: Float -> distance * 0.1f },
            velocityThreshold = { with(density) { 10.dp.toPx() } },
            snapAnimationSpec = tween(),  // snapAnimationSpec로 지정
            decayAnimationSpec = exponentialDecay(),  // decayAnimationSpec 추가
            confirmValueChange = { true }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Spacer(modifier = Modifier
            .heightIn(min = screenHeightSizeDp-305.dp, max= screenHeightSizeDp-305.dp + 227.dp)
            .height(screenHeightSizeDp-305.dp + state.requireOffset().dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(305.dp)
                .background(Color.Green)
                .anchoredDraggable(state, Orientation.Vertical),
            colors = cardColors(
                containerColor = Color.LightGray  // 카드의 배경 색상 설정
            ),
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp, bottomStart = 0.dp, bottomEnd = 0.dp),
            border = BorderStroke(2.dp, Color.Black)
        ){
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                SpecificPreview(progress)
                SpecificList(showRouteDetails)
            }
        }
    }
}

fun calcTotalTime(){

}




@Preview(showBackground = true)
@Composable
fun SpecificRoutePreview(){
    SpecificRouteScreen()
}