package com.hansung.sherpa.ui.main

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.hansung.sherpa.R

/**
 * 긴급전화 추가 버튼
 *
 * @param contactList 긴급 전화번호 리스트
 * @param onClick
 */
@Preview
@Composable
fun AddEmergencyContactFAB(contactList: List<Contact>  = listOf(), onClick: (Contact) -> Unit = {}) {
    val openAlertDialog = remember { mutableStateOf(false) }

    // 긴급전화 + 버튼
    FloatingActionButton(
        onClick = { openAlertDialog.value = true },
        containerColor = MaterialTheme.colorScheme.onSecondary
    ) {
        CustomIcon(IconType.Resource(R.drawable.add), "")
    }

    // + 버튼 클릭시 모달 렌더링
    if (openAlertDialog.value) {
        AddEmergencyScreen(contactList, openAlertDialog, onClick)
    }
}
