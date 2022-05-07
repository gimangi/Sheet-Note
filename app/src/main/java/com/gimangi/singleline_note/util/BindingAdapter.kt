package com.gimangi.singleline_note.util

import android.util.Log
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.gimangi.singleline_note.ui.shared.SearchEditText
import java.text.SimpleDateFormat
import java.util.*

object BindingAdapter {
    private const val TAG = "BindingAdapter"

    // date -> text 형변환 출력
    @JvmStatic
    @BindingAdapter("dateToText")
    fun getTextFromDate(textView: TextView, date: Date?) {
        if (date != null) {
            textView.text = SimpleDateFormat("yyyy.MM.dd (E)").format(date)
        } else {
            Log.d(TAG, "date is null")
        }
    }

    // 단위를 마지막에 붙여서 출력
    @JvmStatic
    @BindingAdapter("mergeContent", "mergeSuffix")
    fun mergeSuffix(textView: TextView, content: String, suffix: String) {
        textView.text = "$content $suffix"
    }


}