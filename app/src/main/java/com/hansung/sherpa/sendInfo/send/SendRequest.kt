package com.hansung.sherpa.sendInfo.send

/**
 * [Sherpa 내부 서버] FCM을 이용한 메세지 전달을 위한 클래스
 *
 * @param title 전송할 메세지 제목 ※ topic/title 준수할 것
 * @param body 전송할 메세지 내용
 * @param token 전송할 토큰 값
 */
class SendRequest (
    title:String, body: String, token:String = "None"
) {
    val message: Message

    /**
     * 객체 매핑
     */
    init {
        val notification = Notification(title,body)
        message = Message(token, notification)
    }
}

/**
 * 전송 메세지 클래스
 * 
 * @property token 전송할 클라이언트의 FCM token 값
 * @property notification 전송할 메세지 내용
 */
data class Message(
    val token:String,
    val notification: Notification
)

/**
 * 전송할 메세지 내용
 * 
 * @property title 메세지 제목
 * @property body 메세지 내용
 */
data class Notification(
    val title:String,
    val body:String
)
