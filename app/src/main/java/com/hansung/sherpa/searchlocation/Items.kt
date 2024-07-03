package com.hansung.sherpa.searchlocation

import com.google.gson.annotations.SerializedName


data class Items (

  @SerializedName("title"       ) var title       : String? = null,
  @SerializedName("link"        ) var link        : String? = null,
  @SerializedName("category"    ) var category    : String? = null,
  @SerializedName("description" ) var description : String? = null,
  @SerializedName("telephone"   ) var telephone   : String? = null,
  @SerializedName("address"     ) var address     : String? = null,
  @SerializedName("roadAddress" ) var roadAddress : String? = null,
  @SerializedName("mapx"        ) var mapx        : String? = null,
  @SerializedName("mapy"        ) var mapy        : String? = null

)