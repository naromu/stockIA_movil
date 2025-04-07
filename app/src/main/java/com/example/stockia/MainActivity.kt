package com.example.stockia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.stockia.routes.AppNavHost
import com.example.stockia.ui.theme.StockIATheme
import com.example.stockia.view.login.LoginPreview
import com.example.stockia.view.login.LoginView
import com.example.stockia.view.login.RegisterPreview
import com.example.stockia.view.login.RegisterView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        try {
            setContent {
                StockIATheme {
                    AppNavHost(context = this)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
        style = MaterialTheme.typography.titleLarge

    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StockIATheme {
        Greeting("Android")
    }
}