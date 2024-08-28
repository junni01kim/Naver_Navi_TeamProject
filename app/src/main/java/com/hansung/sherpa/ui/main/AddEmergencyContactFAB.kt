package com.hansung.sherpa.ui.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hansung.sherpa.R

private val TitlePadding = PaddingValues(top = 8.dp, bottom = 16.dp)
private val TitleStyle = TextStyle(
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.Bold,
    fontSize = 14.sp,
    lineHeight = 20.0.sp,
    letterSpacing = 0.1.sp,
)

private val ConfirmTextStyle = TextStyle(
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.Bold,
    fontSize = 20.sp,
    lineHeight = 20.0.sp,
    letterSpacing = 0.1.sp,
)

@Preview
@Composable
fun AddEmergencyContactFAB(contactList: List<Contact>  = listOf(), onClick: (Contact) -> Unit = {}) {
    var openAlertDialog = remember { mutableStateOf(false) }

    FloatingActionButton(onClick = { openAlertDialog.value = true }) {
        CustomIcon(IconType.Resource(R.drawable.add), "")
    }

    if (openAlertDialog.value) {
        AddEmergencyScreen(contactList, openAlertDialog, onClick)
    }
}
