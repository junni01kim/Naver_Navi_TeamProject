package com.hansung.sherpa

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hansung.sherpa.FCM.MessageViewModel
import com.hansung.sherpa.dialog.SherpaDialog

@Composable
fun ExampleAlam(messageViewModel: MessageViewModel = viewModel()) {
    val title by messageViewModel.title.collectAsState()
    val body by messageViewModel.body.collectAsState()
    // 포스트 맨 "알림" 앞에 달고 전송하면 바뀔거임
    SherpaDialog(title, listOf(body), "확인") { /* TODO: 확인 버튼 눌릴 시 수행할 동작 */ }
}