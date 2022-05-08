package com.gimangi.singleline_note.ui.shared

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.gimangi.singleline_note.databinding.ItemSlnEditTextBinding

class SlnEditText @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ItemSlnEditTextBinding =
        ItemSlnEditTextBinding.inflate(LayoutInflater.from(context), this, true)

    val text: String
        get() = binding.etInput.text.toString()

    val editText: EditText
        get() = binding.etInput

    init {
        initTextWatcher()
        initClearButton()
        addFocusListener()
    }

    fun addTextWatcher(action: () -> Unit) {
        binding.etInput.addTextChangedListener {
            action()
        }
    }

    private fun initClearButton() {
        binding.btnClear.setOnClickListener {
            binding.etInput.setText("")
        }
    }

    private fun initTextWatcher() {
        binding.etInput.addTextChangedListener(TextExistWatcher())
    }

    inner class TextExistWatcher : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {
            if (binding.etInput.text.isNotEmpty())
                binding.btnClear.visibility = View.VISIBLE
            else
                binding.btnClear.visibility = View.INVISIBLE
        }

    }

    private fun addFocusListener() {
        binding.etInput.onFocusChangeListener = OnFocusChangeListener { _, focused ->
            this.isSelected = focused
        }
    }

}