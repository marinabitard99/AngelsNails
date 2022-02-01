package com.marinanitockina.angelsnails.ui.screenclient

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.marinanitockina.angelsnails.RecordSorter
import com.marinanitockina.angelsnails.mvvm.models.Record
import com.marinanitockina.angelsnails.mvvm.models.Service
import com.marinanitockina.angelsnails.mvvm.models.UserState
import com.marinanitockina.angelsnails.ui.screengeneral.RecordsList
import com.marinanitockina.angelsnails.ui.screengeneral.ServiceList
import com.marinanitockina.angelsnails.ui.theme.AngelsNailsTheme
import com.marinanitockina.angelsnails.ui.theme.DarkPink
import kotlinx.coroutines.launch
import java.util.*

@ExperimentalFoundationApi
@Composable
fun ClientScreen(
    records: List<Record?> = emptyList(),
    services: List<Service?> = emptyList(),
    onSaveRecord: (Record) -> Unit = {}
) {

    val pagerState = rememberPagerState()
    val pages = listOf("My Records", "Book a Service")
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
                    }
                )
            }
        }

        HorizontalPager(
            count = pages.size,
            state = pagerState,
            verticalAlignment = Alignment.Top
        ) { page ->
            when (page) {
                0 -> {

                    val recordSorter = RecordSorter.ClientRecordSorter()

                    RecordsList(
                        records = recordSorter.sortRecords(Date(), records),
                        role = UserState.Role.CLIENT
                    )
                }
                1 -> ServiceList(
                    services = services,
                    onFinishClicked = onSaveRecord,
                    role = UserState.Role.CLIENT
                )
            }
        }

    }

}

@ExperimentalFoundationApi
@Preview("ClientScreen")
@Composable
fun ClientScreenPreview() {
    AngelsNailsTheme {
        ClientScreen(
            services = listOf(
                Service(
                    "Massage",
                    "38.20€",
                    "https://www.lieliskadavana.lv/files/uploaded/programs/central_photo_20161019151003219.jpeg"
                ), Service(
                    "Massage",
                    "38.20€",
                    "https://www.lieliskadavana.lv/files/uploaded/programs/central_photo_20161019151003219.jpeg"
                ), Service(
                    "Massage",
                    "38.20€",
                    "https://www.lieliskadavana.lv/files/uploaded/programs/central_photo_20161019151003219.jpeg"
                )
            )
        )
    }
}