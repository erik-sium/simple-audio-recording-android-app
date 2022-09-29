package com.example.simpleaudiorecordingandroidapp.ui.main

import android.content.ContextWrapper
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.simpleaudiorecordingandroidapp.R
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

class MainFragment : Fragment() {

    private lateinit var audioRecorder: MediaRecorder
    private lateinit var audioPlayer: MediaPlayer

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recordButton: Button? = getView()?.findViewById(R.id.recordButton)
        recordButton?.setOnClickListener {
            view -> btnRecordPressed()
        }

        val stopRecordingButton: Button? = getView()?.findViewById(R.id.stopRecordingButton)
        stopRecordingButton?.setOnClickListener {
            view -> btnStopRecordingPressed()
        }

        val playButton: Button? = getView()?.findViewById(R.id.playButton)
        playButton?.setOnClickListener {
            view -> btnPlayAudioPressed()
        }
    }

    private fun getAudioRecordingFilePath(): String {

        val contextWrapper: ContextWrapper = ContextWrapper(activity?.applicationContext)
        val musicDirectory: File? = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC)

        if(musicDirectory != null && musicDirectory.exists()) {
            val file: File = File(musicDirectory, "testRecording.3gp")
            return file.path
        }

        throw FileNotFoundException("cannot find Music folder")
    }

    private fun btnRecordPressed(): Unit {

        try {
            audioRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setOutputFile(getAudioRecordingFilePath())
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB)
                setAudioEncodingBitRate(128000)
                setAudioSamplingRate(44100)
                prepare()
                start()
            }

            Toast.makeText(this.context, "Recording started", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun btnStopRecordingPressed(): Unit {
        audioRecorder.stop()
        audioRecorder.release()

        Toast.makeText(this.context, "Recording stopped", Toast.LENGTH_SHORT).show()
    }

    private fun btnPlayAudioPressed() : Unit {

        try {
            audioPlayer = MediaPlayer().apply {
                setDataSource(getAudioRecordingFilePath())
                prepare()
                start()
            }

            Toast.makeText(this.context, "Audio recording is playing", Toast.LENGTH_SHORT).show()
        } catch(e: Exception) {
            e.printStackTrace()
        }

        this.context
    }
}