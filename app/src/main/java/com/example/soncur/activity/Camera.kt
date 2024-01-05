package com.example.soncur.activity

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.res.Configuration
import android.graphics.*
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.soncur.R
import com.example.soncur.activity.StaticRef.productShape
import com.example.soncur.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.info_bottom_sheet.*
import java.util.concurrent.Executors


class Camera : Fragment(), ObjectDetectorHelper.DetectorListener {

    private var binding: ActivityMainBinding? = null
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraSelector: CameraSelector
    private var imageCapture:ImageCapture? = null
    private var camera: androidx.camera.core.Camera?=null
    private var imageAnalyzer: ImageAnalysis? = null
    private val cameraExecutor = Executors.newSingleThreadExecutor()
    private var objectDetectorHelper:ObjectDetectorHelper?=null
    private var checkIterations = true
    private var list:List<String>?=null

    private val cameraProviderResult = registerForActivityResult(ActivityResultContracts.RequestPermission()){ permissionGranted->
        if(permissionGranted){
            startCamera()
        }else {
            Snackbar.make(binding!!.root,"The camera permission is required", Snackbar.LENGTH_INDEFINITE).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityMainBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraProviderResult.launch(android.Manifest.permission.CAMERA)

        objectDetectorHelper = ObjectDetectorHelper(context = requireActivity(),
            objectDetectorListener = this)

        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        imageCapture = ImageCapture.Builder()
            .setFlashMode(ImageCapture.FLASH_MODE_AUTO)
            .build()

        startCamera()
        binding?.apply {
            var flashManager = 0
            flash_button.setOnClickListener {
                if(flashManager==0){
                    switchFlashLight(true)
                    flash_on_off.setImageDrawable(
                        ContextCompat.getDrawable(requireActivity(),
                            R.drawable.torch
                        ));
                    flashManager=1
//                    showFragment(ResultFragment())
                }else{
                    switchFlashLight(false)
                    flash_on_off.setImageDrawable(
                        ContextCompat.getDrawable(requireActivity(),
                            R.drawable.torch
                        ));
                    flashManager=0
                }
            }
            val scannerAnimation: Animation? =
                AnimationUtils.loadAnimation(requireContext(), R.anim.up_animation)
            scanner.startAnimation(scannerAnimation)
        }

    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
        cameraExecutor.shutdown()
    }
    @SuppressLint("UnsafeOptInUsageError")
    private fun startCamera(){
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(binding!!.preview.display.rotation).build().also{
                    it.setSurfaceProvider(binding!!.preview.surfaceProvider)
                }
            imageAnalyzer =
                ImageAnalysis.Builder()
                    .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                    .setTargetRotation(binding!!.preview.display.rotation)
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor) { image ->
                            objectDetectorHelper!!.detect(image, image.image!!,1)
                            image.close()
                        }
                    }
            try{
                cameraProvider!!.unbindAll()
                camera = cameraProvider!!.bindToLifecycle(this,cameraSelector,preview,imageCapture,imageAnalyzer)

            } catch (e: Exception) {
                Log.d(TAG, "Use case binding failed")
            }

        }, ContextCompat.getMainExecutor(requireContext()))

    }

    private fun switchFlashLight(status: Boolean) {
        if (camera!!.cameraInfo.hasFlashUnit()) {
            camera!!.cameraControl.enableTorch(status)

        }else{
            flash_on_off.setImageDrawable(
                ContextCompat.getDrawable(requireActivity(),
                    R.drawable.torch
                ));
        }
    }
    private fun vibrate() {
        val vibrator = requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) { // Vibrator availability checking
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        100,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                vibrator.vibrate(100)
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        imageAnalyzer?.targetRotation = binding!!.preview.display.rotation
    }

    override fun onError(error: String) {
        requireActivity().runOnUiThread {
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
    }
    private fun showFragment(fragment: Fragment){
        try{
            val frame = requireActivity().supportFragmentManager.beginTransaction()
            frame.replace(R.id.fragment_main,fragment)
            frame.commit()
        }catch (e:Exception){

        }
    }
    override fun onResults(
        largest: String
    ) {
        try {
        requireActivity().runOnUiThread {
            list = largest.split(",")
            Log.d("Result","$largest")
            if("1" == list!![0] && checkIterations && list!![1]==productShape){
                vibrate()
                checkIterations = false
                showFragment(ResultFragment())
            }
        }
        }catch (e:Exception){
            Log.e("MSG",e.message.toString());
        }

    }
}