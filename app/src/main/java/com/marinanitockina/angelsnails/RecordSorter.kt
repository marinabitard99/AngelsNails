package com.marinanitockina.angelsnails

import com.marinanitockina.angelsnails.mvvm.models.Record
import java.util.*

// Class for sorting records based on user
sealed class RecordSorter {

    abstract fun sortRecords(date: Date, records: List<Record?>): List<Record?>
    val millisInDay: Long = 1000 * 60 * 60 * 24

    class DayRecordSorter : RecordSorter() {

        // Sorts records by selected day
        override fun sortRecords(date: Date, records: List<Record?>): List<Record?> {

            return records.filter {
                (it!!.time ?: 0L >= date.time) && (it.time ?: 0L < date.time + millisInDay)
            }.sortedWith(
                compareBy { it!!.time }
            )
        }

    }

    class ClientRecordSorter : RecordSorter() {

        // Sorts records by relevancy (closest first, after that past)
        override fun sortRecords(date: Date, records: List<Record?>): List<Record?> {

            val upcomingRecords: MutableList<Record?> = records.filter {
                (it!!.time ?: 0L > date.time)
            }.sortedWith(
                compareBy { it!!.time }
            ).toMutableList()
            return upcomingRecords.also {
                it.addAll(records.filter { record ->
                    (record!!.time ?: 0L <= date.time)
                }.sortedByDescending { record ->
                    record!!.time
                })
            }

        }

    }

}
