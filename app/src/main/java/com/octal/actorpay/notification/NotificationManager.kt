package com.octal.actorpay.notification

import android.app.*
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.octal.actorpay.MainActivity
import com.octal.actorpay.R
import com.octal.actorpay.database.prefrence.SharedPre
import java.io.IOException


class NotificationManager : FirebaseMessagingService() {

    lateinit var sharedPre: SharedPre
    private var isMuted = false
    private var SendNotification = true
    private var uri: Uri? = null
    private var title: String? = null
    private var type: String? = null
    private var body: String? = null

    private var intent: Intent? = null
    private var token: String? = null
    override fun onNewToken(s: String) {
        super.onNewToken(s)
        token = s
        if(sharedPre==null){
            sharedPre= SharedPre.getInstance(this)!!
        }
        sharedPre.setFirebaseToken(s)

    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if(sharedPre==null){
            sharedPre= SharedPre.getInstance(this)!!
        }
        if (remoteMessage != null) {
            isMuted = sharedPre!!.isNotificationMuted
            try {
                uri = if (isMuted) {
                    null
                } else {
                    val sound = sharedPre!!.notificationSound
                    if (sound == null || sound == "") {
                        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                    } else {
                        Uri.parse(sharedPre!!.notificationSound)
                    }
                }
            } catch (e: Exception) {
                showNotification("ActorPay","Message in Catch Please check first",Intent(this,MainActivity::class.java))
            }
            super.onMessageReceived(remoteMessage)
            try {
                SendNotification = sharedPre!!.isLoggedIn
                title = getString(R.string.app_name)
                NOTIFICATION_ID = System.currentTimeMillis()
                    .toInt()
                type = remoteMessage.notification!!.title
                body = remoteMessage.notification!!.body

                if (SendNotification) {

                }else{

                }
                /* Map<String, String> map = remoteMessage.getData();
                    handleDataMessage(map);*/
            } catch (e: Exception) {
                Log.e("error in Notification",e.message.toString())

            }
        }
    }

    private fun handleDataMessage(json: Map<String, String>?) {
        Log.e(getString(R.string.app_name), "push json: " + json.toString())
        try {
            if (json != null) {
                val title = json["title"]
                val message = json["body"]
                // String badge = json.get("badge");
                if (SendNotification) {
                    //intent = Intent(this, HomeActivity::class.java)
                    showNotification(title, message, intent)
                }
            }
        } catch (e: Exception) {
            Log.e(getString(R.string.app_name), "Exception: " + e.message)
        }
    }

    fun showNotification(title: String?, body: String?, intent: Intent?) {
        val logo: Bitmap
        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addParentStack(MainActivity::class.java)
        stackBuilder.addNextIntent(intent)
        val pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT)
        // PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //to be able to launch your activity from the notification
       val builder =
            NotificationCompat.Builder(this, getString(R.string.app_name))
        builder.setSmallIcon(R.mipmap.ic_launcher)

        logo = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        builder.setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setColorized(true)
            .setAutoCancel(true)
            .setSound(null)
            .setNotificationSilent()
            .setContentIntent(pendingIntent)
            .setPriority(Notification.PRIORITY_DEFAULT)
            .setDefaults(Notification.BADGE_ICON_LARGE)
            .setLights(1, 1, 1)
            .setOngoing(false)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = getString(R.string.app_name)
            val description = getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(getString(R.string.app_name), name, importance)
            channel.description = description
            channel.enableVibration(true)
            channel.setSound(null, null)
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
            notificationManager.notify(NOTIFICATION_ID, builder.build())
        } else {
            val notificationManager = NotificationManagerCompat.from(this)
            val notificationCompat = builder.build()
            val managerCompat = NotificationManagerCompat.from(this)
            managerCompat.notify(NOTIFICATION_ID, notificationCompat)
        }
        if (!sharedPre.isNotificationMuted) {
            playSound(this)
        }
    }

    fun playSound(context: Context) {
        val myAudioManager = context.getSystemService(AUDIO_SERVICE) as AudioManager
        val i = myAudioManager.ringerMode
        if (myAudioManager.ringerMode == AudioManager.RINGER_MODE_VIBRATE) uri = Uri.parse(
            sharedPre!!.notificationSound
        )
        if (sharedPre!!.notificationSound == null || sharedPre!!.notificationSound == "") {
            uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }
        val mediaPlayer = MediaPlayer()
        try {
            mediaPlayer.setDataSource(context, uri!!)
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION)
            mediaPlayer.prepare()
            mediaPlayer.setOnCompletionListener { mp -> mp.release() }
            mediaPlayer.start()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        private var NOTIFICATION_ID = 3
    }

}