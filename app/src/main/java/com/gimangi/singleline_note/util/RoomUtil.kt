package com.gimangi.singleline_note.util

import com.gimangi.singleline_note.data.database.dto.MemoTableEntity
import com.gimangi.singleline_note.data.database.room.MemoDatabase
import com.gimangi.singleline_note.di.SlnApplication

object RoomUtil {
    private var context = SlnApplication.instance!!

    fun getMemoById(memoId: Int): MemoTableEntity? {
        return MemoDatabase.getInstance(context)!!.memoDao().getMemoById(memoId)
    }

}