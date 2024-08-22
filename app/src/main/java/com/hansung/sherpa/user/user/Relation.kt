package com.hansung.sherpa.user.user

data class Relation(
    val userRelationId:Int,
    val caretakerId:Int,
    val caregiverId:Int,
    val relation:String
)