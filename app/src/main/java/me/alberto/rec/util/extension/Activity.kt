package me.alberto.rec.util.extension

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.app.admin.DevicePolicyManager.*
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import me.alberto.rec.R
import me.alberto.rec.receiver.AdminReceiver
import me.alberto.rec.util.DEVICE_ADMIN_RC
import me.alberto.rec.util.READ_PHONE_STATE
import me.alberto.rec.util.getFirstLoadPermissions

fun Activity.checkAndMakeAdmin(callback: () -> Unit) {
    val deviceManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    val componentName = ComponentName(this, AdminReceiver::class.java)
    if (!deviceManager.isAdminActive(componentName)) {
        Log.d("admin", "not admin")
        val intent = Intent(ACTION_ADD_DEVICE_ADMIN).apply {
            putExtra(EXTRA_DEVICE_ADMIN, componentName)
            putExtra(EXTRA_ADD_EXPLANATION, getString(R.string.activate_admin_rationale))
        }
        startActivityForResult(intent, DEVICE_ADMIN_RC)
    } else {
        Log.d("admin", "admin active")
        callback()
    }
}


fun Activity.checkFirstLoadPermissions(callback: () -> Unit) {
    val permissions = getFirstLoadPermissions()

    if (permissions.all { ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }) {
        callback()
    } else {
        ActivityCompat.requestPermissions(this,
            *permissions.toTypedArray(),
            READ_PHONE_STATE
        )
    }


}

fun isMarshmallowPlus(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
}


fun Activity.isAdminResultOk(requestCode: Int, resultCode: Int): Boolean {
    return requestCode == DEVICE_ADMIN_RC && resultCode == Activity.RESULT_OK
}

fun Context.isFirstLoadPermissionsGranted(): Boolean {
    val permissions = getFirstLoadPermissions()
    return permissions.all {
        ContextCompat.checkSelfPermission(
            this,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }

}

fun Activity.isFirstLoadOk(requestCode: Int): Boolean {
    return requestCode == READ_PHONE_STATE && isFirstLoadPermissionsGranted()
}