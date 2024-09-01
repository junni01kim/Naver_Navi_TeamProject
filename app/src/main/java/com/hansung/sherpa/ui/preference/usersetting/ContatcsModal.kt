package com.hansung.sherpa.ui.preference.usersetting

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ripple
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsModal(
    state : MutableState<Boolean>
){
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        sheetState = bottomSheetState,
        onDismissRequest = { state.value = false },
        dragHandle = {},
    ) {
        Column {
            ContactsModalHeader(state)
            ContactField(state)
        }
    }
}

@Composable
fun ContactsModalHeader(
    state : MutableState<Boolean>
){
    val textStyle : TextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 24.dp,
                    vertical = 8.dp
                )
        ) {
            Text(
                text = "연락처 정보",
                modifier = Modifier
                    .padding(top = (12).dp)
                    .align(Alignment.TopCenter),
                style = textStyle
            )
            TextButton(
                modifier = Modifier
                    .align(Alignment.TopEnd),
                onClick = { state.value = false }
            ) {
                Text(
                    text = "취소",
                    color = Color(219, 0, 35),
                    style = textStyle
                )
            }
        }
    }
}

@Composable
fun ContactField(
    state : MutableState<Boolean>
){
    val lightGrayColor = Color(229,226,234)
    val originalPasswordInput = remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val newPasswordText = remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }
    val nEqualPasswordToast = Toast.makeText(LocalContext.current, "현재 비밀번호가 맞지 않습니다.", Toast.LENGTH_SHORT)

    val onClickChangeButton : () -> Unit = {
        val originalPassword = ""
        // TODO: 원본 비밀번호 가져오기
        when {
            !isValidPassword(newPasswordText.value) ->
                nEqualPasswordToast.show()
            else -> state.value = false
            // TODO: 비밀번호 변경
        }
    }

    Column(
        Modifier.padding(horizontal = 20.dp)
    )
    {
        Text(text = "이메일")
        Spacer(modifier = Modifier.padding(4.dp))
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(lightGrayColor)
                .fillMaxWidth()
        ) {
            TextField(
                value = originalPasswordInput.value,
                onValueChange = { newText ->
                    if (!newText.contains('\t') && !newText.contains('\n')) {
                        originalPasswordInput.value = newText
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("team.sherpa@github.com") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),

                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = lightGrayColor,
                    focusedContainerColor = lightGrayColor,
                ),
            )
        }
    }

    Spacer(modifier = Modifier.padding(8.dp))
    Column(
        Modifier.padding(horizontal = 20.dp)
    )
    {
        Text(text = "비밀번호")
        Spacer(modifier = Modifier.padding(4.dp))
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(lightGrayColor)
                .fillMaxWidth()
        ) {
            TextField(
                value = newPasswordText.value,
                onValueChange = { newText ->
                    if (!newText.contains('\t') && !newText.contains('\n')) {
                        newPasswordText.value = newText
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("비밀번호 입력") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = {passwordVisible = !passwordVisible}){
                        Icon(imageVector = image, description)
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = lightGrayColor,
                    focusedContainerColor = lightGrayColor,
                ),
            )
        }
        Spacer(modifier = Modifier.padding(8.dp))
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(219, 0, 35))
                .fillMaxWidth()
                .clickable(
                    onClick = { onClickChangeButton() },
                    indication = ripple(bounded = true),
                    interactionSource = interactionSource
                )
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ){
            Text(
                text = "이메일 변경",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
            )
        }
    }
}

@Composable
@Preview
fun ContactsPreview(){
    val state = remember {
        mutableStateOf(false)
    }
    Column {
        ContactsModalHeader(state)
        ContactField(state)
    }
}


