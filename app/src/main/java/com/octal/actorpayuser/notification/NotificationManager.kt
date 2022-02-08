package com.octal.actorpayuser.notification

import android.app.*
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
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
import com.octal.actorpayuser.MainActivity
import com.octal.actorpayuser.R
import com.octal.actorpayuser.database.datastore.DataStoreBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.io.IOException


class NotificationManager : FirebaseMessagingService() {

    private var title: String? = null
    private var type: String? = null
    private var body: String? = null

    private var token: String? = null

    private val dateStore: DataStoreBase by inject()
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)


    override fun onNewToken(s: String) {
        super.onNewToken(s)
        token = s
       /* if(sharedPre==null){
            sharedPre= SharedPre.getInstance(this)!!
        }
        sharedPre.setFirebaseToken(s)*/
        Log.e("Firebase Messaging", "onNewToken: $token", )

        scope.launch {
        dateStore.setDeviceToken(s)
        }

    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        /*if(sharedPre==null){
            sharedPre= SharedPre.getInstance(this)!!
        }*/
        scope.launch {
                    dateStore.isLoggedIn().collect {
                        isLogin->

                        super.onMessageReceived(remoteMessage)
                        try {

                            title = getString(R.string.app_name)
                            NOTIFICATION_ID = System.currentTimeMillis()
                                .toInt()
                            type = remoteMessage.notification!!.title
                            body = remoteMessage.notification!!.body

                            showNotification(
                                type,
                                body,
                                Intent(this@NotificationManager, MainActivity::class.java)
                            )
                        } catch (e: Exception) {
                            Log.e("error in Notification", e.message.toString())

                        }
                    }
        }
    }



    fun showNotification(title: String?, body: String?, intent: Intent?) {
        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addParentStack(MainActivity::class.java)
        stackBuilder.addNextIntent(intent)
        val pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT)
        // PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //to be able to launch your activity from the notification
       val builder =
            NotificationCompat.Builder(this, getString(R.string.app_name))
        builder.setSmallIcon(R.mipmap.ic_launcher)

        builder.setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setColorized(true)
            .setAutoCancel(true)
            .setSound(null)
            .setContentIntent(pendingIntent)
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
//            val notificationManager = NotificationManagerCompat.from(this)
            val notificationCompat = builder.build()
            val managerCompat = NotificationManagerCompat.from(this)
            managerCompat.notify(NOTIFICATION_ID, notificationCompat)
        }
        scope.launch {
            dateStore.getNotificationMuted().collect { isNotificationMuted ->
                dateStore.getNotificationSound().collect {
                    notificationSound->
                    if (isNotificationMuted)
                        playSound(this@NotificationManager,notificationSound)
                }
            }
        }
    }

    fun playSound(context: Context,notificationSound:String) {


        val mediaPlayer = MediaPlayer()
        try {
//            mediaPlayer.setDataSource(context, uri!!)
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