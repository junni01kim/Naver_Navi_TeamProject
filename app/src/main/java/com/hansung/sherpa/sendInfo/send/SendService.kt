package com.hansung.sherpa.sendInfo.send

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface SendService {
    /**
     * [Sherpa 내부 서버] token 값을 이용해 직접적으로 클라이언트에서 메세지를 전달하는 API
     *
     * @param request 전달할 내용. ※ 전달할 상대방의 FCM token을 필수적으로 기입해야 한다.
     */
    @POST("fcm")
    fun postSendServiceToToken(@Body request: SendRequest): Call<ResponseBody>

    /**
     * [Sherpa 내부 서버] 자신과 relation된 상대방에게 메세지를 전달하는 API
     *
     * @param userId 사용자의 userId
     * @param request 전달할 내용.
     */
    @POST("fcm/send/{userId}")
    fun postSendService(@Path("userId") userId: Int, @Body request: SendRequest): Call<ResponseBody>

    /**
     * [Sherpa 내부 서버] 안내를 시작할 경로를 생성 및 상대방에게 전달하는 API
     *
     * @param caretakerId 경로를 전송하는 주체의 Id TODO: 사용자도 전송하므로 수정해야 한다.
     * @param request body에는 수정된 transportRoute json 객체가 들어간다.
     */
    @POST("navigation/create/{caretakerId}")
    fun postCreateNavigation(@Path("caretakerId") caretakerId:Int, @Body request: String): Call<ResponseBody>

    /**
     * [Sherpa 내부 서버] 재탐색 된 경로를 보호자에게 전달하는 API
     * ※ 첫 전송은 navigation 클래스의 레코드에
     *
     * @param caretakerId 사용자(본인)의 Id
     * @param request body에는 수정된 coordParts json 객체가 들어간다.
     */
    @PATCH("navigation/route/{caretakerId}")
    fun patchUpdateRoute(@Path("caretakerId") caretakerId:Int, @Body request: String): Call<ResponseBody>

    /**
     * [Sherpa 내부 서버] 종료된 경로를 서버에서 제거하는 API 상대방에게도 종료 여부를 전달한다.
     *
     * @param userId 경로 안내를 종료한 사용자의 userId
     */
    @DELETE("delete/{userId}")
    fun deleteRoute(@Path("userId") userId:Int): Call<ResponseBody>
}