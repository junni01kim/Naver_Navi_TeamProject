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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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

val contactList : List<Contact> = listOf(
    Contact("홍길동", "부산광역시 동래구 석사북로 9-2(사직동) 47856 한국", "010-1111-1111", R.drawable._1),
    Contact("엄홍길", "강원도 정선군 북평면 오대천로 600-16 26100 한국", "010-2222-2222", R.drawable._2),
    Contact("고길동", "경상북도 김천시 봉산면 예지1길 45 39563 한국", "010-3333-3333", R.drawable._3),
    Contact("길동홍", "경상북도 상주시 화서면 황산길 191-20 37142 한국", "010-4444-4444", R.drawable._4),
    Contact("금길동", "경상남도 산청군 생초면 생초안길 13-8 52203 한국", "010-5555-5555", R.drawable._5),
    Contact("홍길길", "경기도 이천시 부발읍 부발중앙로 183 17321 한국", "010-6666-6666", R.drawable._6),
    Contact("홍수동", "경기도 평택시 지제중앙로 149-20(지제동) 17823 한국", "010-7777-7777", R.drawable._7),
    Contact("홍홍홍", "경상남도 남해군 남면 남면로66번길 5 52436 한국", "010-8888-8888", R.drawable._8),
)

@Composable
fun AddEmergencyScreen(openDialog: MutableState<Boolean> = remember { mutableStateOf(true) }) {
    val dialogWidth = 800.dp
    val dialogHeight = 300.dp
    var isAddContact by remember { mutableStateOf(true) }
    var isOpenContacts by remember { mutableStateOf(false) }
    var isEnabled by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }
    if (openDialog.value) {
        Dialog(
            onDismissRequest = { openDialog.value = false }
        ) {
            // Draw a rectangle shape with rounded corners inside the dialog
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
                                .offset(x = (2).dp, y = 5.dp) // Image와 겹치도록 위치 조정
                                .zIndex(1f) // Icon이 이미지 위로 오도록 설정
                                .clip(RoundedCornerShape(50.dp))
                                .clickable {
                                    isAddContact = true
                                    isEnabled = false
                                }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "edit",
                                modifier = Modifier
                                    .background(Color.White)
                                    .padding(2.dp)
                            )
                        }

                    }
                }
                AddEmergencyButton(isEnabled = isEnabled)
            }
        }
    }
    if (isOpenContacts) {
        SelectContactDialog(contactList, {
            isOpenContacts = false
            isAddContact = false
        }) {
            it -> selectedIndex = it
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
            Text(text = "연락처 불러오기")
        }
    }
}

// 추가하기 버튼
@Composable
fun AddEmergencyButton(isEnabled: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        FilledTonalButton(onClick = {  }, enabled = isEnabled) {
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
                    .fillMaxWidth(0.50F)
                    .padding(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top // Row의 자식들을 수직으로 상단 정렬
                ) {
                    Image(
                        painter = painterResource(id = contact.image),
                        contentDescription = "배경사진",
                        modifier = Modifier
                            .weight(1f) // Row에서 Image가 가능한 공간을 다 차지하게 설정
                            .aspectRatio(1f) // Image를 1:1 비율로 설정
                            .clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .offset(x = (-14).dp, y = (-9).dp) // Image와 겹치도록 위치 조정
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
    val name: String,
    val address: String,
    val phone: String,
    @DrawableRes val image: Int
)

@Composable
fun SelectContactDialog(list: List<Contact>, onDismissRequest: () -> Unit, onClick: (Int) -> Unit = {}) {
    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            modifier = Modifier.wrapContentSize().clip(RoundedCornerShape(20.dp))
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3), // 3열로 고정
                modifier = Modifier.fillMaxWidth()
                    .wrapContentHeight()
                    .background(MaterialTheme.colorScheme.background)
                , // 그리드가 화면 전체를 차지하게 설정
                contentPadding = PaddingValues(8.dp), // 그리드의 패딩
                horizontalArrangement = Arrangement.spacedBy(8.dp), // 열 간의 간격 설정
                verticalArrangement = Arrangement.spacedBy(8.dp) // 행 간의 간격 설정
            ) {
                items(list.size) { index ->
                    val item = list[index]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth() // 각 Card가 열의 너비를 채우도록 설정
                            .aspectRatio(0.65f) // 카드의 가로세로 비율을 설정
                            .padding(4.dp) // Card 내부의 패딩
                            .clickable {
                                onDismissRequest()
                                onClick(index)
                            }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp), // Column 내의 padding 설정
                            horizontalAlignment = Alignment.CenterHorizontally, // 텍스트를 가운데 정렬
                            verticalArrangement = Arrangement.Center // 텍스트를 수직으로 중앙에 배치
                        ) {
                            Box(
                                contentAlignment = Alignment.Center, // 중앙에 배치
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f) // Box를 정사각형으로 설정
                            ) {
                                Image(
                                    painter = painterResource(id = item.image), // 사용할 이미지
                                    contentDescription = "프로필 사진",
                                    modifier = Modifier
                                        .size(60.dp) // 이미지의 크기 설정
                                        .clip(CircleShape), // 이미지를 원형으로 자름
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp)) // 이미지와 텍스트 사이의 간격
                            Text(text = item.name, style = ContentStyle)
                            Text(text = item.address,
                                style = ContentStyle,
                                maxLines = 1, // 한 줄까지만 표시
                                overflow = TextOverflow.Ellipsis // 넘치는 부분은 생략
                            )
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
    var isEnabled by remember { mutableStateOf(false) }
    if (openDialog.value) {
        Dialog(
            onDismissRequest = { openDialog.value = false }
        ) {
            // Draw a rectangle shape with rounded corners inside the dialog
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
    AddContactButton({})
}

@Preview
@Composable
fun PreviewAddEmergencyButton() {
    AddEmergencyButton(true)
}

@Preview
@Composable
fun PreviewContactCard() {
    ContactCard(contactList[0])
}




