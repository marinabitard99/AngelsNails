package com.marinanitockina.angelsnails.ui.screengeneral

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.marinanitockina.angelsnails.mvvm.models.Record
import com.marinanitockina.angelsnails.mvvm.models.UserState
import com.marinanitockina.angelsnails.ui.theme.DarkPink
import com.marinanitockina.angelsnails.ui.theme.Pink100
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalFoundationApi
@Composable
fun RecordsList(
    records: List<Record?> = emptyList(),
    role: UserState.Role = UserState.Role.CLIENT,
    dateText: String = "",
    onDeleteRecord: (String, UserState.Role) -> Unit = { _, _ -> }
) {
    if (records.isEmpty()) {
        if (role == UserState.Role.CLIENT) {
            EmptyRecordsList()
        } else {
            EmptyRecordsList("No records on $dateText")
        }
    } else {

        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
        ) {

            val date = Date()
            val grouped = records.groupBy { (it!!.time ?: 0L) > date.time }

            grouped.forEach { (isRecordUpcoming, records) ->

                stickyHeader {
                    RecordHeader(isUpcoming = isRecordUpcoming)
                }

                items(items = records) { record ->
                    RecordItem(record = record, role = role, onDeleteRecord = onDeleteRecord)
                }

            }

        }
    }
}

@Composable
fun RecordHeader(isUpcoming: Boolean = true) {
    Text(
        text = if (isUpcoming) "Upcoming appointments" else "Past appointments",
        style = MaterialTheme.typography.h6,
        color = DarkPink,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.background)
            .padding(start = 15.dp)
    )
}

@Composable
fun RecordItem(
    record: Record?,
    role: UserState.Role = UserState.Role.CLIENT,
    onDeleteRecord: (String, UserState.Role) -> Unit = { _, _ -> }
) {
    val formatter: DateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    formatter.timeZone = TimeZone.getDefault()
    val dateString = formatter.format(Date(record!!.time!!))

    val currentTime = System.currentTimeMillis()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 5.dp),
        shape = RoundedCornerShape(30.dp),
        backgroundColor = if (currentTime < record.time!!) Pink100 else Color.LightGray,
        border = BorderStroke(
            width = 2.dp,
            color = DarkPink
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 15.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "${record.nameService!!} - $dateString",
                    style = MaterialTheme.typography.h6,
                    color = DarkPink,
                    fontSize = 18.sp
                )

                if (currentTime < record.time) {

                    val deleteRecordDialogState = rememberMaterialDialogState()

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = "Delete record",
                        tint = DarkPink,
                        modifier = Modifier
                            .clickable {
                                deleteRecordDialogState.show()
                            }
                            .padding(0.dp)
                    )

                    MaterialDialog(
                        dialogState = deleteRecordDialogState,
                        buttons = {
                            positiveButton(
                                text = "Yes",
                                textStyle = TextStyle(
                                    color = DarkPink,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                onDeleteRecord(record.id!!, role)
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
                        title("Are you sure you want to delete your record?")
                    }
                }

            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "Rīga, Mangaļu iela 1",
                    modifier = Modifier
                        .padding(top = 5.dp),
                    color = DarkPink,
                    fontSize = 16.sp,
                )

                if (role == UserState.Role.ADMIN) {
                    Text(
                        text = record.nameMaster!!,
                        modifier = Modifier
                            .padding(top = 5.dp),
                        color = DarkPink,
                        fontSize = 16.sp,
                    )
                }

            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = if (role == UserState.Role.CLIENT) {
                        "${record.nameMaster!!} - ${record.phoneMaster!!}"
                    } else {
                        record.phoneClient?.let {
                            "${record.nameClient!!} - $it"
                        } ?: run {
                            "${record.nameClient!!}\n${record.emailClient}"
                        }
                    },
                    color = DarkPink,
                    fontSize = 16.sp,
                )

                Text(
                    text = record.priceService!!,
                    color = DarkPink,
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.Bottom)
                )
            }

        }

    }
}

@Preview("ClientRecord")
@Composable
fun ClientRecordPreview() {
    RecordItem(
        record = Record(
            emailClient = "test@test.com",
            nameMaster = "Alla Zurabova",
            nameService = "Manicure",
            priceService = "35.00$",
            nameClient = "Client 1",
            phoneClient = "22222222",
            phoneMaster = 232323224L,
            time = 1500000000000
        ),
        role = UserState.Role.CLIENT
    )
}

@Preview("MasterRecord")
@Composable
fun MasterRecordPreview() {
    RecordItem(
        record = Record(
            emailClient = "test@test.com",
            nameMaster = "Alla Zurabova",
            nameService = "Manicure",
            priceService = "35.00$",
            nameClient = "Client 1",
            phoneClient = "22222222",
            phoneMaster = 232323224L,
            time = 1500000000000
        ),
        role = UserState.Role.MASTER
    )
}

@Preview("AdminRecord")
@Composable
fun AdminRecordPreview() {
    RecordItem(
        record = Record(
            emailClient = "test@test.com",
            nameMaster = "Alla Zurabova",
            nameService = "Manicure",
            priceService = "35.00$",
            nameClient = "Client 1",
            phoneMaster = 232323224L,
            time = 2500000000000
        ),
        role = UserState.Role.ADMIN
    )
}