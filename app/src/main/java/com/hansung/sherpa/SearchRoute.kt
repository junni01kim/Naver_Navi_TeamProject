package com.hansung.sherpa

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hansung.sherpa.geocoding.GeocodingAPI

// TemporaryClass는 임시 클래스라는 의미로 LatLng라는 클래스와 type를 값으로 가진다.
// path의 두가지 Pair은 출발지와 도착지의 lon lag를 의미한다.(실제론 LanLng를 의미) type는 대중교통의 종류를 의미한다.
data class TemporaryClass(val path:Pair<Pair<Double,Double>,Pair<Double,Double>>, val type:String)

data class TemporaryClasses(private val temporaryClassArray: Array<TemporaryClass>){
    private var hasTemporaryClassIndex = -1

    // 아무값도 없다면 -1로 다시 되돌아가게 한다. (끝에 도달해야 다시 그릴 수 있다.)
    fun hasTemporaryClass():Boolean {
        if(temporaryClassArray.size<hasTemporaryClassIndex) {
            hasTemporaryClassIndex = -1
            return false
        }
        else
            return true
    }

    // 선그리기 도중 오류가 나면 hasTemporaryClassIndex를 초기화 시키기 위한 함수
    fun resetHasTemporaryClassIndex() {
        hasTemporaryClassIndex = -1
    }

    // 배열을 증가시키면서 배열을 참조하기 위한 함수
    fun getHasTemporaryClassIndex():TemporaryClass {
        return temporaryClassArray[++hasTemporaryClassIndex]
    }
}

// 텍스트뷰에 값이 그래도 유지되고 있게하기 위한 뷰모델이다.
// searchRouteViewModel의 이름과 위치는 바꿀 필요가 있을듯
class searchRouteViewModel:ViewModel() {
    val departureText = MutableLiveData<String>()
}

// 선 그리는 함수
fun drawRoute(temporaryClasses: TemporaryClasses){
    while (temporaryClasses.hasTemporaryClass()) {
        val nowTemporaryClass = temporaryClasses.getHasTemporaryClassIndex()
        when(nowTemporaryClass.type){
            "walk" -> Log.d("move", "걷기")
            "bus" -> Log.d("move", "버스")
            "subway" -> Log.d("move", "지하철")
            else -> Log.d("move", "오류")
        }
    }
}

fun SearchRoute(departure:String) {
    // 단순히 geocoding이 되나 확인하기 위한 코드(삭제해도 됨)
    val geocodingAPI = GeocodingAPI(departure)

    val temporaryClasses = TemporaryClasses(emptyArray<TemporaryClass>())
    // TODO(geocoding한 값으로 출발 도착지 값 받아오기)
    // TODO(값을 temporaryClassArray에 geocoding한 값 넣기 or 넣어져 있을 수도 있다.)

    drawRoute(temporaryClasses)
}