package me.alberto.rec.util

import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import androidx.core.content.ContextCompat.getSystemService

const val DEVICE_ADMIN_RC = 110
const val READ_PHONE_STATE = 111
const val DATE_FORMAT = "dd-MM-yyyy hh-mm-ss"
const val DURATION_FORMAT = "mm-ss"
const val DIRECTORY = "/Rec"


fun getFirstLoadPermissions(): ArrayList<String> {
    val permissions = arrayListOf(
        android.Manifest.permission.READ_PHONE_STATE,
        android.Manifest.permission.RECORD_AUDIO,
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    return permissions
}

fun isPiePlus(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

fun isOreoPlus(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

fun isQPlus(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q




