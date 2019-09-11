package com.aigo.italyapp.entity

import android.graphics.*
import android.media.ImageReader
import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import com.aigo.italyapp.util.Config
import com.aigo.italyapp.util.Utils
import com.aigo.italyapp.view.DlibView
import com.tzutalin.dlib.Constants
import com.tzutalin.dlib.FaceDet
import com.tzutalin.dlib.VisionDetRet
import java.io.File
import java.io.FileOutputStream
import java.util.logging.Logger

/**
 * Created by Thang Tran on 15/08/2019
 */

@RequiresApi(Build.VERSION_CODES.KITKAT)
class FaceObj(private var backgroundHandler: Handler, private var dlibView: DlibView) : ImageReader.OnImageAvailableListener {

    private var faceDet : FaceDet? = null
    private var resultDlib : VisionDetRet? = null

    private var bitmap : Bitmap? = null

    private var countBlink : Int = 0
    private var countSmile : Int = 0

    private var countFrame : Int = 0

    init {
        this.faceDet = FaceDet(Constants.getFaceShapeModelPath())
    }


    override fun onImageAvailable(p0: ImageReader?) {

        if (countFrame % Config.SKIP_FRAME_DLIB == 0){

            backgroundHandler.post {

                bitmap = getBitmapFromImageReader(p0)

                if (bitmap != null){
                    this.resultDlib = this.detect(bitmap!!)

                    dlibView.setResult(this.resultDlib)
                    if (this.resultDlib != null ){
                        Logger.getLogger("LANDMARK").warning(this.resultDlib!!.faceLandmarks.size.toString())
                        this.liveness(this.resultDlib!!.faceLandmarks)
                    }
                }

            }
        }
        countFrame++
    }

    /**
     * Convert array to bitmap
     * @param p0: ImageReader
     * @return Bitmap
     */
    private fun getBitmapFromImageReader(p0: ImageReader?): Bitmap? {

        val image = p0?.acquireLatestImage()
        val imageBuffer = image?.planes?.get(0)?.buffer
        val imageBytes = imageBuffer?.remaining()?.let { ByteArray(it) }
        imageBuffer?.get(imageBytes)
        image?.close()
        if (imageBytes != null){
            var bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

            bitmap = Utils.scaleDown(bitmap!!, Config.SIZE_IMAGE_INPUT.toFloat())

            val matrix = Matrix()
            //For some reason the bitmap is rotated the incorrect way
            matrix.postRotate(270f)
            // Flip bitmap is the incorrect way
            matrix.preScale(1.0f, -1.0f)

            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }
        return null
    }

    /**
     * get face landmarks in image bitmap
     * @param bitmap: Bitmap
     * @return: landmarks
     */
    private fun detect(bitmap: Bitmap): VisionDetRet? {
        this.bitmap = bitmap

        val start = System.currentTimeMillis()
        val results = faceDet?.detect(bitmap)
        Logger.getLogger("RUN TIME").warning((System.currentTimeMillis() - start).toString())

        if (results != null && results.size > 0) {
            return results[0]
        }

        return null
    }

    /**
     * Detect Face Liveness: open mouth and eye blink
     */
    private fun liveness(landmarks: java.util.ArrayList<Point>){
        Log.e("LIVENESS", this.countBlink.toString() + " " + this.countSmile.toString())
        if (blink(landmarks)) this.countBlink++
        if (smile(landmarks)) this.countSmile++
    }

    /**
     * Detect eye blink
     */
    private fun blink(landmarks : java.util.ArrayList<Point>): Boolean{
        // eye left
        var a = distance(landmarks[37], landmarks[41])
        var b = distance(landmarks[38], landmarks.get(40))
        var c = distance(landmarks[36], landmarks[39])

        var eye = (a + b) / (2.0 * c)
        Logger.getLogger("EYE LEFT").warning(eye.toString())

        if (eye< Config.EYE_AR_THRESH){
            return true
        }

        // eye right
        a = distance(landmarks[43], landmarks[47])
        b = distance(landmarks[44], landmarks[46])
        c = distance(landmarks[42], landmarks[45])

        eye = (a + b) / (2.0 * c)
        Logger.getLogger("EYE RIGHT").warning(eye.toString())

        if (eye < Config.EYE_AR_THRESH){
            return true
        }

        return false
    }

    /**
     * Detect open mouth
     */
    private fun smile(landmarks : java.util.ArrayList<Point>) : Boolean{

        val a = distance(landmarks[61], landmarks[67])
        val b = distance(landmarks[62], landmarks[66])
        val c = distance(landmarks[60], landmarks[65])

        val mouth = (a + b) / (2.0 * c)

        Logger.getLogger("MOUTH").warning(mouth.toString())

        if (mouth > Config.MOUTH_AR_THRESH){
            return  true
        }

        return false
    }

    /**
     * Calculator distance Point1 to Point2
     */
    private fun distance(p1: Point, p2: Point) : Double{
        return Math.sqrt(Math.pow((p1.x - p2.x).toDouble(), 2.0) + Math.pow((p1.y - p2.y).toDouble(), 2.0) )
    }
}