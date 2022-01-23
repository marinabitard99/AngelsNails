package com.marinanitockina.angelsnails.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var id: String? = null,
    val name: String? = null,
    val phone: Long? = null,
    val email: String? = null,
    val role: String? = null,
)

