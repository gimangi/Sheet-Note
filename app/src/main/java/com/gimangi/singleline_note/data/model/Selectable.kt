package com.gimangi.singleline_note.data.model

data class Selectable<T>(
    val data: T,
    var selected: Boolean
)
