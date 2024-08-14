package com.hansung.sherpa.ui.preference.policyinformation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.hansung.sherpa.R

class PolicyInfoActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PolicyComposable({finish()})
        }
    }
}

@Composable
fun PolicyComposable(finish:()->Unit){

    val composition by rememberLottieComposition(spec = LottieCompositionSpec.Url("https://lottie.host/d250c751-0557-4cca-a29b-33695dd0f575/s7VIbpkvf2.json"))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "여기에 정보 처리 방침 작성", fontSize = 32.sp)

        Button(onClick = { finish() },
            modifier = Modifier.padding(32.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xff071520))
        ){
            Text(text = "설정창으로 돌아가기", fontSize = 24.sp, color = Color.White)
        }
    }
}