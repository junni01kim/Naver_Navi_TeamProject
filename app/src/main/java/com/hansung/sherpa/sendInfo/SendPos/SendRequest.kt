package com.hansung.sherpa.sendInfo.SendPos

class SendRequest (
    title:String, body: String, token:String = "None"
) {
    val message: Message

    init {
        val notification = Notification(title,body)
        message = Message(token, notification)
    }
}

data class Message(
    val token:String,
    val notification: Notification
)

data class Notification(
    val title:String,
    val body:String
)
