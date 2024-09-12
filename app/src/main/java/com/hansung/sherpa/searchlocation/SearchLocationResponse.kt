package com.hansung.sherpa.searchlocation

import com.google.gson.annotations.SerializedName

/**
 * @author 6-keem
 *
 * @param lastBuildDate 검색 결과를 생성한 시간
 * @param total 찾은 목적지 개수
 * @param start 검색 시작 위치
 * @param display 한 번에 표시할 검색 결과 개수
 * @param items 찾은 장소들
 */
data class SearchLocationResponse (

  @SerializedName("lastBuildDate" ) var lastBuildDate : String?          = null,
  @SerializedName("total"         ) var total         : Int?             = null,
  @SerializedName("start"         ) var start         : Int?             = null,
  @SerializedName("display"       ) var display       : Int?             = null,
  @SerializedName("items"         ) var items         : ArrayList<Items> = arrayListOf()

)