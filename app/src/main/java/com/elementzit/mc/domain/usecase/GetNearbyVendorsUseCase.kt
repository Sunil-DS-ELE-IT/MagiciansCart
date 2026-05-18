package com.elementzit.mc.domain.usecase

import android.location.Location
import com.elementzit.mc.domain.model.Vendor
import com.elementzit.mc.domain.repository.VendorRepository
import javax.inject.Inject

class GetNearbyVendorsUseCase @Inject constructor(
    private val vendorRepository: VendorRepository
) {
    suspend operator fun invoke(currentLat: Double, currentLng: Double): List<Vendor> {
        val allVendors = vendorRepository.getVendors()
        
        return allVendors.map { vendor ->
            val results = FloatArray(1)
            Location.distanceBetween(
                currentLat, currentLng,
                vendor.latitude, vendor.longitude,
                results
            )
            val distanceInMeters = results[0]
            val distanceInKm = distanceInMeters / 1000
            
            vendor.copy(
                distanceText = if (distanceInMeters < 1000) {
                    "${distanceInMeters.toInt()} m away"
                } else {
                    "${"%.1f".format(distanceInKm)} km away"
                }
            )
        }.filter { vendor ->
            val results = FloatArray(1)
            Location.distanceBetween(
                currentLat, currentLng,
                vendor.latitude, vendor.longitude,
                results
            )
            results[0] <= 25000 // 25 KM in meters
        }.sortedBy { vendor ->
            val results = FloatArray(1)
            Location.distanceBetween(
                currentLat, currentLng,
                vendor.latitude, vendor.longitude,
                results
            )
            results[0]
        }
    }
}
