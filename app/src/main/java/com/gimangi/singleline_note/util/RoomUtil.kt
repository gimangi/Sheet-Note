package com.gimangi.singleline_note.util

import com.gimangi.singleline_note.R
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

    fun insertMemo(memoName: String, suffix: String) {
        val newTable = MemoTableEntity(
            memoName = memoName,
            suffix = suffix,
            status = context.getString(R.string.memo_status_items),
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

    fun deleteMemo(memoId: Int) = dao.deleteMemoTable(getDummyMemo(memoId))

    private fun getDummyMemo(memoId: Int) = MemoTableEntity("", "", 0, "", Date(), listOf(), memoId)
}