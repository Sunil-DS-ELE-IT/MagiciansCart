package com.elementzit.mc.domain.model

data class Vendor(
    val vendorId: String = "",
    val shopName: String = "",
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val imageUrl: String = "",
    val distanceText: String = "",
    // Added fields for registration
    val ownerEmail: String = ""
)
