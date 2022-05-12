package com.gimangi.singleline_note.data.model

import java.util.*

data class MemoPreviewData(
    val memoId: Int,
    val title: String,
    val date: Date,
    val summary: String,
    val status: String,
    val suffix: String,
    var selected: Boolean
)
