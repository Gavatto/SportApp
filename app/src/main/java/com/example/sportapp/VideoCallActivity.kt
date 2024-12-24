package com.example.sportapp

import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.video.VideoCanvas

class VideoCallActivity : AppCompatActivity() {
    private lateinit var rtcEngine: RtcEngine
    private lateinit var audioManager: AudioManager
    private val eventHandler = object : IRtcEngineEventHandler() {
        override fun onUserJoined(uid: Int, elapsed: Int) {
            runOnUiThread {
                val remoteContainer = findViewById<FrameLayout>(R.id.remote_video_container)
                val remoteView = RtcEngine.CreateRendererView(this@VideoCallActivity)
                remoteContainer.addView(remoteView)
                rtcEngine.setupRemoteVideo(VideoCanvas(remoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid))
            }
        }

        override fun onUserOffline(uid: Int, reason: Int) {
            runOnUiThread {
                val remoteContainer = findViewById<FrameLayout>(R.id.remote_video_container)
                remoteContainer.removeAllViews()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_call)

        // Отримання даних про користувача
        val username = intent.getStringExtra("USERNAME")
        if (username.isNullOrEmpty()) {
            Toast.makeText(this, "Канал не задано", Toast.LENGTH_SHORT).show()
            finish()
        }

        try {
            // Ініціалізація Agora SDK
            rtcEngine = RtcEngine.create(baseContext, "31bdfbc0817442ccb7eb292a52db67dc", eventHandler)
            rtcEngine.enableVideo()

            // Встановлення локального відео
            val localContainer = findViewById<FrameLayout>(R.id.local_video_container)
            val localView = RtcEngine.CreateRendererView(this)
            localContainer.addView(localView)
            rtcEngine.setupLocalVideo(VideoCanvas(localView, VideoCanvas.RENDER_MODE_HIDDEN, 0))

            // Підключення до каналу
            rtcEngine.joinChannel(null, username, null, 0)

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Помилка ініціалізації відеозв'язку", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Ініціалізація кнопки завершення дзвінка
        val endCallButton = findViewById<Button>(R.id.endCallButton)
        endCallButton.setOnClickListener {
            endCall()
        }

        // Ініціалізація мікшера гучності
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        val volumeControl = findViewById<SeekBar>(R.id.volumeSeekBar)
        volumeControl.max = audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL)
        volumeControl.progress = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL)

        volumeControl.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, progress, 0)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    private fun endCall() {
        rtcEngine.leaveChannel()
        RtcEngine.destroy()

        // Перехід на сторінку CaloriesActivity
        val intent = Intent(this, CaloriesActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        rtcEngine.leaveChannel()
        RtcEngine.destroy()
    }
}
