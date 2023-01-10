package com.marinanitockina.angelsnails.mvvm.models

import com.google.firebase.database.IgnoreExtraProperties

// ServiceMaster class
@IgnoreExtraProperties
data class ServiceMaster(
    val name: String? = null,
    val phone: Long? = null,
    val pictureUrl: String? = null
)
