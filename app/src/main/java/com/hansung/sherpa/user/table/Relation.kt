package com.hansung.sherpa.user.table

data class Relation(
    val userRelationId:Int,
    val caretakerId:Int,
    val caregiverId:Int,
    val relation:String
)