package me.alberto.rec.util.extension

import android.content.Context
import android.telephony.TelephonyManager

fun Context.getTelephonyManager(): TelephonyManager {
    return getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
}