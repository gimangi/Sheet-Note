package com.gimangi.singleline_note.ui.memo

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gimangi.singleline_note.data.database.dto.MemoItemEntity
import com.gimangi.singleline_note.data.database.dto.MemoTableEntity
import com.gimangi.singleline_note.data.model.MemoItemData
import com.gimangi.singleline_note.util.RoomUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemoDetailViewModel : ViewModel() {

    var memoId = -1

    val memoTableName = ObservableField<String>()
    val selectedSummary = ObservableField<String>()
    val summary = ObservableField<Long>()
    val suffix = ObservableField<String>()

    val memoTableData = MutableLiveData<MemoTableEntity>()

    fun getMemoData(): MutableLiveData<MemoTableEntity> {
        val res = MutableLiveData<MemoTableEntity>()
        CoroutineScope(Dispatchers.IO).launch {
            res.postValue(RoomUtil.getMemoById(memoId))
        }
        return res
    }

    fun insertMemoItem(table: MemoTableEntity, item: MemoItemEntity): MutableLiveData<MemoTableEntity> {
        val res = MutableLiveData<MemoTableEntity>()
        CoroutineScope(Dispatchers.IO).launch {
            res.postValue(RoomUtil.insertMemoItem(table, item))
        }
        return res
    }

    fun updateMemoTable(table: MemoTableEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            RoomUtil.updateMemo(table)
        }
    }

}