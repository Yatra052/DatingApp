package com.example.datingapp.Model

data class MessageModel(
    val senderId: String ?= " ",
    val message: String ?= "",
    val currentTime: String ?= "",
    val currentDate: String ?=""
)
