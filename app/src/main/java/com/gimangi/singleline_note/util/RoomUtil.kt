package com.gimangi.singleline_note.util

import com.gimangi.singleline_note.R
import com.gimangi.singleline_note.data.database.dto.MemoItemEntity
import com.gimangi.singleline_note.data.database.dto.MemoStatus
import com.gimangi.singleline_note.data.database.dto.MemoTableEntity
import com.gimangi.singleline_note.data.database.room.MemoDatabase
import com.gimangi.singleline_note.data.model.MemoItemData
import com.gimangi.singleline_note.di.SlnApplication
import java.util.*

object RoomUtil {
    private val context = SlnApplication.instance!!
    private val dao = MemoDatabase.getInstance(context)!!.memoDao()

    suspend fun getMemoAll(): List<MemoTableEntity> {
        return dao.getMemoAll() ?: return listOf()
    }

    suspend fun getMemoById(memoId: Int): MemoTableEntity? {
        return dao.getMemoById(memoId)
    }

    suspend fun insertMemo(memoName: String, suffix: String) {
        val newTable = MemoTableEntity(
            memoName = memoName,
            suffix = suffix,
            status = MemoStatus.SUM,
            updatedAt = Date(),
            rowList = mutableListOf(
                MemoItemEntity(
                    order = 1,
                    item = "",
                    value = 0,
                    tableId = 0
                )
            )
        )
        dao.insertMemoTable(newTable)
    }

    suspend fun insertMemoItem(table: MemoTableEntity, item: MemoItemEntity): MemoTableEntity {
        return dao.addMemoItem(table, item)
    }

    suspend fun updateMemo(tableEntity: MemoTableEntity): MemoTableEntity {
        tableEntity.updatedAt = Date()
        dao.updateMemoTable(tableEntity)
        return tableEntity
    }

    suspend fun updateMemoDefine(memoId: Int, memoName: String, suffix: String) {
        dao.updateMemoTableDefine(memoId, memoName, suffix)
    }

    suspend fun deleteMemoList(list: List<Int>) = dao.deleteMemoTables(list)
}