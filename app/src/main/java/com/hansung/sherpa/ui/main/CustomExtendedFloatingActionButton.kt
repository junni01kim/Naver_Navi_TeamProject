package com.hansung.sherpa.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.tooling.preview.Preview

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
fun CustomExtendedFloatingActionButton(
    onClick: () -> Unit,
    text: String,
    icon: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    expanded: Boolean
) {

    val ExtendedFabStartIconPadding = 16.dp

    val ExtendedFabEndIconPadding = 12.dp

    val ExtendedFabTextPadding = 20.dp

    val ExtendedFabMinimumWidth = 80.dp

    val ExtendedFabCollapseAnimation = fadeOut(
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

    val ExtendedFabExpandAnimation = fadeIn(
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
    ExtendedFloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        text = {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                val startPadding = if (expanded) ExtendedFabStartIconPadding else 0.dp
                val endPadding = if (expanded) ExtendedFabTextPadding else 0.dp

                Row(
                    modifier = Modifier
                        .sizeIn(
                            minWidth = if (expanded) ExtendedFabMinimumWidth
                            else FabPrimaryTokens.ContainerWidth
                        )
                        .padding(start = startPadding, end = endPadding),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = if (expanded) Arrangement.Start else Arrangement.Center
                ) {
                    AnimatedVisibility(
                        visible = expanded,
                        enter = ExtendedFabExpandAnimation,
                        exit = ExtendedFabCollapseAnimation,
                    ) {
                        Row(Modifier.clearAndSetSemantics {}) {
                            Text(text)
                            Spacer(Modifier.width(ExtendedFabEndIconPadding))
                        }
                    }
                    Icon(imageVector = icon, contentDescription = contentDescription)
                }
            }
        },
        icon = { }, // 아이콘 생략
    )
}

@Preview
@Composable
fun MyScreen() {
    val isExpanded = remember {
        mutableStateOf(
            false
        )
    }
    CustomExtendedFloatingActionButton(
        onClick = { isExpanded.value = !isExpanded.value},
        text = "Reverse Extended FAB",
        icon = Icons.Filled.Edit,
        contentDescription = "Favorite",
        modifier = Modifier, // 여기에 필요한 Modifier를 추가하세요
        expanded = isExpanded.value
    )
}