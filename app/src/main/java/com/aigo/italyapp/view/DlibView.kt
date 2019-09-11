package com.aigo.italyapp.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.aigo.italyapp.util.Config
import com.tzutalin.dlib.VisionDetRet

/**
 * Created by Thang Tran on 16/08/2019
 */

class DlibView: View{

    private var resultDlib : VisionDetRet? = null
    private var height : Int? = 0
    private var width : Int? = 0

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        this.resultDlib = null
    }
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0) {
        this.resultDlib = null
    }

    fun setResult(resultDlib : VisionDetRet?){
        this.resultDlib = resultDlib

        postInvalidate()
    }
    fun setSizeImage(height: Int, width: Int){
        Log.e("SIZE IMAGE", height.toString() + " " + width.toString())
        this.height = height
        this.width = height
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val paint = Paint()
        paint.color = Color.GREEN
        paint.strokeWidth = 5F
        paint.style = Paint.Style.STROKE

        if (this.resultDlib != null){

            // drawing detection
            var bounds = Rect()
            bounds.bottom = (this.resultDlib!!.bottom * (this.width!!.toFloat() / Config.SIZE_IMAGE_INPUT)).toInt()
            bounds.left = (this.resultDlib!!.left  * (this.height!!.toFloat() / Config.SIZE_IMAGE_INPUT)).toInt()
            bounds.top = (this.resultDlib!!.top * (this.width!!.toFloat() / Config.SIZE_IMAGE_INPUT)).toInt()
            bounds.right = (this.resultDlib!!.right * (this.height!!.toFloat() / Config.SIZE_IMAGE_INPUT)).toInt()

            canvas?.drawRect(bounds, paint)

            paint.style = Paint.Style.FILL_AND_STROKE
            // drawing landmark
            for (point in this.resultDlib!!.faceLandmarks) {
                val pointX = point.x * (this.width!!.toFloat() / Config.SIZE_IMAGE_INPUT)
                val pointY = point.y * (this.height!!.toFloat() / Config.SIZE_IMAGE_INPUT)
                canvas?.drawCircle(pointX, pointY, 2F, paint)
            }
        }
    }
}