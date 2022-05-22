package com.gimangi.singleline_note.util

import com.gimangi.singleline_note.data.database.dto.MemoStatus
import com.gimangi.singleline_note.data.database.dto.MemoTableEntity

object NumeralUtil {

    fun getSummary(table: MemoTableEntity): Long {
        var sum = 0L
        for (i in table.rowList)
            sum += i.value

        return when (table.status) {
            MemoStatus.SUM -> sum
            MemoStatus.AVG -> sum / table.rowList.size
            MemoStatus.MAX -> table.rowList.maxOf { it.value }
            MemoStatus.MIN -> table.rowList.minOf { it.value }
        }
    }
}