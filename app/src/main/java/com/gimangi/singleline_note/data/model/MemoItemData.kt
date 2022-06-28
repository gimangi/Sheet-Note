package com.gimangi.singleline_note.data.model

data class MemoItemData(
    var number: Int,
    var name: String,
    var value: Long,
    val itemId: Int,
    val tableId: Int
)
