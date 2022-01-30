package com.marinanitockina.angelsnails

import android.util.Log
import com.marinanitockina.angelsnails.mvvm.models.Record
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

sealed class RecordSorter {

    abstract fun sortRecords(date: Date, records: List<Record?>): List<Record?>
    val millisInDay: Long = 1000 * 60 * 60 * 24

    class DayRecordSorter : RecordSorter() {

        override fun sortRecords(date: Date, records: List<Record?>): List<Record?> {

            val recordDate = Date.from(Instant.now())
            val df = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
            var formattedDate: String = df.format(date)

            Log.d("date time min",
                (date.time).toString()
            )

            Log.d("date time min word",
                formattedDate
            )

            formattedDate = df.format(date)

            Log.d("date time max",
                (date.time + millisInDay).toString()
            )

            Log.d("date time max word",
                formattedDate
            )

            records.forEach {
                Log.d("date time rec",
                    (it!!.time).toString()
                )

                val formatter: DateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                formatter.timeZone = TimeZone.getDefault()
                val dateString = formatter.format(Date(it!!.time!!))

                Log.d("date time rec word",
                    dateString
                )

                Log.d("date time tes",
                    ((it!!.time ?: 0L >= date.time) && (it.time ?: 0L < date.time + millisInDay - 1)).toString()
                )
            }

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
