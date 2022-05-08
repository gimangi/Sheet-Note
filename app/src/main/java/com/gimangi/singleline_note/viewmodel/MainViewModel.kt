package com.gimangi.singleline_note.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gimangi.singleline_note.data.database.dto.MemoTableEntity
import com.gimangi.singleline_note.data.database.room.MemoDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val context: Context
) : ViewModel() {

    val searchResultData = MutableLiveData<String>()

    fun getMemoData(memoId: Int): MutableLiveData<MemoTableEntity> {
        val liveData = MutableLiveData<MemoTableEntity>()
        CoroutineScope(Dispatchers.IO).launch {
            liveData.value = MemoDatabase.getInstance(context)!!.memoDao().getMemoById(memoId)
        }
        return liveData
    }

}