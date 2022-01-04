package com.marinanitockina.angelsnails.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.marinanitockina.angelsnails.models.Service
import com.marinanitockina.angelsnails.models.UserState


@Composable
fun ClientScreen(userState: UserState, services: List<Service>) {
    LazyColumn() {
        items(items = services) { service ->
            Text(text = service.name ?: "null")
        }
    }
}