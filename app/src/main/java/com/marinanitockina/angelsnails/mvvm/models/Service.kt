package com.marinanitockina.angelsnails.mvvm.models

data class Service(
    val id: String? = null,
    val name: String? = null,
    val price: String? = null,
    val pictureUrl: String? = null,
    val masterIds: MutableMap<String, Boolean> = mutableMapOf(),
    val masters: MutableMap<String, ServiceMaster> = mutableMapOf()
)