package com.gimangi.singleline_note.data.database.converter

import androidx.room.TypeConverter
import com.gimangi.singleline_note.data.database.dto.MemoItemEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*

class MemoConverter {

    companion object {
        private val gson = Gson()
        private const val DATE_SAVE_FORMAT = "yyyy-MM-dd HH:mm:ss"

        @JvmStatic
        @TypeConverter
        fun strToList(str: String?): List<MemoItemEntity> {
            if (str.isNullOrEmpty())
                return mutableListOf()
            return gson.fromJson<List<MemoItemEntity>>(str)
        }

        @JvmStatic
        @TypeConverter
        fun listToStr(lst: List<MemoItemEntity>): String {
            if (lst.isEmpty()) {
                return ""
            }
            return gson.toJson(lst)
        }
    }

    @TypeConverter
    fun strToDate(str: String?): Date? {
        if (str.isNullOrEmpty())
            return null;
        return SimpleDateFormat(DATE_SAVE_FORMAT).parse(str)
    }

    @TypeConverter
    fun dateToStr(date: Date?): String? {
        if (date == null)
            return null
        return SimpleDateFormat(DATE_SAVE_FORMAT).format(date)
    }
}

inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)