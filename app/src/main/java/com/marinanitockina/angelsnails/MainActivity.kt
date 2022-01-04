package com.marinanitockina.angelsnails

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.google.firebase.auth.FirebaseAuth
import com.marinanitockina.angelsnails.data.UserViewModel
import com.marinanitockina.angelsnails.ui.LoginScreen
import com.marinanitockina.angelsnails.ui.SignedInScreen
import com.marinanitockina.angelsnails.ui.theme.AngelsNailsTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseAuth.getInstance().addAuthStateListener { auth ->
            Log.d("Auth changed", auth.currentUser?.email ?: "null")
            auth.currentUser?.email?.let { it ->
                viewModel.fetchUserRole(it)
            }
        }

        setContent {
            AngelsNailsTheme {
                AppScreen(viewModel = viewModel)
            }
        }
    }

}

@Composable
fun AppScreen(viewModel: UserViewModel) {
    Surface(color = MaterialTheme.colors.background) {
        val currentUser = viewModel.accountState.value
        if (currentUser == null) {
            LoginScreen(viewModel = viewModel)
        } else {
            SignedInScreen(userState = currentUser, services = viewModel.serviceState)
        }
    }
}

