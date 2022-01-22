package com.marinanitockina.angelsnails.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.marinanitockina.angelsnails.models.Record
import com.marinanitockina.angelsnails.models.Service
import com.marinanitockina.angelsnails.models.UserState
import com.marinanitockina.angelsnails.models.UserState.Role.*
import com.marinanitockina.angelsnails.ui.theme.DarkPink
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title

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
            LoggedInAppBar(userName = userState.account?.displayName!!)
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

@Composable
fun LoggedInAppBar(userName: String = "User") {
    CenterTopAppBar(
        title = {
            Text(text = "Welcome, $userName!")
        },
        contentColor = DarkPink,
        elevation = 0.dp,
        actions = {

            val logoutDialogState = rememberMaterialDialogState()

            MaterialDialog(
                dialogState = logoutDialogState,
                buttons = {
                    positiveButton(
                        text = "Yes",
                        textStyle = TextStyle(
                            color = DarkPink,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        Firebase.auth.signOut()
                    }

                    negativeButton(
                        text = "Cancel",
                        textStyle = TextStyle(
                            color = DarkPink,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            ) {
                title("Do you sure you want to log out?")
            }

            IconButton(onClick = { logoutDialogState.show() }) {
                Icon(
                    imageVector = Icons.Rounded.ExitToApp,
                    contentDescription = "Log out",
                    tint = DarkPink
                )
            }

        }
    )
}