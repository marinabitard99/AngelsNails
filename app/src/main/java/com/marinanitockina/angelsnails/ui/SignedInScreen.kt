package com.marinanitockina.angelsnails.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import com.marinanitockina.angelsnails.models.Record
import com.marinanitockina.angelsnails.models.Service
import com.marinanitockina.angelsnails.models.UserState
import com.marinanitockina.angelsnails.models.UserState.Role.*

@ExperimentalFoundationApi
@Composable
fun SignedInScreen(userState: UserState, records: Map<String, Record?>, services: Map<String, Service?>) {
    when (userState.role) {
        CLIENT -> ClientScreen(userState = userState, records = records, services = services)
        MASTER -> MasterScreen()
        ADMIN -> AdminScreen()
    }
}