package com.gimangi.singleline_note.util

import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.gimangi.singleline_note.R
import com.gimangi.singleline_note.di.SlnApplication
import com.gimangi.singleline_note.ui.shared.SearchEditText
import com.gimangi.singleline_note.ui.shared.SlnEditText
import java.text.SimpleDateFormat
import java.util.*

object BindingAdapter {
    private val context = SlnApplication.instance
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

    // 완료 버튼 색상 및 enable 결정
    @JvmStatic
    @BindingAdapter("setButtonEnable")
    fun setButtonEnable(button: Button, value: Boolean) {
        if (value) {
            button.setTextColor(ContextCompat.getColor(context!!, R.color.white))
            button.isEnabled = true
        } else {
            button.setTextColor(ContextCompat.getColor(context!!, R.color.gray4))
            button.isEnabled = false
        }
    }

    // EditText 힌트
    @JvmStatic
    @BindingAdapter("slnEditTextHint")
    fun slnEditTextHint(slnEditText: SlnEditText, hint: String) {
        slnEditText.editText.hint = hint
    }

}