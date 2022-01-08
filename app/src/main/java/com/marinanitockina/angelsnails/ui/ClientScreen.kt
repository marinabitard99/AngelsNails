package com.marinanitockina.angelsnails.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.marinanitockina.angelsnails.R
import com.marinanitockina.angelsnails.models.Service
import com.marinanitockina.angelsnails.models.UserState
import com.marinanitockina.angelsnails.ui.theme.DarkPink
import com.skydoves.landscapist.glide.GlideImage

@ExperimentalFoundationApi
@Composable
fun ClientScreen(userState: UserState, services: List<Service>) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Services")
                },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu Btn")
                    }
                },
                contentColor = DarkPink,
                elevation = 2.dp
            )
        }
    ) {
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
                items(items = services) { service ->
                    ServiceItem(service = service)
                }
            }
        }
    }
}

@Composable
fun ServiceItem(service: Service) {
    Card(shape = RoundedCornerShape(25.dp)) {
        Box {
            GlideImage(
                imageModel = service.pictureUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                previewPlaceholder = R.drawable.cat
            )
            Text(
                text = service.name ?: "",
                color = DarkPink,
                fontFamily = FontFamily.Serif,
                fontSize = 20.sp,
                modifier = Modifier
                    .align(Alignment.BottomStart)
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

    Spacer(modifier = Modifier.height(14.dp))

}

@ExperimentalFoundationApi
@Preview("ServiceList")
@Composable
fun ServiceListPreview() {
    ClientScreen(
        userState = UserState(null, UserState.Role.CLIENT), services = listOf(
            Service(
                "Massage",
                "38.20€",
                "https://www.lieliskadavana.lv/files/uploaded/programs/central_photo_20161019151003219.jpeg"
            ),
            Service(
                "Massage",
                "38.20€",
                "https://www.lieliskadavana.lv/files/uploaded/programs/central_photo_20161019151003219.jpeg"
            ),
            Service(
                "Massage",
                "38.20€",
                "https://www.lieliskadavana.lv/files/uploaded/programs/central_photo_20161019151003219.jpeg"
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