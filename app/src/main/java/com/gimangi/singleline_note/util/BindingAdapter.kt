package com.gimangi.singleline_note.util

import android.util.Log
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.*

object BindingAdapter {
    private const val TAG = "BindingAdapter"

    @JvmStatic
    @BindingAdapter("dateToText")
    fun getTextFromDate(textView: TextView, date: Date?) {
        if (date != null) {
            textView.text = SimpleDateFormat("yyyy.MM.dd (E)").format(date)
        } else {
            Log.d(TAG, "date is null")
        }
    }

    @JvmStatic
    @BindingAdapter("mergeContent", "mergeSuffix")
    fun mergeSuffix(textView: TextView, content: String, suffix: String) {
        textView.text = "$content $suffix"
    }
}