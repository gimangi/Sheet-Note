package com.gimangi.singleline_note.data.model

import androidx.databinding.ObservableField
import java.util.*

data class MemoPreviewData(
    val memoId: Int,
    val title: String,
    val date: Date,
    val summary: String,
    val status: String,
    val suffix: String,
    val selected: ObservableField<Boolean>
)
