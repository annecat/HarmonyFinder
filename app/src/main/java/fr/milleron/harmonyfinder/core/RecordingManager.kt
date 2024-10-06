package fr.milleron.harmonyfinder.core

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import androidx.core.content.ContextCompat


class RecordingManager(private val context: Context) {
    private var mediaRecorder: MediaRecorder? = null
    private var outputFile: String = ""

    fun startRecording() {
        outputFile = "${context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)}/recording.3gp"

        mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context) // API level 31 and above
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder() // API level below 31
        }.apply {  // Updated to use the new constructor
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(outputFile)
            prepare()
            start()
        }
    }

    fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            reset()
            release()
        }
        mediaRecorder = null
    }

    fun hasAudioPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context, Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }
}