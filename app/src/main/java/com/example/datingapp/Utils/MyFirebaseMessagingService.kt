package com.example.datingapp.Utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.datingapp.MainActivity
import com.example.datingapp.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService: FirebaseMessagingService(){

    private val channelId = "datingapp"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val inte = Intent(this, MainActivity::class.java)
        inte.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE)
        createNotificationChannel(manager as NotificationManager)


        val inte1 = PendingIntent.getActivities(this, 0, arrayOf(inte), PendingIntent.FLAG_IMMUTABLE)


        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setAutoCancel(true)
            .setContentIntent(inte1)
            .build()



        manager.notify(Random.nextInt(), notification)




    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(manager: NotificationManager) {


        val channel =  NotificationChannel(channelId, "datingappchat",
            NotificationManager.IMPORTANCE_HIGH
            )


        channel.description = "New Chat"
        channel.enableLights(true)


      manager.createNotificationChannel(channel)
    }

}