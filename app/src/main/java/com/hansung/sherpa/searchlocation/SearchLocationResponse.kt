package com.hansung.sherpa.searchlocation

import com.google.gson.annotations.SerializedName


data class SearchLocationResponse (

  @SerializedName("lastBuildDate" ) var lastBuildDate : String?          = null,
  @SerializedName("total"         ) var total         : Int?             = null,
  @SerializedName("start"         ) var start         : Int?             = null,
  @SerializedName("display"       ) var display       : Int?             = null,
  @SerializedName("items"         ) var items         : ArrayList<Items> = arrayListOf()

)