package com.hansung.sherpa.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialogDefaults.titleContentColor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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

@Composable
fun AddEmergencyContactFAB() {
    var openAlertDialog by remember { mutableStateOf(false) }

    FloatingActionButton(onClick = { openAlertDialog = true }) {
        CustomIcon(IconType.Resource(R.drawable.add), "")
    }

    if (openAlertDialog) {
        AddEmergencyContactPopup { openAlertDialog = false }
    }
}

@Composable
fun AddEmergencyContactPopup(onDismissRequest: () -> Unit) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .width(800.dp)
                .height(300.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AddEmergencyContactTitle()
                AddEmergencyContactContents()
                AddEmergencyContactButtons(onDismissRequest)
            }
        }
    }
}

@Preview
@Composable
fun AddEmrgModalScreen() {
    val onDismissRequest = {}
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .width(800.dp)
                .height(300.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AddEmergencyContactTitle()
                AddEmergencyContactContents()
                AddEmergencyContactButtons(onDismissRequest)
            }
        }
    }
}

@Composable
fun AddEmergencyContactTitle() {
    val mergedStyle = LocalTextStyle.current.merge(TitleStyle)
    CompositionLocalProvider(
        LocalContentColor provides titleContentColor,
        LocalTextStyle provides mergedStyle
    ) {
        Box(Modifier.padding(TitlePadding)) {
            Text(text = "긴급 전화 추가")
        }
    }
}

@Composable
fun AddEmergencyContactContents() {
    var expanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.height(150.dp)) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "대표 이미지", textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .padding(5.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "add",
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "연락처 선택")
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    ElevatedButton(
                        onClick = { expanded = true },
                        modifier = Modifier.width(200.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Localized description"
                            )
                            Text(
                                text = "긴급 연락처를 선택하세요.",
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    DropdownMenu(
                        modifier = Modifier
                            .requiredSizeIn(maxHeight = 200.dp)
                            .wrapContentSize(),
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        scrollState = scrollState,
                        offset = DpOffset(50.dp, 2.dp)
                    ) {
                        repeat(30) {
                            DropdownMenuItem(
                                modifier = Modifier.width(160.dp),
                                text = { Text("User ${it + 1}") },
                                onClick = { /* TODO */ },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Outlined.AccountCircle,
                                        contentDescription = null
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddEmergencyContactButtons(onDismissRequest: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        TextButton(onClick = { /*TODO*/ }) {
            Text("추가", color = Color.Blue, style = ConfirmTextStyle)
        }
        TextButton(onClick = onDismissRequest) {
            Text("취소", color = Color.Red, style = ConfirmTextStyle)
        }
    }
}
