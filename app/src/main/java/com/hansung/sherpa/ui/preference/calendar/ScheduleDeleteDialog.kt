package com.hansung.sherpa.ui.preference.calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.hansung.sherpa.ui.theme.PurpleGrey40

@Composable
fun ScheduleDeleteDialog(
    openDialogCustom: MutableState<Boolean>,
    onDelete : (Boolean) -> Unit
) {
    Dialog(onDismissRequest = { openDialogCustom.value = false}) {
        CustomDialogUI(
            openDialogCustom = openDialogCustom,
            onDelete = onDelete
        )
    }
}

//Layout
@Composable
fun CustomDialogUI(
    modifier: Modifier = Modifier,
    openDialogCustom: MutableState<Boolean>,
    onDelete : (Boolean) -> Unit
){
    val lightGrayColor = Color(229,226,234)
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.padding(10.dp,5.dp,10.dp,10.dp),
        elevation = CardDefaults.elevatedCardElevation(8.dp)
    ) {
        Column(
            modifier
                .background(Color.White)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "일정 삭제",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth(),
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Text(
                    text = "해당 일정을 삭제 하시겠습니까?",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.padding(4.dp))
            }
            //.......................................................................
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .background(lightGrayColor),
                horizontalArrangement = Arrangement.SpaceAround) {

                androidx.compose.material3.TextButton(onClick = {
                    openDialogCustom.value = false
                    onDelete(false)
                }) {
                   Text(
                        "취소",
                        fontWeight = FontWeight.Bold,
                        color = PurpleGrey40,
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                    )
                }
                androidx.compose.material3.TextButton(onClick = {
                    openDialogCustom.value = false
                    onDelete(true)
                }) {
                    Text(
                        "삭제",
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(219, 0, 35),
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                    )
                }
            }
        }
    }
}
