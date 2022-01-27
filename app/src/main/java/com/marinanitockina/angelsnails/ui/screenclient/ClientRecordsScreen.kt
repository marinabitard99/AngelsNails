package com.marinanitockina.angelsnails.ui.screenclient

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.marinanitockina.angelsnails.mvvm.models.Record
import com.marinanitockina.angelsnails.ui.generalcomposables.EmptyRecordsList
import com.marinanitockina.angelsnails.ui.theme.DarkPink
import com.marinanitockina.angelsnails.ui.theme.Pink100
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun ClientRecordsList(records: List<Record?> = emptyList()) {
    if (records.isEmpty()) {
        EmptyRecordsList("No records yet!")
    } else {
        LazyColumn(modifier = Modifier.padding(top = 5.dp)) {
            items(items = records) { record ->
                ClientRecord(record = record)
            }
        }
    }
}

@Composable
fun ClientRecord(record: Record?) {
    val formatter: DateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    formatter.timeZone = TimeZone.getDefault()
    val dateString = formatter.format(Date(record!!.time!!))

    val currentTime = System.currentTimeMillis()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 5.dp)
            .height(100.dp),
        shape = RoundedCornerShape(30.dp),
        backgroundColor = if (currentTime < record.time!!) Pink100 else Color.LightGray,
        border = BorderStroke(
            width = 2.dp,
            color = DarkPink
        )
    ) {
        Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 15.dp)) {
            Text(
                text = "${record.nameService!!} - $dateString",
                modifier = Modifier.align(Alignment.TopStart),
                style = MaterialTheme.typography.h6,
                color = DarkPink,
                fontSize = 18.sp
            )
            Text(
                text = record.nameMaster!!,
                modifier = Modifier.align(Alignment.BottomStart),
                color = DarkPink,
                fontSize = 16.sp,
            )
            Text(
                text = "Rīga, Mangaļu iela 1",
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(top = 5.dp),
                color = DarkPink,
                fontSize = 16.sp,
            )
            Text(
                text = record.priceService!!,
                modifier = Modifier.align(Alignment.BottomEnd),
                color = DarkPink,
                fontSize = 16.sp,
            )
        }
    }
}

@Preview("ClientRecord")
@Composable
fun ClientRecordPreview() {
    ClientRecord(
        record = Record(
            nameMaster = "Alla Zurabova",
            nameService = "Manicure",
            priceService = "35.00$",
            time = 1500000000000
        )
    )
}