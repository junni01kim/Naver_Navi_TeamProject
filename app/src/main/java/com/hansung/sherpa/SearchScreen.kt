package com.hansung.sherpa

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hansung.sherpa.itemsetting.BusLane
import com.hansung.sherpa.itemsetting.BusSectionInfo
import com.hansung.sherpa.itemsetting.SubPath
import com.hansung.sherpa.itemsetting.SubwayLane
import com.hansung.sherpa.itemsetting.SubwaySectionInfo
import com.hansung.sherpa.itemsetting.TransportRoute
import com.hansung.sherpa.compose.navigation.Navigation
import java.text.SimpleDateFormat

/**
 * 컴포넌트의 속성(modifier)을 관리
 * 값들을 통일하기 위해서 사용하였다.
 * 현재는 싱글톤을 이용하여 제작해두었지만 나중에 별개 클래스로 분리할 예정
 */
object Property {
    // Departure TextField, Destination TextField에 사용할 공통 속성
    object TextField {
        val height = 50.dp
        val modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(height)
        val textStyle = TextStyle.Default.copy(fontSize = 12.sp)
        val shape = RoundedCornerShape(12.dp)
        val containerColor = Color.LightGray
        val textColor = Color.DarkGray
        val singleLine = true
    }

    // Search, Back, Change Button에 사용할 공통 속성
    object Button{
        val modifier = Modifier
            .width(60.dp)
            .height(50.dp)
        val shape = RoundedCornerShape(0.dp)
        val colors = Color.Transparent
    }

    // Search, Back, Change Button에 사용할 공통 속성
    object Icon{
        val modifier = Modifier.size(30.dp)
        val tint = Color.Gray
    }
}

/**
 * 경로 안내를 진행할 경로를 검색하고 결정하는 화면
 * @param navController 홈화면에서 navController 원형, ※ 매개변수 지정 필수
 */
@Composable
fun SearchScreen(
    navController: NavHostController = rememberNavController(), // rememberNavController()은 Preview를 생성하기 위함
    destinationValue:String = "", // ""는 Preview를 생성하기 위함
    modifier: Modifier = Modifier,
) {
    var routeList by remember { mutableStateOf(listOf<TransportRoute>())}

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.LightGray),
        verticalArrangement = Arrangement.spacedBy(2.dp)) {
        // 검색 항목을 구현한 Composable
        SearchArea(navController, destinationValue, routeList){ routeList = it }

        // 하단 LazyColumn item을 정렬 방식을 지정하는 Composable
        SortingArea()

        // 경로 검색 결과 리스트가 나오는 Composable
        RouteListArea(routeList)
    }
}

/**
 * 검색과 관련된 Composable을 작성하는 공간
 * -구성-
 * 버튼: 검색, 뒤로가기, 변경 버튼
 * 입력창: 출발지, 목적지
 */
@Composable
fun SearchArea(navController: NavController, _destinationValue: String, routeList: List<TransportRoute>, listUpdate: (List<TransportRoute>) -> Unit) {
    // 저장되는 데이터 목록
    // Departure TextField, Destination TextField에 사용할 변수
    var departureValue by remember { mutableStateOf("") }
    var destinationValue by remember { mutableStateOf(if (_destinationValue=="아무것도 전달되지 않았음") "" else _destinationValue) }

    // 아이템 간격 모듈화
    val space = 10.dp
    Row(horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.White)
            .padding(vertical = 5.dp)
    ) {
        /**
         * Change Button
         * Departure TextField/ Destination TextField 텍스트 변경 버튼
         */
        Column(verticalArrangement = Arrangement.Center){
            IconButton(modifier = Property.Button.modifier,
                onClick = {
                    val tempString = departureValue
                    departureValue = destinationValue
                    destinationValue = tempString
                }) {
                // 버튼에 들어갈 이미지
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.swap),
                    modifier = Property.Icon.modifier,
                    tint = Property.Icon.tint,
                    contentDescription = "텍스트 전환 버튼 아이콘"
                )
            }
        }

        Column {
            /**
             * Departure TextField
             * 출발지 입력창
             *
             * Back Button
             * 뒤로가기(홈화면) 이동 창
             */
            Row(verticalAlignment = Alignment.CenterVertically){
                TextField(
                    value = departureValue,
                    onValueChange = {departureValue = it},
                    modifier = Property.TextField.modifier,
                    textStyle = Property.TextField.textStyle,
                    shape = Property.TextField.shape,
                    colors = TextFieldDefaults.colors(unfocusedContainerColor = Property.TextField.containerColor, unfocusedTextColor = Property.TextField.textColor, focusedContainerColor = Property.TextField.containerColor),
                    singleLine = Property.TextField.singleLine,
                    placeholder = { Text("출발지를 입력하세요", fontSize = 12.sp) }
                )
                Spacer(modifier = Modifier.width(5.dp))
                IconButton(modifier = Property.Button.modifier,
                    onClick = {
                        navController.popBackStack()
                    }) {
                    // 버튼에 들어갈 이미지
                    Icon(
                        imageVector = Icons.Default.Close,
                        modifier = Property.Icon.modifier,
                        tint = Property.Icon.tint,
                        contentDescription = "뒤로가기 버튼 아이콘"
                    )
                }
            }

            Spacer(modifier = Modifier.height(space))

            /**
             * Destination TextField
             * 도착지 입력창
             *
             * Search Button
             * 경로 검색하기 버튼
             */
            Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically){
                TextField(
                    value = destinationValue,
                    onValueChange = {destinationValue = it},
                    modifier = Property.TextField.modifier,
                    textStyle = Property.TextField.textStyle,
                    shape = Property.TextField.shape,
                    colors = TextFieldDefaults.colors(unfocusedContainerColor = Property.TextField.containerColor, unfocusedTextColor = Property.TextField.textColor, focusedContainerColor = Property.TextField.containerColor),
                    singleLine = Property.TextField.singleLine,
                    placeholder = { Text("목적지를 입력하세요", fontSize = 12.sp) }
                )
                Spacer(modifier = Modifier.width(5.dp))
                /**
                 * 경로 값을 대입해주는 영역
                 * TextField에 있던 값들을 지우고
                 * 해당 내용을 기반으로 경로를 검색한다.
                 */
                IconButton(modifier = Property.Button.modifier,
                    onClick = {
                        departureValue = ""
                        destinationValue = ""

                        // 테스트용 코드 (하단에 코드 샘플 기재) << 부끄러우니까 보지 마세요
                        val transportRoutes = Navigation().getDetailTransitRoutes("tempString","tempString")
                        listUpdate(transportRoutes)

                    }) {
                    // 버튼에 들어갈 이미지
                    Icon(
                        imageVector = Icons.Default.Search,
                        modifier = Property.Icon.modifier,
                        tint = Property.Icon.tint,
                        contentDescription = "검색 버튼 아이콘"
                    )
                }
            }
        }
    }
}

