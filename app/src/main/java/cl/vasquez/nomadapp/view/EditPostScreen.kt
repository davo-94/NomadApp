package cl.vasquez.nomadapp.view

import cl.vasquez.nomadapp.data.remote.dto.PostDto
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.vasquez.nomadapp.viewmodel.PostViewModel
import cl.vasquez.nomadapp.utils.PermissionManager
import coil.compose.AsyncImage
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Close
import cl.vasquez.nomadapp.data.SessionManager
import kotlinx.coroutines.runBlocking
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPostScreen(
    navController: NavController,
    postId: Long,
    viewModel: PostViewModel = viewModel()
) {
    val context = LocalContext.current

    /** Cargar posts al entrar */
    LaunchedEffect(Unit) {
        viewModel.loadPosts()
    }

    /** Observamos posts desde backend */
    val posts by viewModel.posts.collectAsState()
    val post = posts.find { it.id == postId }

    /** Mientras carga */
    if (post == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    /** Estados locales */
    var title by remember { mutableStateOf(post.title) }
    var description by remember { mutableStateOf(post.description) }
    val date = post.date

    /** Nuevas imágenes seleccionadas */
    var newImageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }

    /** Preview fullscreen */
    var selectedImageUrl by remember { mutableStateOf<String?>(null) }

    /** Selector de imágenes */
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris.isNotEmpty()) {
            uris.forEach { uri ->
                try {
                    context.contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (_: SecurityException) { }
            }
            newImageUris = uris
        }
    }

    // Launcher para solicitar permisos de fotos
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            galleryLauncher.launch("image/*")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Publicación") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        runBlocking { SessionManager.logout() }
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }) {
                        Icon(Icons.Default.Logout, contentDescription = "Cerrar sesión")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 160.dp),
                minLines = 6,
                maxLines = 10
            )

            Spacer(modifier = Modifier.height(16.dp))

            /** Imágenes actuales del post */
            if (post.imageUrls.isNotEmpty()) {
                Text("Imágenes actuales", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(post.imageUrls) { url ->
                        AsyncImage(
                            model = url,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .height(100.dp)
                                .aspectRatio(4f / 3f)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { selectedImageUrl = url }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            /** Agregar nuevas imágenes con solicitud de permisos */
            Button(
                onClick = {
                    // Verificar si ya tiene permisos de fotos
                    if (PermissionManager.hasPhotoPermission(context)) {
                        galleryLauncher.launch("image/*")
                    } else {
                        // Si no tiene permisos, solicitarlos usando PermissionManager
                        PermissionManager.requestPhotoPermission(permissionLauncher)
                    }
                }
            ) {
                Text("Agregar imágenes")
            }

            /** Preview nuevas imágenes */
            if (newImageUris.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(newImageUris) { uri ->
                        AsyncImage(
                            model = uri,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val updated = PostDto(
                        id = postId,
                        title = title,
                        description = description,
                        date = date
                    )

                    viewModel.updatePost(postId, updated) {
                        if (newImageUris.isNotEmpty()) {
                            viewModel.addImagesToPost(
                                postId = postId,
                                imageUris = newImageUris,
                                context = context
                            ) {
                                navController.popBackStack()
                            }
                        } else {
                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar cambios")
            }
        }
    }

    /** Preview fullscreen */
    selectedImageUrl?.let { imageUrl ->
        Dialog(onDismissRequest = { selectedImageUrl = null }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Preview imagen",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxWidth()
                )

                IconButton(
                    onClick = { selectedImageUrl = null },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = Color.White
                    )
                }
            }
        }
    }
}
