package com.gimangi.singleline_note.util

import android.util.Log
import android.view.View
import android.view.View.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.compose.ui.platform.textInputServiceFactory
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.gimangi.singleline_note.R
import com.gimangi.singleline_note.di.SlnApplication
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
    @BindingAdapter("setButtonEnable", "setButtonDefaultColor")
    fun setButtonEnable(button: Button, value: Boolean, defaultColor: Int) {
        if (value) {
            button.setTextColor(getColor(defaultColor))
            button.isEnabled = true
        } else {
            button.setTextColor(getColor(R.color.gray4))
            button.isEnabled = false
        }
    }

    // EditText 힌트
    @JvmStatic
    @BindingAdapter("slnEditTextHint")
    fun slnEditTextHint(slnEditText: SlnEditText, hint: String) {
        slnEditText.editText.hint = hint
    }

    // Button Visibility GONE
    @JvmStatic
    @BindingAdapter("setVisible")
    fun setVisible(view: View, flag: Boolean) {
        if (flag)
            view.visibility = VISIBLE
        else
            view.visibility = INVISIBLE
    }

    @JvmStatic
    @BindingAdapter("setGone")
    fun setGone(view: View, flag: Boolean) {
        if (flag)
            view.visibility = VISIBLE
        else
            view.visibility = GONE
    }

    // Set selected
    @JvmStatic
    @BindingAdapter("setSelected")
    fun setSelected(view: View, value: Boolean) {
        view.isSelected = value
    }

    // int를 TextView에 출력
    @JvmStatic
    @BindingAdapter("setTextInt")
    fun setTextInt(editText: EditText, value: Int) {
        editText.setText(value.toString())
    }

    @JvmStatic
    @BindingAdapter("setTextInt")
    fun setTextInt(textView: TextView, value: Int) {
        textView.text = value.toString()
    }

    @JvmStatic
    @BindingAdapter("setTextLong")
    fun setTextLong(textView: TextView, value: Long) {
        textView.text = value.toString()
    }

    @JvmStatic
    @BindingAdapter("setTextLong")
    fun setTextLong(editText: EditText, value: Long) {
        editText.setText(value.toString())
    }

    // 짝수 행에 대해서 색상 구분
    @JvmStatic
    @BindingAdapter("setEvenRowColor")
    fun setRowEvenColor(view: View, num: Int) {
        if (num % 2 == 0)
            view.setBackgroundColor(getColor(R.color.even_item))
        else
            view.setBackgroundColor(getColor(R.color.white))
    }

    private fun getColor(value: Int) = ContextCompat.getColor(context!!, value)

}