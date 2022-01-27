package com.marinanitockina.angelsnails.mvvm.models

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Record(
    val id: String? = null,
    val email: String? = null,
    val idMaster: String? = null,
    val nameMaster: String? = null,
    val idService: String? = null,
    val nameService: String? = null,
    val priceService: String? = null,
    val time: Long? = null
) {
    @Exclude
    fun toMap() : Map<String, Any?> {
        return mapOf(
            "email" to email,
            "idMaster" to idMaster,
            "nameMaster" to nameMaster,
            "idService" to idService,
            "nameService" to nameService,
            "priceService" to priceService,
            "time" to time
        )
    }
}
