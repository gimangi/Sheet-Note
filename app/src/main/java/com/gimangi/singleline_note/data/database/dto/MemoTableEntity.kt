package com.gimangi.singleline_note.data.database.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "memo")
data class MemoTableEntity(
    var memoName: String,
    var updatedAt: Date,
    val memoList: List<MemoItemEntity>,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "memo_id")
    var memoId: Int = 0
)
