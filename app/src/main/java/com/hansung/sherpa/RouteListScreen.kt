package com.hansung.sherpa

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRowScopeInstance.weight
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.room.util.TableInfo

@Composable
fun SearchScreen(
    navController: NavController = rememberNavController(), // rememberNavController()은 Preview를 생성하기 위함
    modifier: Modifier = Modifier
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        Row(horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        ) {
            var departureValue by remember { mutableStateOf("") }
            var destinationValue by remember { mutableStateOf("") }

            Column{
                Row{
                    TextField(
                        value = departureValue,
                        onValueChange = {departureValue = it},
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(50.dp),
                        textStyle = TextStyle.Default.copy(fontSize = 12.sp),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        placeholder = { Text("출발지를 입력하세요", fontSize = 12.sp) }
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Button(
                        modifier = Modifier.width(60.dp).height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(Color.Black),
                        onClick = {
                            navController.navigate(SherpaScreen.Search.name)
                        }
                    ) {
                        // 버튼에 들어갈 이미지
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.close),
                            contentDescription = "홈 화면에서 사용하는 검색 버튼"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row{
                    TextField(
                        value = destinationValue,
                        onValueChange = {destinationValue = it},
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(50.dp),
                        textStyle = TextStyle.Default.copy(fontSize = 12.sp),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        placeholder = { Text("목적지를 입력하세요", fontSize = 12.sp) }
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Button(
                        modifier = Modifier.width(60.dp).height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(Color.Black),
                        onClick = {
                            navController.navigate(SherpaScreen.Search.name)
                        }
                    ) {
                        // 버튼에 들어갈 이미지
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.search),
                            contentDescription = "홈 화면에서 사용하는 검색 버튼"
                        )
                    }
                }
            }
        }
        Text("Sample")
    }
}

@Preview
@Composable
fun SearchPreview(){
    SearchScreen()
}