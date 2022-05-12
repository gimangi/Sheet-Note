package com.gimangi.singleline_note.ui.main

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gimangi.singleline_note.data.database.dto.MemoTableEntity
import com.gimangi.singleline_note.data.model.MemoPreviewData
import com.gimangi.singleline_note.util.RoomUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    val isEditMode = ObservableField(false)
    val selectedMemoExist = ObservableField(false)
    val searchResultData = MutableLiveData<String>()

    fun getMemoDataList(): MutableLiveData<List<MemoPreviewData>> {
        val res = MutableLiveData<List<MemoPreviewData>>()
        CoroutineScope(Dispatchers.IO).launch {
            val data = RoomUtil.getMemoAll()

            if (data.isNullOrEmpty()) {
                res.postValue(listOf())
            } else {
                res.postValue(
                    RoomUtil.getMemoAll().map { entity ->
                        MemoPreviewData(
                            memoId = entity.memoId,
                            title = entity.memoName,
                            date = entity.updatedAt,
                            summary = entity.summary.toString(),
                            status = entity.status,
                            suffix = entity.suffix,
                            selected = ObservableField(false)
                        )
                    }.sortedBy { d -> d.date }.reversed()
                )
            }
        }
        return res
    }


    fun deleteMemoDataList(list: List<Int>): MutableLiveData<Int> {
        val res = MutableLiveData<Int>()
        CoroutineScope(Dispatchers.IO).launch {
            res.postValue(RoomUtil.deleteMemoList(list))
        }
        return res
    }

}