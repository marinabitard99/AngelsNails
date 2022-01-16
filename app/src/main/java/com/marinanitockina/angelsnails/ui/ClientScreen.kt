package com.marinanitockina.angelsnails.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.marinanitockina.angelsnails.R
import com.marinanitockina.angelsnails.models.Record
import com.marinanitockina.angelsnails.models.Service
import com.marinanitockina.angelsnails.models.ServiceMaster
import com.marinanitockina.angelsnails.models.UserState
import com.marinanitockina.angelsnails.ui.theme.AngelsNailsTheme
import com.marinanitockina.angelsnails.ui.theme.DarkPink
import com.marinanitockina.angelsnails.ui.theme.Pink100
import com.marinanitockina.angelsnails.ui.theme.Pink50
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


@ExperimentalFoundationApi
@Composable
fun ClientScreen(
    userState: UserState,
    records: Map<String, Record?> = emptyMap(),
    services: Map<String, Service?> = emptyMap()
) {

    Scaffold(
        topBar = {
            CenterTopAppBar(
                title = {
                    Text(text = "Welcome, ${userState.account?.displayName ?: "Welcome back"}!")
                },
                contentColor = DarkPink,
                elevation = 0.dp,
            )
        }
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
                    0 -> RecordsList(records = records)
                    1 -> ServiceList(services = services)
                }
            }


        }

    }

}

@Composable
fun RecordsList(records: Map<String, Record?>) {
    LazyColumn {
        items(items = records.values.toList()) { record ->
            UserRecord(record = record)
        }
    }
}

