package com.jeevan.audiorecorderview

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Handler
import android.os.SystemClock
import android.util.AttributeSet
import android.widget.Button
import android.widget.Chronometer
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import java.io.IOException

class AudioRecorderView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    private val soundVisualizerView: SoundVisualizerView
    private val chronometerStart: Chronometer
    private val chronometer: Chronometer
    private val resetButton: Button
    private val recordButton: RecordButton

    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private var isRecording = false
    private var isPlaying = false
    private var startTime: Long = 0
    private val handler = Handler()
    private var state = State.BEFORE_RECORDING
        set(value) {
            field = value

            resetButton.isEnabled = (value == State.AFTER_RECORDING) ||
                    (value== State.ON_PLAYING)
            recordButton.updateIconWithState(field)
        }
    private  var recordingFilePath: String = ""


    init {
        inflate(context, R.layout.recorder_view, this)

        soundVisualizerView = findViewById(R.id.soundVisualizerView)
        chronometerStart = findViewById(R.id.start_length)
        chronometer = findViewById(R.id.total_length)
        resetButton = findViewById(R.id.restButton)
        recordButton = findViewById(R.id.recordButton)

        initView()

        initVariables()

        bindViews()
    }

    private fun bindViews() {

        soundVisualizerView.onRequestCurrentAmplitude = {
            recorder?.maxAmplitude?:0
        }

        resetButton.setOnClickListener {

            stopPlaying()
            soundVisualizerView.clearVisualization()
            chronometerStart.base = SystemClock.elapsedRealtime()
            chronometerStart.stop()
            chronometer.text = chronometerStart.text
            state = State.BEFORE_RECORDING
        }

        recordButton.setOnClickListener {
            // Your record button logic here
            when (state) {
                State.BEFORE_RECORDING -> startRecording()
                State.ON_RECORDING -> stopRecording()
                State.AFTER_RECORDING -> startPlaying()
                State.ON_PLAYING -> stopPlaying()
            }
        }

    }



    private fun startRecording() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(recordingFilePath)
            prepare()
        }

        try {
            recorder?.start()
            startTime = SystemClock.elapsedRealtime()
            isRecording = true
            chronometer.text = " "
            chronometerStart.base = startTime
            chronometerStart.start()
            soundVisualizerView.startVisualizing(false)
            state = State.ON_RECORDING
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }



    private fun stopRecording() {
        recorder?.stop()
        recorder?.release()
        recorder = null

        isRecording = false

        chronometerStart.stop()
        val cal = SystemClock.elapsedRealtime() - startTime
        val minutes = (cal / 1000 / 60)
        val seconds = ((cal / 1000) % 60)
        val formattedTime = String.format("%02d:%02d", minutes, seconds)
        chronometer.text = formattedTime
        chronometerStart.text =" "
        handler.removeCallbacksAndMessages(null)

        soundVisualizerView.stopVisualizing()
        state = State.AFTER_RECORDING
    }



    private fun startPlaying() {

        player = MediaPlayer().apply {
            setDataSource(recordingFilePath)
            prepare()
        }
        player?.setOnCompletionListener {
            stopPlaying()
            state = State.AFTER_RECORDING
        }

        isPlaying = true
        player?.start()
        val handler = Handler()
        handler.post(object : Runnable {
            override fun run() {
                val currentPosition = player?.currentPosition ?: 0
                val minutes = currentPosition / 1000 / 60
                val seconds = (currentPosition / 1000) % 60
                val formattedTime = String.format("%02d:%02d", minutes, seconds)
                chronometerStart.text = formattedTime

                if (isPlaying) {
                    handler.postDelayed(this, 1000)
                }
            }
        })

        state = State.ON_PLAYING
        soundVisualizerView.startVisualizing(true)

    }


    private fun stopPlaying() {
        player?.release()
        chronometerStart.stop()
        player = null
        isPlaying = false
        soundVisualizerView.stopVisualizing()
        handler.removeCallbacksAndMessages(null)
        state = State.AFTER_RECORDING
    }

    private fun initVariables(){
        state = State.BEFORE_RECORDING
    }

    private fun initView() {
        recordButton.updateIconWithState(state)
    }

    fun setPath(absolutePath: String){
        recordingFilePath = absolutePath

    }

}

//Record Button
class RecordButton(
    context:Context,
    attrs: AttributeSet
): AppCompatImageButton(context, attrs) {

    init {
        setBackgroundResource(R.drawable.shape_oval_button)
    }

    fun updateIconWithState(state: State){
        when (state){
            State.BEFORE_RECORDING -> {
                setImageResource(R.drawable.icon_mic)
            }
            State.ON_RECORDING -> { setImageResource(R.drawable.icon_stop)}
            State.AFTER_RECORDING -> {setImageResource(R.drawable.icon_play)}
            State.ON_PLAYING -> {setImageResource(R.drawable.icon_stop)}
        }
    }
}

//States
enum class State {
    BEFORE_RECORDING,
    ON_RECORDING,
    AFTER_RECORDING,
    ON_PLAYING
}