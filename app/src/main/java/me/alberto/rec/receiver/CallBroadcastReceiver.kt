package me.alberto.rec.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import me.alberto.rec.services.CallService

class CallBroadcastReceiver: BroadcastReceiver() {
    private val TAG = "CallBroadcastReceiver"
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action){
            ACTION_IN -> {
                context?.let { CallService.startService(context) }
            }

        }

    }


    companion object {
        const val RINGING = 1
        const val ANSWERED =2
        const val  OUTGOING = 3

        const val ACTION_IN = "android.intent.action.PHONE_STATE"
        const val ACTIION_OUT = "android.intent.action.NEW_OUTGOING_CALL"
    }


}



