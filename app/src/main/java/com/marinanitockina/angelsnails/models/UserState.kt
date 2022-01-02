package com.marinanitockina.angelsnails.models

import com.google.firebase.auth.FirebaseUser

data class UserState(
    val account: FirebaseUser,
    val role: Role,
) {

    enum class Role {
        CLIENT,
        MASTER,
        ADMIN
    }

}


