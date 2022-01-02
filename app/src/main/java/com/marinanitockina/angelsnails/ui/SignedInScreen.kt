package com.marinanitockina.angelsnails.ui

import androidx.compose.runtime.Composable
import com.marinanitockina.angelsnails.models.UserState
import com.marinanitockina.angelsnails.models.UserState.Role.*

@Composable
fun SignedInScreen(userState: UserState) {
    when (userState.role) {
        CLIENT -> ClientScreen()
        MASTER -> MasterScreen()
        ADMIN -> AdminScreen()
    }
}