package com.hansung.sherpa.ui.main

import androidx.annotation.DrawableRes
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
import androidx.compose.material.icons.filled.Create
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import com.hansung.sherpa.R

@Composable
fun AddEmergencyScreen(contactList: List<Contact>  = listOf(), openDialog: MutableState<Boolean> = remember { mutableStateOf(true) }, onClick: (Contact) -> Unit = {}) {
    val dialogWidth = 800.dp
    val dialogHeight = 300.dp
    var isAddContact by remember { mutableStateOf(true) }
    var isOpenContacts by remember { mutableStateOf(false) }
    var isEnabled by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(0) }
    val onDismissRequest = { openDialog.value = false }
    if (openDialog.value) {
        Dialog(
            onDismissRequest = onDismissRequest
        ) {
            Card(modifier = Modifier
                .height(dialogHeight)
                .width(dialogWidth),
                shape = RoundedCornerShape(20.dp)
                ) {
                if(isAddContact) {
                    AddContactButton { isOpenContacts = true }
                } else {
                    isEnabled = true
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        ContactCard(contactList[selectedIndex])
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = (-10).dp, y = 10.dp) // Image와 겹치도록 위치 조정
                                .zIndex(1f) // Icon이 이미지 위로 오도록 설정
                                .clip(RoundedCornerShape(50.dp))
                                .clickable {
                                    isAddContact = true
                                    isEnabled = false
                                }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                tint = MaterialTheme.colorScheme.onSecondary,
                                contentDescription = "추가한 연락처 지우기",
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.secondary)
                            )
                        }

                    }
                }
                AddEmergencyButton(isEnabled = isEnabled, onDismissRequest) {
                    onClick(contactList[selectedIndex])
                }
            }
        }
    }
    if (isOpenContacts) {
        SelectContactDialog(contactList, {
            isOpenContacts = false
            isAddContact = true
        }) {
            it -> selectedIndex = it
            isOpenContacts = false
            isAddContact = false
        }
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
        colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        onClick = { onClick() },
        enabled = true
    ) {
        val iconSize = 50.dp
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                modifier = Modifier
                    .width(iconSize)
                    .height(iconSize),
                imageVector = Icons.Default.Add, contentDescription = "연락처 추가 버튼")
            Text(text = "긴급연락처 불러오기")
        }
    }
}

// 추가하기 버튼
@Composable
fun AddEmergencyButton(isEnabled: Boolean,onDismissRequest: () -> Unit = {}, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        OutlinedButton(onClick = onDismissRequest) {
            Text(text = "돌아가기")
        }
        FilledTonalButton(
            onClick =
            {
                onClick()
                onDismissRequest()
            }
            , enabled = isEnabled) {
            Text("추가하기")
        }
    }
}

@Composable
fun ContactCard(contact: Contact) {
    Card(modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth()
        .fillMaxHeight(0.7F)
        .zIndex(0.9F),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
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
                    Image(
                        painter = painterResource(id = contact.image),
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
    @DrawableRes val image: Int
)

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
                    .padding(top =  10.dp, end = 10.dp)
                    .align(Alignment.End)
                    .clickable { onDismissRequest() }
                ,
                tint = MaterialTheme.colorScheme.secondary,
                imageVector = Icons.Default.Close, contentDescription = "연락처 리스트 닫기")
            LazyColumn(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background).padding(bottom = 30.dp),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(list.size) { index ->
                    val item = list[index]
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
                            modifier = Modifier
                                .padding(8.dp),
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .aspectRatio(1f)
                            ) {
                                Image(
                                    painter = painterResource(id = item.image),
                                    contentDescription = "프로필 사진",
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Column {
                                Text(text = item.name, style = ContentStyle+ TextStyle(fontWeight = FontWeight.ExtraBold))
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

@Preview
@Composable
fun PreviewSelectContactDialog() {
    val contactList : List<Contact> = listOf(
        Contact(0, "홍길동", "부산광역시 동래구 석사북로 9-2(사직동) 47856 한국", "010-1111-1111", "N", R.drawable._1),
        Contact(0, "엄홍길", "강원도 정선군 북평면 오대천로 600-16 26100 한국", "010-2222-2222", "N", R.drawable._2),
        Contact(0, "고길동", "경상북도 김천시 봉산면 예지1길 45 39563 한국", "010-3333-3333", "N", R.drawable._3)
    )
    SelectContactDialog(contactList, {})
}


@Preview
@Composable
fun PreviewAddEmergencyScreen() {
    AddEmergencyScreen()
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

@Preview
@Composable
fun PreviewContactCard() {
    val contactList : List<Contact> = listOf(
        Contact(0, "홍길동", "부산광역시 동래구 석사북로 9-2(사직동) 47856 한국", "010-1111-1111", "N", R.drawable._1),
        Contact(0, "엄홍길", "강원도 정선군 북평면 오대천로 600-16 26100 한국", "010-2222-2222", "N", R.drawable._2),
        Contact(0, "고길동", "경상북도 김천시 봉산면 예지1길 45 39563 한국", "010-3333-3333", "N", R.drawable._3),
    )
    ContactCard(contactList[0])
}