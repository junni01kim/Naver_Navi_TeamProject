package com.hansung.sherpa.emergency

/**
 * [Sherpa 내부 서버] 긴급 연락처 관련 API 반환 값
 * 
 * @property code API 상태 코드
 * @property message API 결과 메세지
 * @property data [확장 노드] 함수에 따른 API 반환 객체
 */
data class EmergencyResponse (
    val code: Int? = null,
    val message: String? = null,
    val `data`: Emergency? = null
)

/**
 * [Sherpa 내부 서버] 긴급 연락처 관련 API 반환 값
 *
 * @property code API 상태 코드
 * @property message API 결과 메세지
 * @property data [확장 노드] 함수에 따른 API 반환 객체
 */
data class EmergencyListResponse (
    val code: Int? = null,
    val message: String? = null,
    val `data`: MutableList<Emergency>? = null
)

/**
 * ※ 삭제 예정
 * [Sherpa 내부 서버] 긴급 연락처 관련 API 반환 값
 *
 * @property code API 상태 코드
 * @property message API 결과 메세지
 * @property data [확장 노드] 함수에 따른 API 반환 객체
 */
data class DeleteEmergencyResponse (
    val code: Int? = null,
    val message: String? = null,
    val `data`: String? = null
)