package com.marinanitockina.angelsnails

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.google.firebase.auth.FirebaseAuth
import com.marinanitockina.angelsnails.mvvm.UserViewModel
import com.marinanitockina.angelsnails.ui.screengeneral.LoginScreen
import com.marinanitockina.angelsnails.ui.screengeneral.SignedInScreen
import com.marinanitockina.angelsnails.ui.theme.AngelsNailsTheme

@ExperimentalFoundationApi
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<UserViewModel>()

    // App starting point
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFirebaseAuthStateListener()
        setContent {
            AngelsNailsTheme {
                AppScreen(viewModel = viewModel)
            }
        }
    }

    // Sets firebase auth listener and changes user state
    private fun setFirebaseAuthStateListener() {
        FirebaseAuth.getInstance().addAuthStateListener { auth ->

            Log.d("Auth changed", auth.currentUser?.email ?: "null")
            if (auth.currentUser != null) {
                auth.currentUser?.email?.let { it ->
                    viewModel.fetchUserRole(it)
                }
            } else {
                viewModel.clearUserData()
            }

        }
    }

}

// App's main UI composable
@ExperimentalFoundationApi
@Composable
fun AppScreen(viewModel: UserViewModel = UserViewModel()) {
    Surface(color = MaterialTheme.colors.background) {
        val currentUser = viewModel.accountState.value
        if (currentUser == null) {
            // Show login screen when user is not logged in
            LoginScreen(
                loginMethod = viewModel::signWithCredential,
                isLoading = viewModel.loadingState.value
            )
        } else {
            // Show signed in screen when user is logged in
            SignedInScreen(
                isLoading = viewModel.loadingState.value,
                userState = currentUser,
                records = viewModel.userRecordsState,
                masters = viewModel.masterListState,
                services = viewModel.serviceState,
                onSaveRecord = viewModel::saveRecord,
                onDeleteRecord = viewModel::deleteRecord
            )
        }
    }
}

