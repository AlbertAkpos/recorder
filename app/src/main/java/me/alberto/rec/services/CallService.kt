package me.alberto.rec.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaRecorder
import android.os.IBinder
import android.telephony.TelephonyManager
import android.util.Log
import me.alberto.rec.util.extension.beginForeGround
import me.alberto.rec.util.extension.getTelephonyManager
import me.alberto.rec.util.helper.NotificationHelper
import me.alberto.rec.util.helper.RecordeHelper
import me.alberto.rec.util.isOreoPlus

class CallService : Service() {
    private val phoneStateListener = PhoneStateListener()
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "service started")
        registerCallListener()
        val notificationHelper = NotificationHelper(this)
        beginForeGround(
            notificationHelper.createForegroundNotif(NotificationHelper.FOREGROUND_REC_CHANNEL),
            NotificationHelper.FOREGROUND_REC_NOTIF_ID,
        )

    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onstart command")
        return START_STICKY
    }

    private fun registerCallListener() {
        Log.d(TAG, "call listener registered")
        getTelephonyManager().listen(
            phoneStateListener,
            android.telephony.PhoneStateListener.LISTEN_CALL_STATE
        )
    }


    inner class PhoneStateListener : android.telephony.PhoneStateListener() {
        private var wasRinging = false
        private var recording = false
        private val recorderHelper = RecordeHelper(this@CallService)
        override fun onCallStateChanged(state: Int, phoneNumber: String?) {
            super.onCallStateChanged(state, phoneNumber)
            when (state) {
                TelephonyManager.CALL_STATE_RINGING -> {
                    if (!wasRinging) {
                        Log.d(TAG, "new incoming call")
                    } else {
                        Log.d(TAG, "another call")
                    }

                    wasRinging = true
                }
                TelephonyManager.CALL_STATE_OFFHOOK -> {
                    if (!wasRinging) {
                        Log.d(TAG, "New outgoing call")
                        startRecording(phoneNumber ?: "")
                    } else {
                        Log.d(TAG, "call answered. Phone; $phoneNumber")
                        startRecording(phoneNumber ?: "")
                    }

                    Log.d(TAG, "off hook")

                    wasRinging = true
                }

                TelephonyManager.CALL_STATE_IDLE -> {
                    if (wasRinging && recording) {
                        Log.d(TAG, "call ended with recording")
                        stopRecording()
                        stopSelf()
                    } else if (wasRinging && !recording) {
                        Log.d(TAG, "Call rejected")
                        stopSelf()
                    }

                    Log.d(TAG, "idle")


                    wasRinging = false
                }
            }
        }

        private fun startRecording(phoneNumber: String) {
            recorderHelper.startRecording(phoneNumber)
            recording = true
        }

        private fun stopRecording() {
            recorderHelper.stopRecording()
            recording = false
        }
    }

    override fun onDestroy() {
        Log.d(TAG, "service destroyed")
        getTelephonyManager().listen(
            phoneStateListener,
            android.telephony.PhoneStateListener.LISTEN_NONE
        )
        super.onDestroy()
    }

    companion object {
        private val TAG = "CallService"
        fun startService(context: Context) {
            val intent = Intent(context, CallService::class.java)
            if (isOreoPlus()) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

    }
}