package me.alberto.rec.listener

import android.telephony.TelephonyManager
import android.util.Log
import me.alberto.rec.util.helper.RecordeHelper

class PhoneStateListener(private val recorderHelper: RecordeHelper) :
    android.telephony.PhoneStateListener() {
    private var wasRinging = false
    private var recording = false
    private val TAG = "PhoneStateListener"


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
                } else {
                    Log.d(TAG, "call answered. Phone; $phoneNumber")
                    recorderHelper.startRecording(phoneNumber ?: "")
                    recording = true
                }

                Log.d(TAG, "off hook")

                wasRinging = true
            }

            TelephonyManager.CALL_STATE_IDLE -> {
                if (wasRinging && recording) {
                    Log.d(TAG, "call ended with recording")
                    recorderHelper.stopRecording()
                    recording = false

                } else if (wasRinging && !recording) {
                    Log.d(TAG, "Call rejected")
                }

                Log.d(TAG, "idle")


                wasRinging = false
            }
        }
    }
}