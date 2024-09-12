package com.hansung.sherpa.searchlocation

import com.google.gson.annotations.SerializedName

/**
 * @author 6-keem
 *
 * @param title 장소명
 * @param link 링크
 * @param category 카테고리 ex) 음식점
 * @param telephone 전화번호
 * @param description 상세정보
 * @param address 주소
 * @param roadAddress 도로명 주소
 * @param mapx longitude
 * @param mapy latitude
 */
data class Items (

  @SerializedName("title"       ) var title       : String? = null,
  @SerializedName("link"        ) var link        : String? = null,
  @SerializedName("category"    ) var category    : String? = null,
  @SerializedName("description" ) var description : String? = null,
  @SerializedName("telephone"   ) var telephone   : String? = null,
  @SerializedName("address"     ) var address     : String? = null,
  @SerializedName("roadAddress" ) var roadAddress : String? = null,
  @SerializedName("mapx"        ) var mapx        : Double? = null,
  @SerializedName("mapy"        ) var mapy        : Double? = null

)