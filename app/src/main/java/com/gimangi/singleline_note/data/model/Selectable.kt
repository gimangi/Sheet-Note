package com.gimangi.singleline_note.data.model

import androidx.databinding.ObservableField

data class Selectable<T>(
    val data: T,
    var selected: ObservableField<Boolean>
)
