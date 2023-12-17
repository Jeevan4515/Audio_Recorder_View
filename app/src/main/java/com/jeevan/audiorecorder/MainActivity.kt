package com.jeevan.audiorecorder

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jeevan.audiorecorderview.AudioRecorderView

class MainActivity : AppCompatActivity() {

    private val requiredPermissions = arrayOf(android.Manifest.permission.RECORD_AUDIO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestAudioPermission()

        var audioRecorderView : AudioRecorderView = findViewById(R.id.recorder_view)
        audioRecorderView.setPath("${externalCacheDir?.absolutePath}/recording.mp3")

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val audioRecordPermissionGranted =
            requestCode == REQUEST_RECORD_AUDIO_PERMISSION &&
                    grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED

        if (!audioRecordPermissionGranted) {
            finish()
        }
    }


    private fun requestAudioPermission() {
        requestPermissions(requiredPermissions, REQUEST_RECORD_AUDIO_PERMISSION)
    }


    companion object {
        val REQUEST_RECORD_AUDIO_PERMISSION = 201
    }

}