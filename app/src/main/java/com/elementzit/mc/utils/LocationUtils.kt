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
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            updateLocation(context, fusedLocationClient) { name, loc -> locationName = name; locationObj = loc }
        } else {
            locationName = "Permission Denied"
        }
    }

    LaunchedEffect(Unit) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) -> {
                updateLocation(context, fusedLocationClient) { name, loc -> locationName = name; locationObj = loc }
            }
            else -> {
                launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
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
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                onUpdate(addresses?.firstOrNull()?.locality ?: "Unknown", location)
            } else {
                onUpdate("Unavailable", null)
            }
        }
    } catch (e: SecurityException) {
        onUpdate("Error", null)
    }
}
