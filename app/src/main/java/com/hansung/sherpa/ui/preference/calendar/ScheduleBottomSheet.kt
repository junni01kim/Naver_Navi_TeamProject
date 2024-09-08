package com.hansung.sherpa.ui.preference.calendar

import android.icu.util.Calendar
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.commandiron.wheel_picker_compose.WheelDatePicker
import com.commandiron.wheel_picker_compose.WheelTimePicker
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime


/**
 * 일정 추가 수정 sheet
 *
 * @param closeBottomSheet sheet 닫을 때 callback
 * @param scheduleData 껍데기 스케줄 정보 객체
 * @param scheduleModalSheetOption 추가인지 수정인지
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ScheduleBottomSheet(
    closeBottomSheet : (ScheduleData, Boolean) -> Unit,
    scheduleData: ScheduleData,
    scheduleModalSheetOption: ScheduleModalSheetOption
){
    val invalidDateToast = Toast.makeText(LocalContext.current, "날짜가 올바르지 않습니다.", Toast.LENGTH_SHORT)
    val invalidTitleToast = Toast.makeText(LocalContext.current, "제목을 입력해주세요", Toast.LENGTH_SHORT)
    val locationSheetState = remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    /**
     * 닫을 때 유효성 검사
     */
    val onClosedButtonClick : (Boolean) -> Unit = { flag ->
        when {
            flag && !scheduleData.isDateValidate.value -> invalidDateToast.show()
            flag && scheduleData.title.value.isEmpty() -> invalidTitleToast.show()
            // 추가하거나 수정
            else -> coroutineScope.launch {
                bottomSheetState.hide()
                closeBottomSheet(scheduleData, flag)
            }
        }
    }

    /**
     * 일정이 하루 중일인 경우
     */
    LaunchedEffect(scheduleData.isWholeDay.value){
        when(scheduleData.isWholeDay.value){
            // 시간을 자정으로 바꿔줌
            true -> {
                scheduleData.startDateTime.longValue = resetToMidnight(scheduleData.startDateTime.longValue)
                scheduleData.endDateTime.longValue = resetToMidnight(scheduleData.endDateTime.longValue)
                scheduleData.isDateValidate.value = checkDateValidation(scheduleData.startDateTime.longValue, scheduleData.endDateTime.longValue)
            }
            // 아닌 경우 현재 시간에서 분만 0 으로 바꿔줌
            false -> {
                scheduleData.startDateTime.longValue = Calendar.getInstance().apply {
                    if(scheduleData.title.value.isNotEmpty())
                        timeInMillis = scheduleData.startDateTime.longValue
                    set(Calendar.MINUTE, 0)
                }.timeInMillis
                scheduleData.endDateTime.longValue = Calendar.getInstance().apply {
                    if(scheduleData.title.value.isNotEmpty())
                        timeInMillis = scheduleData.endDateTime.longValue
                    set(Calendar.MINUTE, 0)
                }.timeInMillis
            }
        }
    }

    /**
     * 스케줄 추가 | 수정창 출력
     */
    ModalBottomSheet(
        sheetState = bottomSheetState,
        onDismissRequest = { onClosedButtonClick(false) },
        dragHandle = {},
    ){
        LazyColumn {
            stickyHeader {
                ScheduleBottomSheetHeader(
                    onClosedButtonClick = onClosedButtonClick,
                    scheduleModalSheetOption = scheduleModalSheetOption
                )
            }
            item {
                BottomSheetBody(
                    scheduleData = scheduleData,
                    locationSheetState = locationSheetState
                )
                if(locationSheetState.value) {
                    LocationBottomSheet(locationSheetState) { items ->
                        scheduleData.scheduledLocation.name = items.title.toString()
                        scheduleData.scheduledLocation.address = items.roadAddress.toString()
                        scheduleData.scheduledLocation.lat = items.mapx!!
                        scheduleData.scheduledLocation.lon = items.mapy!!
                    }
                }
//                Alert(scheduleData = scheduleData)
                Memo(scheduleData = scheduleData)
            }
        }
    }
}

