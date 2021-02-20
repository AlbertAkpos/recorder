package me.alberto.rec

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import me.alberto.rec.services.CallService
import me.alberto.rec.util.extension.checkFirstLoadPermissions
import me.alberto.rec.util.extension.isFirstLoadOk

class HomeActivity : AppCompatActivity() {
    private val TAG = "HomeActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkFirstLoadPermissions { startCallService() }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (isFirstLoadOk(requestCode)) {
            startCallService()
        }
    }

    private fun startCallService() {
        Log.d(TAG, "start call service")
        CallService.startService(applicationContext)
    }
}