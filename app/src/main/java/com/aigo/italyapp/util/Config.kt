package com.aigo.italyapp.util

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * Created by Thang Tran on 15/08/2019
 */

class Config{
    companion object{
        const val GALLERY = 1
        const val REQUEST_EXTERNAL_STORAGE = 100
        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
        val PERMISSIONS_STORAGE =
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)

        const val SIZE_IMAGE_INPUT : Int = 200
        const val EYE_AR_THRESH = 0.17
        const val MOUTH_AR_THRESH = 0.35
        const val  SKIP_FRAME_DLIB = 1
    }
}