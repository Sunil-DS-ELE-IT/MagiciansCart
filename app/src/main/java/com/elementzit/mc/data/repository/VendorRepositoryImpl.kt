package com.elementzit.mc.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.elementzit.mc.domain.model.Vendor
import com.elementzit.mc.domain.repository.VendorRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class VendorRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : VendorRepository {

    override suspend fun getVendors(): List<Vendor> {
        return try {
            val snapshot = firestore.collection("vendors").get().await()
            snapshot.toObjects(Vendor::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
