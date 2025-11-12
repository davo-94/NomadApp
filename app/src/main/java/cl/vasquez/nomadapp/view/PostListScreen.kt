package cl.vasquez.nomadapp.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cl.vasquez.nomadapp.viewmodel.PostViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.outlined.CheckCircle
import cl.vasquez.nomadapp.data.SessionManager
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Pantalla que muestra la lista de publicaciones guardadas
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostListScreen(
    navController: NavController,
    viewModel: PostViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    // Observamos el flujo de publicaciones desde el ViewModel
    val posts by viewModel.postList.collectAsState(initial = emptyList())

    // Estado local para confirmar eliminación
    var postToDelete by remember { mutableStateOf<cl.vasquez.nomadapp.data.Post?>(null) }

    // Control del Snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Obtenemos el email del usuario desde SessionManager (modo bloqueante, idealmente Flow)
    val userEmail: String = runBlocking { SessionManager.getUserEmail().first() ?: "Usuario" }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Mis Publicaciones") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    /**
                     * Mostrar el email del usuario logueado
                     */
                    Text(
                        text = userEmail,
                        modifier = Modifier.padding(horizontal = 8.dp),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    /**
                     * Botón de logout (mantiene consistencia visual con otras pantallas)
                     */
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
            // Si hay publicaciones, las mostramos en una lista
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
                                            val pagerState =
                                                rememberPagerState(pageCount = { post.imageUris.size })

                                            // Carrusel deslizable (HorizontalPager)
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

                                // 🔹 Botones de acción: Editar y Eliminar
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    TextButton(onClick = {
                                        navController.navigate("editPost/${post.id}")
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Editar",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Editar", color = MaterialTheme.colorScheme.primary)
                                    }
                                    TextButton(onClick = { postToDelete = post }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Eliminar",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Eliminar", color = MaterialTheme.colorScheme.error)
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // Si no hay publicaciones, mostramos un mensaje vacío
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

    // 🔸 Diálogo de confirmación de eliminación
    postToDelete?.let { post ->
        AlertDialog(
            onDismissRequest = { postToDelete = null },
            title = { Text("Eliminar publicación") },
            text = { Text("¿Seguro que deseas eliminar \"${post.title}\"? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deletePost(post)
                        postToDelete = null
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Publicación eliminada con éxito")
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { postToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
