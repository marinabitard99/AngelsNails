package com.marinanitockina.angelsnails.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ServiceMaster(
    val name: String? = null,
    val phone: Long? = null,
    val pictureUrl: String? = null
)
