package com.gimangi.singleline_note.data.database.dto

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gimangi.singleline_note.R
import java.util.*

@Entity(tableName = "memo")
data class MemoTableEntity(
    var memoName: String,
    var suffix: String,
    var summary: Long = 0,
    var status: MemoStatus,
    var updatedAt: Date,
    val rowList: MutableList<MemoItemEntity>,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "memo_id")
    val memoId: Int = 0
)

enum class MemoStatus() {
    SUM,
    AVG,
    MAX,
    MIN
}

fun Context.getStatusString(status: MemoStatus): String {
    return when (status) {
        MemoStatus.SUM -> getString(R.string.memo_status_sum)
        MemoStatus.AVG -> getString(R.string.memo_status_avg)
        MemoStatus.MAX -> getString(R.string.memo_status_max)
        MemoStatus.MIN -> getString(R.string.memo_status_min)
    }
}