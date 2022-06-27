package com.gimangi.singleline_note.ui.memo

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gimangi.singleline_note.R
import com.gimangi.singleline_note.data.database.dto.MemoTableEntity
import com.gimangi.singleline_note.di.SlnApplication
import com.gimangi.singleline_note.util.RoomUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemoCreateViewModel() : MemoDefineViewModel(
    SlnApplication.context().getString(R.string.memo_create_toolbar_title)
) {

    fun insertNewMemo(memoName: String, suffix: String) {
        CoroutineScope(Dispatchers.IO).launch {
            RoomUtil.insertMemo(memoName, suffix)
        }
    }

}