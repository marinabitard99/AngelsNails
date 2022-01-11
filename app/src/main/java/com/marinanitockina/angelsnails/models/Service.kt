package com.marinanitockina.angelsnails.models

data class Service(
    val name: String? = null,
    val price: String? = null,
    val pictureUrl: String? = null,
    val masterIds: MutableMap<String, Boolean> = mutableMapOf(),
    val masters: MutableMap<String, ServiceMaster> = mutableMapOf()
)