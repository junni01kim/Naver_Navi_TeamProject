package com.hansung.sherpa.ui.preference.emergency

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun ItemRow(name:String?, address:String?, telNum:String?, deleteItem: () -> Unit, openItemInfo: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .drawBehind {
                val strokeWidth = 1 * density
                val y = size.height - strokeWidth / 2
                drawLine(
                    Color(240, 240, 240),
                    Offset(0f, y),
                    Offset(size.width, y),
                    strokeWidth
                )
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        deleteItem()
                    },
                    onTap = {
                        openItemInfo()
                    }
                )
            }
            .padding(horizontal = 12.dp, vertical = 12.dp)) {
        Text(
            text = name ?: "None",
            modifier = Modifier.width(80.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = address ?: "None",
            modifier = Modifier.width(175.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = telNum ?: "None",
            modifier = Modifier.width(120.dp),
            maxLines = 1,
            textAlign = TextAlign.End
        )
    }
}