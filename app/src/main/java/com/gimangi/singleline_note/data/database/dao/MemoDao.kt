package com.gimangi.singleline_note.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.gimangi.singleline_note.data.database.dto.MemoItemEntity
import com.gimangi.singleline_note.data.database.dto.MemoTableEntity

@Dao
interface MemoDao {

    @Query("SELECT * FROM memo")
    fun getMemoAll(): List<MemoTableEntity>

    @Insert
    fun insertMemoTable(memoTableEntity: MemoTableEntity)

    @Update
    fun updateMemoTable(memoTableEntity: MemoTableEntity)

    @Query("SELECT memoList FROM memo WHERE memo_id = :memoId")
    fun getMemoItems(memoId: Int): List<MemoItemEntity>

    @Insert
    fun insertMemoItem(memoItemEntity: MemoItemEntity)

    @Update
    fun updateMemoItem(memoItemEntity: MemoItemEntity)


}
