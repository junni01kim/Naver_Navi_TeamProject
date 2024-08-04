package com.hansung.sherpa.searchscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hansung.sherpa.R
import com.hansung.sherpa.itemsetting.BusLane
import com.hansung.sherpa.itemsetting.BusSectionInfo
import com.hansung.sherpa.itemsetting.SubPath
import com.hansung.sherpa.itemsetting.SubwayLane
import com.hansung.sherpa.itemsetting.SubwaySectionInfo
import com.hansung.sherpa.itemsetting.TransportRoute

/**
 * 경로 안내를 진행할 경로를 검색하고 결정하는 화면
 * 화면은 SearchArea, SortingArea, RouteListArea로 구성된다.
 * 
 * SearchArea: 출발지와 도착지를 입력하고, 입력 값을 기반으로 경로를 요청하는 영역
 * SortingArea: 결과 경로 리스트를 정렬하여 보여주기 설정 영역
 * RouteListArea: 경로 요청 후 실직적으로 보여줄 경로 리스트 영역
 *
 * @param navController 홈화면 navController 원형, ※ 화면을 이동한다면, 매개변수로 지정 필수
 */
@Composable
fun SearchScreen(
    navController: NavHostController = rememberNavController(), // rememberNavController()은 Preview를 생성하기 위함
    destinationValue:String = "", // ""는 Preview를 생성하기 위함
    modifier: Modifier = Modifier,
) {
    // 검색 경로들이 저장될 객체이다.
    var routeList by remember { mutableStateOf(listOf<TransportRoute>())}
    // 경로를 요청한 시간을 저장하기 위한 변수이다. State Hoisting을 이용해 값을 통일하기 위해 해당 위치에 저장
    var searchingTime by remember { mutableStateOf( 0L )}

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.LightGray),
        verticalArrangement = Arrangement.spacedBy(2.dp)) {
        // 검색 항목을 구현한 Composable
        // 출발지와 도착지를 입력하고, 입력 값을 기반으로 경로를 요청하는 영역
        SearchArea(navController, destinationValue){ childRouteList, childSearchingTime ->
            routeList = childRouteList
            searchingTime = childSearchingTime
        }

        // 하단 LazyColumn item을 정렬 방식을 지정하는 Composable
        // 결과 경로 리스트를 정렬하여 보여주기 설정 영역
        SortingArea(searchingTime)

        // 경로 검색 결과 리스트가 나오는 Composable
        // 경로 요청 후 실직적으로 보여줄 경로 리스트 영역
        RouteListArea(routeList, searchingTime)
    }
}









fun getLaneName(subPath: SubPath): String{
    var name: String? = null
    when(subPath.trafficType){
        // 지하철
        1 -> {
            val subway = subPath.sectionInfo as SubwaySectionInfo
            val subwayLane = subway.lane[0] as SubwayLane
            name = "${subwayLane.name}호선"
        }

        // 버스 <- 지역마다 색상이 달라서, 경기 서울 기준으로 색 부여
        // https://librewiki.net/wiki/%ED%8B%80:%EB%B2%84%EC%8A%A4_%EB%85%B8%EC%84%A0%EC%83%89
        2 -> {
            val bus = subPath.sectionInfo as BusSectionInfo
            val busLane = bus.lane[0] as BusLane
            name = "${busLane.busNo}번"
        }
        // 도보
        3 -> name = "도보"
    }
    return name!!
}

fun hourOfMinute(minute:Int) =
    if(minute > 60) "${minute/60}시간 ${minute%60}분"
    else if(minute % 60 == 0) "${minute/60}시간"
    else "${minute%60}분"

fun minuteOfSecond(second:Int) =
    if(second >= 60) "${second/60}분 뒤 도착"
    else if(second == -1) "도착 정보 없음"
    else "곧 도착"

@Composable
fun typeOfIcon(trafficType: Int) =
    when(trafficType) {
        // 지하철
        1 -> ImageVector.vectorResource(R.drawable.subway)
        // 버스
        2 -> ImageVector.vectorResource(R.drawable.express_bus)
        // 도보
        3 -> ImageVector.vectorResource(R.drawable.walk)
        else -> ImageVector.vectorResource(R.drawable.close)
    }

fun typeOfColor(subPath: SubPath):Color {
    var color:Color? = null
    when(subPath.trafficType){
        // 지하철
        1 -> {
            val subway = subPath.sectionInfo as SubwaySectionInfo
            val subwayLane = subway.lane[0] as SubwayLane
            color = when(subwayLane.subwayCode){
                // 1호선
                1 -> Color(0xFF0052A4)
                // 2호선
                2 -> Color(0xFF00A84D)
                // 3호선
                3 -> Color(0xFFEF7C1C)
                // 4호선
                4 -> Color(0xFF00A5DE)
                // 5호선
                5 -> Color(0xFF996CAC)
                // 6호선
                6 -> Color(0xFFCD7C2F)
                // 7호선
                7 -> Color(0xFF747F00)
                // 8호선
                8 -> Color(0xFFE6186C)
                else -> Color(0x00000000)
            }
        }

        // 버스 <- 지역마다 색상이 달라서, 경기 서울 기준으로 색 부여
        // https://librewiki.net/wiki/%ED%8B%80:%EB%B2%84%EC%8A%A4_%EB%85%B8%EC%84%A0%EC%83%89
        2 -> {
            val bus = subPath.sectionInfo as BusSectionInfo
            val busLane = bus.lane[0] as BusLane
            color = when(busLane.type){
                // 일반 (경기 시내일반: 일반)
                1 -> Color(0xFF33CC99)
                // 좌석 (경기 일반좌석: 좌석)
                2 -> Color(0xFF0068b7)
                // 마을
                3 -> Color(0xFF53b332)
                // 직행 좌석
                4 -> Color(0xFFe60012)
                // 공항 버스 (시외버스 공항)
                5 -> Color(0x00a0e9)
                // 간선 급행 (경기 간선급행)
                6 -> Color(0xFFe60012)
                // 외곽 (대전 외곽)
                10 -> Color(0xFF53b332)
                // 간선
                11 -> Color(0xFF0068b7)
                // 지선
                12 -> Color(0xFF53b332)
                // 순환
                13 -> Color(0xFFf2b70a)
                // 광역
                14 -> Color(0xFFe60012)
                // 급행 (부산 급행 ※ 급행은 모두 색상이 다양해서 부산으로 함)
                15 -> Color(0xFFff3300)
                // 관광 버스 (색상 존재 X)
                16 -> Color.Black
                // 농어촌 버스 (색상 존재 X)
                20 -> Color.Black
                // 경기도 시외형버스
                22 -> Color(0xFF)
                // 급행 간선 (인천 급행 간선)
                26 -> Color(0xFF5112ab)
                else -> Color.Black
            }
        }
        // 도보
        3 -> color = Color.LightGray
    }
    return color!!
}

@Preview
@Composable
fun SearchPreview(){
    SearchScreen()
}
