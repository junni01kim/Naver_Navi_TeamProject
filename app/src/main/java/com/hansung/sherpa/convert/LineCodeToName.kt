package com.hansung.sherpa.convert

//https://lab.odsay.com/guide/releaseReference#defineCode
fun lineCodeToName(lineCode:Int):String{
    when(lineCode){
        1-> return "1호선"
        2-> return "2호선"
        3-> return "3호선"
        4-> return "4호선"
        5-> return "5호선"
        6-> return "6호선"
        7-> return "7호선"
        8-> return "8호선"
        9-> return "9호선"

        91-> return "GTX-A"

        101-> return "공항철도"
        102-> return "인천공항자기부상철도"

        104-> return "경의중앙선"
        107-> return "용인경전철"
        108-> return "지하철경춘선"
        109-> return "신분당선"

        110-> return "의정부경전철"
        112-> return "경강선"
        113-> return "우이신설선"
        114-> return "서해선"

        115-> return "김포골드라인"
        116-> return "수인분당선"
        117-> return "신림선"


        21-> return "인천지하철1호선"
        22-> return "인천지하철2호선"

        31-> return "대전지하철1호선"

        41-> return "대구지하철1호선"
        42-> return "대구지하철2호선"
        43-> return "대구지하철3호선"

        51-> return "광주지하철1호선"

        71-> return "부산지하철1호선"
        72-> return "부산지하철2호선"
        73-> return "부산지하철3호선"
        74-> return "부산지하철4호선"

        78-> return "동해선"
        79-> return "부산-김해경전철"

        else -> return "NO_MATCHING_SUBWAY_CODE"
    }
}