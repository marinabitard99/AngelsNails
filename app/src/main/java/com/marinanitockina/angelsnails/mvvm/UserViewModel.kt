package com.marinanitockina.angelsnails.mvvm

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.marinanitockina.angelsnails.mvvm.models.Record
import com.marinanitockina.angelsnails.mvvm.models.Service
import com.marinanitockina.angelsnails.mvvm.models.ServiceMaster
import com.marinanitockina.angelsnails.mvvm.models.UserState
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val repository = UserRepository()

    val loadingState = mutableStateOf(false)
    var accountState: MutableState<UserState?> = mutableStateOf(null)
        private set
    var serviceState = mutableStateListOf<Service?>()
        private set
    var userRecordsState = mutableStateListOf<Record?>()
        private set
    var masterListState = mutableStateMapOf<String, ServiceMaster?>()
        private set

    init {
        repository.userCallback = { user ->
            val role = when (user?.role) {
                "master" -> {
                    repository.getMasterRecords(user.id!!)
                    UserState.Role.MASTER
                }
                "admin" -> {
                    repository.getAllRecords()
                    repository.getServices()
                    UserState.Role.ADMIN
                }
                else -> {
                    repository.getClientRecords(FirebaseAuth.getInstance().currentUser!!.email!!)
                    repository.getServices()
                    UserState.Role.CLIENT
                }
            }

            accountState.value = UserState(FirebaseAuth.getInstance().currentUser!!, role)
        }

        repository.servicesCallback = { serviceList ->
            serviceState.clear()
            serviceState.addAll(serviceList)
            repository.getServiceMasters()
        }

        repository.serviceMastersCallback = { masterList ->
            masterListState.putAll(masterList)
            serviceState.forEach { service ->
                service?.masterIds?.forEach { masterIdFromService ->
                    masterList.forEach { receivedMaster ->
                        if (masterIdFromService.key == receivedMaster.key)
                            service.masters[masterIdFromService.key] = masterList.getValue(masterIdFromService.key)!!
                    }
                }
            }
            loadingState.value = false
        }

        repository.recordsCallback = { userRecordsList ->
            userRecordsState.clear()
            userRecordsState.addAll(userRecordsList)
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
        repository.getClientRecords(FirebaseAuth.getInstance().currentUser!!.email!!)
    }

    fun clearUserData() {
        accountState.value = null
        serviceState.clear()
        userRecordsState.clear()
        masterListState.clear()
    }

}