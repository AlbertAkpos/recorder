package me.alberto.rec.util.helper

import android.content.Context
import android.media.AudioManager
import android.media.MediaRecorder
import android.os.Environment
import android.util.Log
import me.alberto.rec.util.DATE_FORMAT
import me.alberto.rec.util.DIRECTORY
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class RecordeHelper(private val context: Context) {
    private val mediaRecorder = MediaRecorder()
    private val TAG = "RecordeHelper"

    fun startRecording(contactName: String) {
        val date = SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(Date())
        val directory =
            File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), DIRECTORY)
        if (!directory.exists()) {
            directory.mkdir()
        }

        val fileName = "$contactName $date"
        try {
            val audioFile = File.createTempFile(fileName, ".mp3", directory)
            mediaRecorder.apply {
                setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setOutputFile(audioFile.absolutePath)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            }

            mediaRecorder.prepare()

            mediaRecorder.start()
            Log.d(TAG, "Recording started")

        } catch (exp: Exception) {
            exp.printStackTrace()
        }


    }

    fun stopRecording() {
        try {
            mediaRecorder.stop()
            mediaRecorder.release()
        } catch (exp: Exception) {
            exp.printStackTrace()
        }
    }
}