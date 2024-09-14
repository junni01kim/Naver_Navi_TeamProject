package com.hansung.sherpa.ui.account.signup

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hansung.sherpa.ui.theme.BmHanna

/**
 * Screen 명칭이 나오는 영역
 */
@Composable
fun TitleArea() {
    Row(verticalAlignment = Alignment.CenterVertically){
        /**
         * User Icon
         *
         * 디자인을 위한 사람 아이콘
         */
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "디자인을 위한 사람 아이콘",
            modifier = Modifier.size(60.dp,60.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        /**
         * Login Text
         *
         * 로그인 텍스트
         */
        Text(
            text = "회원가입",
            fontSize = 40.sp,
            fontFamily = BmHanna
        )
    }
}