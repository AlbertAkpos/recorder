package me.alberto.rec.util.extension

import android.app.Notification
import android.app.Service
import android.content.pm.ServiceInfo
import me.alberto.rec.util.isQPlus

fun Service.beginForeGround(notification: Notification, notificationId: Int) {
    if (isQPlus()) {
        startForeground(
            notificationId,
            notification,
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MANIFEST
        )
    } else {
        startForeground(notificationId, notification)
    }
}