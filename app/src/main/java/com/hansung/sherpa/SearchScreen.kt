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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

// Search, Back, Change Button에 사용할 공통 속성
class DefaultIcon{
    companion object {
        val modifier = Modifier.size(30.dp)
        val tint = Color.Gray
    }
}

data class StackedData(
    val inputs: List<Float>,
    val colors: List<Color>
)

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
                    modifier = DefaultIcon.modifier,
                    tint = DefaultIcon.tint,
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
                        imageVector = Icons.Default.Close,
                        modifier = DefaultIcon.modifier,
                        tint = DefaultIcon.tint,
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
                        imageVector = Icons.Default.Search,
                        modifier = DefaultIcon.modifier,
                        tint = DefaultIcon.tint,
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
    description: String,
) {
    val padding: Dp = 10.dp
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
                .wrapContentHeight()
                .padding(padding)
        ) {
            Row(verticalAlignment = Alignment.Top){
                Row(verticalAlignment = Alignment.Bottom){
                    Text(text = "12분", fontSize = 25.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(30.dp))
                    Text(text ="9시 53분 도착")
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
                        tint = DefaultIcon.tint,
                        contentDescription = "Drop-Down Arrow"
                    )
                }
            }

            val routeList = listOf(TempRoute(5), TempRoute(25), TempRoute(70) ,TempRoute(50), TempRoute(50))
            Chart(routeList,200)

            if (expandedState) {
                ExpandItem()
                ExpandItem()
                ExpandItem()
            }
        }
    }
}

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

data class TempRoute(val partTime:Int, val name:String="출발지 이름", val time:String = "소요시간", val type:Int = 0, val number:String = "이름", val time2:String = "도착시간")
class TempColor{
    companion object{
        val color = listOf(Color.Red, Color.Yellow, Color.Green, Color.Blue, Color.Magenta)
    }
}
@Composable
fun Chart(routeList:List<TempRoute>, fullTime:Int) {
    val width = 400.dp
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(12.dp).background(Color.LightGray)) {
        routeList.forEachIndexed { index, it ->
            Text(text = it.time,
                fontSize = 7.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(width*it.partTime/fullTime).fillMaxHeight().background(TempColor.color[index],
                    CircleShape), textAlign = TextAlign.Center)
        }
    }
}

@Preview
@Composable
fun SearchPreview(){
    SearchScreen()
}