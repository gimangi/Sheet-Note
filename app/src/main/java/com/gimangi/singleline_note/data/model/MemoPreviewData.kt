package com.gimangi.singleline_note.data.model

import java.util.*

data class MemoPreviewData(
    val title: String,
    val date: Date,
    val content: String,
    val status: String,
    val suffix: String,
    var selected: Boolean
)
