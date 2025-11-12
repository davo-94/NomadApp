package cl.vasquez.nomadapp.view

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.vasquez.nomadapp.viewmodel.PostViewModel
import coil.compose.AsyncImage
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import cl.vasquez.nomadapp.data.SessionManager
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPostScreen(
    navController: NavController,
    postId: Int,
    viewModel: PostViewModel = viewModel()
) {
    //  Obtenemos el contexto y coroutineScope
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    /**
     * Cargamos el post actual desde la lista del ViewModel
     */
    val post = viewModel.postList.collectAsState(initial = emptyList()).value.find { it.id == postId }

    /**
     * Si no se encuentra el post, mostramos mensaje de error
     */
    if (post == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Publicación no encontrada")
        }
        return
    }

    /**
     * Estados locales inicializados con los valores actuales del post
     */
    var title by remember { mutableStateOf(post.title) }
    var description by remember { mutableStateOf(post.description) }
    val date = remember { post.date } // mantenemos la fecha original
    //  ahora soportamos múltiples imágenes (convirtiendo desde List<String> a List<Uri>)
    var imageUris by remember {
        mutableStateOf(
            post.imageUris.filter { it.isNotBlank() }.map { Uri.parse(it) }
        )
    }

    /**
     * Lanza el selector de imágenes (galería del teléfono)
     * Incluye solicitud de permiso persistente para que las URIs sigan disponibles tras reiniciar la app.
     * Ahora permite seleccionar múltiples imágenes.
     */
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            uris.forEach { uri ->
                try {
                    context.contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (_: SecurityException) { /* ya teníamos permiso, ignorar */ }
            }
            imageUris = uris // reemplaza el set actual por las nuevas seleccionadas
        }
    }

    /**
     * Estructura principal con barra superior
     */
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Publicación") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    /**
                     * Botón de logout (mantiene consistencia visual con el resto de pantallas)
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
        /** Interfaz visual del formulario de edición */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Editar publicación",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            /**
             * Campo: Título
             */
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            /**
             * Campo: Descripción
             */
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(8.dp))

            /**
             * Botón para seleccionar nuevas imágenes desde la galería
             */
            Button(onClick = { launcher.launch("image/*") }) {
                Text("Cambiar imágenes")
            }

            Spacer(modifier = Modifier.height(8.dp))

            /**
             * Vista previa de las imágenes seleccionadas (actuales o nuevas)
             */
            if (imageUris.isNotEmpty()) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(imageUris) { uri ->
                        AsyncImage(
                            model = uri,
                            contentDescription = "Imagen seleccionada",
                            modifier = Modifier
                                .size(120.dp)
                                .padding(4.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            /**
             * Botón para guardar los cambios
             */
            Button(
                onClick = {
                    if (title.isNotEmpty() && description.isNotEmpty()) {
                        coroutineScope.launch {
                            //  Actualizamos el post existente (no insertamos uno nuevo)
                            viewModel.updatePost(
                                cl.vasquez.nomadapp.data.Post(
                                    id = postId,
                                    title = title,
                                    description = description,
                                    date = date,
                                    imageUris = imageUris.map { it.toString() } // guardamos lista
                                )
                            )
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
}
