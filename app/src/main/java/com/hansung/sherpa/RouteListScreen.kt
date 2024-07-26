package com.hansung.sherpa

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.room.util.TableInfo

@Composable
fun SearchScreen(
    navController: NavController = rememberNavController(), // rememberNavController()은 Preview를 생성하기 위함
    modifier: Modifier = Modifier
) {
    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Row(horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
        ) {
            var departureValue by remember { mutableStateOf("") }
            var destinationValue by remember { mutableStateOf("") }

            Column{
                TextField(
                    value = departureValue,
                    onValueChange = {departureValue = it},
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(25.dp)
                        .padding(vertical = 10.dp),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    placeholder = { Text("검색어를 입력하세요") }
                )
                TextField(
                    value = destinationValue,
                    onValueChange = {destinationValue = it},
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(25.dp)
                        .padding(vertical = 10.dp),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    placeholder = { Text("검색어를 입력하세요") }
                )
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