@Composable
fun UserRecord(record: Record?) {
    val formatter: DateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    formatter.timeZone = TimeZone.getDefault()
    val dateString = formatter.format(Date(record!!.time!!))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .height(80.dp),
        shape = RoundedCornerShape(30.dp),
        backgroundColor = Pink100,
        border = BorderStroke(
            width = 2.dp,
            color = DarkPink
        )
    ) {
        Box(modifier = Modifier.padding(horizontal = 25.dp, vertical = 15.dp)) {
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
                text = record.priceService!!,
                modifier = Modifier.align(Alignment.BottomEnd),
                color = DarkPink,
                fontSize = 16.sp,
            )
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun ServiceList(services: Map<String, Service?> = emptyMap()) {
    CompositionLocalProvider(
        LocalOverScrollConfiguration provides null
    ) {
        LazyColumn(
            modifier = Modifier.padding(
                start = 10.dp,
                end = 10.dp,
                top = 16.dp,
                bottom = 2.dp
            ),
        ) {
            items(items = services.values.toList()) { service ->
                ServiceItem(service = service!!)
            }
        }
    }
}

@Composable
fun ServiceItem(service: Service) {

    val extended = remember { mutableStateOf(false) }
    val scale = animateFloatAsState(if (extended.value) 0.95f else 1f)

    Card(shape = RoundedCornerShape(25.dp),
        modifier = Modifier
            .scale(scale.value),
        onClick = {
            extended.value = !extended.value
        }
    ) {
        Box {
            GlideImage(
                imageModel = service.pictureUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                shimmerParams = ShimmerParams(
                    baseColor = Pink50,
                    highlightColor = Pink100,
                    durationMillis = 1000,
                    dropOff = 0.65f,
                    tilt = 20f
                ),
                previewPlaceholder = R.drawable.cat
            )
            Chip(
                text = service.name ?: "",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp)
            )
            Chip(
                text = service.price!!,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(10.dp)
            )
        }
    }

    AnimatedVisibility(
        visible = extended.value,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        Column {
            Text(
                text = "Choose a master for ${service.name?.lowercase()}",
                style = MaterialTheme.typography.h6,
                color = DarkPink,
                modifier = Modifier.padding(start = 9.dp)
            )

            val expandedItem: MutableState<String?> = remember { mutableStateOf(null) }
            val expandedListHeight = animateIntAsState(
                if (expandedItem.value == null) {
                    (service.masters.size * 70)
                } else {
                    (service.masters.size * 70 + 95)
                }
            )

            LazyColumn(
                modifier = Modifier.height(
                    expandedListHeight.value.dp
                )
            ) {
                items(items = service.masters.toList()) { master ->
                    MasterCard(
                        master = master.second,
                        expanded = master.second.name == expandedItem.value,
                        onSelectClicked = {
                            expandedItem.value = it
                        })
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(14.dp))

}

@Composable
fun MasterCard(
    master: ServiceMaster = ServiceMaster(),
    expanded: Boolean = false,
    onSelectClicked: (String?) -> Unit = {}
) {

    val height = animateIntAsState(if (expanded) 165 else 70)

    Card(
        shape = RoundedCornerShape(25.dp),
        modifier = Modifier
            .height(height.value.dp)
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 9.dp),
        elevation = 2.dp,
        backgroundColor = Color.White
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
        ) {
            Box {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .fillMaxWidth()
                ) {
                    GlideImage(
                        imageModel = master.pictureUrl,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .clip(CircleShape)
                            .border(1.dp, DarkPink, CircleShape)
                            .width(43.dp)
                            .height(43.dp),
                        shimmerParams = ShimmerParams(
                            baseColor = Pink50,
                            highlightColor = Pink100,
                            durationMillis = 500,
                            dropOff = 0.65f,
                            tilt = 20f
                        ),
                    )
                    Text(
                        master.name!!,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 10.dp),
                        style = MaterialTheme.typography.body1
                    )
                }

                OutlinedButton(
                    onClick = { if (expanded) onSelectClicked(null) else onSelectClicked(master.name) },
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Pink100),
                    border = BorderStroke(1.dp, DarkPink),
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Text(
                        text = if (expanded) "HIDE" else "SELECT",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.CenterVertically),
                        color = DarkPink
                    )
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {

                Divider(
                    Modifier.height(1.dp),
                    color = Color.LightGray
                )

                Column {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    ) {
                        Text(
                            text = "Date 15.01.2022",
                            style = MaterialTheme.typography.body1,
                            color = DarkPink,
                            modifier = Modifier
                                .align(Alignment.TopStart),
                            fontSize = 18.sp
                        )

                        Text(
                            text = "Time 15:00",
                            style = MaterialTheme.typography.body1,
                            color = DarkPink,
                            modifier = Modifier
                                .align(Alignment.TopEnd),
                            fontSize = 18.sp
                        )

                    }

                    OutlinedButton(
                        onClick = { },
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Pink100),
                        border = BorderStroke(1.dp, DarkPink),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 5.dp)
                    ) {
                        Text(
                            text = "FINISH",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.CenterVertically),
                            color = DarkPink
                        )
                    }
                }

            }


//
//            val dateDialogState = rememberMaterialDialogState()
//            val timeDialogState = rememberMaterialDialogState()
//            val confirmDialogState = rememberMaterialDialogState()
//
//            Row(
//                modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                val calendar = Calendar.getInstance()
//                calendar.add(Calendar.DATE, 1)
//                val df = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
//                val formattedDate = df.format(calendar.time)
//                Text("Date: ")
//                TextButton(onClick = { /*TODO*/ }) {
//                    Text(formattedDate)
//                }
//                val availableTimes = listOf(
//                    "9:00", "10:00", "11:00", "12:00", "13:00",
//                    "14:00", "15:00", "16:00", "17:00", "18:00"
//                )
//                val shownTime = remember { availableTimes.random() }
//                Text("Time: ")
//                TextButton(onClick = {/*TODO*/ }) {
//                    Text(shownTime)
//                }
//            }

//            val dateDialogState = rememberMaterialDialogState()
//            MaterialDialog(
//                dialogState = dialogState,
//                buttons = {
//                    positiveButton(text = "Ok", textStyle = TextStyle(color = DarkPink)) {
//                        val t
//                    }
//                    negativeButton(text ="Cancel", textStyle = TextStyle(color = DarkPink))
//                }
//            ) {
//                datepicker(
//                    yearRange = IntRange(2022, 2022),
//                    colors = DatePickerDefaults.colors(
//                        headerBackgroundColor = Pink100,
//                        headerTextColor = DarkPink,
//                        activeBackgroundColor = Pink100,
//                        inactiveBackgroundColor = Color.Transparent,
//                        activeTextColor = DarkPink,
//                        inactiveTextColor = MaterialTheme.colors.onBackground
//                    )
//                ) { date ->
//                    // Do stuff with java.time.LocalDate object which is passed in
//                }
//            }

