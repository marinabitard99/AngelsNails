package com.marinanitockina.angelsnails.ui.screengeneral

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.marinanitockina.angelsnails.mvvm.models.Record
import com.marinanitockina.angelsnails.mvvm.models.Service
import com.marinanitockina.angelsnails.mvvm.models.ServiceMaster
import com.marinanitockina.angelsnails.mvvm.models.UserState
import com.marinanitockina.angelsnails.ui.generalcomposables.CenterTopAppBar
import com.marinanitockina.angelsnails.ui.screenadmin.AdminScreen
import com.marinanitockina.angelsnails.ui.screenclient.ClientScreen
import com.marinanitockina.angelsnails.ui.screenmaster.MasterScreen
import com.marinanitockina.angelsnails.ui.theme.DarkPink
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title

@ExperimentalFoundationApi
@Composable
fun SignedInScreen(
    isLoading: Boolean = false,
    userState: UserState,
    records: List<Record?> = emptyList(),
    masters: Map<String, ServiceMaster?> = emptyMap(),
    services: List<Service?> = emptyList(),
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
                UserState.Role.CLIENT -> ClientScreen(
                    records = records,
                    services = services,
                    onSaveRecord = onSaveRecord
                )
                UserState.Role.MASTER -> MasterScreen(records = records)
                UserState.Role.ADMIN -> AdminScreen(
                    records = records,
                    masters = masters,
                    services = services
                )
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
            val activity = LocalContext.current as Activity

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
                        GoogleSignIn.getClient(
                            activity,
                            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                        )
                            .signOut()
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
                title("Are you sure you want to log out?")
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