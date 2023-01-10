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

    // Upon initiating setting callbacks in repository for changing app states
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

            accountState.value = if (role == UserState.Role.MASTER) {
                UserState.MasterState(FirebaseAuth.getInstance().currentUser!!, role, user?.id!!)
            } else {
                UserState.ClientState(FirebaseAuth.getInstance().currentUser!!, role)
            }

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

    // Tries to sign in a user with the given AuthCredential
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

    // For getting all user information from the database
    fun fetchUserRole(email: String) {
        loadingState.value = true
        repository.getUserInfo(email)
    }

    // For saving new record in the database
    fun saveRecord(record: Record) {
        loadingState.value = true
        repository.saveRecord(record)
        repository.getClientRecords(FirebaseAuth.getInstance().currentUser!!.email!!)
    }

    // For removing selected record from the database
    fun deleteRecord(recordId: String, role: UserState.Role) {
        repository.deleteRecord(recordId)
        when (role) {
            UserState.Role.CLIENT -> repository.getClientRecords(FirebaseAuth.getInstance().currentUser!!.email!!)
            UserState.Role.MASTER -> {
                val accountState = accountState.value as UserState.MasterState
                repository.getMasterRecords(accountState.masterId)
            }
            UserState.Role.ADMIN -> repository.getAllRecords()
        }
    }

    // For clearing all app states and returning to login screen
    fun clearUserData() {
        accountState.value = null
        serviceState.clear()
        userRecordsState.clear()
        masterListState.clear()
    }

}