//            val dateAndTimeDialogState = rememberMaterialDialogState()
//            MaterialDialog(
//                dialogState = dateAndTimeDialogState,
//                buttons = {
//                    positiveButton("Finish", textStyle = TextStyle(color = DarkPink))
//                    negativeButton("Cancel", textStyle = TextStyle(color = DarkPink))
//                }
//            ) {
//                title(text = "Select date and time")
//
//                customView {
//                    val dateDialogState = rememberMaterialDialogState()
//                    MaterialDialog(
//                        dialogState = dialogState,
//                        buttons = {
//                            positiveButton(text = "Ok", textStyle = TextStyle(color = DarkPink))
//                            negativeButton(text = "Cancel", textStyle = TextStyle(color = DarkPink))
//                        }
//                    ) {
//                        datepicker(
//                            yearRange = IntRange(2022, 2022),
//                            colors = DatePickerDefaults.colors(
//                                headerBackgroundColor = Pink100,
//                                headerTextColor = DarkPink,
//                                activeBackgroundColor = Pink100,
//                                inactiveBackgroundColor = Color.Transparent,
//                                activeTextColor = DarkPink,
//                                inactiveTextColor = MaterialTheme.colors.onBackground
//                            )
//                        ) { date ->
//                            // Do stuff with java.time.LocalDate object which is passed in
//                        }
//                    }
//

//
//                }
//            }

        }
    }
}

@ExperimentalFoundationApi
@Preview("ClientScreen")
@Composable
fun ClientScreenPreview() {
    AngelsNailsTheme {
        ClientScreen(
            userState = UserState(null, UserState.Role.CLIENT), services = mapOf(
                Pair(
                    "a", Service(
                        "Massage",
                        "38.20€",
                        "https://www.lieliskadavana.lv/files/uploaded/programs/central_photo_20161019151003219.jpeg"
                    )
                ),
                Pair(
                    "b", Service(
                        "Massage",
                        "38.20€",
                        "https://www.lieliskadavana.lv/files/uploaded/programs/central_photo_20161019151003219.jpeg"
                    )
                ),
                Pair(
                    "c", Service(
                        "Massage",
                        "38.20€",
                        "https://www.lieliskadavana.lv/files/uploaded/programs/central_photo_20161019151003219.jpeg"
                    )
                )
            )
        )
    }
}

@ExperimentalFoundationApi
@Preview("ServiceList")
@Composable
fun ServiceListPreview() {
    ServiceList(
        services = mapOf(
            Pair(
                "a", Service(
                    "Massage",
                    "38.20€",
                    "https://www.lieliskadavana.lv/files/uploaded/programs/central_photo_20161019151003219.jpeg"
                )
            ),
            Pair(
                "b", Service(
                    "Massage",
                    "38.20€",
                    "https://www.lieliskadavana.lv/files/uploaded/programs/central_photo_20161019151003219.jpeg"
                )
            ),
            Pair(
                "c", Service(
                    "Massage",
                    "38.20€",
                    "https://www.lieliskadavana.lv/files/uploaded/programs/central_photo_20161019151003219.jpeg"
                )
            )
        )
    )
}

@Preview("Service")
@Composable
fun ServiceItemPreview() {
    ServiceItem(
        service = Service(
            "Massage",
            "38.20€",
            "https://www.lieliskadavana.lv/files/uploaded/programs/central_photo_20161019151003219.jpeg"
        )
    )
}

@Preview("UserRecord")
@Composable
fun UserRecordPreview() {
    UserRecord(
        record = Record(
            nameMaster = "Alla Zurabova",
            nameService = "Manicure",
            priceService = "35.00$",
            time = 1500000000000
        )
    )
}