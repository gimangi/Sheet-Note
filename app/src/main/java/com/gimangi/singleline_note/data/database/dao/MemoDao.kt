package com.gimangi.singleline_note.data.database.dao

import androidx.room.*
import com.gimangi.singleline_note.data.database.dto.MemoItemEntity
import com.gimangi.singleline_note.data.database.dto.MemoTableEntity

@Dao
interface MemoDao {

    @Query("SELECT * FROM memo")
    fun getMemoAll(): List<MemoTableEntity>?

    @Query("SELECT * FROM memo WHERE memo_id = :memoId")
    fun getMemoById(memoId: Int): MemoTableEntity?

    @Delete
    fun deleteMemoTable(memoTableEntity: MemoTableEntity): Boolean

    @Insert
    fun insertMemoTable(memoTableEntity: MemoTableEntity)

    @Update
    fun updateMemoTable(memoTableEntity: MemoTableEntity)

    @Insert
    fun insertMemoItem(memoItemEntity: MemoItemEntity)

    @Update
    fun updateMemoItem(memoItemEntity: MemoItemEntity)


}
