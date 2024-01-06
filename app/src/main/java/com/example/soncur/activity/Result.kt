package com.example.soncur.activity
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.fragment.app.Fragment
import com.example.soncur.R
import com.example.soncur.activity.StaticRef.productId
import com.example.soncur.activity.StaticRef.uFinalProduct
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.result_screen.*
import java.util.concurrent.TimeUnit

class ResultFragment : Fragment() {

    private var videoUrl: String? = null
    private lateinit var auth: FirebaseAuth
    private var isPlaying = false
    private var duration = 0
    private var videoView: VideoView? = null
    private val handler = Handler(Looper.getMainLooper())
    private val updateProgressRunnable = object : Runnable {
        override fun run() {
            updateProgress()
            handler.postDelayed(this, 1000) // Update every 1 second
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.result_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        videoView = view.findViewById(R.id.video)

        line1.text = uFinalProduct.relationship
        line2.text = uFinalProduct.name
        line3.text = uFinalProduct.date
        line4.text = uFinalProduct.bd_for

        line1_2.text = uFinalProduct.type
        line2_2.text = "Base Metal: "+uFinalProduct.material
        line3_2.text = "Metal Plating: "+uFinalProduct.plating

        auth = FirebaseAuth.getInstance()

        videoUrl = when (productId) {
            "ID00022" -> "android.resource://${requireActivity().packageName}/raw/rohan_sumati"
            else -> "android.resource://${requireActivity().packageName}/raw/animation_p"
        }

        videoSet(videoView!!)

        seekBar.addOnChangeListener { slider, value, fromUser ->
            if (fromUser) {
                val newPosition = (value * duration / 100).toLong()
                videoView!!.seekTo(newPosition.toInt())
                updateCurrentTime(newPosition.toInt())
            }
        }

        playPauseButton.setOnClickListener {
            if (isPlaying) {
                pauseVideo()
            } else {
                playVideo()
            }
        }

        videoView!!.setOnCompletionListener {
            stopVideo()
        }
        videoView!!.setOnPreparedListener {
            try {
                duration = videoView!!.duration
                seekBar.valueTo = duration.toFloat()
                handler.post(updateProgressRunnable)
                if (isPlaying) {
                    pauseVideo()
                } else {
                    playVideo()
                }
            } catch (e: Exception) {
                // Handle exceptions
            }
        }
    }

    private fun playVideo() {
        videoView!!.start()
        isPlaying = true
        playPauseButton.setImageResource(R.drawable.baseline_pause_24)
    }

    private fun pauseVideo() {
        videoView!!.pause()
        isPlaying = false
        playPauseButton.setImageResource(R.drawable.baseline_play_arrow_24)
    }

    private fun stopVideo() {
        videoView!!.stopPlayback()
        isPlaying = false
        playPauseButton.setImageResource(R.drawable.baseline_play_arrow_24)
        seekBar.value = 0.0f
        updateCurrentTime(0)
        handler.removeCallbacks(updateProgressRunnable)
    }

    private fun updateProgress() {
        val currentProgress = videoView!!.currentPosition.toFloat()
        seekBar.value = currentProgress
        updateCurrentTime(currentProgress.toInt())
    }

    private fun updateCurrentTime(currentPosition: Int) {
        val formattedTime = formatTime(currentPosition.toLong())
        currentTimeTextView.text = formattedTime
    }

    private fun formatTime(milliseconds: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) % 60
        return String.format("%02d:%02ds", minutes, seconds)
    }
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateProgressRunnable)
    }

    private fun videoSet(videoView: VideoView) {
        val uri: Uri = Uri.parse(videoUrl)
        videoView.setVideoURI(uri)
    }
}