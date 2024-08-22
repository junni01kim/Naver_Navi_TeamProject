package com.hansung.sherpa.user.relation

data class Relation(
    val userRelationId:Int,
    val caretakerId:Int,
    val caregiverId:Int,
    val relation:String
)