package com.gimangi.singleline_note.util

import com.gimangi.singleline_note.data.database.dto.MemoItemEntity
import com.gimangi.singleline_note.data.database.dto.MemoTableEntity
import com.gimangi.singleline_note.data.database.room.MemoDatabase
import com.gimangi.singleline_note.di.SlnApplication
import java.util.*

object RoomUtil {
    private val context = SlnApplication.instance!!
    private val dao = MemoDatabase.getInstance(context)!!.memoDao()

    fun getMemoAll(): List<MemoTableEntity> {
        return dao.getMemoAll() ?: return listOf()
    }

    fun getMemoById(memoId: Int): MemoTableEntity? {
        return dao.getMemoById(memoId)
    }

    fun insertMemo(memoName: String) {
        val newTable = MemoTableEntity(
            memoName = memoName,
            updatedAt = Date(),
            memoList = listOf(
                MemoItemEntity(
                    order = 1,
                    item = "",
                    value = 0
                )
            )
        )
        dao.insertMemoTable(newTable)
    }

    fun updateMemo(tableEntity: MemoTableEntity) {
        tableEntity.updatedAt = Date()
        dao.updateMemoTable(tableEntity)
    }

}