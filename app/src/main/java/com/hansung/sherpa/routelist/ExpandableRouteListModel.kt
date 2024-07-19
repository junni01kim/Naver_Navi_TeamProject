package com.hansung.sherpa.routelist

/**
 * RouteList RecyclerView에 전달되는 클래스
 * @param type parent와 child를 구분하기 위한 함수
 * @param parent 요약 정보 데이터
 * @param child 세부 정보 데이터
 * @param isExpanded 아이템의 확장여부를 저장하기 위한 플래그
 */
class ExpandableRouteListModel {
    // 아이템의 종류를 알기 위한 타입번호
    companion object{
        const val PARENT = 1
        const val CHILD = 2

    }

    var type : Int
    lateinit var parent: Route // 요약 정보 리스트
    lateinit var child : Route.DetailRoute // 세부 정보 리스트
    var isExpanded : Boolean

    /**
     * 요약 정보를 저장하는 생성자
     * 해당 생성자를 사용하기 위해선 type은 무조건 PARENT
     */
    constructor( type : Int, parent: Route, isExpanded : Boolean = false){
        this.type = type
        this.parent = parent
        this.isExpanded = isExpanded
    }

    /**
     * 세부 정보를 저장하는 생성자
     * 해당 생성자를 사용하기 위해선 type은 무조건 CHILD
     */
    constructor(type : Int, child : Route.DetailRoute, isExpanded : Boolean = false){
        this.type = type
        this.child = child
        this.isExpanded = isExpanded
    }
}