/**
 * 일정 수정 | 추가 sheet의 몸통 내용
 *
 * @param scheduleData 스케줄 정보가 저장되는 객체
 * @param locationSheetState 위치 검색 창의 on | off 상태 변수
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomSheetBody(
    scheduleData: ScheduleData,
    locationSheetState : MutableState<Boolean>
){
    // 경로 안내 변수 == true 면 시간 대입
    if(!scheduleData.scheduledLocation.isGuide)
        scheduleData.scheduledLocation.guideDatetime = scheduleData.startDateTime.longValue

    val isGuide = remember { mutableStateOf(scheduleData.scheduledLocation.isGuide) }
    val guideDatetime = remember { mutableLongStateOf(scheduleData.scheduledLocation.guideDatetime) }
    val isValidateGuideDatetime = remember { mutableStateOf(true) }
    var isSearched by remember { mutableStateOf(false) }
    var locationString by remember { mutableStateOf("위치") }

    // 경로 안내 변수가 바뀌면
    LaunchedEffect(isGuide.value) {
        scheduleData.scheduledLocation.isGuide = isGuide.value
    }
    // 경로 안내 시간이 바뀌면
    LaunchedEffect(guideDatetime.longValue) {
        scheduleData.scheduledLocation.guideDatetime = guideDatetime.longValue
    }

    // 위치가 업데이트 되면 isSearched 변수가 true or false
    LaunchedEffect(scheduleData.scheduledLocation.name){
        isSearched = when(scheduleData.scheduledLocation.name){
            "" -> false
            else -> true
        }
    }

    // isSearched 변수가 바뀌면 동작
    LaunchedEffect(isSearched) {
       when(isSearched){
           true -> {
               locationString = scheduleData.scheduledLocation.name
           }
           false -> {
               locationString = "위치"
               scheduleData.scheduledLocation.name = ""
               scheduleData.scheduledLocation.address = ""
               scheduleData.scheduledLocation.lat = 0.0
               scheduleData.scheduledLocation.lon = 0.0
           }
       }
    }
    val lightGrayColor = Color(229,226,234)
    val textFieldColors = TextFieldDefaults.colors(
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        unfocusedContainerColor = lightGrayColor,
        focusedContainerColor = lightGrayColor,
    )
    val itemModifier = Modifier
        .fillMaxWidth()
        .height(60.dp)
        .background(lightGrayColor)

    Spacer(modifier = Modifier.padding(4.dp))
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        // 제목 부분
        Column(modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(lightGrayColor)
            .fillMaxWidth()
        ){
            TextField(
                value = scheduleData.title.value,
                onValueChange = { text ->
                    if (!text.contains('\t') && !text.contains('\n')) {
                        scheduleData.title.value = text
                    }
                },
                placeholder = {
                    Text(text = "제목")
                },
                colors = textFieldColors,
                modifier = itemModifier,
                singleLine = true
            )
            Row(
                modifier = itemModifier
                    .padding(horizontal = 16.dp)
                    .clickable {
                        if (!locationSheetState.value)
                            locationSheetState.value = true
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        modifier = Modifier.fillMaxWidth(0.9f),
                        text = locationString,
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = Color.DarkGray
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if(isSearched){
                        Text(
                            modifier = Modifier.fillMaxWidth(0.9f),
                            text = scheduleData.scheduledLocation.address,
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = Color.Gray
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                if(isSearched){
                    IconButton(
                        modifier = Modifier.wrapContentSize(),
                        onClick = {
                            isSearched = false
                            isGuide.value = false
                        }) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = "",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            AnimatedVisibility(visible = scheduleData.scheduledLocation.name.isNotEmpty()) {
                Row(
                    modifier = itemModifier,
                ){
                    Text(
                        text = "경로 안내",
                        style = TextStyle(
                            fontSize = 16.sp,
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 16.dp),
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 110.dp))
                    Switch(
                        checked = isGuide.value,
                        onCheckedChange = {
                            isGuide.value = it
                        },
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(end = 8.dp),
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = Color(41,161,255)
                        )
                    )
                }
            }
            AnimatedVisibility(isGuide.value) {
                Column(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.primaryContainer)
                        .fillMaxWidth()
                ) {
                    ExpandableSection(modifier = itemModifier, title = "시간",
                        localDatetimeMills = guideDatetime,
                        isDateValidate = isValidateGuideDatetime,
                        isWholeDay = remember {
                            mutableStateOf(false)
                        })
                    {
                        scheduleData.isDateValidate.value = checkDateValidation(
                            scheduleData.startDateTime.longValue,
                            scheduleData.endDateTime.longValue
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.padding(10.dp))

        Column(modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.LightGray)
            .fillMaxWidth()

        ){
            Row(
                modifier = itemModifier,
            ){
                Text(
                    text = "하루종일",
                    style = TextStyle(
                        fontSize = 16.sp,
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 16.dp),
                )
                Spacer(modifier = Modifier.padding(horizontal = 110.dp))
                Switch(
                    checked = scheduleData.isWholeDay.value,
                    onCheckedChange = {
                        scheduleData.isWholeDay.value = it
                    },
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 8.dp),
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = Color(41,161,255)
                    )
                )
            }

            // 스케줄 시작 날짜 및 시간 출력 부분
            ExpandableSection(modifier = itemModifier, title = "시작",
                localDatetimeMills = scheduleData.startDateTime,
                isDateValidate = scheduleData.isDateValidate,
                isWholeDay = scheduleData.isWholeDay){
                scheduleData.isDateValidate.value = checkDateValidation(scheduleData.startDateTime.longValue, scheduleData.endDateTime.longValue)
            }
            // 스케줄 종료 날짜 및 시간 출력 부분
            ExpandableSection(modifier = itemModifier, title = "종료",
                localDatetimeMills = scheduleData.endDateTime,
                isDateValidate = scheduleData.isDateValidate,
                isWholeDay = scheduleData.isWholeDay){
                scheduleData.isDateValidate.value = checkDateValidation(scheduleData.startDateTime.longValue, scheduleData.endDateTime.longValue)
            }
            Repeat(modifier = itemModifier, scheduleData = scheduleData)
        }
    }
}

/**
 * 설정한 날짜가 유효한 날짜 인지
 *
 * @param start
 * @param end
 */
