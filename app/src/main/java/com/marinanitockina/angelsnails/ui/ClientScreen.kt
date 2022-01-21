package com.marinanitockina.angelsnails.ui

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.google.firebase.auth.FirebaseAuth
import com.marinanitockina.angelsnails.R
import com.marinanitockina.angelsnails.models.Record
import com.marinanitockina.angelsnails.models.Service
import com.marinanitockina.angelsnails.models.ServiceMaster
import com.marinanitockina.angelsnails.ui.theme.AngelsNailsTheme
import com.marinanitockina.angelsnails.ui.theme.DarkPink
import com.marinanitockina.angelsnails.ui.theme.Pink100
import com.marinanitockina.angelsnails.ui.theme.Pink50
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*


@ExperimentalFoundationApi
@Composable
fun ClientScreen(
    records: Map<String, Record?> = emptyMap(),
    services: Map<String, Service?> = emptyMap(),
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
                0 -> UserRecordsList(records = records)
                1 -> ServiceList(services = services, onFinishClicked = onSaveRecord)
            }
        }

    }

}

@ExperimentalFoundationApi
@Composable
fun ServiceList(services: Map<String, Service?> = emptyMap(), onFinishClicked: (Record) -> Unit = {}) {
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
            items(items = services.toList()) { service ->
                ServiceItem(service = service, onFinishClicked = onFinishClicked)
            }
        }
    }
}

@Composable
fun ServiceItem(
    service: Pair<String, Service?> = Pair("", Service()),
    onFinishClicked: (Record) -> Unit = {}
) {

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
                imageModel = service.second?.pictureUrl,
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
                text = service.second?.name ?: "",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp)
            )
            Chip(
                text = service.second?.price ?: "",
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
                text = "Choose a master for ${service.second?.name?.lowercase() ?: ""}",
                style = MaterialTheme.typography.h6,
                color = DarkPink,
                modifier = Modifier.padding(start = 9.dp)
            )

            val expandedItem: MutableState<String?> = remember { mutableStateOf(null) }
            val expandedListHeight = animateIntAsState(
                if (expandedItem.value == null) {
                    ((service.second?.masters?.size ?: 0) * 70)
                } else {
                    ((service.second?.masters?.size ?: 0) * 70 + 95)
                }
            )

            LazyColumn(
                modifier = Modifier.height(
                    expandedListHeight.value.dp
                )
            ) {
                items(items = service.second?.masters?.toList() ?: emptyList()) { master ->
                    MasterCard(
                        serviceId = service.first,
                        serviceName = service.second?.name ?: "",
                        servicePrice = service.second?.price ?: "",
                        master = master,
                        expanded = master.second.name == expandedItem.value,
                        onSelectClicked = {
                            expandedItem.value = it
                        },
                        onFinishClicked = onFinishClicked
                    )
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(14.dp))

}

