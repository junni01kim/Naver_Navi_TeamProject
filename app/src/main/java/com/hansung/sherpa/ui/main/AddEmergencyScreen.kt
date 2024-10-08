package com.hansung.sherpa.ui.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import com.hansung.sherpa.ui.theme.lightScheme

@Composable
fun AddEmergencyScreen(
    contactList: List<Contact>  = listOf(),
    openDialog: MutableState<Boolean> = remember { mutableStateOf(true) },
    onClick: (Contact) -> Unit = {}
) {
    val dialogWidth     = 800.dp
    val dialogHeight    = 300.dp

    var isAddContact        by remember { mutableStateOf(false) }   // 긴급 연락처 불러오기 모달 Boolean
    var isOpenContactList   by remember { mutableStateOf(false) }   // 리스트 조회 후 연락처 선택하는 모달 Boolean
    var isAddButtonEnabled  by remember { mutableStateOf(false) }   // 추가하기 버튼 활성화
    var selectedIndex       by remember { mutableIntStateOf(0) }    // 전화번호 리스트 중 선택한 index

    val onDismissRequest = { openDialog.value = false }

    // 다이얼로그
    val dialogCardModifier      = Modifier.height(dialogHeight).width(dialogWidth)
    // 추가한 연락처 Wrapper
    val contactWrapperModifier  = Modifier.fillMaxWidth().padding(10.dp)
    // 추가한 연락처 지우는 Icon
    val closeIconModifier       = Modifier
        .offset(x = (-5).dp, y = 5.dp)
        .zIndex(1f)
        .clip(RoundedCornerShape(50.dp))
        .clickable {
            isAddContact = true
            isAddButtonEnabled = false
        }

    if (openDialog.value) {
        Dialog(
            onDismissRequest = onDismissRequest
        ) {
            // 카드 형식으로 Wrapping
            Card(modifier = dialogCardModifier,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(20.dp)
                ) {
                if(isAddContact) { // 연락처를 선택한 경우
                    isAddButtonEnabled = true
                    Box(modifier = contactWrapperModifier) {
                        ContactCard(contactList[selectedIndex]) // 선택한 연락처
                        Box(modifier = closeIconModifier.align(Alignment.TopEnd)) { // 지우기 ICON
                            Icon(
                                imageVector = Icons.Default.Close,
                                tint = MaterialTheme.colorScheme.onSecondary,
                                contentDescription = "추가한 연락처 지우기",
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.surfaceDim)
                            )
                        }

                    }
                }
                else { // 연락처를 선택하지 않은 경우
                    AddContactButton { isOpenContactList = true }
                }
                // 취소하기/추가하기
                AddEmergencyButton(isEnabled = isAddButtonEnabled, onDismissRequest) {
                    onClick(contactList[selectedIndex])
                }
            }
        }
    }
    if (isOpenContactList) { // 연락처 리스트 모달을 연 경우
        SelectContactDialog(
            contactList, // 연락처 리스트
            {
                // 모달을 닫은 경우
                isOpenContactList = false // 현재 모달을 닫고,
                isAddContact = false // 연락처를 선택하지 않은 화면으로 돌아간다.
            },
            {
                // 특정한 연락처를 선택한 경우
                selectedIndex = it // 선택한 index를 저장하고,
                isOpenContactList = false // 현재 모달을 닫고,
                isAddContact = true // 연락처를 선택한 화면으로 넘긴다.
            }
        )
    }
}

// 연락처 불러오기 버튼
@Composable
fun AddContactButton(onClick: () -> Unit) {
    OutlinedButton(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
            .fillMaxHeight(0.7F),
        border = BorderStroke(0.dp, color = Color.Transparent),
        shape = RoundedCornerShape(25.dp),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        onClick = { onClick() },
        enabled = true
    ) {
        val iconSize = 50.dp
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                modifier = Modifier.width(iconSize).height(iconSize),
                imageVector = Icons.Default.Add, contentDescription = "연락처 추가 버튼",
                tint = MaterialTheme.colorScheme.outline
            )
            Text(text = "긴급연락처 불러오기", color = MaterialTheme.colorScheme.outline)
        }
    }
}

// 추가하기 버튼
@Composable
fun AddEmergencyButton(isEnabled: Boolean, onDismissRequest: () -> Unit = {}, onClick: () -> Unit = {}) {
    MaterialTheme(colorScheme = lightScheme) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            OutlinedButton(
                onClick = onDismissRequest,
                border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.scrim)
            ) {
                Text(text = "돌아가기", color = MaterialTheme.colorScheme.scrim)
            }
            FilledTonalButton(
                onClick =
                {
                    onClick()
                    onDismissRequest()
                },
                enabled = isEnabled,
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.scrim,
                    contentColor = MaterialTheme.colorScheme.surface,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    disabledContentColor = MaterialTheme.colorScheme.outlineVariant,
                )
            ) {
                Text("추가하기")
            }
        }
    }
}

