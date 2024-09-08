package com.hansung.sherpa.ui.main

import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hansung.sherpa.R
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.emergency.EmergencyManager
import com.hansung.sherpa.ui.theme.SherpaTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Base64

private val CustomIconWidth = 33.dp

private val CustomIconHeight = 33.dp

private val ExtendedFabStartIconPadding = 1.dp

private val ExtendedFabEndIconPadding = 10.dp

private val ExtendedFabTextPadding = 20.dp

private val ExtendedFabMinimumWidth = 40.dp

/**
 * IconType 이미지 유형별로 분류해서 이미지 추가할 수 있도록
 *
 */
sealed class IconType {
    data class Vector(val imageVector: ImageVector) : IconType()
    data class Resource(val resId: Int) : IconType()
    data class Painter(@DrawableRes val resId: Int): IconType()
    data class Bitmap(val imageBitmap: ImageBitmap): IconType()
}

/**
 * Extend FAB 축소 애니메이션
 */
private val ExtendedFabCollapseAnimation = fadeOut(
    animationSpec = tween(
        durationMillis = MotionTokens.DurationShort2.toInt(),
        easing = MotionTokens.EasingLinearCubicBezier,
    )
) + shrinkHorizontally(
    animationSpec = tween(
        durationMillis = MotionTokens.DurationLong2.toInt(),
        easing = MotionTokens.EasingEmphasizedCubicBezier,
    ),
    shrinkTowards = Alignment.Start,
)

/**
 * Extend FAB 확장 애니메이션
 */
private val ExtendedFabExpandAnimation = fadeIn(
    animationSpec = tween(
        durationMillis = MotionTokens.DurationShort4.toInt(),
        delayMillis = MotionTokens.DurationShort2.toInt(),
        easing = MotionTokens.EasingLinearCubicBezier,
    ),
) + expandHorizontally(
    animationSpec = tween(
        durationMillis = MotionTokens.DurationLong2.toInt(),
        easing = MotionTokens.EasingEmphasizedCubicBezier,
    ),
    expandFrom = Alignment.Start,
)

/**
 * 기조는 ExtendedFAB와 같으므로 공식문서 참고.
 *
 * 기존 FAB에서 반전시킨 UI :
 * before : 아이콘 -> 텍스트
 * after : 텍스트 -> 아이콘
 *
 * @param modifier
 * @param name
 * @param phone
 * @param icon
 * @param expanded
 * @param onClick
 * @param contentDescription
 * @param openDialog < 클릭 여부
 */
@Composable
fun CustomExtendedFAB(
    modifier: Modifier = Modifier,
    name: String = "ㅇㅇㅇ",
    phone: String = "000-0000-0000",
    icon: IconType,
    expanded: MutableState<Boolean>,
    onClick: () -> Unit  = { expanded.value = !expanded.value},
    contentDescription: String? = "",
    openDialog: MutableState<Boolean> = remember { mutableStateOf(true) }
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.onSecondary
    ) {
        val startPadding = if (expanded.value) ExtendedFabStartIconPadding else 0.dp
        val endPadding = if (expanded.value) ExtendedFabTextPadding else 0.dp

        Row(
            modifier = Modifier
                .sizeIn(
                    minWidth = if (expanded.value) ExtendedFabMinimumWidth
                    else FabPrimaryTokens.ContainerWidth
                )
                .padding(start = startPadding, end = endPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (expanded.value) Arrangement.Start else Arrangement.Center
        ) {
            AnimatedVisibility(
                visible = expanded.value,
                enter = ExtendedFabExpandAnimation,
                exit = ExtendedFabCollapseAnimation,
            ) {
                Row(Modifier.clearAndSetSemantics {}) {
                    Icon(modifier = Modifier
                        .height(40.dp)
                        .wrapContentWidth()
                        .clickable { openDialog.value = true }
                        , imageVector = Icons.Default.ChevronLeft, contentDescription = "긴급 연락처 요청")
                    Column(horizontalAlignment = Alignment.End) {
                        Text(name, textAlign = TextAlign.End)
                        Text(phone, textAlign = TextAlign.End, style = TextStyle(fontSize = 12.sp), modifier = Modifier.padding(start = 20.dp))
                    }
                    Spacer(Modifier.width(ExtendedFabEndIconPadding))
                }
            }
            CustomIcon(icon, contentDescription)
        }
    }
}

/**
 * IconType에 따라 Icon에 이미지를 추가하는 함수
 *
 * @param icon
 * @param contentDescription
 */
