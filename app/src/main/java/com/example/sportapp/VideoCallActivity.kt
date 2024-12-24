package com.example.sportapp

import android.os.Bundle
import android.widget.Toast
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.video.VideoCanvas


class VideoCallActivity : AppCompatActivity() {
    private lateinit var rtcEngine: RtcEngine
    private val eventHandler = object : IRtcEngineEventHandler() {
        override fun onUserJoined(uid: Int, elapsed: Int) {
            runOnUiThread {
                // Додаємо відео іншого користувача
                val remoteContainer = findViewById<FrameLayout>(R.id.remote_video_container)
                val remoteView = RtcEngine.CreateRendererView(this@VideoCallActivity)
                remoteContainer.addView(remoteView)
                rtcEngine.setupRemoteVideo(VideoCanvas(remoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid))
            }
        }

        override fun onUserOffline(uid: Int, reason: Int) {
            runOnUiThread {
                // Видаляємо відео іншого користувача
                val remoteContainer = findViewById<FrameLayout>(R.id.remote_video_container)
                remoteContainer.removeAllViews()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_call)

        val username = intent.getStringExtra("USERNAME")
        if (username.isNullOrEmpty()) {
            Toast.makeText(this, "Канал не задано", Toast.LENGTH_SHORT).show()
            finish()
        }

        try {
            rtcEngine = RtcEngine.create(baseContext, "31bdfbc0817442ccb7eb292a52db67dc", eventHandler)
            rtcEngine.enableVideo()

            val localContainer = findViewById<FrameLayout>(R.id.local_video_container)
            val localView = RtcEngine.CreateRendererView(this)
            localContainer.addView(localView)
            rtcEngine.setupLocalVideo(VideoCanvas(localView, VideoCanvas.RENDER_MODE_HIDDEN, 0))

            rtcEngine.joinChannel(null, username, null, 0)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Помилка ініціалізації відеозв'язку", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        rtcEngine.leaveChannel()
        RtcEngine.destroy()
    }
}