@RequiresApi(Build.VERSION_CODES.N)
fun checkDateValidation(start : Long, end : Long) : Boolean {
    return (end - start) >= 0
}


/**
 * 시간 설정하는 창이 출력 되는 부분
 *
 * @param title 해당 Composable의 타이틀
 * @param localDatetimeMills 설정된 시간을 저장하는 변수
 * @param isDateValidate 설정 시간이 유효한지
 * @param isWholeDay 스케줄이 하루 종일인지
 * @param onChangedDateTime 시간이 변하면 callback
 */

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpandableSection(
    modifier: Modifier,
    title : String,
    localDatetimeMills : MutableLongState,
    isDateValidate : MutableState<Boolean>,
    isWholeDay : MutableState<Boolean>,
    onChangedDateTime : () -> Unit
){
    val localDateTime = Calendar.getInstance().apply {
        timeInMillis = localDatetimeMills.longValue
    }
    val dateFormat = SimpleDateFormat("yyyy.MM.dd")
    val timeFormat = SimpleDateFormat("HH:mm")

    var isDateExpanded by rememberSaveable { mutableStateOf(false) }
    var isTimeExpanded by rememberSaveable { mutableStateOf(false) }

    // 하루 종일이면 시간 설정창이 없어짐
    if(isWholeDay.value)
        isTimeExpanded = false

    // 날짜가 바뀌면
    val onDateChanged : (LocalDate) -> Unit = { snappedDate ->
        // 현재 local 캘린더 변수를 바꾸고 localDatetimeMills에 적용한다
        localDateTime.apply {
            set(Calendar.YEAR, snappedDate.year)
            set(Calendar.MONTH, snappedDate.monthValue - 1)
            set(Calendar.DAY_OF_MONTH, snappedDate.dayOfMonth)
        }.also {
            localDatetimeMills.longValue = it.timeInMillis
            onChangedDateTime()
        }
    }

    // 시간이 바뀌면
    val onTimeChanged : (LocalTime) -> Unit = { snappedTime ->
        // 현재 local 캘린더 변수를 바꾸고 localDatetimeMills에 적용한다
        localDateTime.apply {
            timeInMillis = localDatetimeMills.longValue
            set(Calendar.HOUR_OF_DAY, snappedTime.hour)
            set(Calendar.MINUTE, snappedTime.minute)
        }.also {
            localDatetimeMills.longValue = it.timeInMillis
            onChangedDateTime()
        }
    }

    val textStyle = TextStyle(
        fontSize = 16.sp,
        textDecoration = if (isDateValidate.value) TextDecoration.None else TextDecoration.LineThrough,
    )
    Row(
        modifier = modifier,
    ){
        Text(
            text = title,
            style = TextStyle(
                fontSize = 16.sp,
            ),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 16.dp),
        )
        Spacer(modifier = Modifier.padding(horizontal =
            when{
                !isWholeDay.value -> 70.dp
                else -> 100.dp
            }
        ))
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .align(Alignment.CenterVertically)
                .background(Color.LightGray)
                .height(40.dp)
                .padding(8.dp),
        ){
            Text(
                text = dateFormat.format(localDateTime.time),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        // 날짜와 시간 변경 창이 중첩되어 출력되지 않게 함
                        if (isTimeExpanded)
                            isTimeExpanded = false
                        isDateExpanded = !isDateExpanded
                    },
                // 유효성 여부에 따라 색깔이 바뀜
                color = when{
                    !isDateValidate.value -> Color.Red
                    isDateExpanded -> Color(41,161,255)
                    else -> Color.Black
                },
                style = textStyle,
            )
        }
        Spacer(modifier = Modifier.padding(horizontal = 2.dp))
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .align(Alignment.CenterVertically)
                .background(Color.LightGray)
                .height(
                    when {
                        isWholeDay.value -> 0.dp
                        else -> 40.dp
                    }
                )
                .padding(8.dp)
        ){
            Text(
                text = timeFormat.format(localDateTime.time),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        if (isDateExpanded)
                            isDateExpanded = false
                        isTimeExpanded = !isTimeExpanded
                    },
                color = when {
                    !isDateValidate.value -> Color.Red
                    isTimeExpanded -> Color(41,161,255)
                    else -> Color.Black
                },
                style = textStyle
            )
        }
    }

    /**
     * 날짜와 시간을 설정하는 창이 나타날 때 애니메이션 효과 적용
     *
     * visible = true면 나타남
     */
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()
    ) {
        AnimatedVisibility(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .fillMaxWidth(),
            visible = isDateExpanded
        ) {
            ExpandableCalendar(localDateTime = localDateTime, onDateChanged = onDateChanged)
        }
    }
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()
    ) {
        AnimatedVisibility(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .fillMaxWidth(),
            visible = if(isWholeDay.value) false else isTimeExpanded
        ) {
            ExpandableTime(localDateTime = localDateTime, onTimeChanged = onTimeChanged)
        }
    }
}

