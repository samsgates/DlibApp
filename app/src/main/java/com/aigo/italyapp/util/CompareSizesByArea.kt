package com.aigo.italyapp.util

import android.os.Build
import android.util.Size
import androidx.annotation.RequiresApi
import java.lang.Long.signum

/**
 * Created by Thang Tran on 16/08/2019
 */

internal class CompareSizesByArea : Comparator<Size> {

    // We cast here to ensure the multiplications won't overflow
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun compare(lhs: Size, rhs: Size) =
        signum(lhs.width.toLong() * lhs.height - rhs.width.toLong() * rhs.height)

}