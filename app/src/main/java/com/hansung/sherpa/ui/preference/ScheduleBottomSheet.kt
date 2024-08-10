package com.hansung.sherpa.ui.preference

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.commandiron.wheel_picker_compose.WheelDatePicker
import com.commandiron.wheel_picker_compose.WheelTimePicker
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ScheduleBottomSheet(
    closeBottomSheet : (ScheduleData, Boolean) -> Unit,
    scheduleData: ScheduleData,
    scheduleModalSheetOption: ScheduleModalSheetOption
){
    if(scheduleData.isWholeDay.value){
        val millisecondsInDay = 24 * 60 * 60 * 1000
        scheduleData.startDateTime.longValue -= (scheduleData.startDateTime.longValue % millisecondsInDay)
        scheduleData.endDateTime.longValue -= (scheduleData.endDateTime.longValue % millisecondsInDay)
        scheduleData.isDateValidate.value = checkDateValidation(scheduleData.startDateTime.longValue, scheduleData.endDateTime.longValue)
    }
    val invalidDateToast = Toast.makeText(LocalContext.current, "날짜가 올바르지 않습니다.", Toast.LENGTH_SHORT)
    val locationSheetState = remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    val onClosedButtonClick : (Boolean) -> Unit = { flag ->
        if (flag && !scheduleData.isDateValidate.value)
            invalidDateToast.show()
        else {
            coroutineScope.launch {
                bottomSheetState.hide()
                closeBottomSheet(scheduleData, flag)
            }
        }
    }

    ModalBottomSheet(
        sheetState = bottomSheetState,
        onDismissRequest = { onClosedButtonClick(false) },
        modifier = Modifier
            .fillMaxHeight(0.88f)
            .heightIn(min = 200.dp),
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
                        scheduleData.scheduledLocation.lat = items.mapx!!.toDouble()
                        scheduleData.scheduledLocation.lon = items.mapy!!.toDouble()
                    }
                }
                Memo(scheduleData = scheduleData)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomSheetBody(
    scheduleData: ScheduleData,
    locationSheetState : MutableState<Boolean>
){
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
            TextField(
                value = scheduleData.scheduledLocation.name,
                onValueChange = {},
                enabled = false,
                placeholder = {
                    Text(text = "위치")
                },
                colors = textFieldColors,
                modifier = itemModifier
                    .clickable {
                        if(!locationSheetState.value)
                            locationSheetState.value = true
                    },
                singleLine = true
            )
        }
        Spacer(modifier = Modifier.padding(16.dp))

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
                        .align(Alignment.CenterVertically),
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = Color(41,161,255)
                    )
                )
            }

            ExpandableSection(modifier = itemModifier, title = "시작",
                localDatetimeMills = scheduleData.startDateTime,
                isDateValidate = scheduleData.isDateValidate,
                isWholeDay = scheduleData.isWholeDay){
                scheduleData.isDateValidate.value = checkDateValidation(scheduleData.startDateTime.longValue, scheduleData.endDateTime.longValue)
            }
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

@RequiresApi(Build.VERSION_CODES.N)
fun checkDateValidation(start : Long, end : Long) : Boolean {
    return (end - start) >= 0
}


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
    if(isWholeDay.value)
        isTimeExpanded = false

    val onDateChanged : (LocalDate) -> Unit = { snappedDate ->
        localDateTime.apply {
            set(Calendar.YEAR, snappedDate.year)
            set(Calendar.MONTH, snappedDate.monthValue - 1)
            set(Calendar.DAY_OF_MONTH, snappedDate.dayOfMonth)
        }.also {
            localDatetimeMills.longValue = it.timeInMillis
            onChangedDateTime()
        }
    }
    val onTimeChanged : (LocalTime) -> Unit = { snappedTime ->
        localDateTime.apply {
            timeInMillis = localDatetimeMills.longValue
            set(Calendar.HOUR, snappedTime.hour)
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
                        if (isTimeExpanded)
                            isTimeExpanded = false
                        isDateExpanded = !isDateExpanded
                    },
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpandableTime(
    localDateTime : Calendar,
    onTimeChanged : (LocalTime) -> Unit
){
    WheelTimePicker(
        startTime = LocalTime.of(
            localDateTime.get(Calendar.HOUR),
            localDateTime.get(Calendar.MINUTE)
        )
    ){
            snappedTime -> onTimeChanged(snappedTime)
    }
}


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
                                selectedItem == items[0] -> scheduleData.repeat.value.repeatable = false
                                else -> selectedItem?.let { setRepeat(scheduleData, it) }
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

    scheduleData.repeat.value.repeatable = false
    scheduleData.repeat.value.cycle = when(str){
        "매일" -> CronFormatter.format(1, scheduleData.startDateTime.longValue)
        "매주" -> CronFormatter.format(2, scheduleData.startDateTime.longValue)
        "2주마다" -> CronFormatter.format(3, scheduleData.startDateTime.longValue)
        "매월" -> CronFormatter.format(4, scheduleData.startDateTime.longValue)
        "매년" -> CronFormatter.format(5, scheduleData.startDateTime.longValue)
        else -> throw NullPointerException("Exception while to set cron")
    }
}

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
    val itemModifier = Modifier
        .fillMaxWidth()
        .height(60.dp)
        .background(lightGrayColor)
    Spacer(modifier = Modifier.padding(16.dp))
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
                value = scheduleData.title.value,
                onValueChange = { text ->
                    if (!text.contains('\t') && !text.contains('\n')) {
                        scheduleData.title.value = text
                    }
                },
                placeholder = {
                    Text(text = "URL")
                },
                colors = textFieldColors,
                modifier = itemModifier,
                singleLine = true
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(lightGrayColor),
                value = scheduleData.comment.value,
                onValueChange = { text ->
                    if((text.length.toDouble() / 26) <= 4)
                        scheduleData.comment.value = text
                },
                placeholder = {
                    Text(text = "메모")
                },
                colors = textFieldColors,
                maxLines = 5,
            )
        }
        Spacer(modifier = Modifier.padding(16.dp))

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
                0.0,
                0.0
            )
        },
        isWholeDay = remember { mutableStateOf(false) },
        isDateValidate = remember { mutableStateOf(true )},
        startDateTime = remember { mutableLongStateOf(0) },
        endDateTime = remember { mutableLongStateOf(0) },
        repeat = remember { mutableStateOf(Repeat(repeatable = false, cycle = "")) },
        comment = remember { mutableStateOf("") }
    )
    LazyColumn {
        item { BottomSheetBody(
            scheduleData = scheduleData,
            remember {
                    mutableStateOf(false)
            }
        )
        Memo(scheduleData = scheduleData)
        }
    }
}