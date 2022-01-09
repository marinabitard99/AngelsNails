package com.marinanitockina.angelsnails.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.marinanitockina.angelsnails.R
import com.marinanitockina.angelsnails.models.Service
import com.marinanitockina.angelsnails.models.UserState
import com.marinanitockina.angelsnails.ui.theme.AngelsNailsTheme
import com.marinanitockina.angelsnails.ui.theme.DarkPink
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@Composable
fun ClientScreen(userState: UserState, services: List<Service>) {

    Scaffold(
        topBar = {
            CenterTopAppBar(
                title = {
                    Text(text = "Welcome, ${userState.account?.displayName ?: "Welcome back"}!  ")
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
                                fontSize = 16.sp,
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
            ) { page ->
                when (page) {
                    0 -> Text("Hello content")
                    1 -> ServiceList(services = services)
                }
            }


        }

    }

}

@ExperimentalFoundationApi
@Composable
fun ServiceList(services: List<Service> = emptyList()) {
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
            Chip(
                text = service.name ?: "",
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
@Preview("ClientScreen")
@Composable
fun ClientScreenPreview() {
    AngelsNailsTheme {
        ClientScreen(
            userState = UserState(null, UserState.Role.CLIENT), listOf(
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
}

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