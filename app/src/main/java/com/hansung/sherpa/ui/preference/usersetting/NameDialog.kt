package com.hansung.sherpa.ui.preference.usersetting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.hansung.sherpa.ui.theme.PurpleGrey40

@Composable
fun NameDialog(
    modifier: Modifier = Modifier,
    openDialogCustom: MutableState<Boolean>
){
    val text = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val lightGrayColor = Color(229,226,234)
    val isValidColor = remember { mutableStateOf(Color.Black) }
    var isChangeable by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = { openDialogCustom.value = false}) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.padding(10.dp,5.dp,10.dp,10.dp),
            elevation = CardDefaults.elevatedCardElevation(8.dp)
        ) {
            Column(
                modifier
                    .background(Color.White)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "닉네임 변경",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .fillMaxWidth(),
                        style = TextStyle(
                            fontSize = 18.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    TextField(
                        value = text.value,
                        onValueChange = { newText ->
                            if (!newText.contains('\t') && !newText.contains('\n')) {
                                text.value = newText
                                isValidColor.value = Color.Black
                                isChangeable = false
                            }
                        },
                        placeholder = { Text(text = "새 닉네임")},
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .clip(RoundedCornerShape(12.dp))
                            .align(Alignment.CenterHorizontally),
                        singleLine = true,
                        trailingIcon = {
                            if(text.value.isNotEmpty()) {
                                IconButton(onClick = {
                                    val isValid = isValidName(text.value)
                                    when(isValid){
                                        true -> {
                                            isChangeable = true
                                            isValidColor.value = Color.Green
                                        }
                                        false -> {
                                            isChangeable = false
                                            isValidColor.value = Color.Red
                                        }
                                    }

                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.CheckCircleOutline,
                                        contentDescription = "",
                                        tint = isValidColor.value
                                    )
                                }
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            unfocusedContainerColor = lightGrayColor,
                            focusedContainerColor = lightGrayColor,
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            capitalization = KeyboardCapitalization.Characters,
                            autoCorrectEnabled = false,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done,
                            showKeyboardOnFocus = null ?: true
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            keyboardController?.hide()
                        }),
                    )

                    Spacer(modifier = Modifier.padding(4.dp))
                }
                //.......................................................................
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .background(lightGrayColor),
                    horizontalArrangement = Arrangement.SpaceAround) {

                    TextButton(onClick = {
                        openDialogCustom.value = false
                    }) {
                        Text(
                            "취소",
                            fontWeight = FontWeight.Bold,
                            color = PurpleGrey40,
                            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                        )
                    }
                    TextButton(onClick = {
                        openDialogCustom.value = false
                    }) {
                        Text(
                            "변경",
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(219, 0, 35),
                            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                        )
                    }
                }
            }
        }
    }
}

fun isValidName(name : String) : Boolean {
    val nameRegex = "^(?=[A-Za-z_])(?=.*[a-zA-Z])(?=.*\\d)[A-Za-z\\d_]{4,20}\$".toRegex()
    return nameRegex.matches(name)
}

@Composable
@Preview
fun NameDialogPreview(){
    val state = remember {
        mutableStateOf(false)
    }
    NameDialog(openDialogCustom = state)
}