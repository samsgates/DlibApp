package com.aigo.italyapp.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.aigo.italyapp.R
import com.aigo.italyapp.entity.FaceObj
import com.aigo.italyapp.util.Config
import com.aigo.italyapp.util.Utils
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private var imageView: ImageView? = null

    @SuppressLint("WrongThread")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var intent = Intent(this, DlibActivity::class.java)
        startActivity(intent)

        imageView = findViewById(R.id.imageView)

        verifyStoragePermissions(this)


    }

    /**
     * Add Permission
     */
    private fun verifyStoragePermissions(activity: Activity) {
        val permission = ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity, Config.PERMISSIONS_STORAGE,
                Config.REQUEST_EXTERNAL_STORAGE
            )
        }
    }

}
