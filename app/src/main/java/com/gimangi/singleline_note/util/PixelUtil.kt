package com.gimangi.singleline_note.util

import android.content.res.Resources
import android.util.TypedValue

object PixelUtil {

    private val displayMetrics
        get() = Resources.getSystem().displayMetrics

    fun dpToPx(dp: Int) =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), displayMetrics).toInt()

    fun dpToPxF(dp: Int): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), displayMetrics)

}

val Number.dpToPx: Int
    get() = PixelUtil.dpToPx(this.toInt())

val Number.dpToPxF: Float
    get() = PixelUtil.dpToPxF(this.toInt())