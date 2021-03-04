package com.soe.superbill.fcm

import android.app.ListActivity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.loader.content.Loader
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.soe.superbill.App.Companion.context
import com.soe.superbill.Globals.APP_PREFERENCES
import com.soe.superbill.R


class MyFirebaseMessagingService : FirebaseMessagingService() {
    private var mSettings: SharedPreferences? = null

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val data = remoteMessage.data
       sendNotification(data["title"]!!,data["message"]!!)

    }
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        sendRegistrationToServer(token)
    }
    // [END on_new_token]
    /**
     * Schedule async work using WorkManager.
     */
    private fun scheduleJob() {
        // [START dispatch_job]
        val work: OneTimeWorkRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
                .build()
        WorkManager.getInstance().beginWith(work).enqueue()
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

    override fun onCreate() {
        super.onCreate()
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
    }

    var mLoader: Loader<*>? = null

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String) {
        // TODO: Implement this method to send token to your app server.
        val editor = mSettings!!.edit()
        editor.putString(APP_PREFERENCES_TOKEN, token)
        editor.putBoolean(APP_PREFERENCES_TOKEN_SEND_STATUS, false)
        editor.apply()


    }
    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(title: String,message: String) {
        val intent = Intent(this, ListActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT)

        val channelId = "test_channel_id"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val icon = BitmapFactory.decodeResource(context!!.getResources(),
                R.drawable.ic_notification_e_svitlo)
        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notification_e_svitlo) //                        .setContentTitle(getString(R.string.fcm_message))
                .setLargeIcon(icon)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setLights(255, 1000, 1500)
                .setPriority(NotificationCompat.PRIORITY_MAX) //                        .setDefaults(Notification.DEFAULT_SOUND | Notification.FLAG_SHOW_LIGHTS)
                .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                    "test",
                    NotificationManager.IMPORTANCE_HIGH)
            channel.importance = NotificationManager.IMPORTANCE_HIGH
            channel.enableLights(true)
            channel.lightColor = Color.WHITE
            val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build()
            channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), audioAttributes)
            //            notificationBuilder.setChannelId("test_channel_id");
            notificationManager.createNotificationChannel(channel)
            val notificationManagerCompat = NotificationManagerCompat.from(this)
            notificationManagerCompat.notify(100, notificationBuilder.build())
            return
        }
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
        const val APP_PREFERENCES_TOKEN = "token"
        const val APP_PREFERENCES_TOKEN_SEND_STATUS = "token_send_status"
    }
}