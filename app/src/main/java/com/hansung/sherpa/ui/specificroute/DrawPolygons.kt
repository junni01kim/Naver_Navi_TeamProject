package com.hansung.sherpa.ui.specificroute

import androidx.compose.runtime.Composable
import com.hansung.sherpa.accidentpronearea.AccidentProneArea
import com.naver.maps.map.compose.PolygonOverlay

// TODO: 위험지역 그림 
@Composable
fun DrawPolygons(accidentProneAreas: ArrayList<AccidentProneArea>) {
    accidentProneAreas.forEach {
        it.polygons?.coordinates?.forEach { coordinates ->
            PolygonOverlay(
                coords = coordinates,
                // TODO: 색깔 바꿔주셈 ㅎㅎ
            )
        }
    }
}