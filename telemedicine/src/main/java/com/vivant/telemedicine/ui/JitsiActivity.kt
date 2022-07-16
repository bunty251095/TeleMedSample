package com.vivant.telemedicine.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.vivant.telemedicine.common.Constants
import com.vivant.telemedicine.common.NavigationConstants
import com.vivant.telemedicine.common.Utilities
import com.vivant.telemedicine.common.openAnotherActivity
import com.vivant.telemedicine.databinding.ActivityJitsiBinding
import org.jitsi.meet.sdk.*
import java.net.MalformedURLException
import java.net.URL

class JitsiActivity : JitsiMeetActivity() {

    private lateinit var binding: ActivityJitsiBinding

    private var mode = ""
    private var roomName = ""

    private val options = JitsiMeetConferenceOptions.Builder()

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            onBroadcastReceived(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJitsiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            if (intent.hasExtra(Constants.MODE)) {
                mode = intent.getStringExtra(Constants.MODE)!!
                Utilities.printLogError("Mode--->$mode")
            }
            if (intent.hasExtra(Constants.ROOM_NAME)) {
                roomName = intent.getStringExtra(Constants.ROOM_NAME)!!
                Utilities.printLogError("RoomName--->$roomName")
            }
            //supportActionBar!!.hide()
            initialiseJitsi()
            registerForBroadcastMessages()
            //launchAppointment(roomName)
            when(mode) {
                Constants.VIDEO_CALL -> {
                    launchAppointmentVideo(roomName)
                }
                Constants.AUDIO_CALL -> {
                    launchAppointmentAudio(roomName)
                }
                Constants.TEXT_CHAT -> {
                    launchAppointmentChat(roomName)
                }
            }
        } catch ( e : Exception ) {
            e.printStackTrace()
        }
    }

    private fun initialiseJitsi() {
        // Initialize default options for Jitsi Meet conferences.
        val serverURL: URL = try {
            // When using JaaS, replace "https://meet.jit.si" with the proper serverURL
            URL(Constants.JITSI_URL)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            throw RuntimeException("Invalid server URL!")
        }
        val defaultOptions = JitsiMeetConferenceOptions.Builder()
            .setServerURL(serverURL)
            // When using JaaS, set the obtained JWT here
            //.setToken("MyJWT")
            // Different features flags can be set
            //.setFeatureFlag("toolbox.enabled", false)
            //.setFeatureFlag("filmstrip.enabled", false)
            .setFeatureFlag("welcomepage.enabled", false)
            .setFeatureFlag("pip.enabled", true) // <- this line you have to add
            .setFeatureFlag("calendar.enabled", false)
            .setFeatureFlag("call-integration.enabled", false)
            .setFeatureFlag("conference-timer.enabled", true)
            .setFeatureFlag("close-captions.enabled", false)
            .setFeatureFlag("chat.enabled", true)
            .setFeatureFlag("invite.enabled", false)
            .setFeatureFlag("live-streaming.enabled", false)
            .setFeatureFlag("meeting-name.enabled", false)
            .setFeatureFlag("meeting-password.enabled", false)
            .setFeatureFlag("raise-hand.enabled", false)
            .setFeatureFlag("video-share.enabled", false)
            .setFeatureFlag("overflow-menu.enabled", false)
            .build()
        JitsiMeet.setDefaultConferenceOptions(defaultOptions)
    }

    private fun registerForBroadcastMessages() {
        val intentFilter = IntentFilter()
        for (type in BroadcastEvent.Type.values()) {
            intentFilter.addAction(type.action)
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter)
    }

    private fun launchAppointment(room:String) {
        if (room.isNotEmpty()) {
            launch(this, options.setRoom(room).build())
        }
    }

    private fun launchAppointmentVideo(room:String) {
        if (room.isNotEmpty()) {
            options.setRoom(room)
            options.setFeatureFlag("audio-mute.enabled",true)
            options.setFeatureFlag("chat.enabled",true)
            launch(this, options.build())
        }
    }

    private fun launchAppointmentAudio(room:String) {
        if (room.isNotEmpty()) {
            options.setRoom(room)
            options.setVideoMuted(true)
            options.setFeatureFlag("video-mute.enabled",false)
            options.setFeatureFlag("chat.enabled",true)
            launch(this, options.build())
        }
    }

    private fun launchAppointmentChat(room:String) {
        if (room.isNotEmpty()) {
            options.setRoom(room)
            options.setVideoMuted(true)
            options.setFeatureFlag("audio-mute.enabled",false)
            options.setFeatureFlag("video-mute.enabled",false)
            launch(this, options.build())
        }
    }

    // Example for handling different JitsiMeetSDK events
    private fun onBroadcastReceived(intent: Intent?) {
        try {
            if (intent != null) {
                val event = BroadcastEvent(intent)
                Utilities.printLogError("Received event--->${event.type}")
                //Utilities.printData("EventData",event.data,true)
                when(event.type) {
                    BroadcastEvent.Type.PARTICIPANT_JOINED -> {

                    }
                    BroadcastEvent.Type.PARTICIPANT_LEFT -> {

                    }
                    BroadcastEvent.Type.READY_TO_CLOSE,BroadcastEvent.Type.CONFERENCE_TERMINATED -> {
                        openAnotherActivity(destination = NavigationConstants.HOME, clearTop = true)
                    }
                    else ->{}
                }
            }
        } catch ( e : Exception ) {
            e.printStackTrace()
        }
    }

    // Example for sending actions to JitsiMeetSDK
    private fun hangUp() {
        val hangupBroadcastIntent: Intent = BroadcastIntentHelper.buildHangUpIntent()
        LocalBroadcastManager.getInstance(this.applicationContext).sendBroadcast(hangupBroadcastIntent)
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }

}
