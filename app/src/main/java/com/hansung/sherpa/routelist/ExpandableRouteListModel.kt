package com.hansung.sherpa.routelist

class ExpandableRouteListModel {
    companion object{
        const val PARENT = 1
        const val CHILD = 2

    }
    lateinit var parent: ResponseRoutes.Route // 부모 리스트
    var type : Int
    lateinit var child : ResponseRoutes.Route.DetailRoute // 확장되면 나올 리스트
    var isExpanded : Boolean
    private var isCloseShown : Boolean

    constructor( type : Int, parent: ResponseRoutes.Route, isExpanded : Boolean = false,isCloseShown : Boolean = false){
        this.type = type
        this.parent = parent
        this.isExpanded = isExpanded
        this.isCloseShown = isCloseShown
    }

    constructor(type : Int, child : ResponseRoutes.Route.DetailRoute, isExpanded : Boolean = false,isCloseShown : Boolean = false){
        this.type = type
        this.child = child
        this.isExpanded = isExpanded
        this.isCloseShown = isCloseShown
    }

}