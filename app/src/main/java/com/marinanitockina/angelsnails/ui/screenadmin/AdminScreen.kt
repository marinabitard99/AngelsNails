package com.marinanitockina.angelsnails.ui.screenadmin

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.marinanitockina.angelsnails.RecordSorter
import com.marinanitockina.angelsnails.mvvm.models.Record
import com.marinanitockina.angelsnails.mvvm.models.Service
import com.marinanitockina.angelsnails.mvvm.models.ServiceMaster
import com.marinanitockina.angelsnails.mvvm.models.UserState
import com.marinanitockina.angelsnails.ui.screengeneral.RecordsList
import com.marinanitockina.angelsnails.ui.screengeneral.ServiceList
import com.marinanitockina.angelsnails.ui.theme.DarkPink
import com.marinanitockina.angelsnails.ui.theme.Pink100
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@ExperimentalFoundationApi
@Composable
fun AdminScreen(
    records: List<Record?> = emptyList(),
    masters: Map<String, ServiceMaster?> = emptyMap(),
    services: List<Service?> = emptyList(),
    onDeleteRecord: (String, UserState.Role) -> Unit = { _, _ -> }
) {

    // state for datepicker dialog
    val dateDialogState = rememberMaterialDialogState()

    // calendar for showing selected date, today's by default
    val calendar: Calendar by remember { mutableStateOf(Calendar.getInstance()) }
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    val df = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    var formattedDate: String by remember { mutableStateOf(df.format(calendar.time)) }

    var openCalendar = false

    // creates a PagerState that is remembered across compositions
    val pagerState = rememberPagerState()
    val pages = mutableListOf(formattedDate, "Masters", "Services")
    val scope = rememberCoroutineScope()

    // Datepicker dialog
    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(
                text = "Ok",
                textStyle = TextStyle(
                    color = DarkPink,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            negativeButton(
                text = "Cancel",
                textStyle = TextStyle(
                    color = DarkPink,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    ) {
        datepicker(
            initialDate = LocalDateTime.ofInstant(
                calendar.toInstant(),
                calendar.timeZone.toZoneId()
            ).toLocalDate(),
            colors = DatePickerDefaults.colors(
                headerBackgroundColor = Pink100,
                headerTextColor = DarkPink,
                activeBackgroundColor = Pink100,
                inactiveBackgroundColor = Color.Transparent,
                activeTextColor = DarkPink,
                inactiveTextColor = MaterialTheme.colors.onBackground
            )
        ) { selectedDate ->

            //setting calendar for showing selected date
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            val dateAsString = selectedDate.format(formatter)

            calendar.set(Calendar.DAY_OF_MONTH, dateAsString.substring(0, 2).toInt())
            calendar.set(Calendar.MONTH, dateAsString.substring(3, 5).toInt() - 1)
            calendar.set(Calendar.YEAR, dateAsString.substring(6).toInt())
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            formattedDate = selectedDate.format(formatter)
        }
    }

    // Make possible to open calendar when first page is selected, and change first tab's name
    when (pagerState.currentPage) {
        0 -> {
            openCalendar = true
            pages[0] = formattedDate
        }
        1 -> {
            openCalendar = false
            pages[0] = "Records"
        }
        2 -> {
            openCalendar = false
            pages[0] = "Records"
        }
    }

    // Column that contains TabRow and HorizontalPager
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
            // Add tabs for all of our pages (three here)
            // shows date picker dialog when clicked
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
                        if (openCalendar && index == 0) {
                            dateDialogState.show()
                        } else {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                    }
                )
            }
        }

        // We have 3 pages for masters
        HorizontalPager(
            count = pages.size,
            state = pagerState,
            verticalAlignment = Alignment.Top
        ) { page ->
            when (page) {
                // Records page
                0 -> {

                    // In records list show sorted records by day
                    val recordSorter = RecordSorter.DayRecordSorter()

                    RecordsList(
                        records = recordSorter.sortRecords(
                            date = calendar.time,
                            records = records
                        ),
                        role = UserState.Role.ADMIN,
                        dateText = formattedDate,
                        onDeleteRecord = onDeleteRecord
                    )
                }
                1 -> {
                    // Masters page
                    MastersList(masters = masters, role = UserState.Role.ADMIN)
                }
                2 -> {
                    // Service page
                    ServiceList(services = services, role = UserState.Role.ADMIN)
                }
            }
        }

    }
}
