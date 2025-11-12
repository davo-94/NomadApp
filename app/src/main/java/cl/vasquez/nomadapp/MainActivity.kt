package cl.vasquez.nomadapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.tooling.preview.Preview
import cl.vasquez.nomadapp.ui.theme.NomadAppTheme
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.MaterialTheme
import cl.vasquez.nomadapp.navigation.AppNavigation
import cl.vasquez.nomadapp.data.SessionManager


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Inicializar SessionManager con el contexto de la app
        SessionManager.initialize(applicationContext)

        enableEdgeToEdge()
        setContent {
            NomadAppTheme {
                AppNavigation()

                }
            }
        }
    }


@Composable
fun HelloNomad() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("NomadApp – Bitácora Nómada", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Base Compose + MVVM lista")
    }
}

