package com.hansung.sherpa.searchlocation

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Search API Request 인터페이스
 */
interface SearchLocationService {
    /** Search API GET 요청을 위한 함수
     *
     * @param query ★필수★ 장소 문자열 (UTF-8 encoding required)
     * @param display 한 번에 표시할 검색 결과 개수 (기본값: 1, 최댓값: 5)
     * @param start 검색 시작 위치(기본값: 1, 최댓값: 1)
     * @param sort 검색 결과 정렬 방법  - random: 정확도순으로 내림차순 정렬(기본값) - comment: 업체 및 기관에 대한 카페, 블로그의 리뷰 개수순으로 내림차순 정렬
     */
    @GET("v1/search/local.json")
    fun searchLocation(@Query("query") query : String,
                       @Query("display") display : Int? = 5,
                       @Query("start") start : Int? = 1,
                       @Query("sort") sort : String? = "sim") : Call<SearchLocationResponse>
}
