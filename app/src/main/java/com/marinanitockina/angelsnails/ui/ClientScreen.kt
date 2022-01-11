package com.marinanitockina.angelsnails.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
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
import com.marinanitockina.angelsnails.ui.theme.Pink100
import com.marinanitockina.angelsnails.ui.theme.Pink50
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@Composable
fun ClientScreen(userState: UserState, services: Map<String, Service?>) {

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

    AnimatedVisibility(visible = extended.value) {
        LazyColumn(modifier = Modifier.height((service.masters.size * 30).dp)) {
            items(items = service.masters.toList()) { master ->
                Text(master.second.name!!)
            }
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
            userState = UserState(null, UserState.Role.CLIENT), mapOf(
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