package com.marinanitockina.angelsnails

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.marinanitockina.angelsnails.ui.BookingScreen
import com.marinanitockina.angelsnails.ui.LoginScreen
import com.marinanitockina.angelsnails.ui.theme.AngelsNailsTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<AppViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AngelsNailsTheme {
                    AppScreen(viewModel = viewModel)
                }
            }
        }
    }

@Composable
fun AppScreen(viewModel: AppViewModel) {
    Surface(color = MaterialTheme.colors.background) {
        val currentUser = viewModel.accountState.value
        if (currentUser == null) {
            LoginScreen(viewModel = viewModel)
        } else {
            BookingScreen(
                currentUser.account,
                currentUser.role
            )
        }
    }
}

