package com.marinanitockina.angelsnails.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import com.marinanitockina.angelsnails.models.Service
import com.marinanitockina.angelsnails.models.UserState
import com.marinanitockina.angelsnails.models.UserState.Role.*

@ExperimentalFoundationApi
@Composable
fun SignedInScreen(userState: UserState, services: List<Service>) {
    when (userState.role) {
        CLIENT -> ClientScreen(userState = userState, services = services)
        MASTER -> MasterScreen()
        ADMIN -> AdminScreen()
    }
}