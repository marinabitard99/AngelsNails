package com.marinanitockina.angelsnails.ui

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.marinanitockina.angelsnails.models.UserState

@Composable
fun SignedInScreen(userState: UserState) {
    Toast.makeText(LocalContext.current, "hallo ${userState.account.displayName}", Toast.LENGTH_SHORT).show()
}