package com.hansung.sherpa.ui.preference.updateinformation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.hansung.sherpa.BuildConfig
import com.hansung.sherpa.R

class UpdateInfoActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UpdateInfoComposable({ finish() })
        }
    }
}

@Composable
fun UpdateInfoComposable(finish:()->Unit){

    val composition by rememberLottieComposition(spec = LottieCompositionSpec.Url("https://lottie.host/1213f1ee-48c9-4f12-8059-61e00023bd51/GqY0bd2PEF.json"))

    var visible by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffe9edc9)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Box(modifier = Modifier
            .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            LottieAnimation(modifier = Modifier
                .width(380.dp)
                .height(380.dp),
                composition = composition,
                iterations = LottieConstants.IterateForever
            )
            
            Image(modifier = Modifier
                .width(200.dp)
                .height(200.dp),
                painter = painterResource(id = R.drawable.appicon_dog),
                contentDescription = "App Icon Dog"
            )
        }
        
        Text(text = "Sherpa", color = Color.Black, fontSize = 32.sp)
        
        Text(text = "v${BuildConfig.VERSION_NAME}", fontSize = 32.sp, fontWeight = FontWeight.Bold)

        Button(onClick = { finish() },
            modifier = Modifier.padding(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xff071520))
            ){
            Text(text = "설정창으로 돌아가기", fontSize = 24.sp, color = Color.White)
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(),
        exit = scaleOut()
    ) {
        //Text(text = "멍멍", fontSize = 12.sp)
    }

}

@Composable
@Preview
fun preview(){
    UpdateInfoComposable({})
}
