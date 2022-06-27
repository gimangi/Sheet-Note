package com.gimangi.singleline_note.ui.memo

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

open abstract class MemoDefineViewModel(private val titleStr: String) : ViewModel() {

    val title = ObservableField(titleStr)
    val allTextNotEmpty = ObservableField(false)
    var name = ""
    var suffix = ""

}