package com.hansung.sherpa.ui.preference

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarScreen(title: String = "캘린더", finish : () -> Unit, content: @Composable () -> Unit) {
    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    titleContentColor = Color.DarkGray
                ),

                title = {
                    Text(
                        text = title,
                        style = androidx.compose.ui.text.TextStyle(
                            fontFamily = FontFamily.Cursive,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { finish() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "뒤로가기")
                    }
                },
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        content = { innerpadding ->
            Surface(modifier = Modifier.padding(innerpadding)) {
                content()
            }
        }
    )
}