package com.hansung.sherpa.ui.searchscreen

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hansung.sherpa.dialog.SherpaDialog
import com.hansung.sherpa.itemsetting.TransportRoute

/**
 * 경로 안내를 진행할 경로를 검색하고 결정하는 화면
 * 화면은 SearchArea, SortingArea, RouteListArea로 구성된다.
 * 
 * SearchArea: 출발지와 도착지를 입력하고, 입력 값을 기반으로 경로를 요청하는 영역
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
    var searchingTime by remember { mutableStateOf( System.currentTimeMillis() )}

    var dialogToggle = remember { mutableStateOf(false) }

    if(dialogToggle.value){
        SherpaDialog(
            title = "승인요청",
            message = listOf("보호자에게\n경로를 요청하시겠습니까?"),
            confirmButtonText = "요청",
            dismissButtonText = "취소"
        ) {
            // TODO: 보호자에게 조회 권한 요청 보내기
            // TODO: 나중에 인증받으면 StaticValue.searchPermission을 true로 할 것

            dialogToggle.value = false
        }
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.LightGray),
        verticalArrangement = Arrangement.spacedBy(2.dp)) {
        // 검색 항목을 구현한 Composable
        // 출발지와 도착지를 입력하고, 입력 값을 기반으로 경로를 요청하는 영역
        SearchArea(navController, destinationValue, dialogToggle){ childRouteList, childSearchingTime ->
            routeList = childRouteList
            searchingTime = childSearchingTime
        }

        // 경로 검색 결과 리스트가 나오는 Composable
        // 경로 요청 후 실직적으로 보여줄 경로 리스트 영역
        RouteListArea(navController, routeList, searchingTime)
    }
}

@Preview
@Composable
fun SearchPreview(){
    SearchScreen()
}
