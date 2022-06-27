package com.gimangi.singleline_note.ui.memo

import com.gimangi.singleline_note.R
import com.gimangi.singleline_note.di.SlnApplication
import com.gimangi.singleline_note.util.RoomUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemoEditViewModel : MemoDefineViewModel(
    SlnApplication.context().getString(R.string.memo_edit_toolbar_title)
) {
    var id: Int = 0

    fun updateMemoDefine(memoName: String, suffix: String) {
        CoroutineScope(Dispatchers.IO).launch {
            RoomUtil.updateMemoDefine(id, memoName, suffix)
        }
    }
}