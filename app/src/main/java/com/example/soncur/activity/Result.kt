package com.example.soncur.activity

import android.content.ContentValues.TAG
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.example.soncur.activity.StaticRef.productId
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.result_screen.*

class Result : AppCompatActivity() {

    var videoUrl:String?= null
//    var db = FirebaseFirestore.getInstance()
//    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.soncur.R.layout.result_screen)

        close_button.setOnClickListener(View.OnClickListener {
            val iLoginScreen = Intent(this@Result, Login::class.java)
            startActivity(iLoginScreen)
            finish()
        })
        Log.e("@PID:", productId)
         videoUrl = when (productId) {
             "ID00021" -> "android.resource://$packageName/raw/meeras_square_ring_silver"
             "ID00032" -> "android.resource://$packageName/raw/anu_gold_pendant_square"
             else -> "android.resource://$packageName/raw/animation_p"
         }

        progressbar_video.visibility = View.VISIBLE
//        auth = FirebaseAuth.getInstance()

        val videoView = findViewById<VideoView>(com.example.soncur.R.id.video)

        videoSet(videoView);

//        val docRef = db.collection("Links").document(auth!!.uid.toString())
//        docRef.get()
//            .addOnSuccessListener { document ->
//                if (document != null) {
//                    try {
//                        videoUrl = document.data!!.getValue(productId).toString()
//                        videoSet(videoView)
//                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
//                    }catch (e:Exception){
//                        Toast.makeText(this,"Not found",Toast.LENGTH_LONG).show()
//                    }
//
//                } else {
//                    Log.d(TAG, "No such document")
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.d(TAG, "get failed with ", exception)
//            }

        videoView.setOnCompletionListener {
           Toast.makeText(this,"Thank you",Toast.LENGTH_LONG).show()
        }

        videoView.setOnPreparedListener {
            try {
                videoView.start()
                progressbar_video.visibility = View.GONE
                visibility.visibility = View.GONE
            }catch (e:Exception){

            }

        }

        videoView.setOnInfoListener { mp, what, extra ->

            when (what) {
                MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START -> {
                    progressbar_video.setVisibility(View.GONE)
                    return@setOnInfoListener true
                }
                MediaPlayer.MEDIA_INFO_BUFFERING_START -> {
                    progressbar_video.setVisibility(View.VISIBLE)
                    return@setOnInfoListener true
                }
                MediaPlayer.MEDIA_INFO_BUFFERING_END -> {
                    progressbar_video.setVisibility(View.GONE)
                    return@setOnInfoListener true
                }
            }
            return@setOnInfoListener false
        }
    }

    override fun onStop() {
        super.onStop()

    }
    private fun videoSet(videoView:VideoView) {

        val uri: Uri = Uri.parse(videoUrl)
        videoView.setVideoURI(uri)
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        mediaController.setMediaPlayer(videoView)
        videoView.setMediaController(mediaController)

    }
}