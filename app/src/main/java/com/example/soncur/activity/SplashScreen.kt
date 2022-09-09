package com.example.soncur.activity

import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.os.Handler
import android.view.View
import android.widget.Toast
import android.widget.VideoView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.soncur.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.splash_screen.*


class SplashScreen: AppCompatActivity() {
    private var cancellationSignal: CancellationSignal? = null
    private var auth:FirebaseAuth?=null
    private val  authenticationCallback: BiometricPrompt.AuthenticationCallback
        get() =
            @RequiresApi(Build.VERSION_CODES.P)
            object: BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    super.onAuthenticationError(errorCode, errString)
                    notifyUser("Authentication error: $errString")
                }
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
                    startActivity(Intent(this@SplashScreen, Login::class.java))
                    finish()

                }
            }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onStart() {
        super.onStart()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        auth = FirebaseAuth.getInstance()
        videoAnim()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun videoAnim() {

        val video: Uri = Uri.parse("android.resource://$packageName/raw/animation_p")
        video_animation.setVideoURI(video)
        video_animation.setZOrderOnTop(true)
        video_animation.setOnCompletionListener {
            val timeout = 100
            Handler().postDelayed({
                 onGoToNext()
            }, timeout.toLong())

        }
        video_animation.setOnPreparedListener {
            video_animation.start()
        }

    }


    @RequiresApi(Build.VERSION_CODES.P)
    private fun onGoToNext(){

        checkBiometricSupport()

        val biometricPrompt : BiometricPrompt = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            BiometricPrompt.Builder(this)
                .setTitle("Unlock Soncur")
                .setDescription("Confirm your screen lock PIN, Pattern or Password")
                .setDeviceCredentialAllowed(true)
                .build()
        } else {
            BiometricPrompt.Builder(this)
                .setTitle("Unlock Soncur")
                .setDescription("Confirm your screen lock PIN, Pattern or Password")
                .setNegativeButton("Cancel", this.mainExecutor, DialogInterface.OnClickListener { dialog, which ->
                }).build()
        }
        biometricPrompt.authenticate(getCancellationSignal(), mainExecutor, authenticationCallback)
    }


    private fun notifyUser(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            auth!!.signOut()
            notifyUser("Authentication was cancelled by the user")
        }
        return cancellationSignal as CancellationSignal
    }
    private fun checkBiometricSupport(): Boolean {
        val keyguardManager : KeyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if(!keyguardManager.isKeyguardSecure) {
            notifyUser("Fingerprint hs not been enabled in settings.")
            auth!!.signOut()
            startActivity(Intent(this@SplashScreen, Login::class.java))
            finish()
            return false
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED) {
            notifyUser("Fingerprint hs not been enabled in settings.")
            return false
        }
        return if (packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            true
        } else true
    }

}