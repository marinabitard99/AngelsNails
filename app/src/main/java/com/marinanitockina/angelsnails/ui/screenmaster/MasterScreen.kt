package com.marinanitockina.angelsnails.ui.screenmaster

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.marinanitockina.angelsnails.models.Record
import com.marinanitockina.angelsnails.ui.generalcomposables.EmptyRecordsList
import com.marinanitockina.angelsnails.ui.theme.DarkPink
import com.marinanitockina.angelsnails.ui.theme.Pink100
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MasterScreen(records: Map<String, Record?>) {

    val pagerState = rememberPagerState()
    val pages = listOf("Today's records") //TODO CHANGE TO DATE
    val scope = rememberCoroutineScope()

    Column {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                    color = DarkPink
                )
            }
        ) {
            // Add tabs for all of our pages
            pages.forEachIndexed { index, title ->
                Tab(
                    text = {
                        Text(
                            text = title,
                            color = DarkPink,
                            fontSize = 18.sp,
                            style = MaterialTheme.typography.h6
                        )
                    },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                )
            }
        }

        HorizontalPager(
            count = pages.size,
            state = pagerState,
            verticalAlignment = Alignment.Top
        ) { page ->
            when (page) {
                0 ->  MasterRecordsList(records = records)
            }
        }

    }

}

@Composable
fun MasterRecordsList(records: Map<String, Record?>) {
    if (records.isEmpty()) {
        EmptyRecordsList("No records for today!") //TODO FOR THIS DAY
    } else {
        LazyColumn(modifier = Modifier.padding(top = 5.dp)) {
            items(items = records.values.toList()) { record ->
                MasterRecord(record = record)
            }
        }
    }

}

@Composable
fun MasterRecord(record: Record?) {
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