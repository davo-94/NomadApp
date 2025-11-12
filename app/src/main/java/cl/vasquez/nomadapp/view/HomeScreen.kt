package cl.vasquez.nomadapp.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cl.vasquez.nomadapp.data.SessionManager
import cl.vasquez.nomadapp.view.components.HeaderSection
import cl.vasquez.nomadapp.view.components.PrimaryButton
import cl.vasquez.nomadapp.view.components.SecondaryButton
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bitácora Nómada") },
                actions = {
                    IconButton(onClick = {
                        runBlocking { SessionManager.logout() }
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Cerrar sesión",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            HeaderSection(
                title = "¡Bienvenido a tu Bitácora Nómada!",
                subtitle = "Publica tus experiencias y explora las de otros viajeros."
            )

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                text = "Nueva Publicación",
                onClick = { navController.navigate("post_form") }
            )

            Spacer(modifier = Modifier.height(12.dp))

            SecondaryButton(
                text = "Mis publicaciones",
                onClick = { navController.navigate("post_list") }
            )

            Spacer(modifier = Modifier.height(12.dp))

            SecondaryButton(
                text = "Contacto",
                onClick = { navController.navigate("contact_form") }
            )
        }
    }
}
