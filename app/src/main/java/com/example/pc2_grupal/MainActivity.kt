package com.example.pc2_grupal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.pc2_grupal.ui.navigation.AppNavHost
import com.example.pc2_grupal.ui.theme.PC2_GRUPALTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PC2_GRUPALTheme {
                AppNavHost()
            }
        }
    }
}
