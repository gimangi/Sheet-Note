package com.gimangi.singleline_note.adapter

import androidx.databinding.ObservableField

interface ItemTouchHelperListener {

    val modifyMode: ObservableField<Boolean>
    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean
    fun onItemSwipe(position: Int)
}