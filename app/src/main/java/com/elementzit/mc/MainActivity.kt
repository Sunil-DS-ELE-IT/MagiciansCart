package com.elementzit.mc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.elementzit.mc.navigation.MarketplaceNavGraph
import com.elementzit.mc.ui.theme.MarketplaceTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * The main entry point for the application.
 * Annotated with @AndroidEntryPoint to enable Hilt dependency injection in this Activity.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    /**
     * Called when the activity is first created.
     * Sets up the UI, applies themes, and initializes navigation.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Full Edge-to-Edge support
        enableEdgeToEdge()
        
        setContent {
            MarketplaceTheme {
                // Initialize the NavController to manage app navigation
                val navController = rememberNavController()
                
                // Set up the main navigation graph
                MarketplaceNavGraph(
                    navController = navController,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
