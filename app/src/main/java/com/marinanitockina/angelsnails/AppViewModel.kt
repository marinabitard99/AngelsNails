package com.marinanitockina.angelsnails

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.marinanitockina.angelsnails.model.LoadingState
import com.marinanitockina.angelsnails.model.UserState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AppViewModel : ViewModel() {

    val loadingState = MutableStateFlow(LoadingState.IDLE)
    var accountState: MutableState<UserState?> = mutableStateOf(null)
        private set

    fun signInWithEmailAndPassword(email: String, password: String) = viewModelScope.launch {
        try {
            loadingState.emit(LoadingState.LOADING)
            Firebase.auth.signInWithEmailAndPassword(email, password)
            loadingState.emit(LoadingState.LOADED)
        } catch (e: Exception) {
            loadingState.emit(LoadingState.error(e.localizedMessage))
        }
    }

    fun signWithCredential(credential: AuthCredential) {
        viewModelScope.launch {
            try {
                loadingState.emit(LoadingState.LOADING)
                Firebase.auth.signInWithCredential(credential)
                loadingState.emit(LoadingState.LOADED)
            } catch (e: Exception) {
                loadingState.emit(LoadingState.error(e.localizedMessage))
            }
        }
    }

}