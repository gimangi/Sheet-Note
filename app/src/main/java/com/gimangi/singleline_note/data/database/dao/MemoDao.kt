package com.gimangi.singleline_note.data.database.dao

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.room.*
import com.gimangi.singleline_note.data.database.dto.MemoItemEntity
import com.gimangi.singleline_note.data.database.dto.MemoTableEntity
import java.util.*

@Dao
abstract class MemoDao {

    @Query("SELECT * FROM memo")
    abstract fun getMemoAll(): List<MemoTableEntity>?

    @Query("SELECT * FROM memo WHERE memo_id = :memoId")
    abstract fun getMemoById(memoId: Int): MemoTableEntity?

    @Query("DELETE FROM memo WHERE memo_id IN (:list)")
    abstract fun deleteMemoTables(list: List<Int>): Int

    @Insert
    abstract fun insertMemoTable(memoTableEntity: MemoTableEntity)

    @Update
    abstract fun updateMemoTable(memoTableEntity: MemoTableEntity)

    @Transaction
    open fun addMemoItem(memoTableEntity: MemoTableEntity, memoItemEntity: MemoItemEntity) {
        insertMemoItem(memoItemEntity)
        updateMemoTable(
            memoTableEntity.apply {
                updatedAt = Date()
                memoList.add(memoItemEntity)
            }
        )
    }

    @Insert
    abstract fun insertMemoItem(memoItemEntity: MemoItemEntity)

    @Update
    abstract fun updateMemoItem(memoItemEntity: MemoItemEntity)


}
