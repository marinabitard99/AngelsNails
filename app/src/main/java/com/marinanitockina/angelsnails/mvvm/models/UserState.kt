package com.marinanitockina.angelsnails.mvvm.models

import com.google.firebase.auth.FirebaseUser

// UserState class that contains user's data and their role
sealed class UserState(
    val account: FirebaseUser?,
    val role: Role
) {

    class ClientState(account: FirebaseUser?, role: Role) :
        UserState(account, role)

    class MasterState(account: FirebaseUser?, role: Role, val masterId: String) :
        UserState(account, role)

    enum class Role {
        CLIENT,
        MASTER,
        ADMIN
    }

}


