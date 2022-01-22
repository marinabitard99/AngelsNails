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
import com.marinanitockina.angelsnails.models.Record
import com.marinanitockina.angelsnails.models.Service
import com.marinanitockina.angelsnails.models.UserState
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val repository = UserRepository()

    val loadingState = mutableStateOf(false)
    var accountState: MutableState<UserState?> = mutableStateOf(null)
        private set
    var serviceState = mutableStateMapOf<String, Service?>()
        private set
    var userRecordsState = mutableStateMapOf<String, Record?>()

    init {
        repository.userCallback = { user ->
            val role = when (user?.role) {
                "master" -> {
                    loadingState.value = false
                    UserState.Role.MASTER
                }
                "admin" -> {
                    loadingState.value = false
                    UserState.Role.ADMIN
                }
                else -> {
                    repository.getUserRecords(FirebaseAuth.getInstance().currentUser!!.email!!)
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
                service.value?.masterIds?.forEach { masterIdFromService ->
                    masterList.forEach { receivedMaster ->
                        if (masterIdFromService.key == receivedMaster.key)
                            service.value?.masters?.put(
                                masterIdFromService.key,
                                masterList.getValue(masterIdFromService.key)!!
                            )
                    }
                }
            }
            loadingState.value = false
        }

        repository.userRecordsCallback = { userRecordsList ->
            userRecordsState.clear()
            userRecordsState.putAll(userRecordsList)
//            userRecordsState.toList().sortedBy { (_, value) -> value!!.time }.toMap()
            loadingState.value = false
        }
    }

    fun signWithCredential(credential: AuthCredential) {
        viewModelScope.launch {
            try {
                loadingState.value = true
                Firebase.auth.signInWithCredential(credential)
                loadingState.value = false
            } catch (e: Exception) {
                loadingState.value = false
            }
        }
    }

    fun fetchUserRole(email: String) {
        loadingState.value = true
        repository.getUserInfo(email)
    }

    fun saveRecord(record: Record) {
        loadingState.value = true
        repository.saveRecord(record)
        repository.getUserRecords(FirebaseAuth.getInstance().currentUser!!.email!!)
    }

    fun clearUserData() {
        accountState.value = null
        serviceState.clear()
        userRecordsState.clear()
    }

}