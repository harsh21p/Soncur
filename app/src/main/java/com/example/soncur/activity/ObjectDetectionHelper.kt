package com.example.soncur.activity

import android.content.Context
import android.graphics.*
import android.media.Image
import android.media.ThumbnailUtils
import android.util.Log
import androidx.camera.core.ImageProxy
import com.example.soncur.ml.Model1
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder


class ObjectDetectorHelper(
    val context: Context,
    val objectDetectorListener: DetectorListener?
) {

    private var imageW = 224
    private var imageH = 224
    var imageBitmap:Bitmap? = null
    fun detect(image: ImageProxy,imageImage: Image,identifier:Int) {
        try {
            if(identifier==0){
                imageBitmap  = convertImageProxyToBitmap(image)
            }else{
                imageBitmap = toBitmap(imageImage)
            }
            val imageInput = convertBitmapToByteBuffer(imageBitmap!!)
            val model = Model1.newInstance(context)
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
            inputFeature0.loadBuffer(imageInput!!)
            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer
            val fArray = outputFeature0.floatArray

            var largest:Float= 0.0f
            var i =0
            var maxNo = 0
            for (num in fArray) {
                if (largest < num) {
                    largest = num
                    maxNo = i
                }
                i+=1
            }
            val outputImage = getOutputImage(imageInput)
            model.close()
            objectDetectorListener?.onResults(
                largest,maxNo,outputImage!!)

        }catch (e:Exception){
            Log.d("EX",e.toString())
        }

    }
    private fun toBitmap(image: Image): Bitmap? {
        val planes: Array<Image.Plane> = image.getPlanes()
        val yBuffer: ByteBuffer = planes[0].getBuffer()
        val uBuffer: ByteBuffer = planes[1].getBuffer()
        val vBuffer: ByteBuffer = planes[2].getBuffer()
        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()
        val nv21 = ByteArray(ySize + uSize + vSize)
        //U and V are swapped
        yBuffer[nv21, 0, ySize]
        vBuffer[nv21, ySize, vSize]
        uBuffer[nv21, ySize + vSize, uSize]
        val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.getWidth(), image.getHeight(), null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 75, out)
        val imageBytes: ByteArray = out.toByteArray()
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    private fun convertBitmapToByteBuffer(image: Bitmap): ByteBuffer? {
        val dimension = Math.min(image.width,image.height)
        val image1 = ThumbnailUtils.extractThumbnail(image,dimension,dimension)
        val bitmap = Bitmap.createScaledBitmap(image1,imageW,imageH,false)
        val byteBuffer =
            ByteBuffer.allocateDirect(4  * imageW * imageH * 3)
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(imageW * imageH)
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, imageW, imageH)
        var pixel = 0
        for (i in 0 until imageW) {
            for (j in 0 until imageH) {
                val `val` = intValues[pixel++]
                byteBuffer.putFloat(((`val` shr 16) and 0xFF) * (1f / 255f))
                byteBuffer.putFloat(((`val` shr 8) and 0xFF) * (1f / 255f))
                byteBuffer.putFloat((`val` and 0xFF) * (1f / 255f))
            }
        }
        return byteBuffer
    }

    private fun convertImageProxyToBitmap(image: ImageProxy): Bitmap? {
        val byteBuffer = image.planes[0].buffer
        byteBuffer.rewind()
        val bytes = ByteArray(byteBuffer.capacity())
        byteBuffer[bytes]
        val clonedBytes = bytes.clone()
        return BitmapFactory.decodeByteArray(clonedBytes, 0, clonedBytes.size)
    }



    private fun getOutputImage(output:ByteBuffer?): Bitmap {
        output?.rewind() // Rewind the output buffer after running.

        val bitmap = Bitmap.createBitmap(imageW, imageH, Bitmap.Config.ARGB_8888)
        val pixels = IntArray(imageW * imageH) // Set your expected output's height and width
        for (i in 0 until imageW * imageH) {
            val a = 0xFF
            val r: Float = output?.float!! * 255.0f
            val g: Float = output?.float!! * 255.0f
            val b: Float = output?.float!! * 255.0f
            pixels[i] = a shl 24 or (r.toInt() shl 16) or (g.toInt() shl 8) or b.toInt()
        }
        bitmap.setPixels(pixels, 0, imageW, 0, 0, imageW, imageH)

        return bitmap
    }

    interface DetectorListener {
        fun onError(error: String)
        fun onResults(
            largest:Float,maxNo:Int,
            bitmapBuffer: Bitmap
        )
    }
}