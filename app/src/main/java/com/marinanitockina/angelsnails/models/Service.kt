package com.marinanitockina.angelsnails.models

data class Service(
    val name: String? = null,
    val price: String? = null,
    val pictureUrl: String? = null,
    val masters: Map<String, Boolean> = emptyMap()
)