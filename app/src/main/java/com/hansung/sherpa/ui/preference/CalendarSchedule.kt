package com.hansung.sherpa.ui.preference

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CurrentDateColumn(
    selectedDate: LocalDate
){
    var str = selectedDate.format(DateTimeFormatter.ofPattern("yyyy년 M월 d일 "))
    str += when(selectedDate.dayOfWeek){
        java.time.DayOfWeek.MONDAY -> "(월)"
        java.time.DayOfWeek.TUESDAY -> "(화)"
        java.time.DayOfWeek.WEDNESDAY -> "(수)"
        java.time.DayOfWeek.THURSDAY -> "(목)"
        java.time.DayOfWeek.FRIDAY -> "(금)"
        java.time.DayOfWeek.SATURDAY -> "(토)"
        java.time.DayOfWeek.SUNDAY -> "(일)"
        else -> ""
    }

    Column(
        modifier = Modifier.padding(
            horizontal = 16.dp,
            vertical = 4.dp
        )
    ) {
        Text(
            text = str,
            style = androidx.compose.ui.text.TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleColumns(){
    var isEmpty by remember { mutableStateOf(false) }
    var isDeleted = mutableListOf<MutableState<Boolean>>()
    // TODO: 날짜에 해당하는 스케줄 받아오는 API 호출
    // TODO: 날짜별 정렬 필요 (하루종일이 맨 먼저)
    // 스케줄 API 요청 필요
    val scheduleResponseList = remember { mutableStateListOf<ScheduleResponse>() }
    val scheduleList = remember { mutableStateListOf<ScheduleData>() }
    scheduleResponseList.add(ScheduleResponse(
            title = "대학교",
            scheduledLocation = ScheduleLocation(
                "한성대학교",
                0.0,
                0.0
            ),
            isWholeDay = true,
            startDateTime =  0,
            endDateTime = 0,
            repeat = Repeat(repeatable = false, cycle = ""),
            comment = ""
        )
    )

    scheduleResponseList.add(ScheduleResponse(
            title = "중학교",
            scheduledLocation = ScheduleLocation(
                "한성여중",
                0.0,
                0.0
            ),
            isWholeDay = false,
            startDateTime =  0,
            endDateTime = 0,
            repeat = Repeat(repeatable = false, cycle = ""),
            comment = ""
        )
    )
    scheduleResponseList.add(ScheduleResponse(
            title = "고등학교",
            scheduledLocation = ScheduleLocation(
                "한성여고",
                0.0,
                0.0
            ),
            isWholeDay = false,
            startDateTime =  0,
            endDateTime = 0,
            repeat = Repeat(repeatable = false, cycle = ""),
            comment = ""
        )
    )

    for (scheduleResponse in scheduleResponseList){
        scheduleList.add(
            ScheduleData(
                title = remember { mutableStateOf(scheduleResponse.title) },
                scheduledLocation = remember {
                    ScheduleLocation(
                        scheduleResponse.scheduledLocation.name,
                        scheduleResponse.scheduledLocation.lat,
                        scheduleResponse.scheduledLocation.lon
                    )
                },
                isWholeDay = remember { mutableStateOf(scheduleResponse.isWholeDay) },
                isDateValidate = remember { mutableStateOf(true )},
                startDateTime = remember { mutableLongStateOf(scheduleResponse.startDateTime) },
                endDateTime = remember { mutableLongStateOf(scheduleResponse.endDateTime) },
                repeat = remember { mutableStateOf(
                    Repeat(repeatable = scheduleResponse.repeat.repeatable, cycle = scheduleResponse.repeat.cycle)
                )},
                comment = remember { mutableStateOf(scheduleResponse.comment) }
            )
        )
        isDeleted.add(remember { mutableStateOf(true) })
    }
    
    val onDelete : (ScheduleData) -> Unit = { deleteItem ->
        // TODO: 삭제한 deleteItem -> 서버에서도 삭제
        isDeleted[scheduleList.indexOf(deleteItem)].value = false
        var flag = false
        for(status in isDeleted){
            if(status.value) {
                flag = true
                break
            }
        }
        if(!flag)
            isEmpty = true
    }
    
    if (isEmpty){
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 90.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "등록된 일정이 없습니다.")
        }
    }
    else {
        LazyColumn(modifier = Modifier
            .heightIn(max = 700.dp)
            .wrapContentSize()
            .fillMaxWidth()
            .padding(bottom = 80.dp)
        ){
            items(scheduleList) { item ->
                ScheduleColumn(onDelete = onDelete, scheduleData = item, scheduleResponse = scheduleResponseList[scheduleList.indexOf(item)])
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleColumn(
    scheduleData: ScheduleData,
    scheduleResponse: ScheduleResponse,
    onDelete : (ScheduleData) -> Unit
){
    var state by remember {
        mutableStateOf(true)
    }
    var showEditSheet by remember { mutableStateOf(false) }
    val textStyle = androidx.compose.ui.text.TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    )
    val columnModifier = Modifier
        .padding(
            horizontal = 8.dp,
            vertical = 8.dp
        )
        .fillMaxHeight()
    if (state){
        Row(
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 4.dp
                )
                .clip(RoundedCornerShape(8.dp))
                .background(Color(234, 232, 239))
                .fillMaxWidth()
                .height(70.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            // TODO: 삭제 Dialog 띄워야함
                            onDelete(scheduleData)
                            state = false
                        },
                        onTap = {
                            showEditSheet = true
                        }
                    )
                }
        ){
            Column(
                modifier = columnModifier.width(70.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when(scheduleResponse.isWholeDay){
                    true ->
                        Text(
                            text = "하루종일",
                            fontWeight = FontWeight.Bold
                        )
                    false -> {
                        val simpleDateFormat = SimpleDateFormat("hh:mm")
                        val startTime = simpleDateFormat
                            .format(scheduleResponse.startDateTime)
                        val endTime = simpleDateFormat
                            .format(scheduleResponse.endDateTime)
                        Text(
                            text = startTime,
                            style = textStyle
                        )
                        Text(text = "~" + endTime)
                    }
                }
            }
            Row(modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .width(4.dp)
                .fillMaxHeight(0.65f)
                .background(Color(41, 161, 255))
                .align(Alignment.CenterVertically)){}
            Column(
                modifier = columnModifier,
                verticalArrangement = Arrangement.Center
            ) {
                Row{
                    Text(
                        text = scheduleResponse.title,
                        style = textStyle
                    )
                }
                Row{
                    Text(text = scheduleResponse.scheduledLocation.name)
                }
            }
        }
        if (showEditSheet){
            val closeBottomSheet : (ScheduleData, Boolean) -> Unit = { item, flag ->
                if (flag){
                    scheduleResponse.title = item.title.value
                    scheduleResponse.scheduledLocation.name = item.scheduledLocation.name
                    scheduleResponse.scheduledLocation.lat = item.scheduledLocation.lat
                    scheduleResponse.scheduledLocation.lat = item.scheduledLocation.lon
                    scheduleResponse.isWholeDay = item.isWholeDay.value
                    scheduleResponse.startDateTime = item.startDateTime.longValue
                    scheduleResponse.endDateTime = item.endDateTime.longValue
                    scheduleResponse.comment = item.comment.value
                    scheduleResponse.repeat.repeatable = item.repeat.value.repeatable
                    scheduleResponse.repeat.cycle = item.repeat.value.cycle
                    // TODO: 일정 수정 API 호출
                }
                showEditSheet = false
            }
            // TODO: 올바르지 않은 시간에도 remeber로 저장하니 취소 눌러도 저장됨 response 올려서 상단에서 수정하는 것이 나을 것 같음
            ScheduleBottomSheet(
                closeBottomSheet = closeBottomSheet,
                scheduleData = cloneScheduleData2Response(scheduleResponse = scheduleResponse) ,
                scheduleModalSheetOption = ScheduleModalSheetOption.EDIT)
        }
    }
}

@Composable
fun cloneScheduleData2Response(scheduleResponse: ScheduleResponse): ScheduleData {
    return ScheduleData(
        title = remember { mutableStateOf(scheduleResponse.title) },
        scheduledLocation = remember {
            ScheduleLocation(
                scheduleResponse.scheduledLocation.name,
                scheduleResponse.scheduledLocation.lat,
                scheduleResponse.scheduledLocation.lon,
            )
        },
        isWholeDay = remember { mutableStateOf(scheduleResponse.isWholeDay) },
        isDateValidate = remember { mutableStateOf(true )},
        startDateTime = remember { mutableLongStateOf(scheduleResponse.startDateTime) },
        endDateTime = remember { mutableLongStateOf(scheduleResponse.endDateTime) },
        repeat = remember { mutableStateOf(Repeat(repeatable = scheduleResponse.repeat.repeatable, cycle = scheduleResponse.repeat.cycle)) },
        comment = remember { mutableStateOf(scheduleResponse.comment) }
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreView1() {
    CalendarScreen{}
}