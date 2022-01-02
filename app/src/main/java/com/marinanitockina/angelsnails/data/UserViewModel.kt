package com.marinanitockina.angelsnails.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.marinanitockina.angelsnails.models.LoadingState
import com.marinanitockina.angelsnails.models.UserState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val repository = UserRepository()
    val loadingState = MutableStateFlow(LoadingState.IDLE)
    var accountState: MutableState<UserState?> = mutableStateOf(null)
        private set

    init {
        repository.notifyViewModel = { user ->
            val role = when (user?.role) {
                "master" -> UserState.Role.MASTER
                "admin" -> UserState.Role.ADMIN
                else -> UserState.Role.CLIENT
            }

            accountState.value = UserState(FirebaseAuth.getInstance().currentUser!!, role)
        }
        fetchUserRole()
    }

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

    fun fetchUserRole() {
        val currentAccount = FirebaseAuth.getInstance().currentUser
        currentAccount?.let { account ->
            account.email?.let {
                repository.getUserInfo(it)
            }
        }
    }

}