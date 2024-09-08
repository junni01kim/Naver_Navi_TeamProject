package com.hansung.sherpa.emergency

import com.google.gson.annotations.SerializedName

/**
 * Sherpa 내부 서버에서 이용되는 Emergency Entity
 * ※ EmergencyManager의 반환 값으로 사용된다.
 *
 * @param emergencyId 긴급 연락처 고유 ID
 * @param userId 긴급 연락처를 추가한 사용자 ID
 * @param deleteYn deleteYn 긴급 연락처 삭제 여부
 * @param name 사용자가 설정한 긴급연락처 이름
 * @param telNum 긴급연락처 전화번호
 * @param address 긴급연락처 주소
 * @param bookmarkYn 홈 화면 설정 Yn
 * @param fileData 긴급 연락처의 프로필 사진
 */
data class Emergency (
    @SerializedName("emergencyId") val emergencyId: Int = -1,
    @SerializedName("userId") val userId: Int,
    @SerializedName("deleteYn") val deleteYn: String = "n",
    @SerializedName("name") val name: String = "",
    @SerializedName("telNum") val telNum: String = "",
    @SerializedName("address") val address: String = "",
    @SerializedName("bookmarkYn") val bookmarkYn: String = "N",
    @SerializedName("fileData") val fileData: String = ""
)