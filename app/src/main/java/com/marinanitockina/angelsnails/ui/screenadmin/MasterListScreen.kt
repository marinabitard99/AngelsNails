package com.marinanitockina.angelsnails.ui.screenadmin

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.marinanitockina.angelsnails.mvvm.models.ServiceMaster
import com.marinanitockina.angelsnails.mvvm.models.UserState
import com.marinanitockina.angelsnails.ui.screengeneral.MasterItem

// UI for showing list of all masters
@Composable
fun MastersList(
    masters: Map<String, ServiceMaster?> = emptyMap(),
    role: UserState.Role = UserState.Role.ADMIN
) {
    // List that shows all masters
    LazyColumn(
        modifier = Modifier.padding(
            start = 10.dp,
            end = 10.dp,
            top = 16.dp,
            bottom = 2.dp
        ),
    ) {
        items(items = masters.toList()) { master ->
            MasterItem(
                master = Pair(master.first, master.second!!),
                role = role
            )
        }
    }
}