package com.hansung.sherpa.ui.preference.calendar

//import org.threeten.bp.DayOfWeek
//import org.threeten.bp.LocalDate
//import org.threeten.bp.YearMonth
//import org.threeten.bp.format.DateTimeFormatter
//import org.threeten.bp.format.TextStyle
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hansung.sherpa.schedule.RouteManager
import com.hansung.sherpa.schedule.ScheduleManager
import com.hansung.sherpa.schedule.Schedules
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class CalendarActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this);
        setContent {
            CalendarScreen{
                finish()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(
    finish : () -> Unit
){
    var scheduleDataList = remember { mutableStateListOf<ScheduleData>() }
    var showBottomSheet by remember { mutableStateOf(false) }

    val scheduleManager = ScheduleManager()
    val routeManager = RouteManager()

    val currentSelectedDate = remember { mutableStateOf<LocalDateTime>(LocalDateTime.of(1970,12,12,0,0,0)) }

    val updateScheduleList : (List<Schedules>?) -> Unit = { scheduleResponse ->
        scheduleDataList.clear()
        scheduleResponse?.forEach {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
            dateFormat.timeZone = TimeZone.getTimeZone(ZoneId.systemDefault())
            val startTime = java.util.Calendar.getInstance().apply { timeInMillis = dateFormat.parse(it.dateBegin).time }
            val endTime = java.util.Calendar.getInstance().apply { timeInMillis = dateFormat.parse(it.dateEnd).time }

            val isWholeDay = when {
                startTime.get(java.util.Calendar.HOUR) == 0 && startTime.get(java.util.Calendar.MINUTE) == 0 && startTime.get(
                    java.util.Calendar.SECOND) == 0 -> true
                else -> false
            }
            val route = runBlocking(Dispatchers.IO) {
                run { it.routeId?.let { it1 -> routeManager.findRoute(it1) } }
            }

            scheduleDataList.add(
                ScheduleData(
                    title =  mutableStateOf(it.title) ,
                    comment = mutableStateOf(it.description) ,
                    startDateTime = mutableLongStateOf(startTime.timeInMillis) ,
                    endDateTime =  mutableLongStateOf(endTime.timeInMillis) ,
                    isDateValidate = mutableStateOf(true) ,
                    isWholeDay = mutableStateOf(isWholeDay) ,
                    scheduledLocation =
                    if(route != null){
                        ScheduleLocation(
                            name = route.location.name,
                            lat = route.location.latitude,
                            lon = route.location.longitude,
                            address = it.address,
                            isGuide = if (it.guideDatetime.toLong() != 0L) true else false,
                            guideDatetime = it.guideDatetime.toLong()
                        )
                    } else {
                        ScheduleLocation(
                            name = "",
                            lat = 0.0,
                            lon = 0.0,
                            address = "",
                            isGuide = false,
                            guideDatetime = 0
                        )
                    },
                    routeId = route?.routeId,
                    scheduleId = it.scheduleId!!
                )
            )
        }
    }

    // TODO: 날짜 변경 시 일정 조회
    val updateScheduleData : (LocalDate?) -> Unit = { it ->
        val changeDate = it ?: currentSelectedDate.value.toLocalDate()
        val localDatetime = changeDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val scheduleResponse = scheduleManager.findSchedules(SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date(localDatetime)))?.data
        currentSelectedDate.value = changeDate.atStartOfDay()
        updateScheduleList(scheduleResponse)
    }

    // TODO: 시트 닫았을 때 추가 버튼을 통한 닫힘이면 서버에 일정 추가
    val closeBottomSheet : (ScheduleData, Boolean) -> Unit = { item, isAdded ->
        if(isAdded){
            var routeId : Int? = null
            if(item.scheduledLocation.name.isNotEmpty()) {
                routeId = runBlocking(Dispatchers.IO) {
                    withContext(Dispatchers.IO) { routeManager.insertRoute(scheduleData = item) }
                }?.routeId
            }
            scheduleManager.insertSchedules(scheduleData = item, routeId = routeId)

            currentSelectedDate.value = LocalDateTime.of(currentSelectedDate.value.year,
                currentSelectedDate.value.monthValue, currentSelectedDate.value.dayOfMonth,
                currentSelectedDate.value.hour, currentSelectedDate.value.minute)
        }
        showBottomSheet = false
    }

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    titleContentColor = Color.DarkGray
                ),

                title = {
                    Text(
                        text = "캘린더",
                        style = androidx.compose.ui.text.TextStyle(
                            fontFamily = FontFamily.Cursive,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { finish() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "뒤로가기")
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showBottomSheet = true },
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .clip(shape = RoundedCornerShape(40.dp))
                    .width(80.dp)
                    .height(40.dp),
                containerColor = Color.Black,
                contentColor = Color.White,
            ) {
                Text(text = "추가하기")
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        content = { innerpadding ->
            Surface(modifier = Modifier.padding(innerpadding)) {
                Calendar(scheduleDataList = scheduleDataList, updateScheduleData = updateScheduleData)
            }
        }
    )

    if (showBottomSheet){
        val mills = currentSelectedDate.value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val startDateTime = android.icu.util.Calendar.getInstance().apply {
            timeInMillis = mills
            set(android.icu.util.Calendar.MINUTE, 0)
        }
        val endDateTime = android.icu.util.Calendar.getInstance().apply {
            timeInMillis = mills
            set(android.icu.util.Calendar.MINUTE, 0)
        }
        val scheduleData = ScheduleData(
            title = remember { mutableStateOf("") },
            scheduledLocation = remember {
                ScheduleLocation(
                    "",
                    "",
                    0.0,
                    0.0,
                    false,
                    guideDatetime = startDateTime.timeInMillis
                )
            },
            isWholeDay = remember { mutableStateOf(false) },
            isDateValidate = remember { mutableStateOf(true )},
            startDateTime = remember { mutableLongStateOf(startDateTime.timeInMillis) },
            endDateTime = remember { mutableLongStateOf(endDateTime.timeInMillis) },
            comment = remember { mutableStateOf("") },
            routeId = null,
            scheduleId = -1
        )
        ScheduleBottomSheet(
            closeBottomSheet = closeBottomSheet,
            scheduleData = scheduleData,
            scheduleModalSheetOption = ScheduleModalSheetOption.ADD
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Calendar(
    scheduleDataList : SnapshotStateList<ScheduleData>,
    config: CalendarConfig = CalendarConfig(),
    currentDate: LocalDate = LocalDate.now(),
    updateScheduleData: (LocalDate?) -> Unit
) {
    val initialPage = (currentDate.year - config.yearRange.first) * 12 + currentDate.monthValue - 1
    var currentSelectedDate by remember { mutableStateOf(currentDate) }
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var currentPage by remember { mutableIntStateOf(initialPage) }
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { (config.yearRange.last - config.yearRange.first + 1) * 12 }
    )
    val coroutineScope = rememberCoroutineScope()
    updateScheduleData(currentSelectedDate)

    val onChangeMonth : @Composable (Int) -> Unit = { offset ->
        with(pagerState) {
            LaunchedEffect(key1 = currentPage) {
                coroutineScope.launch {
                    animateScrollToPage(
                        page = (currentPage + offset).mod(pageCount)
                    )
                }
                currentMonth = currentMonth.plusMonths(offset.toLong())
                currentPage = pagerState.currentPage
                val yearMonth = YearMonth.now()
                currentSelectedDate = when ((currentMonth.month == yearMonth.month && currentMonth.year == yearMonth.year)){
                    true -> LocalDate.now()
                    false -> LocalDate.of(currentMonth.year, currentMonth.month, 1)
                }
            }
        }
    }

    LazyColumn(modifier = Modifier) {
        val headerText = currentMonth.format(DateTimeFormatter.ofPattern("yyyy년 M월"))
        item{
            CalendarHeader(
                onChangeMonth = onChangeMonth,
                text = headerText,
            )
        }
        item {
            HorizontalPager(
                state = pagerState,
                pageSize = PageSize.Fill,
                userScrollEnabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 700.dp)
                    .wrapContentSize()
                    .padding(bottom = 18.dp)
            ) { page ->
                val date = LocalDate.of(
                    config.yearRange.first + page / 12,
                    (page % 12) + 1,
                    1
                )
                CalendarMonthItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    currentDate = date,
                    selectedDate = currentSelectedDate,
                    onSelectedDate = { date ->
                        currentSelectedDate = date
                    }
                )
            }
        }
        item{
            CurrentDateColumn(currentSelectedDate)
            ScheduleColumns(
                scheduleDataList = scheduleDataList,
                updateScheduleData = updateScheduleData
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarMonthItem(
    modifier: Modifier = Modifier,
    currentDate: LocalDate,
    selectedDate: LocalDate,
    onSelectedDate: (LocalDate) -> Unit
) {
    val lastDay = currentDate.lengthOfMonth()
    val firstDayOfWeek = currentDate.withDayOfMonth(1).dayOfWeek.value % 7
    val days = IntRange(1, lastDay).toList()

    Column(modifier = modifier) {
        DayOfWeek()
        LazyVerticalGrid(
            columns = GridCells.Fixed(7)
        ) {
            for (i in 0 until firstDayOfWeek) {
                item {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .padding(top = 10.dp)
                    )
                }
            }
            items(days) { day ->
                val date = currentDate.withDayOfMonth(day)
                val isSelected = remember(selectedDate) {
                    selectedDate.compareTo(date) == 0
                }
                CalendarDay(
                    modifier = Modifier.padding(top = 10.dp),
                    date = date,
                    isToday = date == LocalDate.now(),
                    isSelected = isSelected,
                    onSelectedDate = onSelectedDate
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarDay(
    modifier: Modifier = Modifier,
    date: LocalDate,
    isToday: Boolean,
    isSelected: Boolean,
    hasEvent: Boolean = false,
    onSelectedDate: (LocalDate) -> Unit
) {
    Column(
        modifier = modifier
            .wrapContentSize()
            .size(30.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .background(
                color = when {
                    isSelected -> Color.Black
                    isToday -> Color.Gray
                    else -> Color(234, 232, 239)
                }
            )
            .indication(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(4.dp)
            .clickable { onSelectedDate(date) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val textColor = if (isSelected || isToday) Color.White else
            when(date.dayOfWeek){
                DayOfWeek.SUNDAY -> Color.Red
                DayOfWeek.SATURDAY -> Color.Blue
                else -> Color.Black
            }

        Text(
            modifier = Modifier,
            textAlign = TextAlign.Center,
            text = date.dayOfMonth.toString(),
            color = textColor
        )
        if (hasEvent) {
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(shape = RoundedCornerShape(4.dp))
                    .let {
                        if (isSelected || isToday)
                            it.background(Color.White)
                        else
                            it.background(Color.Black)
                    }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayOfWeek(
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        val daysOfWeek = listOf(
            DayOfWeek.SUNDAY,
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY
        )
        daysOfWeek.forEach { dayOfWeek ->
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                color = when(dayOfWeek){
                    DayOfWeek.SUNDAY -> Color.Red
                    DayOfWeek.SATURDAY -> Color.Blue
                    else -> Color.Black
                },
                text = dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.KOREAN),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CalendarHeader(
    onChangeMonth : @Composable (Int) -> Unit,
    text: String,
) {
    var isLeftArrowClick by remember { mutableStateOf(false) }
    var isRightArrowClick by remember { mutableStateOf(false) }

    if(isLeftArrowClick){
        isLeftArrowClick = false
        onChangeMonth(-1)
    }
    if(isRightArrowClick){
        isRightArrowClick = false
        onChangeMonth(1)
    }

    Row(modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center) {
        IconButton(
            onClick = { isLeftArrowClick = true },
            modifier = Modifier.align(Alignment.CenterVertically)) {
            Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "이전달")
        }
        Text(
            text = text,
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )
        IconButton(
            onClick = { isRightArrowClick = true },
            modifier = Modifier.align(Alignment.CenterVertically)) {
            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "이전달")
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewCalendarPreference() {
    CalendarScreen{}
}