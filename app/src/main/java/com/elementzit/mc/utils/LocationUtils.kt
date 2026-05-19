package com.elementzit.mc.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

@SuppressLint("MissingPermission")
@Composable
fun rememberCurrentLocationData(): Pair<String, Location?> {
    val context = LocalContext.current
    var locationName by remember { mutableStateOf("Fetching...") }
    var locationObj by remember { mutableStateOf<Location?>(null) }
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        
        if (fineGranted || coarseGranted) {
            updateLocation(context, fusedLocationClient) { name, loc -> 
                locationName = name
                locationObj = loc 
            }
        } else {
            locationName = "Permission Denied"
        }
    }

    LaunchedEffect(Unit) {
        val hasFine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val hasCoarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        
        if (hasFine || hasCoarse) {
            updateLocation(context, fusedLocationClient) { name, loc -> 
                locationName = name
                locationObj = loc 
            }
        } else {
            launcher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    return locationName to locationObj
}

private fun updateLocation(context: Context, client: FusedLocationProviderClient, onUpdate: (String, Location?) -> Unit) {
    try {
        client.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val geocoder = Geocoder(context, Locale.getDefault())
                @Suppress("DEPRECATION")
                val addresses = try {
                    geocoder.getFromLocation(location.latitude, location.longitude, 1)
                } catch (e: Exception) {
                    null
                }
                
                val locality = addresses?.firstOrNull()?.locality
                val subAdminArea = addresses?.firstOrNull()?.subAdminArea 
                
                onUpdate(locality ?: subAdminArea ?: "Unknown Location", location)
            } else {
                onUpdate("Location Unavailable", null)
            }
        }.addOnFailureListener {
            onUpdate("Error Fetching", null)
        }
    } catch (e: SecurityException) {
        onUpdate("Permission Error", null)
    }
}
