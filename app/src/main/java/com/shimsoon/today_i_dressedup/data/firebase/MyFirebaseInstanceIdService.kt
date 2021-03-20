package com.shimsoon.today_i_dressedup.data.firebase

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.shimsoon.today_i_dressedup.R
import com.shimsoon.today_i_dressedup.ui.myPage.MyPageActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseInstanceIdService : FirebaseMessagingService() {

    private val TAG = "FirebaseService"

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)
        Log.d(TAG, p0)
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        Log.d("onMessageReceived", p0.data.get("postId"))
        sendNotification(p0.notification?.body)
    }

    private fun sendNotification(body: String?) {

        // Create an Intent for the activity you want to start
        val intent = Intent(this, MyPageActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        // Create the TaskStackBuilder
        val pendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            // Add the intent, which inflates the back stack
            addNextIntentWithParentStack(intent)
            // Get the PendingIntent containing the entire back stack
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        var notificationBuilder = NotificationCompat.Builder(this, "Notification")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Push Notification FCM")
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(notificationSound)
            .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(this)) {
            notify(0, notificationBuilder.build())
        }
    }
}