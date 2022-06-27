package com.gimangi.singleline_note.ui.memo

import com.gimangi.singleline_note.R
import com.gimangi.singleline_note.di.SlnApplication

class MemoEditViewModel : MemoDefineViewModel(
    SlnApplication.context().getString(R.string.memo_edit_toolbar_title)
) {
    var id: Int = 0
}