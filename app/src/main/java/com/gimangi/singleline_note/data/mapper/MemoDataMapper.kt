package com.gimangi.singleline_note.data.mapper

import com.gimangi.singleline_note.data.database.dto.MemoItemEntity
import com.gimangi.singleline_note.data.model.MemoItemData

object MemoDataMapper {

    fun getMemoItemEntity(memoData: MemoItemData): MemoItemEntity {
        return MemoItemEntity(
            order = memoData.number,
            item = memoData.name,
            value = memoData.value,
            itemId = memoData.itemId
        )
    }

    fun getMemoItemData(memoEntity: MemoItemEntity): MemoItemData {
        return MemoItemData(
            number = memoEntity.order,
            name = memoEntity.item,
            value = memoEntity.value,
            itemId = memoEntity.itemId
        )
    }
}