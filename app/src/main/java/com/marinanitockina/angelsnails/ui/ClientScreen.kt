package com.marinanitockina.angelsnails.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.marinanitockina.angelsnails.models.Service
import com.marinanitockina.angelsnails.models.UserState
import com.skydoves.landscapist.glide.GlideImage


@Composable
fun ClientScreen(userState: UserState, services: List<Service>) {
    LazyColumn() {
        items(items = services) { service ->
            ServiceItem(service = service)
        }
    }
}

@Composable
fun ServiceItem(service: Service) {
    GlideImage(imageModel = service.pictureUrl,
    modifier = Modifier
        .fillMaxWidth()
        .height(80.dp))
    Text(text = service.name ?: "null")
}