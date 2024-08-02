package com.hansung.sherpa

import android.util.Log
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.hansung.sherpa.convert.Convert
import com.hansung.sherpa.convert.Coordinate
import com.hansung.sherpa.convert.LegRoute
import com.hansung.sherpa.convert.PathType
import com.hansung.sherpa.convert.coordinate
import com.hansung.sherpa.navigation.Navigation
import com.hansung.sherpa.transit.TmapTransitRouteRequest
import com.hansung.sherpa.transit.TmapTransitRouteResponse
import com.hansung.sherpa.transit.TransitManager
import com.hansung.sherpa.transit.TransitRouteService
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

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
    var routeList by remember { mutableStateOf(mutableListOf<TempRoute>())}

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.LightGray),
        verticalArrangement = Arrangement.spacedBy(2.dp)) {
        // 검색 항목을 구현한 Composable
        SearchArea(navController, destinationValue, routeList){ routeList = it
        Log.d("explain", "${routeList[0].arrivalTime}")}

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
fun SearchArea(navController: NavController, _destinationValue: String, routeList: MutableList<TempRoute>, listUpdate: (MutableList<TempRoute>) -> Unit) {
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
                        val mappingRouteList = mappingToTempRoute(getTransitRoutes())
                        listUpdate(mappingRouteList)

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

fun mappingToTempRoute(originalRouteList:MutableList<MutableList<LegRoute>>):MutableList<TempRoute>{
    val routeList = mutableListOf<TempRoute>()
    originalRouteList.forEach {
        routeList.add(
            TempRoute("소요시간", "도착시간", mutableListOf())
        )
        it.forEach{
            routeList.get(routeList.size-1).expandTempRouteList.add(ExpandTempRoute(it.coordinates.size,"출발지 이름","소요시간",it.pathType,"이름","도착시간", it.coordinates))
        }
    }
    return routeList
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
fun RouteListArea(routeList:List<TempRoute>){
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
fun ExpandableCard(route:TempRoute) {
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
                    Text(text = route.totalTime, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(30.dp))
                    Text(text =route.arrivalTime)
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

            Chart(route.expandTempRouteList,route.getCount())

            if (expandedState) {
                ExpandItem()
                ExpandItem()
                ExpandItem()
            }
        }
    }
}

/**
 * 확장 시 세부 정보를 표시하는 영역
 * 이동수단 단위의 정보를 다룬다.
 */
@Composable
fun ExpandItem() {
    Row(modifier = Modifier.padding(5.dp)){
        Text("출발지 이름")
        Spacer(modifier = Modifier.width(10.dp))
        Text("소요시간")
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.walk),
            contentDescription = "디폴트: 도보"
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text("이름")
        Spacer(modifier = Modifier.width(10.dp))
        Text("도착 시간")
    }
}

// 임시 샘플 코드이다.
class TempRoute(val totalTime:String, val arrivalTime:String, val expandTempRouteList:MutableList<ExpandTempRoute>){
    fun getCount():Int {
        var count = 0
        expandTempRouteList.forEach{
            count+= it.coordinate.size
        }
        return count
    }
}
data class ExpandTempRoute(val partTime:Int, val name:String="출발지 이름", val time:String = "소요시간", val type:PathType = PathType.WALK, val number:String = "이름", val time2:String = "도착시간", val coordinate: MutableList<Coordinate>)
object TempColor{
    val color = listOf(Color.Red, Color.Yellow, Color.Green, Color.Blue, Color.Magenta, Color.Black, Color.Gray,Color.Red, Color.Yellow, Color.Green, Color.Blue, Color.Magenta, Color.Black, Color.Gray,Color.Red, Color.Yellow, Color.Green, Color.Blue, Color.Magenta, Color.Black, Color.Gray)
}

/**
 * 대중교통의 정보를 요약해서 표현하기 위한 차트이다.
 * ※ (2024-07-30) 차트의 말단이 원형이 아니어서 현재 다르게 보이지만, 실제의 경우 양끝단이 도보일 거라는 점에서 이후에 고려
 */
@Composable
fun Chart(routeList:List<ExpandTempRoute>, fullTime:Int) {
    val width = 400.dp
    Box {
        Box(
            modifier = Modifier.fillMaxWidth().height(12.dp).clip(CircleShape).background(Color.LightGray)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
        ) {
            routeList.forEachIndexed { index, it ->
                Text(
                    text = it.time,
                    fontSize = 7.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .width(width * it.partTime / fullTime)
                        .fillMaxHeight()
                        .background(
                            TempColor.color[index],
                            CircleShape
                        ), textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
fun SearchPreview(){
    SearchScreen()
}

// TODO: 임시 코드
// 경로 요청 값 만들기
private fun setRouteRequest(startLatLng: LatLng, endLatLng: LatLng):TmapTransitRouteRequest {
    return TmapTransitRouteRequest(
        startX = startLatLng.longitude.toString(),
        startY = startLatLng.latitude.toString(),
        endX = endLatLng.longitude.toString(),
        endY = endLatLng.latitude.toString(),
        lang = 0,
        format = "json",
        count = 10
    )
}

fun getTransitRoutes(): MutableList<MutableList<LegRoute>> {
    // 검색어 기반 좌표 검색
    /**
     * 미완성이라 주석처리
     * val SL = SearchLocation()
     * startLatLng = SL.searchLatLng(start)
     * endLatLng = SL.searchLatLng(end)
     **/

    val tempStartLatLng = LatLng(37.5004198786564, 127.126936754911) // 인천공항 버스 정류소(오금동)
    val tempEndLatLng = LatLng(37.6134436427887, 126.926493082645) // 은평청여울수영장

    // 좌표 기반 경로 검색
    val routeRequest = setRouteRequest(tempStartLatLng, tempEndLatLng)
    val transitRouteResponse = test(routeRequest)
    val transitRoutes = Convert().convertToRouteMutableLists(transitRouteResponse)

    return transitRoutes
}

fun test(routeRequest: TmapTransitRouteRequest): TmapTransitRouteResponse {
    val appKey = BuildConfig.TMAP_APP_KEY // 앱 키
    lateinit var rr: TmapTransitRouteResponse
    runBlocking<Job> {
        launch(Dispatchers.IO) {
            try {
                val response = Retrofit.Builder()
                    .baseUrl("https://apis.openapi.sk.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create<TransitRouteService?>(TransitRouteService::class.java)
                    .postTransitRoutes(appKey, routeRequest).execute() // API 호출
                rr = Gson().fromJson(response.body()!!.string(), TmapTransitRouteResponse::class.java)
                // Error Log
                /*if (rr.metaData == null) {
                    val errorCode = Gson().fromJson(response.body()!!.string(), TmapTransitErrorCode::class.java)
                    Log.e("Error", "Error Code: ${errorCode.result?.status}, ${errorCode.result?.message}")
                    // getOdsayTransitRoute(Convert().convertTmapToOdsayRequest(routeRequest))
                }*/
            } catch (e: IOException) {
                Log.e("Error", "Transit API Exception")
                rr = TmapTransitRouteResponse()
            }
        }
    }
    return rr
}