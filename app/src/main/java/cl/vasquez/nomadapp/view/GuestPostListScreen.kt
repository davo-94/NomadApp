package cl.vasquez.nomadapp.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cl.vasquez.nomadapp.viewmodel.PostViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import cl.vasquez.nomadapp.data.SessionManager
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

/**
 * Pantalla de visualización de publicaciones (modo invitado)
 * El guest puede ver las publicaciones, pero no editarlas ni eliminarlas.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuestPostListScreen(
    navController: NavController,
    viewModel: PostViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    // Observamos el flujo de publicaciones desde el ViewModel
    val posts by viewModel.postList.collectAsState(initial = emptyList())

    // Email (o “Invitado” si no hay sesión)
    val userEmail: String = runBlocking { SessionManager.getUserEmail().first() ?: "Invitado" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Publicaciones") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    // Mostrar email / Invitado
                    Text(
                        text = userEmail,
                        modifier = Modifier.padding(horizontal = 8.dp),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    // Logout para volver a login
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
        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (posts.isNotEmpty()) {
                LazyColumn {
                    items(posts, key = { it.id }) { post ->

                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Column {

                                /**
                                 * Card de publicación con carrusel de imágenes
                                 */
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp)
                                    ) {
                                        if (post.imageUris.isNotEmpty()) {
                                            // 👇 IMPORTANTE: pageCount debe ser Int (tamaño de las imágenes)
                                            val pagerState = rememberPagerState(
                                                pageCount = { post.imageUris.size }
                                            )

                                            // Carrusel deslizable (HorizontalPager de Compose Foundation)
                                            HorizontalPager(state = pagerState) { page ->
                                                AsyncImage(
                                                    model = post.imageUris[page],
                                                    contentDescription = "Imagen de publicación",
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(220.dp),
                                                    contentScale = ContentScale.Crop
                                                )
                                            }

                                            // Indicadores de páginas (puntos)
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(top = 6.dp),
                                                horizontalArrangement = Arrangement.Center
                                            ) {
                                                repeat(post.imageUris.size) { index ->
                                                    val color =
                                                        if (pagerState.currentPage == index)
                                                            MaterialTheme.colorScheme.primary
                                                        else
                                                            MaterialTheme.colorScheme.outlineVariant

                                                    Box(
                                                        modifier = Modifier
                                                            .size(8.dp)
                                                            .padding(2.dp)
                                                            .background(
                                                                color,
                                                                shape = MaterialTheme.shapes.small
                                                            )
                                                    )
                                                }
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = post.title,
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                        Text(
                                            text = post.date,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = post.description,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }

                                // ❌ Sin botones de edición ni eliminación para el guest
                            }
                        }
                    }
                }
            } else {
                // Vacío
                Box(
                    modifier = Modifier.fillMaxSize(),
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
