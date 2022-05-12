package com.gimangi.singleline_note.data.database.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "memo_item",
    foreignKeys = [
        ForeignKey(
            entity = MemoTableEntity::class,
            parentColumns = ["memo_id"],
            childColumns = ["memo_item_id"],
            onDelete = CASCADE
        )
    ]
)
data class MemoItemEntity(
    var order: Int,
    var item: String,
    var value: Long,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "memo_item_id")
    var itemId: Int = 0
)