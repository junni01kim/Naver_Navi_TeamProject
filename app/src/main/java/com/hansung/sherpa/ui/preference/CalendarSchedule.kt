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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
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
fun ScheduleColumns(
    scheduleDataList: SnapshotStateList<ScheduleData>
){
    var isEmpty by remember { mutableStateOf(true) }
    var currentVisibleColumnsCount by remember { mutableIntStateOf(scheduleDataList.size) }
    var beforeListSize by remember { mutableIntStateOf(scheduleDataList.size) }
    val isDeleted = remember { mutableStateMapOf<ScheduleData, Boolean>() }
    scheduleDataList.sortWith(
        compareBy<ScheduleData> { !it.isWholeDay.value }
            .thenBy { if (it.isWholeDay.value) it.title.value else "" }
            .thenBy { if (!it.isWholeDay.value) it.startDateTime.longValue else Long.MAX_VALUE }
    )
    for (scheduleData in scheduleDataList){
        if(isDeleted[scheduleData] == null){
            isDeleted[scheduleData] = true
        }
    }

    val onDelete : (ScheduleData) -> Unit = { deleteItem ->
        // TODO: 삭제한 deleteItem -> 서버에서도 삭제
        isDeleted[deleteItem] = false
        currentVisibleColumnsCount--
    }

    LaunchedEffect(scheduleDataList.size) {
        when {
            // 일정 추가 시
            scheduleDataList.size > beforeListSize -> {
                currentVisibleColumnsCount++
                beforeListSize = scheduleDataList.size
                for(scheduleData in scheduleDataList){
                    if(isDeleted[scheduleData] == null){
                        isDeleted[scheduleData] = true
                    }
                }
            }
            // 날짜 변경 시
            beforeListSize != 0 && scheduleDataList.size == 0 -> {
                beforeListSize = 0
                currentVisibleColumnsCount = 0
                isDeleted.clear()
            }
        }
    }
    // item 삭제 시
    LaunchedEffect(currentVisibleColumnsCount) {
        isEmpty = currentVisibleColumnsCount == 0
    }

    when(isEmpty){
        true -> {
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
        false -> {
            LazyColumn(modifier = Modifier
                .heightIn(max = 700.dp)
                .wrapContentSize()
                .fillMaxWidth()
                .padding(bottom = 80.dp)
            ){
                items(scheduleDataList) { item ->
                    if(isDeleted[item]!!)
                        ScheduleColumn(onDelete = onDelete, scheduleData = item)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleColumn(
    scheduleData: ScheduleData,
    onDelete : (ScheduleData) -> Unit
){
    val openDialogCustom = remember { mutableStateOf(false) }
    var state by remember { mutableStateOf(true) }
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

    if(openDialogCustom.value){
        ScheduleDeleteDialog(openDialogCustom = openDialogCustom){ onDeleteClick ->
            when(onDeleteClick) {
                true -> {
                    state = false
                    onDelete(scheduleData)
                }
                false -> { }
            }
        }
    }

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
                            openDialogCustom.value = true
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
                when(scheduleData.isWholeDay.value){
                    true ->
                        Text(
                            text = "하루종일",
                            fontWeight = FontWeight.Bold
                        )
                    false -> {
                        val simpleDateFormat = SimpleDateFormat("HH:mm")
                        val startTime = simpleDateFormat
                            .format(scheduleData.startDateTime.longValue)
                        val endTime = simpleDateFormat
                            .format(scheduleData.endDateTime.longValue)
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
                modifier = columnModifier.padding(horizontal = 4.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Row{
                    Text(
                        text = scheduleData.title.value,
                        style = textStyle
                    )
                }
                Row{
                    Text(text = scheduleData.scheduledLocation.name)
                }
            }
        }
        if (showEditSheet){
            val closeBottomSheet : (ScheduleData, Boolean) -> Unit = { item, flag ->
                if (flag){
                    scheduleData.title.value = item.title.value
                    scheduleData.scheduledLocation.name = item.scheduledLocation.name
                    scheduleData.scheduledLocation.lat = item.scheduledLocation.lat
                    scheduleData.scheduledLocation.lat = item.scheduledLocation.lon
                    scheduleData.isWholeDay.value = item.isWholeDay.value
                    scheduleData.startDateTime.longValue = item.startDateTime.longValue
                    scheduleData.endDateTime.longValue = item.endDateTime.longValue
                    scheduleData.comment.value = item.comment.value
                    scheduleData.repeat.value.repeatable = item.repeat.value.repeatable
                    scheduleData.repeat.value.cycle = item.repeat.value.cycle
                    // TODO: 일정 수정 API 호출
                }
                showEditSheet = false
            }
            ScheduleBottomSheet(
                closeBottomSheet = closeBottomSheet,
                scheduleData = cloneScheduleData(scheduleData = scheduleData) ,
                scheduleModalSheetOption = ScheduleModalSheetOption.EDIT)
        }
    }
}

@Composable
fun cloneScheduleData(scheduleData: ScheduleData): ScheduleData {
    return ScheduleData(
        title = remember { mutableStateOf(scheduleData.title.value) },
        scheduledLocation = remember {
            ScheduleLocation(
                scheduleData.scheduledLocation.name,
                scheduleData.scheduledLocation.address,
                scheduleData.scheduledLocation.lat,
                scheduleData.scheduledLocation.lon,
            )
        },
        isWholeDay = remember { mutableStateOf(scheduleData.isWholeDay.value) },
        isDateValidate = remember { mutableStateOf(true )},
        startDateTime = remember { mutableLongStateOf(scheduleData.startDateTime.longValue) },
        endDateTime = remember { mutableLongStateOf(scheduleData.endDateTime.longValue) },
        repeat = remember { mutableStateOf(Repeat(repeatable = scheduleData.repeat.value.repeatable, cycle = scheduleData.repeat.value.cycle)) },
        comment = remember { mutableStateOf(scheduleData.comment.value) }
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreView1() {
    CalendarScreen{}
}