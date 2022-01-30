package com.marinanitockina.angelsnails

import com.marinanitockina.angelsnails.mvvm.models.Record
import java.util.*

sealed class RecordSorter {

    abstract fun sortRecords(date: Date, records: List<Record?>): List<Record?>
    val millisInDay: Long = 1000 * 60 * 60 * 24

    class DayRecordSorter : RecordSorter() {

        override fun sortRecords(date: Date, records: List<Record?>): List<Record?> {

            return records.filter {
                (it!!.time ?: 0L >= date.time) && (it.time ?: 0L < date.time + millisInDay)
            }.sortedWith(
                compareBy { it!!.time }
            )
        }

    }

    class ClientRecordSorter : RecordSorter() {
        override fun sortRecords(date: Date, records: List<Record?>): List<Record?> {
            TODO("Not yet implemented")
        }

    }

}
