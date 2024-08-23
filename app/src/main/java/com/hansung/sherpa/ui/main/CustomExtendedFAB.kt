package com.hansung.sherpa.ui.main

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hansung.sherpa.R
import com.hansung.sherpa.transit.pedestrian.PedestrianResponse

private val CustomIconWidth = 33.dp

private val CustomIconHeight = 33.dp

private val ExtendedFabStartIconPadding = 16.dp

private val ExtendedFabEndIconPadding = 12.dp

private val ExtendedFabTextPadding = 20.dp

private val ExtendedFabMinimumWidth = 80.dp

sealed class IconType {
    data class Vector(val imageVector: ImageVector) : IconType()
    data class Resource(val resId: Int) : IconType()
    data class Painter(@DrawableRes val resId: Int): IconType()
}

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
 * 기존 FAB에서 반전시킨 UI :
 * before : 아이콘 -> 텍스트
 * after : 텍스트 -> 아이콘
 *
 * @param onClick
 * @param text
 * @param icon
 * @param contentDescription
 * @param modifier
 */
@Composable
fun CustomExtendedFAB(
    modifier: Modifier = Modifier,
    text: String,
    icon: IconType,
    expanded: MutableState<Boolean>,
    onClick: () -> Unit  = { expanded.value = !expanded.value},
    contentDescription: String? = "",
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
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
                    Text(text)
                    Spacer(Modifier.width(ExtendedFabEndIconPadding))
                }
            }
            CustomIcon(icon, contentDescription)
        }
    }
}

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
    }
}

@Preview
@Composable
fun ExtendFABImageVector() {
    CustomExtendedFAB(
        text = "Reverse Extended FAB",
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
        text = "아버지\n전화하기: 010-0000-0000",
        icon = IconType.Resource(R.drawable.medical),
        expanded = remember { mutableStateOf(false) }
    )
}

@Preview
@Composable
fun ExtendedFABContainer(isVisible: Boolean = true) {
    val tripleEmrgList = contactList.subList(0, 3)
    val visibleList = remember {
        List(tripleEmrgList.size) { mutableStateOf(false) }
    }
    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.End

    ) {
        if (isVisible) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                items(tripleEmrgList.size) { index ->
                    val item = tripleEmrgList[index]
                    CustomExtendedFAB(
                        text = "${item.name}\n전화하기: ${item.phone}",
                        icon = IconType.Painter(item.image),
                        expanded = visibleList[index]
                    )
                }
            }
            Spacer(modifier = Modifier.padding(vertical = 5.dp))
            AddEmergencyContactFAB()
        }
    }
}