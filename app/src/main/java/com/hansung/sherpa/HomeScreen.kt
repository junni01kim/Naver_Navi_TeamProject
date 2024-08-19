package com.hansung.sherpa

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DensityMedium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hansung.sherpa.ui.main.CustomNavigationDrawer
import com.hansung.sherpa.ui.main.ExtendedFABContainer
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberFusedLocationSource
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController(), // rememberNavController()은 Preview를 생성하기 위함
    modifier: Modifier = Modifier
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val onclick = {
        scope.launch {
            drawerState.apply {
                if (isClosed) open() else close()
            }
        }
    }
    val markerIcon = OverlayImage.fromResource(com.naver.maps.map.R.drawable.navermap_location_overlay_icon)
    // Jetpack Compose
    var mapProperties by remember {
        mutableStateOf(
            MapProperties(maxZoom = 10.0, minZoom = 1.0)
        )
    }

    var mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(isLocationButtonEnabled = false)
        )
    }

    val seoul = LatLng(37.532600, 127.024612)
    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        // 카메라 초기 위치를 설정합니다.
        position = CameraPosition(seoul, 11.0)
    }

    val loc = remember { mutableStateOf(LatLng(37.532600, 127.024612)) }
    CustomNavigationDrawer(navController =  navController,  drawerState = drawerState) {
        Box(Modifier.fillMaxSize()) {
            NaverMap(locationSource = rememberFusedLocationSource(isCompassEnabled = true),
                properties = MapProperties(
                    locationTrackingMode = com.naver.maps.map.compose.LocationTrackingMode.Follow,
                ),
                uiSettings = MapUiSettings(
                    isLocationButtonEnabled = true,
                ),
                onLocationChange = { loc.value = LatLng(it.latitude, it.longitude) }) {
                MarkerComponent(loc.value, markerIcon)
            }

            var destinationValue by remember { mutableStateOf("") }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // 검색 텍스트필드 및 검색 버튼을 위한 행
                Row(horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    /**
                     * DestinationTextField
                     *
                     * 목적지를 점색할 내용을 저장하는 Composable
                     * SearchButton을 클릭 시 해당 정보가 함께 이동
                     */
                    TextField(
                        leadingIcon = {
                            IconButton(onClick = { onclick() }) {
                                Icon(Icons.Filled.DensityMedium, contentDescription = "")
                            }
                        },
                        value = destinationValue,
                        onValueChange = {destinationValue = it},
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(55.dp),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        placeholder = { Text("검색어를 입력하세요") },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                    )

                    /**
                     * SearchButton
                     *
                     * DestinationTextField의 정보를 가지고 RouteListScreen으로 이동시키는 버튼
                     */
                    Button(
                        modifier = Modifier
                            .width(66.dp)
                            .height(55.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(Color.Black),
                        onClick = {
                            navController.navigate("${SherpaScreen.Search.name}/${if(destinationValue=="") "아무것도 전달되지 않았음" else destinationValue}")
                        }
                    ) {
                        // 버튼에 들어갈 이미지
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.route_list_submit),
                            contentDescription = "홈 화면에서 사용하는 검색 버튼"
                        )
                    }
                }

                //if(StaticValue.userInfo.role1 == "CARETAKER"){ ExtendedFABContainer() }
                ExtendedFABContainer()
            }
        }
    }

}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
    fun MarkerComponent(loc: LatLng, markerIcon: OverlayImage) {
    Marker(state = MarkerState(position = loc), markerIcon, anchor = Offset(0.5F, 0.5F))
}

@Preview
@Composable
fun HomePreview(){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val onclick = {
        scope.launch {
            drawerState.apply {
                if (isClosed) open() else close()
            }
        }
    }

    CustomNavigationDrawer(drawerState = drawerState) {

        Box(modifier = Modifier.fillMaxSize()) {
            //TODO
            // ## 네이버 맵은 프리뷰에서 생략 ##
            // -네이버 맵  Composable이 있는 영역.
            // -네이버 맵이 키값을 이용한 통신을 해야되는 관계로 프리뷰가 적용되지 않는다.

            var destinationValue by remember { mutableStateOf("") }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // 검색 텍스트필드 및 검색 버튼을 위한 행
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    /**
                     * DestinationTextField
                     *
                     * 목적지를 점색할 내용을 저장하는 Composable
                     * SearchButton을 클릭 시 해당 정보가 함께 이동한다.
                     */
                    TextField(
                        leadingIcon = {
                            IconButton(onClick = { onclick() }) {
                                Icon(Icons.Filled.DensityMedium, contentDescription = "")
                            }
                        },
                        value = destinationValue,
                        onValueChange = { destinationValue = it },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(55.dp),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        placeholder = { Text("검색어를 입력하세요") }
                    )

                    /**
                     * SearchButton
                     *
                     * DestinationTextField의 정보를 가지고 RouteListScreen으로 이동시키는 버튼
                     */
                    Button(
                        modifier = Modifier
                            .width(66.dp)
                            .height(55.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(Color.Black),
                        onClick = { }
                    ) {
                        // 버튼에 들어갈 이미지
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.route_list_submit),
                            contentDescription = "홈 화면에서 사용하는 검색 버튼"
                        )
                    }
                }
                ExtendedFABContainer()
            }

        }
    }

}
