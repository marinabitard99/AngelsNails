package com.marinanitockina.angelsnails.model

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

data class UserState(
    val role: Role,
    val account: GoogleSignInAccount
) {

    enum class Role {
        RUNNING,
        SUCCESS,
        FAILED,
        IDLE,
    }

}


