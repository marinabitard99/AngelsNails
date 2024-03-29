package com.marinanitockina.angelsnails.ui.screengeneral

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.marinanitockina.angelsnails.R
import com.marinanitockina.angelsnails.mvvm.models.Record
import com.marinanitockina.angelsnails.mvvm.models.ServiceMaster
import com.marinanitockina.angelsnails.mvvm.models.UserState
import com.marinanitockina.angelsnails.ui.generalcomposables.BottomOutlineTextField
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
import com.vanpra.composematerialdialogs.message
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

// UI for master item
@Composable
fun MasterItem(
    serviceId: String = "",
    serviceName: String = "",
    servicePrice: String = "",
    master: Pair<String, ServiceMaster> = Pair("", ServiceMaster()),
    role: UserState.Role = UserState.Role.CLIENT,
    expanded: Boolean = false,
    onSelectClicked: (String?) -> Unit = {},
    onFinishClicked: (Record) -> Unit = {}
) {

    // state for allowing Clients to select master's card
    val expandable = remember {
        role == UserState.Role.CLIENT
    }

    val height = if (expandable) {
        animateIntAsState(if (expanded) 165 else 70)
    } else {
        animateIntAsState(70)
    }

    // Master's card
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
                // Top row for showing master's image, name and expandable button (if user is a client)
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

                // If user is a client, it's possible to extend the card, by clicking on button
                if (expandable) {
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

            }

            // If user is a client, it's possible to extend the card, by clicking on button
            if (expandable) {
                // Animation for expanding master's card
                AnimatedVisibility(
                    visible = expanded,
                    enter = expandVertically(),
                    exit = shrinkVertically(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {

                    // Column that contains user's selected date and time for the next procedure
                    Column {

                        // state for storing selected date
                        var date: String? by remember { mutableStateOf(null) }
                        // state for storing selected time
                        var time: String? by remember { mutableStateOf(null) }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            // Row that contains date icon, and outlined text that shows date picker
                            // upon clicking on it
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

                            // Row that contains clock icon, and outlined text that shows time picker
                            // upon clicking on it
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
                        // State of confirmation dialog
                        val confirmDialogState = rememberMaterialDialogState()

                        // Confirmation dialog that finishes reservation
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

                                    val recordTime = "$date $time"
                                    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm")
                                    val parsedDate = dateFormat.parse(recordTime)!!

                                    val currentUser = FirebaseAuth.getInstance().currentUser!!

                                    val record = Record(
                                        emailClient = currentUser.email!!,
                                        phoneClient = currentUser.phoneNumber,
                                        nameClient = currentUser.displayName,
                                        idMaster = master.first,
                                        nameMaster = master.second.name,
                                        phoneMaster = master.second.phone,
                                        idService = serviceId,
                                        nameService = serviceName,
                                        priceService = servicePrice,
                                        time = parsedDate.time
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
                            message("After submitting, please wait for the confirmation letter on your email.")
                        }

                        // Button that gets enabled upon inputting necessary reservation information
                        // and shows confirmation dialog to finish the record
                        OutlinedButton(
                            onClick = {

                                val currentTimestamp = System.currentTimeMillis()
                                val recordTime = "$date $time"
                                val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm")
                                val parsedDate = dateFormat.parse(recordTime)!!

                                if (currentTimestamp >= parsedDate.time) {
                                    Toast.makeText(
                                        context,
                                        "Invalid record time.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    confirmDialogState.show()
                                }

                            },
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
}