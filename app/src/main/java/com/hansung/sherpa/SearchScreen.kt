package com.hansung.sherpa

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.material3.TextFieldDefaults
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

/**
 * 컴포넌트의 속성(modifier)을 관리
 */
// Departure TextField, Destination TextField에 사용할 공통 속성
class DefaultTextField {
    companion object {
        val height = 50.dp
        val modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(height)
        val textStyle = TextStyle.Default.copy(fontSize = 12.sp)
        val shape = RoundedCornerShape(12.dp)
        val colors = Color.LightGray
        val singleLine = true
    }
}

// Search, Back, Change Button에 사용할 공통 속성
class DefaultButton{
    companion object {
        val modifier = Modifier
            .width(60.dp)
            .height(50.dp)
        val shape = RoundedCornerShape(0.dp)
        val colors = Color.Transparent
    }
}

@Composable
fun SearchScreen(
    navController: NavController = rememberNavController(), // rememberNavController()은 Preview를 생성하기 위함
    modifier: Modifier = Modifier
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.LightGray),
        verticalArrangement = Arrangement.spacedBy(2.dp)) {
        SearchArea()

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

        LazyColumn(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            item { ExpandableCard(title = "Card Title", description = "info") }
            item { ExpandableCard(title = "Test Title", description = "test") }
            item { ExpandableCard(title = "Second Title", description = "second") }
        }
    }
}

/**
 * 검색과 관련된 Composable을 작성하는 공간
 * -구성-
 * 버튼: 검색, 뒤로가기, 변경 버튼
 * 입력창: 출발지, 목적지
 */
@Composable
fun SearchArea() {
    /**
     * 저장되는 데이터 목록
     */
    // Departure TextField, Destination TextField에 사용할 변수
    var departureValue by remember { mutableStateOf("") }
    var destinationValue by remember { mutableStateOf("") }

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
            IconButton(modifier = DefaultButton.modifier,
                onClick = {
                    // TODO: 장소 검색 결과
                }) {
                // 버튼에 들어갈 이미지
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.swap),
                    contentDescription = "텍스트 전환 버튼 아이콘"
                )
            }
        }

        Column {
            /**
             * Departure TextField/ Back Button
             * 출발지 입력창과 뒤로가기(홈화면) 이동 창 배치
             */
            Row(verticalAlignment = Alignment.CenterVertically){
                TextField(
                    value = departureValue,
                    onValueChange = {departureValue = it},
                    modifier = DefaultTextField.modifier,
                    textStyle = DefaultTextField.textStyle,
                    shape = DefaultTextField.shape,
                    colors = TextFieldDefaults.colors(unfocusedContainerColor = DefaultTextField.colors),
                    singleLine = DefaultTextField.singleLine,
                    placeholder = { Text("출발지를 입력하세요", fontSize = 12.sp) }
                )
                Spacer(modifier = Modifier.width(5.dp))
                IconButton(modifier = DefaultButton.modifier,
                    onClick = {
                        // TODO: 장소 검색 결과
                    }) {
                    // 버튼에 들어갈 이미지
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.close),
                        contentDescription = "뒤로가기 버튼 아이콘"
                    )
                }
            }

            Spacer(modifier = Modifier.height(space))

            /**
             * Destination TextField/ Search Button
             * 도착지 입력창과 경로 검색하기 창 배치
             */
            Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically){
                TextField(
                    value = destinationValue,
                    onValueChange = {destinationValue = it},
                    modifier = DefaultTextField.modifier,
                    textStyle = DefaultTextField.textStyle,
                    shape = DefaultTextField.shape,
                    colors = TextFieldDefaults.colors(unfocusedContainerColor = DefaultTextField.colors),
                    singleLine = DefaultTextField.singleLine,
                    placeholder = { Text("목적지를 입력하세요", fontSize = 12.sp) }
                )
                Spacer(modifier = Modifier.width(5.dp))
                IconButton(modifier = DefaultButton.modifier,
                    onClick = {
                        // TODO: 장소 검색 결과
                    }) {
                    // 버튼에 들어갈 이미지
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.search),
                        contentDescription = "검색 버튼 아이콘"
                    )
                }
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