package com.marinanitockina.angelsnails.ui.screengeneral

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marinanitockina.angelsnails.R
import com.marinanitockina.angelsnails.mvvm.models.Record
import com.marinanitockina.angelsnails.mvvm.models.Service
import com.marinanitockina.angelsnails.mvvm.models.UserState
import com.marinanitockina.angelsnails.ui.generalcomposables.Chip
import com.marinanitockina.angelsnails.ui.theme.DarkPink
import com.marinanitockina.angelsnails.ui.theme.Pink100
import com.marinanitockina.angelsnails.ui.theme.Pink50
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage

// UI for showing list of services
@ExperimentalFoundationApi
@Composable
fun ServiceList(
    services: List<Service?> = emptyList(),
    onFinishClicked: (Record) -> Unit = {},
    role: UserState.Role = UserState.Role.CLIENT
) {
    CompositionLocalProvider(
        LocalOverScrollConfiguration provides null
    ) {
        // List of services
        LazyColumn(
            modifier = Modifier.padding(
                start = 10.dp,
                end = 10.dp,
                top = 16.dp,
                bottom = 2.dp
            ),
        ) {
            items(items = services) { service ->
                ServiceItem(service = service, onFinishClicked = onFinishClicked, role = role)
            }
        }
    }
}

// UI for service
@Composable
fun ServiceItem(
    service: Service? = Service(),
    onFinishClicked: (Record) -> Unit = {},
    role: UserState.Role = UserState.Role.CLIENT
) {

    // state for calculating master's item heights
    val expandable = remember {
        role == UserState.Role.CLIENT
    }

    // state for clicking on service cards
    val extended = remember { mutableStateOf(false) }
    val scale = animateFloatAsState(if (extended.value) 0.95f else 1f)

    // Service's container
    Card(shape = RoundedCornerShape(25.dp),
        modifier = Modifier
            .scale(scale.value),
        onClick = {
            extended.value = !extended.value
        }
    ) {
        // Box with image in the background, price in top left corner
        // and procedure's name in bottom right corner
        Box {
            GlideImage(
                imageModel = service?.pictureUrl,
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
                text = service?.name ?: "",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp)
            )
            Chip(
                text = service?.price ?: "",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(10.dp)
            )
        }
    }

    // Service card's animation for showing list of masters
    AnimatedVisibility(
        visible = extended.value,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        Column {
            Text(
                text = if (role == UserState.Role.CLIENT) {
                    "Choose a master for ${service?.name?.lowercase() ?: ""}"
                } else {
                    "Masters for ${service?.name?.lowercase() ?: ""}"
                },
                style = MaterialTheme.typography.h6,
                color = DarkPink,
                modifier = Modifier.padding(start = 9.dp)
            )

            // state of selected master's card
            val expandedItem: MutableState<String?> = remember { mutableStateOf(null) }
            // height of selected/not selected master's card
            val expandedListHeight = animateIntAsState(
                if (!expandable) {
                    ((service?.masters?.size ?: 0) * 70)
                } else if (expandedItem.value == null) {
                    ((service?.masters?.size ?: 0) * 70)
                } else {
                    ((service?.masters?.size ?: 0) * 70 + 95)
                }

            )

            // List of service's masters with determined height
            LazyColumn(
                modifier = Modifier.height(
                    expandedListHeight.value.dp
                )
            ) {
                items(items = service?.masters?.toList() ?: emptyList()) { master ->
                    MasterItem(
                        serviceId = service?.id!!,
                        serviceName = service.name ?: "",
                        servicePrice = service.price ?: "",
                        master = master,
                        role = role,
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

// Preview of service list
@ExperimentalFoundationApi
@Preview("ServiceList")
@Composable
fun ServiceListPreview() {
    ServiceList(
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

// Preview of service card
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