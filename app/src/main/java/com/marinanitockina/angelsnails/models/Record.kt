package com.marinanitockina.angelsnails.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Record(
    val email: String? = null,
    val idMaster: String? = null,
    val nameMaster: String? = null,
    val idService: String? = null,
    val nameService: String? = null,
    val priceService: String? = null,
    val time: Long? = null
)
