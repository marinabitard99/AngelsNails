package com.marinanitockina.angelsnails.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.marinanitockina.angelsnails.models.Record
import com.marinanitockina.angelsnails.models.Service
import com.marinanitockina.angelsnails.models.UserState
import com.marinanitockina.angelsnails.models.UserState.Role.*
import com.marinanitockina.angelsnails.ui.theme.DarkPink

@ExperimentalFoundationApi
@Composable
fun SignedInScreen(
    isLoading: Boolean = false,
    userState: UserState,
    records: Map<String, Record?> = emptyMap(),
    services: Map<String, Service?> = emptyMap(),
    onSaveRecord: (Record) -> Unit = {}
) {

    Scaffold(
        topBar = {
            CenterTopAppBar(
                title = {
                    Text(text = "Welcome, ${userState.account?.displayName ?: "Welcome back"}!")
                },
                contentColor = DarkPink,
                elevation = 0.dp,
            )
        }
    ) {
        if (isLoading) {
            LoadingScreen()
        } else {
            when (userState.role) {
                CLIENT -> ClientScreen(records = records, services = services, onSaveRecord = onSaveRecord)
                MASTER -> MasterScreen()
                ADMIN -> AdminScreen()
            }
        }
    }

}