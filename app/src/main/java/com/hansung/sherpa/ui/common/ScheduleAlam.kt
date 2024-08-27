package com.hansung.sherpa.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.hansung.sherpa.fcm.ScheduleViewModel

@Composable
fun ScheduleAlam(scheduleViewModel: ScheduleViewModel) {
    val showDialog by scheduleViewModel.showDialog.observeAsState(false)
    val title by scheduleViewModel.title.observeAsState("")
    val description by scheduleViewModel.description.observeAsState("")
    val dateBegin by scheduleViewModel.dateBegin.observeAsState("")
    val dateEnd by scheduleViewModel.dateEnd.observeAsState("")

    if(showDialog) {
        SherpaSchedule(title, description, dateBegin, dateEnd, onConfirm = {scheduleViewModel.onDialogDismiss()})
    }
}
