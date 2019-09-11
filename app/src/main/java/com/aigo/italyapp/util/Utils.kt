package com.aigo.italyapp.util

import android.graphics.Bitmap

/**
 * Created by Thang Tran on 15/08/2019
 */

class Utils{

    companion object{
        /**
         * down size image bitmap
         */
        fun scaleDown(bitmap: Bitmap, maxImageSize: Float, filter: Boolean =true): Bitmap {

            val ratio = Math.min(maxImageSize / bitmap.width, maxImageSize / bitmap.height)

            val width = Math.round(bitmap.width * ratio)
            val height = Math.round(bitmap.height * ratio)

            val newBitmap = Bitmap.createScaledBitmap(bitmap, width, height, filter)

            return newBitmap
        }

    }

}