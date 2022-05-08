package com.gimangi.singleline_note.data.database.room

import android.content.Context
import androidx.room.*
import com.gimangi.singleline_note.data.database.converter.MemoConverter
import com.gimangi.singleline_note.data.database.dao.MemoDao
import com.gimangi.singleline_note.data.database.dto.MemoItemEntity
import com.gimangi.singleline_note.data.database.dto.MemoTableEntity

@Database(
    entities = [
        MemoItemEntity::class,
        MemoTableEntity::class
               ],
    version = 1
)
@TypeConverters(MemoConverter::class)
abstract class MemoDatabase : RoomDatabase() {
    abstract fun memoDao(): MemoDao

    companion object {
        private var instance: MemoDatabase? = null

        @Synchronized
        fun getInstance(context: Context): MemoDatabase? {
            if (instance == null) {
                synchronized(MemoDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MemoDatabase::class.java,
                        "memo-database"
                    ).build()
                }
            }
            return instance
        }
    }
}