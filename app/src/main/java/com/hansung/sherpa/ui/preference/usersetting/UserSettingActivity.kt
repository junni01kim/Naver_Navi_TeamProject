package com.hansung.sherpa.ui.preference.usersetting

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class UserSettingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
        setContent {
            UserSettingScreen(
                pickMediaCallback = {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                onFinish = { finish()}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSettingScreen(
    pickMediaCallback : () -> Unit,
    onFinish : () -> Unit,
){
    val onClickState = remember { mutableStateOf(false) }
    LaunchedEffect(onClickState.value) {
        when(onClickState.value){
            true -> pickMediaCallback()
            else -> { }
        }
        onClickState.value = false
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "사용자 설정",
                        style = TextStyle(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onFinish() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
            )
        },
        content = { innerPadding ->
            Surface(modifier = Modifier.padding(innerPadding)) {
                UserSetting(onClickState)
            }
        }
    )
}

@Composable
fun UserSetting(
    onClickState : MutableState<Boolean>
){
    LazyColumn {
        item {
            ProfileImage(onClickState)
            UserAccount()
            Contacts()
            UserName()
            Password()
            Logout()
        }
    }
}

@Composable
fun ProfileImage(
    onClickState: MutableState<Boolean>
){
    val onEditPhotoClick : () -> Unit = { onClickState.value = true }

    Column(
        modifier = Modifier
            .padding(vertical = 24.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box{
            Image(
                modifier = Modifier
                    .size(100.dp)
                    .fillMaxSize()
                    .clip(CircleShape)

                ,
                colorFilter = ColorFilter.tint(Color.LightGray),
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "",
            )
            IconButton(
                modifier = Modifier
                    .offset(x = 55.dp, y = 55.dp),
                onClick = {
                    onEditPhotoClick()
                },
            ){
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "",
                    modifier = Modifier
                        .drawBehind {
                            drawCircle(
                                Color.Black,
                                radius = size.width / 1.4f
                            )
                        }
                        .size(20.dp),
                    tint = Color.White
                )
            }
        }
        Text(
            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
            text = "KEEM",
            style = TextStyle(
                fontFamily = FontFamily.Default,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold
            ),
            color = Color.DarkGray
        )
        Text(
            text = "+82 10-0000-0000",
            style = TextStyle(
                fontFamily = FontFamily.Default,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold
            ),
            color = Color.Gray
        )
    }
}

@Composable
fun UserAccount(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .size(80.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    val strokeWidth = 1 * density
                    val y = size.height - strokeWidth / 2
                    drawLine(
                        Color(240, 240, 240),
                        Offset(0f, 0f),
                        Offset(size.width, 0f),
                        strokeWidth
                    )
                    drawLine(
                        Color(240, 240, 240),
                        Offset(0f, y),
                        Offset(size.width, y),
                        strokeWidth
                    )
                }
                .padding(vertical = 12.dp, horizontal = 24.dp)
        ) {
            Image(
                imageVector = Icons.Default.Person,
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(36.dp),
                colorFilter = ColorFilter.tint(color = Color.Black)
            )
            Spacer(modifier = Modifier.width(8.dp)) // Image와 Text 사이에 공간 추가
            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f)
            ) {
                Text(
                    text = "로그인 계정",
                    style = TextStyle(
                        color = Color.DarkGray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    ),
                )
                Text(
                    text = "team-sherpa@github.com",
                    style = TextStyle(
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    ),
                    modifier = Modifier
                )
            }
        }
    }
}

