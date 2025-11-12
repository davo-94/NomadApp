package cl.vasquez.nomadapp.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import coil.compose.AsyncImage
import cl.vasquez.nomadapp.view.components.BlogCard
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cl.vasquez.nomadapp.viewmodel.PostViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import cl.vasquez.nomadapp.data.SessionManager
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first

/**
 * Pantalla que muestra la lista de publicaciones guardadas
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostListScreen(
    navController: NavController,
    viewModel: PostViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
){
    //Observamos el flujo de publicaciones desde el ViewModel
    val posts by viewModel.postList.collectAsState(initial = emptyList())
    
    // Obtener email del usuario desde SessionManager (blocking, idealmente use Flow)
    val userEmail: String = runBlocking { SessionManager.getUserEmail().first() ?: "Usuario" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Mis Publicaciones") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    // Mostrar email del usuario
                    Text(
                        text = userEmail,
                        modifier = Modifier.padding(horizontal = 8.dp),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    // Botón de logout
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
        //Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            //Si hay publicaciones, las mostramos en una lista con estilo
            if (posts.isNotEmpty()) {
                LazyColumn {
                    items(posts) { post ->
                        BlogCard(
                            title = post.title,
                            date = post.date,
                            description = post.description,
                            imageUrl = post.imageUri, // puede ser null
                            onClick = { /* podria navegar a detalle si se implementa */ }
                        )
                    }
                }
            } else {
                //Si no hay publicaciones, mostramos un mensaje vacío
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Aún no hay publicaciones",
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
