package com.messaging.spik.models

// Class des messages
class Message(val id: String, val text: String, val fromId: String, val toId: String) {
    constructor(): this("", "", "", "")
}