@Composable
fun UserName(){
    val interactionSource = remember { MutableInteractionSource() }
    val state = remember { mutableStateOf(false) }

    if(state.value)
        NameDialog(openDialogCustom = state)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .size(80.dp)
            .clickable(
                onClick = { state.value = true },
                indication = rememberRipple(bounded = true),
                interactionSource = interactionSource
            )
    ){
        Row(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    val strokeWidth = 1 * density
                    val y = size.height - strokeWidth / 2
                    drawLine(
                        Color(240, 240, 240),
                        Offset(0f, 0f),
                        Offset(size.width, 0f),
                        strokeWidth
                    )
                    drawLine(
                        Color(240, 240, 240),
                        Offset(0f, y),
                        Offset(size.width, y),
                        strokeWidth
                    )
                }
                .padding(vertical = 12.dp, horizontal = 24.dp)
        ) {
            Image(
                imageVector = Icons.Default.Tag,
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(36.dp),
                colorFilter = ColorFilter.tint(color = Color.Black)
            )
            Spacer(modifier = Modifier.width(8.dp)) // Image와 Text 사이에 공간 추가
            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f)
            ) {
                Text(
                    text = "닉네임 변경",
                    style = TextStyle(
                        color = Color.DarkGray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    ),
                )
                Text(
                    text = "Keem",
                    style = TextStyle(
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    ),
                    modifier = Modifier
                )
            }
            Image(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
fun Password(){
    val changePasswordSheetState = remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    if(changePasswordSheetState.value) {
        PasswordModal(changePasswordSheetState)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .size(80.dp)
            .clickable(
                onClick = { changePasswordSheetState.value = true },
                indication = rememberRipple(bounded = true),
                interactionSource = interactionSource
            )
    ){
        Row(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    val strokeWidth = 1 * density
                    val y = size.height - strokeWidth / 2
                    drawLine(
                        Color(240, 240, 240),
                        Offset(0f, 0f),
                        Offset(size.width, 0f),
                        strokeWidth
                    )
                    drawLine(
                        Color(240, 240, 240),
                        Offset(0f, y),
                        Offset(size.width, y),
                        strokeWidth
                    )
                }
                .padding(vertical = 12.dp, horizontal = 24.dp)
        ) {
            Image(
                imageVector = Icons.Default.Key,
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(36.dp),
                colorFilter = ColorFilter.tint(color = Color.Black)
            )
            Spacer(modifier = Modifier.width(8.dp)) // Image와 Text 사이에 공간 추가
            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f)
            ) {
                Text(
                    text = "비밀번호 변경",
                    style = TextStyle(
                        color = Color.DarkGray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    ),
                )
            }
            Image(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
fun Contacts() {
    val changEmailSheetState = remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    if(changEmailSheetState.value) {
        ContactsModal(changEmailSheetState)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .size(80.dp)
            .clickable(
                onClick = { changEmailSheetState.value = true },
                indication = rememberRipple(bounded = true),
                interactionSource = interactionSource
            )
    ){
        Row(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    val strokeWidth = 1 * density
                    val y = size.height - strokeWidth / 2
                    drawLine(
                        Color(240, 240, 240),
                        Offset(0f, 0f),
                        Offset(size.width, 0f),
                        strokeWidth
                    )
                    drawLine(
                        Color(240, 240, 240),
                        Offset(0f, y),
                        Offset(size.width, y),
                        strokeWidth
                    )
                }
                .padding(vertical = 12.dp, horizontal = 24.dp)
        ) {
            Image(
                imageVector = Icons.Default.Email,
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(36.dp),
                colorFilter = ColorFilter.tint(color = Color.Black)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f)
            ) {
                Text(
                    text = "이메일 변경",
                    style = TextStyle(
                        color = Color.DarkGray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    ),
                )
            }
            Image(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
fun Logout(){
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .size(80.dp)
            .clickable(
                onClick = { },
                indication = rememberRipple(bounded = true),
                interactionSource = interactionSource
            )
    ){
        Row(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    val strokeWidth = 1 * density
                    val y = size.height - strokeWidth / 2
                    drawLine(
                        Color(240, 240, 240),
                        Offset(0f, 0f),
                        Offset(size.width, 0f),
                        strokeWidth
                    )
                    drawLine(
                        Color(240, 240, 240),
                        Offset(0f, y),
                        Offset(size.width, y),
                        strokeWidth
                    )
                }
                .padding(vertical = 12.dp, horizontal = 24.dp)
        ) {
            Image(
                imageVector = Icons.Default.Logout,
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(36.dp),
                colorFilter = ColorFilter.tint(color = Color.Black)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f)
            ) {
                Text(
                    text = "로그아웃",
                    style = TextStyle(
                        color = Color.DarkGray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    ),
                )
            }
            Image(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
@Preview
fun UserPreview(){
    UserSettingScreen(onFinish = {}, pickMediaCallback = {})
}

