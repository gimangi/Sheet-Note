package com.gimangi.singleline_note.ui.shared

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.MutableLiveData
import com.gimangi.singleline_note.databinding.ItemSearchEditTextBinding

class SearchEditText @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ConstraintLayout(context, attrs, defStyleAttr) {

    private var resultLiveData: MutableLiveData<String>? = null

    private val binding: ItemSearchEditTextBinding =
        ItemSearchEditTextBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        addBtnVisibleWatcher()
        addResultWatcher()
    }

    private fun addBtnVisibleWatcher() {
        binding.etInput.addTextChangedListener {
            if (it != null && it.isNotEmpty())
                binding.ivSearch.visibility = View.INVISIBLE
            else
                binding.ivSearch.visibility = View.VISIBLE
        }
    }

    private fun addResultWatcher() {
        binding.etInput.addTextChangedListener {
            if (it != null && resultLiveData != null) {
                resultLiveData!!.value = it.toString()
            }
        }
    }

    fun registerLiveData(data: MutableLiveData<String>) {
        this.resultLiveData = data
    }

}

