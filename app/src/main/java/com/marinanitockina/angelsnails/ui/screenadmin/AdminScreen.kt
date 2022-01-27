package com.marinanitockina.angelsnails.ui.screenadmin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.marinanitockina.angelsnails.R
import com.marinanitockina.angelsnails.mvvm.models.Record
import com.marinanitockina.angelsnails.mvvm.models.Service
import com.marinanitockina.angelsnails.mvvm.models.ServiceMaster
import com.marinanitockina.angelsnails.mvvm.models.UserState
import com.marinanitockina.angelsnails.ui.generalcomposables.Chip
import com.marinanitockina.angelsnails.ui.screengeneral.RecordsList
import com.marinanitockina.angelsnails.ui.theme.DarkPink
import com.marinanitockina.angelsnails.ui.theme.Pink100
import com.marinanitockina.angelsnails.ui.theme.Pink50
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@Composable
fun AdminScreen(
    records: List<Record?> = emptyList(),
    masters: Map<String, ServiceMaster?> = emptyMap(),
    services: List<Service?> = emptyList(),
) {
    val pagerState = rememberPagerState()
    val pages = listOf("Records", "Masters", "Services")
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
                0 -> RecordsList(records = records, role = UserState.Role.ADMIN)
                1 -> MastersList(masters = masters)
                2 -> AdminServiceList(services = services)
            }
        }

    }
}

@Composable
fun MastersList(masters: Map<String, ServiceMaster?> = emptyMap()) {
    LazyColumn(
        modifier = Modifier.padding(
            start = 10.dp,
            end = 10.dp,
            top = 16.dp,
            bottom = 2.dp
        ),
    ) {
        items(items = masters.toList()) { master ->
            MasterItem(master = master.second)
        }
    }
}

@Composable
fun MasterItem(master: ServiceMaster?) {
    Card(
        shape = RoundedCornerShape(25.dp),
        modifier = Modifier
            .height(70.dp)
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
                        imageModel = master?.pictureUrl,
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
                        master?.name ?: "",
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 10.dp),
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun AdminServiceList(
    services: List<Service?> = emptyList(),
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
                AdminServiceItem(service = service)
            }
        }
    }
}

@Composable
fun AdminServiceItem(
    service: Service? = Service(),
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

    AnimatedVisibility(
        visible = extended.value,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        Column {
            Text(
                text = "Masters for ${service?.name?.lowercase() ?: ""}",
                style = MaterialTheme.typography.h6,
                color = DarkPink,
                modifier = Modifier.padding(start = 9.dp)
            )

            LazyColumn(
                modifier = Modifier.height(
                    ((service?.masters?.size ?: 0) * 70).dp
                )
            ) {
                items(items = service?.masters?.toList() ?: emptyList()) { master ->
                    MasterItem(
                        master = master.second,
                    )
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(14.dp))
}
