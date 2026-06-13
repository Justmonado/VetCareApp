package com.pajasoft.vetcare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pajasoft.vetcare.ui.navigation.VetNavGraph
import com.pajasoft.vetcare.ui.theme.VetCareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VetCareTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppRoot()
                }
            }
        }
    }
}

@Composable
private fun AppRoot() {
    val loggedIn by produceState<Boolean?>(initialValue = null) {
        val token = ServiceLocator.tokenManager.load()
        value = !token.isNullOrBlank()
    }
    if (loggedIn != null) {
        VetNavGraph(startLoggedIn = loggedIn == true)
    }
}