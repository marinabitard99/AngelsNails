package com.marinanitockina.angelsnails.mvvm.models

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

// Record class
@IgnoreExtraProperties
data class Record(
    val id: String? = null,
    val emailClient: String? = null,
    val nameClient: String? = null,
    val phoneClient: String? = null,
    val idMaster: String? = null,
    val nameMaster: String? = null,
    val phoneMaster: Long? = null,
    val idService: String? = null,
    val nameService: String? = null,
    val priceService: String? = null,
    val time: Long? = null
) {
    @Exclude
    fun toMap() : Map<String, Any?> {
        return mapOf(
            "emailClient" to emailClient,
            "nameClient" to nameClient,
            "phoneClient" to phoneClient,
            "idMaster" to idMaster,
            "nameMaster" to nameMaster,
            "phoneMaster" to phoneMaster,
            "idService" to idService,
            "nameService" to nameService,
            "priceService" to priceService,
            "time" to time
        )
    }
}
