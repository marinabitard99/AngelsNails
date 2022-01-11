package com.marinanitockina.angelsnails.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.marinanitockina.angelsnails.models.LoadingState
import com.marinanitockina.angelsnails.models.Service
import com.marinanitockina.angelsnails.models.UserState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val repository = UserRepository()
    val loadingState = MutableStateFlow(LoadingState.IDLE)
    var accountState: MutableState<UserState?> = mutableStateOf(null)
        private set
    var serviceState = mutableStateMapOf<String, Service?>()

    init {
        repository.userCallback = { user ->
            val role = when (user?.role) {
                "master" -> UserState.Role.MASTER
                "admin" -> UserState.Role.ADMIN
                else -> {
                    repository.getServices()
                    UserState.Role.CLIENT
                }
            }

            accountState.value = UserState(FirebaseAuth.getInstance().currentUser!!, role)
        }

        repository.servicesCallback = { serviceList ->
            serviceState.clear()
            serviceState.putAll(serviceList)
            repository.getServiceMasters()
        }

        repository.serviceMastersCallback = { masterList ->
            serviceState.forEach { service ->
                service.value?.masterIds?.forEach { masterFromList ->
                    masterList.forEach { receivedMaster ->
                        if (masterFromList.key == receivedMaster.key)
                            service.value?.masters?.put(
                                masterFromList.key,
                                masterList.getValue(masterFromList.key)!!
                            )
                    }
                }
            }
        }
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

    fun fetchUserRole(email: String) {
        repository.getUserInfo(email)
    }

}