/**
 * 날짜 설정
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpandableCalendar(
    localDateTime : Calendar,
    onDateChanged : (LocalDate) -> Unit
){
    WheelDatePicker(
        startDate = LocalDate.of(
            localDateTime.get(Calendar.YEAR),
            localDateTime.get(Calendar.MONTH) + 1,
            localDateTime.get(Calendar.DAY_OF_MONTH)
        ),
    ){
        snappedDate -> onDateChanged(snappedDate)
    }
}

/**
 * 시간 설정
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpandableTime(
    localDateTime : Calendar,
    onTimeChanged : (LocalTime) -> Unit
){
    WheelTimePicker(
        startTime = LocalTime.of(
            localDateTime.get(Calendar.HOUR_OF_DAY),
            localDateTime.get(Calendar.MINUTE)
        ),
    ){
        snappedTime -> onTimeChanged(snappedTime)
    }
}


/**
 * 일정 반복 설정을 위한 부분
 *
 * 사용 여부 미정
 */
@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Repeat(
    modifier: Modifier,
    scheduleData: ScheduleData,
){
    var expanded by remember { mutableStateOf(false) }
    val items = mapOf(0 to "안 함", 1 to "매일", 2 to "매주", 3 to "2주마다", 4 to "매월", 5 to "매년")
    var selectedItem by remember { mutableStateOf(items[0]) }
    Row(
        modifier = modifier,
    ) {
        Text(
            text = "반복",
            style = TextStyle(
                fontSize = 16.sp,
            ),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 16.dp),
        )
        Spacer(modifier = Modifier.padding(horizontal = 70.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    TextField(
                        readOnly = true,
                        value = selectedItem.toString(),
                        onValueChange = {},
                        label = { Text(selectedItem.toString()) },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .wrapContentSize()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        items.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item.value) },
                                onClick = {
                                    selectedItem = item.value
                                    expanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                            when {
//                                selectedItem == items[0] -> scheduleData.repeat.value.repeatable = false
//                                else -> selectedItem?.let { setRepeat(scheduleData, it) }
                            }
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
fun setRepeat(
    scheduleData: ScheduleData,
    str: String
){
    val items = mapOf(0 to "안 함", 1 to "매일", 2 to "매주", 3 to "2주마다", 4 to "매월", 5 to "매년")

//    scheduleData.repeat.value.repeatable = false
//    scheduleData.repeat.value.cycle = when(str){
//        "매일" -> CronFormatter.format(1, scheduleData.startDateTime.longValue)
//        "매주" -> CronFormatter.format(2, scheduleData.startDateTime.longValue)
//        "2주마다" -> CronFormatter.format(3, scheduleData.startDateTime.longValue)
//        "매월" -> CronFormatter.format(4, scheduleData.startDateTime.longValue)
//        "매년" -> CronFormatter.format(5, scheduleData.startDateTime.longValue)
//        else -> throw NullPointerException("Exception while to set cron")
//    }
}

// TODO: 사용미정
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Alert(
    scheduleData: ScheduleData
){
    val lightGrayColor = Color(229,226,234)

    val itemModifier = Modifier
        .fillMaxWidth()
        .height(60.dp)
        .background(lightGrayColor)
    var expanded by remember { mutableStateOf(false) }
    val items = mapOf(0 to "없음", 1 to "이벤트 시간", 2 to "5분전", 3 to "10분전", 4 to "15분전", 5 to "30분전")
    var selectedItem by remember { mutableStateOf(items[0]) }

    Spacer(modifier = Modifier.padding(10.dp))
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(lightGrayColor)
                .fillMaxWidth()
        ) {
            Row(
                modifier = itemModifier,
            ) {
                Text(
                    text = "알림",
                    style = TextStyle(
                        fontSize = 16.sp,
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 16.dp),
                )
                Spacer(modifier = Modifier.padding(horizontal = 70.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column {
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            TextField(
                                readOnly = true,
                                value = selectedItem.toString(),
                                onValueChange = {},
                                label = { Text(selectedItem.toString()) },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                },
                                modifier = Modifier
                                    .menuAnchor()
                                    .wrapContentSize()
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                items.forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(item.value) },
                                        onClick = {
                                            selectedItem = item.value
                                            expanded = false
                                        },
                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                    )
                                    when {
//                                        selectedItem == items[0] -> scheduleData.repeat.value.repeatable = false
//                                        else -> selectedItem?.let {  }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * 스케줄에 대한 설명 작성 부분
 *
 * @param scheduleData 스케줄 정보 객체
 */
@Composable
fun Memo(
    scheduleData: ScheduleData
){
    val lightGrayColor = Color(229,226,234)
    val textFieldColors = TextFieldDefaults.colors(
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        unfocusedContainerColor = lightGrayColor,
        focusedContainerColor = lightGrayColor,
    )
    Spacer(modifier = Modifier.padding(10.dp))
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(lightGrayColor)
                .fillMaxWidth()
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(lightGrayColor),
                value = scheduleData.comment.value,

                // 엔터와 탭 필터링
                onValueChange = { text ->
                    if((text.length.toDouble() / 26) <= 4 && !text.contains('\t') && !text.contains('\n'))
                        scheduleData.comment.value = text
                },
                placeholder = {
                    Text(text = "메모")
                },
                colors = textFieldColors,
                maxLines = 5,
            )
        }
        Spacer(modifier = Modifier.padding(12.dp))

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun preview(){
    val scheduleData = ScheduleData(
        title = remember { mutableStateOf("") },
        scheduledLocation = remember {
            ScheduleLocation(
                "",
                address = "",
                0.0,
                0.0,
                false,
                0
            )
        },
        isWholeDay = remember { mutableStateOf(false) },
        isDateValidate = remember { mutableStateOf(true )},
        startDateTime = remember { mutableLongStateOf(0) },
        endDateTime = remember { mutableLongStateOf(0) },
        comment = remember { mutableStateOf("") },
        routeId = null,
        scheduleId = -1
    )
    LazyColumn {
        item { BottomSheetBody(
            scheduleData = scheduleData,
            remember {
                    mutableStateOf(false)
            }
        )
//            Alert(scheduleData = scheduleData)
            Memo(scheduleData = scheduleData)
        }
    }
}

/**
 * 시간을 자정으로 바꾸는 함수
 */
@RequiresApi(Build.VERSION_CODES.N)
fun resetToMidnight(timeInMillis: Long): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timeInMillis

    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    return calendar.timeInMillis
}
