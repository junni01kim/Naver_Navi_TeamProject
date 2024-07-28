package com.hansung.sherpa

import android.graphics.drawable.shapes.Shape
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

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
                        modifier = Modifier
                            .width(60.dp)
                            .height(50.dp),
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
                        modifier = Modifier
                            .width(60.dp)
                            .height(50.dp),
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
        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn {
            item{
                ExpandableCard(title = "Card Title", description = "info")
                ExpandableCard(title = "Test Title", description = "test")
                ExpandableCard(title = "Second Title", description = "second")
            }
        }
    }
}

@Composable
fun ExpandableCard(
    title: String,
    titleFontSize: TextUnit = MaterialTheme.typography.titleLarge.fontSize,
    titleFontWeight: FontWeight = FontWeight.Bold,
    description: String,
    descriptionFontSize: TextUnit = MaterialTheme.typography.titleSmall.fontSize,
    descriptionFontWeight: FontWeight = FontWeight.Normal,
    descriptionMaxLines: Int = 4,
    padding: Dp = 12.dp
) {
    var expandedState by remember { mutableStateOf(false) }

    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        onClick = {
            expandedState = !expandedState
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(6f),
                    text = title,
                    fontSize = titleFontSize,
                    fontWeight = titleFontWeight,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(
                    modifier = Modifier
                        .weight(1f)
                        .alpha(0.2f)
                        .rotate(rotationState),
                    onClick = {
                        expandedState = !expandedState
                    }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Drop-Down Arrow"
                    )
                }
            }
            if (expandedState) {
                Text(
                    text = description,
                    fontSize = descriptionFontSize,
                    fontWeight = descriptionFontWeight,
                    maxLines = descriptionMaxLines,
                    overflow = TextOverflow.Ellipsis
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