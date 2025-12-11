package cl.vasquez.nomadapp.view

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import cl.vasquez.nomadapp.viewmodel.LocationViewModel
import cl.vasquez.nomadapp.data.remote.LocationResponse
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.vasquez.nomadapp.data.SessionManager
import cl.vasquez.nomadapp.view.components.HeaderSection
import cl.vasquez.nomadapp.view.components.PrimaryButton
import cl.vasquez.nomadapp.view.components.SecondaryButton
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.runBlocking


@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun HomeScreen(navController: NavController) {
    println("DEBUG_HOME: HomeScreen LOADED")

    val locationViewModel: LocationViewModel = viewModel()
    val location by locationViewModel.location.collectAsStateWithLifecycle(initialValue = null)

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
        // Contenedor principal con fondo + contenido
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)

        ) {
            //  Fondo semitransparente (de Nico)
            AsyncImage(
                model = "https://images.unsplash.com/photo-1501785888041-af3ef285b470",
                contentDescription = "Fondo de viajes",
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.2f),
                contentScale = ContentScale.Crop
            )
            //Widget de ubicacion
            location?.let { loc ->
                Text(
                    text =  "📍 ${loc.city ?: ""}, ${loc.country_name ?: ""}",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            // Contenido principal
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                /**
                 * Seccion superior de encabezado reutilizable
                 * Muestra título y subtítulo de bienvenida
                 */
                HeaderSection(
                    title = "¡Bienvenido a tu Bitácora Nómada!",
                    subtitle = "Publica tus experiencias y explora las de otros viajeros."
                )

                Spacer(modifier = Modifier.height(24.dp))

                /**
                 * Botón principal que lleva a la creación de nueva publicación
                 */
                PrimaryButton(
                    text = "Nueva Publicación",
                    onClick = { navController.navigate("post_form") }
                )

                Spacer(modifier = Modifier.height(12.dp))

                /**
                 * Botón secundario que muestra lista de publicaciones del usuario
                 */
                SecondaryButton(
                    text = "Mis publicaciones",
                    onClick = { navController.navigate("post_list") }
                )

                Spacer(modifier = Modifier.height(12.dp))

                /**
                 * Botón secundario que lleva al formulario de contacto
                 */
                SecondaryButton(
                    text = "Contacto",
                    onClick = { navController.navigate("contact_form") }
                )
            }
        }
    }
}
