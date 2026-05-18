package com.elementzit.mc.domain.model

data class Address(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val phone: String = "",
    val street: String = "",
    val city: String = "",
    val state: String = "",
    val zipCode: String = "",
    val isDefault: Boolean = false
)
