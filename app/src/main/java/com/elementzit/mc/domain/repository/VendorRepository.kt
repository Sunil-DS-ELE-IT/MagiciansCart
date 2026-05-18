package com.elementzit.mc.domain.repository

import com.elementzit.mc.domain.model.Vendor

interface VendorRepository {
    suspend fun getVendors(): List<Vendor>
}