/**
 * LazyColumn에 나열 될 경로를 우선순위 별로 정렬하는데 사용할 버튼들
 *
 * SearchingTime
 * 대중교통 경로가 안내된 시간
 *
 * SortingAlgorithm
 * 대중교통 리스트가 정렬되는 기준
 */
@Composable
fun SortingArea() {
    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .background(Color.White)
        .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text("출발시간")
        Text("최적경로 순")
    }
}

/**
 * 이동할 경로들의 리스트가 나오는 영역
 *
 * LazyColumn
 * 전체 대중교통 리스트가 나온다.
 */
@Composable
fun RouteListArea(routeList:List<TransportRoute>){
    // 샘플 코드
    LazyColumn(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        items(routeList){
            ExpandableCard(it)
        }
    }
}

/**
 * 확장 되는 리스트를 구현하기 위한 공간
 *
 * 탐색된 경로의 전체(요약) 정보가 담기는 영역이다.
 * ※ (2024-07-30) 리스트 확장 후 화면을 밑으로 내렸다가 올리면 리스트가 자동으로 닫히는 오류가 존재한다.
 */
@Composable
fun ExpandableCard(route:TransportRoute) {
    val padding: Dp = 10.dp
    var expandedState by remember { mutableStateOf(false) }

    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f
    )

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(0.dp),
        colors = CardColors(Color.White,Color.DarkGray,Color.White,Color.DarkGray),
        onClick = {
            expandedState = !expandedState
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(padding)
        ) {
            Row(verticalAlignment = Alignment.Top){
                Row(verticalAlignment = Alignment.Bottom){
                    // totalTime 연산 필요
                    Text(text = hourOfMinute(route.info.totalTime), fontSize = 25.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(30.dp))
                    // arrivalTime 연산 필요
                    //Text(text =route.info.totalTime.toString())
                    val arrivalTime = System.currentTimeMillis() + route.info.totalTime*1000 // ms로 반환
                    Text(text = "${SimpleDateFormat("hh:mm").format(arrivalTime)} 도착" )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    modifier = Modifier
                        .size(20.dp)
                        .rotate(rotationState),
                    onClick = {
                        expandedState = !expandedState
                    }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        tint = Property.Icon.tint,
                        contentDescription = "Drop-Down Arrow"
                    )
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            Chart(route.subPath,route.info.totalTime)

            if (expandedState) {
                route.subPath.forEach{
                    ExpandItem(it)
                }
            }
        }
    }
}

/**
 * 확장 시 세부 정보를 표시하는 영역
 * 이동수단 단위의 정보를 다룬다.
 */
@Composable
fun ExpandItem(subPath: SubPath) {
    Row(modifier = Modifier.padding(5.dp)){
        Text("${subPath.sectionInfo.startName?:"도보"}")
        Spacer(modifier = Modifier.width(10.dp))
        Text(hourOfMinute(subPath.sectionInfo.sectionTime!!))
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = typeOfIcon(subPath.trafficType),
            contentDescription = "디폴트: 도보"
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(getLaneName(subPath))
        Spacer(modifier = Modifier.width(10.dp))
        Text("도착 시간")
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

/**
 * 대중교통의 정보를 요약해서 표현하기 위한 차트이다.
 * ※ (2024-07-30) 차트의 말단이 원형이 아니어서 현재 다르게 보이지만, 실제의 경우 양끝단이 도보일 거라는 점에서 이후에 고려
 * @param routeList
 * @param fullTime 시간 기반으로 비율 측정
 */
@Composable
fun Chart(routeList:List<SubPath>, fullTime:Int) {
    val width = 400.dp
    Box {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(15.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(15.dp)
        ) {
            routeList.forEachIndexed { index, it ->
                Box(modifier = Modifier
                    .width((width.value * it.sectionInfo.sectionTime!! / fullTime).dp)
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .background(typeOfColor(it)),
                    contentAlignment = Alignment.Center) {
                    Text(
                        text = "${it.sectionInfo.sectionTime}분",
                        modifier = Modifier.align(Alignment.Center),
                        fontSize = 10.sp,
                        lineHeight = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

fun hourOfMinute(minute:Int) =
    if(minute > 60) "${minute/60}시간 ${minute%60}분"
    else if(minute % 60 == 0) "${minute/60}시간"
    else "${minute%60}분"

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
