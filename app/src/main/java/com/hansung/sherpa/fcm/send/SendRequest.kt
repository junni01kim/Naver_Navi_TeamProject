package com.hansung.sherpa.fcm.send

class SendRequest (
    token:String, title:String, body: String
) {
    val message:Message

    init {
        val notification = Notification(title,body)
        message = Message(token, notification)
    }
}

data class Message(
    val token:String,
    val notification:Notification
)

data class Notification(
    val title:String,
    val body:String
)