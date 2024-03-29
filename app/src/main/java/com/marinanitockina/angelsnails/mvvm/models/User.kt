package com.marinanitockina.angelsnails.mvvm.models

import com.google.firebase.database.IgnoreExtraProperties

// User class
@IgnoreExtraProperties
data class User(
    val id: String? = null,
    val name: String? = null,
    val phone: Long? = null,
    val email: String? = null,
    val role: String? = null,
)

