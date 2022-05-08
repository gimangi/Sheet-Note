package com.gimangi.singleline_note.ui.memo

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MemoCreateViewModel : ViewModel() {
    val allTextNotEmpty = ObservableField(false)
}