/**
 * 연락처를 선택했을 때 보여주는 컴포저블
 *
 * @param contact 선택한 연락처
 */
@Composable
fun ContactCard(contact: Contact) {
    Card(modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth()
        .fillMaxHeight(0.7F)
        .zIndex(0.9F),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
        )
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(10.dp)
            ) {
                Column(
                    modifier = Modifier.wrapContentWidth(),
                ) {
                    /*
                    * 프로필 수정 보류
                    Box(
                        modifier = Modifier
                            .offset(x = (57).dp, y = (0).dp) // Image와 겹치도록 위치 조정
                            .zIndex(1f) // Icon이 이미지 위로 오도록 설정
                            .clip(RoundedCornerShape(50.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Create,
                            contentDescription = "edit",
                            modifier = Modifier
                                .background(Color.White)
                                .padding(2.dp)
                        )
                    }
                    */
                    Image(
                        bitmap = contact.image,
                        contentDescription = "배경사진",
                        modifier = Modifier
                            .offset(x = (0).dp, y = (-10).dp)
                            .size(80.dp)
                            .weight(1f) // Row에서 Image가 가능한 공간을 다 차지하게 설정
                            .aspectRatio(1f) // Image를 1:1 비율로 설정
                            .clip(RoundedCornerShape(50.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Box(modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically)) {
                Column {
                    Text(text = contact.name, style = TitleStyle)
                    Spacer(modifier = Modifier.fillMaxHeight(0.1F))
                    Text(text = contact.phone, style = ContentStyle)
                    Spacer(modifier = Modifier.fillMaxHeight(0.05F))
                    Text(text = contact.address, style = ContentStyle, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}

private val TitleStyle = TextStyle(
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.Bold,
    fontSize = 20.sp,
    lineHeight = 20.0.sp,
    letterSpacing = 0.1.sp,
)

private val ContentStyle = TextStyle(
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    lineHeight = 20.0.sp,
    letterSpacing = 0.1.sp,
)

data class Contact(
    val emergencyId: Int,
    val name: String,
    val address: String,
    val phone: String,
    val bookmarkYn: String,
    val image: ImageBitmap
)

/**
 * 리스트를 보여주고, 연락처를 선택하는 다이얼로그
 *
 * @param list 조회된 연락처 리스트
 * @param onDismissRequest 취소 로직
 * @param onClick 연락처 클릭시 로직
 */
@Composable
fun SelectContactDialog(list: List<Contact>, onDismissRequest: () -> Unit, onClick: (Int) -> Unit = {}) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.background)
                .height(530.dp)
        ) {
            Icon(
                modifier = Modifier
                    .size(35.dp)
                    .padding(top = 10.dp, end = 10.dp)
                    .align(Alignment.End)
                    .clickable { onDismissRequest() }
                ,
                tint = MaterialTheme.colorScheme.secondary,
                imageVector = Icons.Default.Close, contentDescription = "연락처 리스트 닫기")
            /**
             * 연락처 리스트 렌더링
             */
            LazyColumn(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(bottom = 30.dp),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(list.size) { index ->
                    val item = list[index] // Contact
                    Card(
                        modifier = Modifier
                            .aspectRatio(2.8f) // 카드의 가로세로 비율을 설정
                            .padding(4.dp) // Card 내부의 패딩
                            .clickable {
                                onClick(index)
                            }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.aspectRatio(1f)
                            ) {
                                Image(
                                    bitmap = item.image,
                                    contentDescription = "프로필 사진",
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Column {
                                Text(text = item.name, style = ContentStyle + TextStyle(fontWeight = FontWeight.ExtraBold))
                                Text(text = item.phone, style = ContentStyle)
                                Text(text = item.address,
                                    style = ContentStyle,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}



/**
 * Preview 모음 (코드에 영향 없음)
 *
 */

@Preview
@Composable
fun PreviewAddEmergencyScreen() {
    MaterialTheme(colorScheme = lightScheme) {
        AddEmergencyScreen()
    }
}

@Preview
@Composable
fun PreviewAddContactScreen() {
    val openDialog = remember { mutableStateOf(true) }
    val dialogWidth = 800.dp
    val dialogHeight = 300.dp
    var isAddContact by remember { mutableStateOf(false) }
    val isEnabled by remember { mutableStateOf(false) }
    if (openDialog.value) {
        Dialog(
            onDismissRequest = { openDialog.value = false }
        ) {
            Card(modifier = Modifier
                .height(dialogHeight)
                .width(dialogWidth),
                shape = RoundedCornerShape(20.dp)
            ) {
                AddContactButton { isAddContact = false }
                AddEmergencyButton(isEnabled = isEnabled)
            }
        }
    }
}

@Preview
@Composable
fun PreviewAddContactButton() {
    AddContactButton {}
}

@Preview
@Composable
fun PreviewAddEmergencyButton() {
    AddEmergencyButton(true)
}