@Composable
fun CustomIcon(icon: IconType, contentDescription: String?) {
    val modifier = Modifier
        .width(CustomIconWidth)
        .height(CustomIconHeight)
    when(icon) {
        is IconType.Vector -> {
            Icon(modifier = modifier ,imageVector = icon.imageVector, contentDescription = contentDescription)
        }
        is IconType.Resource -> {
            Icon(modifier = modifier, imageVector = ImageVector.vectorResource(id = icon.resId), contentDescription = contentDescription)
        }
        is IconType.Painter -> {
            Image(modifier = modifier
                .aspectRatio(1f)
                .clip(CircleShape), painter = painterResource(id = icon.resId), contentScale = ContentScale.Crop, contentDescription = contentDescription)
        }
        is IconType.Bitmap -> {
            Image(modifier = modifier
                .aspectRatio(1f)
                .clip(CircleShape), bitmap = icon.imageBitmap, contentScale = ContentScale.Crop, contentDescription = contentDescription)
        }
    }
}

@Preview
@Composable
fun ExtendFABImageVector() {
    CustomExtendedFAB(
        name = "홍길동",
        phone = "010-1234-5678",
        icon = IconType.Vector(Icons.Filled.Edit),
        contentDescription = "Favorite",
        modifier = Modifier,
        expanded = remember { mutableStateOf(false) }
    )
}

@Preview
@Composable
fun FabExtendedResource() {
    CustomExtendedFAB(
        name = "아버지",
        phone = "전화하기: 010-0000-0000",
        icon = IconType.Resource(R.drawable.medical),
        expanded = remember { mutableStateOf(true) }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun ExtendedFABContainer(isVisible: Boolean = true) {
    val coroutineScope = rememberCoroutineScope()
    val contactList = remember { mutableStateListOf<Contact>() } // 조회된 긴급 연락처 리스트
    val bookmarkList = remember { mutableStateListOf<Contact>() } // 북마크(메인 화면에 띄워지는) 연락처 리스트
    val isExpandList = remember { mutableStateListOf<MutableState<Boolean>>() } // 북마크 리스트에서 확장되어 있는 것들의 리스트
    val userId = StaticValue.userInfo.userId ?: 0
    LaunchedEffect(Unit) {
        val apiList = withContext(Dispatchers.IO) {
            val result = EmergencyManager().getAllEmergency(userId).data // 현재 userId의 긴급연락처 리스트 가져오기
            result?.mapIndexed { _, emergency ->
                val byteArray = decodeFileData(emergency.fileData)
                Contact(
                    emergencyId     = emergency.emergencyId,
                    name            = emergency.name,
                    phone           = emergency.telNum,
                    image           = BitmapFactory
                        .decodeByteArray(byteArray, 0, byteArray.size)
                        .asImageBitmap(), // 인덱스 초과 시 기본 이미지 설정
                    address         = emergency.address,
                    bookmarkYn      = emergency.bookmarkYn
                )
            } ?: emptyList()
        }

        // UI 업데이트는 메인 스레드에서 진행
        contactList.addAll(apiList)
        bookmarkList.addAll(contactList.filter { it.bookmarkYn == "Y" })
        isExpandList.addAll(bookmarkList.map { mutableStateOf(false) })
    }
    if (isVisible) {
        SherpaTheme {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.End
            ) {
                LazyColumn(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(vertical = 8.dp)
                ) {
                    items(bookmarkList.size) { index ->
                        val item = bookmarkList[index]
                        val openDialog: MutableState<Boolean> = remember { mutableStateOf(false) }
                        if (openDialog.value) EmergencyOptionModal(openDialog ,item)

                        CustomExtendedFAB(
                            name        = item.name,
                            phone       = item.phone,
                            icon        = IconType.Bitmap(item.image),
                            expanded    = isExpandList[index],
                            openDialog  = openDialog
                        )
                    }
                }
                AddEmergencyContactFAB(contactList.filter { it.bookmarkYn == "N" }) {
                    contact ->
                    // 화면은 UI로 먼저 보여주고, 따로 백그라운드로 업데이트
                    coroutineScope.launch(Dispatchers.IO) {
                        EmergencyManager().updateEmergencyBookmark(contact.emergencyId)
                    }
                    bookmarkList.add(contact)
                    isExpandList.add(mutableStateOf(false))
                }
            }
        }
    }
}

/**
 * 인코딩된, 아미지 데이터 디코딩
 *
 * @param base64String 인코딩 byte 값
 * @return
 */
@RequiresApi(Build.VERSION_CODES.O)
fun decodeFileData(base64String: String): ByteArray {
    return Base64.getDecoder().decode(base64String)
}