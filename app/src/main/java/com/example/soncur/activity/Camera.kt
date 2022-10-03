package com.example.soncur.activity

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.*
import android.os.*
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.soncur.R
import com.example.soncur.activity.StaticRef.productShape
import com.example.soncur.adapter.HorizontalProductViewHolder
import com.example.soncur.databinding.ActivityMainBinding
import com.example.soncur.datamodel.ModelHorizontalProduct
import com.google.android.material.snackbar.Snackbar
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.info_bottom_sheet.*
import java.util.concurrent.Executors


class Camera : AppCompatActivity(), ObjectDetectorHelper.DetectorListener {

    private var binding: ActivityMainBinding? = null
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraSelector: CameraSelector
    private var imageCapture:ImageCapture? = null
    private var camera: androidx.camera.core.Camera?=null
    private var imageAnalyzer: ImageAnalysis? = null
    private val cameraExecutor = Executors.newSingleThreadExecutor()
    private var objectDetectorHelper:ObjectDetectorHelper?=null
    private var productList = ArrayList<ModelHorizontalProduct>()
    private val productAdapter =  HorizontalProductViewHolder(productList,this)
    private var checkIterations = true
    private var list:List<String>?=null

    private val cameraProviderResult = registerForActivityResult(ActivityResultContracts.RequestPermission()){ permissionGranted->
        if(permissionGranted){
            startCamera()
        }else {
            Snackbar.make(binding!!.root,"The camera permission is required", Snackbar.LENGTH_INDEFINITE).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        objectDetectorHelper = ObjectDetectorHelper(context = this,
            objectDetectorListener = this)

        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        imageCapture = ImageCapture.Builder()
            .setFlashMode(ImageCapture.FLASH_MODE_AUTO)
            .build()

        startCamera()

        cameraProviderResult.launch(android.Manifest.permission.CAMERA)

        val mRecyclerView = findViewById<RecyclerView>(R.id.list_of_product)
        mRecyclerView.layoutManager = LinearLayoutManager(this@Camera,
            LinearLayoutManager.HORIZONTAL,false)
        mRecyclerView.adapter = productAdapter

        setValue()


        done_button.setOnClickListener(View.OnClickListener {
            if(id.text.isNotEmpty()){
                var check = true
                var i = 0
                while (i < productList.size){
                    if(productList[i].pId == id.text.toString()) {
                        check = false
                        StaticRef.productId = id.text.toString()
                        val iResultScreen = Intent(this@Camera, Result::class.java)
                        vibrate()
                        val timeout = 100
                        Handler().postDelayed({
                            startActivity(iResultScreen)
                            finish()
                        }, timeout.toLong())
                    }
                    i+=1
                }
                if(check) {
                    Toast.makeText(this, "Invalid id", Toast.LENGTH_SHORT).show()
                }
            }


        })

        var flashManager = 0
        flash_button.setOnClickListener(View.OnClickListener {
//            captureImage()
            if(flashManager==0){
                switchFlashLight(true)
                flash_on_off.setImageDrawable(
                    ContextCompat.getDrawable(this,
                        R.drawable.ic_baseline_highlight_24
                    ));
                flashManager=1

            }else{
                switchFlashLight(false)
                flash_on_off.setImageDrawable(
                    ContextCompat.getDrawable(this,
                        R.drawable.ic_baseline_highlight_off_24
                    ));
                flashManager=0
            }
        })


        val scannerAnimation: Animation? = AnimationUtils.loadAnimation(this, R.anim.up_animation)
        scanner.startAnimation(scannerAnimation)


    }

    private fun setValue() {
        var i = 0
        if(StaticRef.uProductList.isNotEmpty()){
            while (i < StaticRef.uProductList.size){
                var uProductListSecond = StaticRef.uProductList[i].split(',')
                if(uProductListSecond.size!=1){
                    var model = ModelHorizontalProduct(uProductListSecond[2],uProductListSecond[1].toInt(),uProductListSecond[5])
                    productList!!.add(model)
                    list_of_product.visibility=View.VISIBLE
                    productAdapter!!.notifyDataSetChanged()

                }else{
                    try {
                        Toast.makeText(this,"Not found",Toast.LENGTH_LONG).show()
                    }catch (e:Exception){

                    }

                }
                i+=1
                try {

                }catch (e:Exception){

                }
            }
        }else {
            try {
                Toast.makeText(this,"Not found",Toast.LENGTH_LONG).show()
            }catch (e:Exception){

            }

        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
        cameraExecutor.shutdown()
    }
    @SuppressLint("UnsafeOptInUsageError")
    private fun startCamera(){
        // listening for data from the camera
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

//                camera = cameraProvider!!.bindToLifecycle(this,cameraSelector,preview,imageCapture)
            } catch (e: Exception) {
                Log.d(TAG, "Use case binding failed")
            }

        }, ContextCompat.getMainExecutor(this))

    }

    private fun switchFlashLight(status: Boolean) {
        if (camera!!.cameraInfo.hasFlashUnit()) {
            camera!!.cameraControl.enableTorch(status)

        }else{
            flash_on_off.setImageDrawable(
                ContextCompat.getDrawable(this,
                    R.drawable.ic_baseline_highlight_off_24
                ));
        }
    }

    private fun captureImage() {
        imageCapture!!.takePicture(
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageCapturedCallback() {
                @SuppressLint("UnsafeOptInUsageError")
                override fun onCaptureSuccess(imageProxy: ImageProxy) {
                    objectDetectorHelper!!.detect(imageProxy, imageProxy.image!!,0)
                    imageProxy.close()
                }
                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(applicationContext,"Something went wrong",Toast.LENGTH_SHORT).show()
                }
            }
        )

    }

    private fun vibrate() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
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

    fun onItemClick(position: Int) {
        StaticRef.productId = productList[position].pId
        val iResultScreen = Intent(this, Result::class.java)
        vibrate()
        val timeout = 100
        Handler().postDelayed({
            startActivity(iResultScreen)
            finish()
        }, timeout.toLong())


    }

    override fun onError(error: String) {
        this.runOnUiThread {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResults(
        largest: String
    ) {
        this.runOnUiThread {
            list = largest.split(",")
            Log.d("Result","$largest")
            if("1" == list!![0] && checkIterations && list!![1]==productShape){
                vibrate()
                checkIterations = false
                val iResultScreen = Intent(this@Camera, Result::class.java)
                startActivity(iResultScreen)
                finish()
            }
//            if(maxNo == 0){
//                identifiedShape="Heart"
//            }else{
//                if(maxNo == 1){
//                    identifiedShape="Circle"
//                }else{
//                    if(maxNo == 2){
//                        identifiedShape="Square"
//                    }else{
//                        if(maxNo == 3) {
//                            identifiedShape = "Regular"
//                        }
//                    }
//                }
//            }
//
//            if( productShape == identifiedShape && largest >= 90.0f){
//                val iResultScreen = Intent(this@Camera, Result::class.java)
//                startActivity(iResultScreen)
//                finish()
//            }
        }
    }
}