package com.elementzit.mc

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * The base Application class for the Marketplace app.
 * Annotated with @HiltAndroidApp to enable Hilt dependency injection.
 */
@HiltAndroidApp
class MagiciansCartApp : Application()