@Composable
fun MasterCard(
    serviceId: String = "",
    serviceName: String = "",
    servicePrice: String = "",
    master: Pair<String, ServiceMaster> = Pair("", ServiceMaster()),
    expanded: Boolean = false,
    onSelectClicked: (String?) -> Unit = {},
    onFinishClicked: (Record) -> Unit = {}
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
                        imageModel = master.second.pictureUrl,
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
                        previewPlaceholder = R.drawable.cat
                    )
                    Text(
                        master.second.name!!,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 10.dp),
                        style = MaterialTheme.typography.body1
                    )
                }

                OutlinedButton(
                    onClick = { if (expanded) onSelectClicked(null) else onSelectClicked(master.second.name) },
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

                Column {

                    var date: String? by remember { mutableStateOf(null) }
                    var time: String? by remember { mutableStateOf(null) }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.align(Alignment.TopStart)) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_baseline_event_24),
                                contentDescription = "Date"
                            )

                            val calendar = Calendar.getInstance()
                            calendar.add(Calendar.DATE, 1)
                            val df = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                            val formattedDate = df.format(calendar.time)

                            val dateDialogState = rememberMaterialDialogState()

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
                                    title = "SELECT DATE OF APPOINTMENT",
                                    yearRange = IntRange(
                                        calendar.get(Calendar.YEAR),
                                        calendar.get(Calendar.YEAR)
                                    ),
                                    colors = DatePickerDefaults.colors(
                                        headerBackgroundColor = Pink100,
                                        headerTextColor = DarkPink,
                                        activeBackgroundColor = Pink100,
                                        inactiveBackgroundColor = Color.Transparent,
                                        activeTextColor = DarkPink,
                                        inactiveTextColor = MaterialTheme.colors.onBackground
                                    )
                                ) { selectedDate ->
                                    val formatter: DateTimeFormatter =
                                        DateTimeFormatter.ofPattern("dd.MM.yyyy")
                                    val formattedString: String = selectedDate.format(formatter)
                                    date = formattedString
                                }
                            }

                            BottomOutlineTextField(
                                placeholder = formattedDate,
                                value = date ?: "",
                                onValueChange = { date = it },
                                modifier = Modifier
                                    .width(90.dp)
                                    .padding(start = 5.dp)
                                    .align(Alignment.CenterVertically)
                                    .clickable { dateDialogState.show() }
                            )
                        }

                        Row(modifier = Modifier.align(Alignment.TopEnd)) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_outline_access_time_24),
                                contentDescription = "Date"
                            )

                            val availableTimes = listOf(
                                "10:00", "11:00", "12:00", "13:00",
                                "14:00", "15:00", "16:00", "17:00"
                            )
                            val shownTime = remember { availableTimes.random() }

                            val timeDialogState = rememberMaterialDialogState()

                            MaterialDialog(
                                dialogState = timeDialogState,
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
                                val formatter = DateTimeFormatter.ofPattern("HH:mm")
                                timepicker(
                                    initialTime = LocalTime.parse(shownTime, formatter),
                                    title = "SELECT TIME OF APPOINTMENT",
                                    timeRange = LocalTime.of(10, 0)..LocalTime.of(17, 0),
                                    is24HourClock = true,
                                    colors = TimePickerDefaults.colors(
                                        activeBackgroundColor = Pink100,
                                        inactiveBackgroundColor = Color.Transparent,
                                        activeTextColor = DarkPink,
                                        inactiveTextColor = MaterialTheme.colors.onBackground
                                    )
                                ) { selectedTime ->
                                    val formattedString: String = selectedTime.format(formatter)
                                    time = formattedString
                                }
                            }

                            BottomOutlineTextField(
                                placeholder = shownTime,
                                value = time ?: "",
                                onValueChange = { time = it },
                                modifier = Modifier
                                    .width(50.dp)
                                    .padding(start = 5.dp)
                                    .align(Alignment.CenterVertically)
                                    .clickable { timeDialogState.show() }
                            )

                        }

                    }

                    val context = LocalContext.current
                    val confirmDialogState = rememberMaterialDialogState()

                    MaterialDialog(
                        dialogState = confirmDialogState,
                        buttons = {
                            positiveButton(
                                text = "Yes",
                                textStyle = TextStyle(
                                    color = DarkPink,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                val timeStamp = System.currentTimeMillis()
                                val record = Record(
                                    email = FirebaseAuth.getInstance().currentUser!!.email!!,
                                    idMaster = master.first,
                                    nameMaster = master.second.name,
                                    idService = serviceId,
                                    nameService = serviceName,
                                    priceService = servicePrice,
                                    time = timeStamp
                                )
                                onFinishClicked(record)
                                Toast.makeText(
                                    context,
                                    "Record saved successfully!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
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
                        title("Do you want to finish the reservation?")
                    }

                    OutlinedButton(
                        onClick = { confirmDialogState.show() },
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Pink100),
                        border = BorderStroke(1.dp, DarkPink),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 12.dp),
                        enabled = !date.isNullOrEmpty() && !time.isNullOrEmpty()
                    ) {
                        Text(
                            text = "SIGN UP",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.CenterVertically),
                            color = DarkPink
                        )
                    }
                }

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
        service = Pair(
            "service", Service(
                "Massage",
                "38.20€",
                "https://www.lieliskadavana.lv/files/uploaded/programs/central_photo_20161019151003219.jpeg"
            )
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