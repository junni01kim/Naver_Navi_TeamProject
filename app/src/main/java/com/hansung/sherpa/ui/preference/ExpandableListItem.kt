package com.hansung.sherpa.ui.preference

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hansung.sherpa.ui.main.MotionTokens

private val ExtendedListItemCollapseAnimation = fadeOut(
    animationSpec = tween(
        durationMillis = MotionTokens.DurationShort2.toInt(),
        easing = MotionTokens.EasingLinearCubicBezier,
    )
) + shrinkVertically(
    animationSpec = tween(
        durationMillis = MotionTokens.DurationLong2.toInt(),
        easing = MotionTokens.EasingEmphasizedCubicBezier,
    ),
    shrinkTowards = Alignment.Top,
)

private val ExtendedListItemExpandAnimation = fadeIn(
    animationSpec = tween(
        durationMillis = MotionTokens.DurationShort4.toInt(),
        delayMillis = MotionTokens.DurationShort2.toInt(),
        easing = MotionTokens.EasingLinearCubicBezier,
    ),
) + expandVertically(
    animationSpec = tween(
        durationMillis = MotionTokens.DurationLong2.toInt(),
        easing = MotionTokens.EasingEmphasizedCubicBezier,
    ),
    expandFrom = Alignment.Bottom,
)

/**
 * TODO
 *
 * @param text ListItem 텍스트
 * @param parentChecked 상위 리스트 토글
 * @param checked 본인 토글
 * @param isRight 정렬 방향
 */
@Composable
fun InlineListItem(
    text: String,
    parentChecked: Boolean,
    checked: MutableState<Boolean> = mutableStateOf(false),
    isRight :Boolean = false,
    isChild : Boolean = false
) {
    ListItem(
        headlineContent = {
            if(isRight) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = text,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                if (isChild) Text(text = text,  modifier = Modifier.padding(start = 65.dp))
                else Text(text = text)
            }
        },
        trailingContent = {
            CustomSwitch(enabled = parentChecked, checked = checked)
        }
    )
    HorizontalDivider()
}

/**
 * TODO
 *
 * @param headline 아이템 큰 글씨
 * @param supporting 아이템 작은 글씨
 * @param parentChecked 상위 리스트 토글
 * @param checked 현재 리스트 토글
 */
@Composable
fun DoubleLineListItem(
    headline: String,
    supporting: String,
    parentChecked: Boolean,
    checked: MutableState<Boolean> = mutableStateOf(false),
) {
    ListItem(
        headlineContent = {
            Text(text = headline)
        },
        supportingContent = {
            Text(text = supporting)},
        trailingContent = {
            CustomSwitch(enabled = parentChecked, checked = checked)
        }
    )
    HorizontalDivider()
}

/**
 * TODO
 *
 * @param headline 아이템 큰 글씨
 * @param supporting 아이템 작은 글씨
 * @param expanded 현재 리스트 펼치기 토글
 * @param checked 현재 리스트 토글
 * @param onExpand 하위 리스트 확장시키는 함수
 * @param onShrink 하위 리스트 축소시키는 함수
 */
@Composable
fun ExpandableListItem(
    headline: String,
    supporting: String? = null,
    expanded: Boolean,
    checked: MutableState<Boolean>,
    onExpand: () -> Unit,
    onShrink: () -> Unit
) {
    var degree by remember {
        mutableFloatStateOf(0.0F)
    }
    val onRotate = { degree = 90.0F - degree }
    ListItem(
        headlineContent = { Text(headline) },
        supportingContent = { supporting?.let { Text(supporting) } },
        trailingContent = { CustomSwitch(expanded, checked, checked, onExpand, onShrink, onRotate) },
        leadingContent = {
            IconButton(onClick = {
                if(!expanded) onExpand()
                else onShrink()
                onRotate()
            }) {
                Icon(
                    Icons.Filled.ChevronRight,
                    contentDescription = "펼치기/접기 화살표",
                    modifier = Modifier.rotate(degree)
                )
            }
        }
    )
}

/**
 * 애니메이션 (펼치기/접기)
 *
 * @param headline
 * @param supporting
 * @param expanded
 * @param checked
 * @param child
 */
@Composable
fun AnimationListItem(
    headline: String,
    supporting: String? = null,
    expanded: MutableState<Boolean>,
    checked: MutableState<Boolean>,
    child: @Composable () -> Unit
) {
    Column {
        ExpandableListItem(
            headline,
            supporting,
            expanded.value,
            checked,
            { expanded.value = true },
            { expanded.value = false }
            )
        HorizontalDivider()
        AnimatedVisibility(
            visible = expanded.value,
            enter = ExtendedListItemExpandAnimation,
            exit = ExtendedListItemCollapseAnimation,
        ) {
            Column {
                child()
            }
        }
    }
}

/**
 * 토글 스위치 버튼 커스텀
 *
 * @param expanded
 * @param parentChecked
 * @param checked
 * @param onExpand
 * @param onShrink
 * @param onRotate
 * @param enabled
 */
@Composable
fun CustomSwitch(
    expanded: Boolean = false,
    parentChecked: MutableState<Boolean> = mutableStateOf(false),
    checked: MutableState<Boolean> = mutableStateOf(false),
    onExpand: () -> Unit = {},
    onShrink: () -> Unit = {},
    onRotate: () -> Unit = {},
    enabled: Boolean = true
) {
    Switch(
        enabled = enabled,
        checked = checked.value,
        onCheckedChange = {
            if(it) onExpand() // 체크 되어 있으면, 리스트 펼치기
            else onShrink() // 아니면 리스트 접기
            if(it != expanded) onRotate() // 체크되어 있고, 리스트가 펼쳐 있는 경우 화살표 안돌아가게
            checked.value = it
            parentChecked.value = it
        }
    )
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun PreviewAlarmSettingsScreen() {
    InlineListItem("Right Aligned Text", true, mutableStateOf(false), false)
}

@Preview
@Composable
fun PreviewDoubleLineListItem() {
    DoubleLineListItem(
        "Two line list item with trailing",
        "Secondary text",
        true)
}
@Preview
@Composable
fun PreviewSwitchMinimalExample() {
    CustomSwitch()
}

@Preview
@Composable
fun PreviewAnimationListItem() {
    val expanded = remember {
        mutableStateOf(false)
    }
    val checked = remember {
        mutableStateOf(false)
    }
    val checked1 = remember {
        mutableStateOf(false)
    }
    val checked2 = remember {
        mutableStateOf(false)
    }
    AnimationListItem(
        "Two line list item with trailing",
        "Secondary text",
        expanded,
        checked) {
        if(!checked.value) {
            checked1.value = false
            checked2.value = false
        }
        InlineListItem("Right Aligned Text", checked.value, checked1)
        InlineListItem("Right Aligned Text", checked.value, checked2)
    }
}