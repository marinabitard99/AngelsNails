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
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

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

    val expandable = remember {
        role == UserState.Role.CLIENT
    }

    val height = if (expandable) {
        animateIntAsState(if (expanded) 165 else 70)
    } else {
        animateIntAsState(70)
    }

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

            if (expandable) {
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

                                    val recordTime = "$date $time"
                                    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm")
                                    val parsedDate = dateFormat.parse(recordTime)!!

                                    val record = Record(
                                        email = FirebaseAuth.getInstance().currentUser!!.email!!,
                                        idMaster = master.first,
                                        nameMaster = master.second.name,
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
                        }

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