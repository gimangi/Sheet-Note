package com.gimangi.singleline_note.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gimangi.singleline_note.data.database.dto.MemoTableEntity
import com.gimangi.singleline_note.util.RoomUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    val searchResultData = MutableLiveData<String>()

    fun getMemoData(memoId: Int): MutableLiveData<MemoTableEntity> {
        val res = MutableLiveData<MemoTableEntity>()
        CoroutineScope(Dispatchers.IO).launch {
            res.postValue(RoomUtil.getMemoById(memoId))
        }
        return res
    }

}