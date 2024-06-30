package com.example.datingapp.Notification.api

import com.example.datingapp.Notification.PushNotification
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {

    @Headers
        ("Content-Type:application/json",
        "Authorization:key = AAAAKtISeqI:APA91bHLuqb4oqo_LBLYaRem1LiREprIIAcoNw2okU94qaoqSpZJRBCUSqGfbTmh9Y2hAe6f0-Zu74S_fobkX-qJ3xX9CLBx7oaAwPVuss0viyf36whCunjbaQ3vZf1XL7UAa8wbGJZL")

    @POST("fcm/send")
    fun sendNotification(@Body notification: PushNotification)
    : Call<PushNotification>



}