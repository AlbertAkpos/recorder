package me.alberto.rec.util.helper

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import me.alberto.rec.R
import me.alberto.rec.util.isOreoPlus

class NotificationHelper(context: Context) : ContextWrapper(context) {

    private val TAG = "NotificationHelper"

    private val smallIcon: Int
        get() = R.mipmap.ic_launcher

    init {
        createNotificationChannel(FOREGROUND_REC_CHANNEL, getString(R.string.foreground_channel))
    }


    private fun createNotificationChannel(
        channelId: String,
        channelName: String,
        silent: Boolean = true
    ) {
        if (silent) {
            createSilentNotificationChannel(channelId, channelName)
        }
    }

    fun createForegroundNotif(channelId: String): Notification {
        val builder = NotificationCompat.Builder(applicationContext, channelId)
        builder.apply {
            setSmallIcon(smallIcon)
            setCategory(Notification.CATEGORY_SERVICE)
        }
        return builder.build()
    }

    fun createNotification(
        title: String,
        body: String? = null,
        channelId: String,
        pendingIntent: PendingIntent? = null,
        silent: Boolean = true
    ): NotificationCompat.Builder {
        val builder = NotificationCompat.Builder(applicationContext, channelId)
        //Support for lower api devices
        builder.setContentTitle(title)
        builder.setContentText(body)
        val bigStyle = NotificationCompat.BigTextStyle()
        bigStyle.setBigContentTitle(title)
        body?.let { bigStyle.bigText(it) }

        builder.setSmallIcon(smallIcon)
        //TODO check out how to use Glide to load imgs as large icons
        builder.setStyle(bigStyle)
        builder.setAutoCancel(true)
        builder.color = ContextCompat.getColor(applicationContext, R.color.purple_200)
        pendingIntent?.let { builder.setContentIntent(pendingIntent) }
        builder.priority =
            if (silent) NotificationCompat.PRIORITY_LOW else NotificationCompat.PRIORITY_HIGH
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        return builder
    }


    private fun createSilentNotificationChannel(channelId: String, channelName: String) {
        if (isOreoPlus()) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
            channel.lightColor = resources.getColor(R.color.purple_200)
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            getManager().createNotificationChannel(channel)
            Log.d(TAG, "Channel created")
        }
    }

    private fun getManager(): NotificationManagerCompat {
        return NotificationManagerCompat.from(applicationContext)
    }

    companion object {
        const val FOREGROUND_REC_CHANNEL = "forground_recording_channel"
        const val FOREGROUND_REC_NOTIF_ID = 103
    }
}