package com.hansung.sherpa.ui.account.signup

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hansung.sherpa.SherpaScreen
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.sherpares.BmHanna
import com.hansung.sherpa.sherpares.SherpaButtonColor
import com.hansung.sherpa.sherpares.SherpaColor
import com.hansung.sherpa.ui.account.module.InfomationGroup
import com.hansung.sherpa.ui.account.module.InfomationGroupDarkMode
import com.hansung.sherpa.ui.account.module.ViewTOS
import com.hansung.sherpa.ui.account.module.rowWidth
import com.hansung.sherpa.user.CreateUserRequest
import com.hansung.sherpa.user.UserManager
import java.sql.Timestamp
import java.util.Calendar

@Composable
fun SignupScreen(navController: NavController = rememberNavController(), modifier: Modifier = Modifier){
    var careToggle by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .rotate(180f)
                    .clip(RoundedCornerShape(800.dp, 800.dp))
                    .background(SherpaColor)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .background(Color.White)
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            TitleArea()

            // 보호자 입력란
            Column {
                Row(
                    modifier = Modifier.width(rowWidth+20.dp),
                    horizontalArrangement = Arrangement.End
                ){
                    Box(modifier = Modifier
                        .size(70.dp, 20.dp)
                        .clip(RoundedCornerShape(100, 100))
                        .background(Color.White)
                        .clickable {
                            careToggle = false
                        },
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "보호자",
                            color = Color.Black,
                            fontFamily = BmHanna
                        )
                    }
                    Box(modifier = Modifier
                        .size(70.dp, 20.dp)
                        .clip(RoundedCornerShape(100, 100))
                        .background(Color.Black)
                        .clickable {
                            careToggle = true
                        },
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "사용자",
                            fontFamily = BmHanna,
                            color = Color.White
                        )
                    }
                }
                if (careToggle) CaretakerArea(navController)
                else CaregiverArea(navController)
            }
        }
    }
}

fun isValidId(id: String): Boolean {
    val regex = "^[a-zA-Z0-9!@#\$%^&*()_+\\-=]{8,20}$".toRegex()
    return regex.matches(id)
}

fun signup(navController: NavController, createUserRequest: CreateUserRequest):Boolean {
    val user1 = UserManager().create(createUserRequest)
    if(user1.code == 200) navController.navigate(SherpaScreen.Login.name)
    else return true
    return false
}

@Composable
@Preview
fun SignupPreview() {
    SignupScreen()
}