package cl.vasquez.nomadapp.view

import cl.vasquez.nomadapp.data.remote.dto.PostDto
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
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.CheckCircle
import cl.vasquez.nomadapp.data.SessionManager
import cl.vasquez.nomadapp.utils.PermissionManager
import kotlinx.coroutines.runBlocking
import coil.compose.AsyncImage
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostFormScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: PostViewModel = viewModel()
) {
    /**
     * Estados locales para los campos del formulario
     * mutableStateOf -> Son valores locales de la pantalla, no vienen
     * del ViewModel.
     * -> Son estados visuales; no datos persistentes.
     */

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val maxDescriptionChars = 1000

    val date = remember {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    }

    /**
     * Estado para múltiples imágenes seleccionadas
     */
    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }

    /**
     * Estado para controlar la visibilidad del diálogo de confirmación
     */
    var showConfirmationDialog by remember { mutableStateOf(false) }

    /**
     * Lanza el selector de imágenes (galería del teléfono)
     * Ahora permite seleccionar varias imágenes y solicita permisos persistentes
     */
    val context = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            uris.forEach { uri ->
                try {
                    context.contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (e: SecurityException) {
                    e.printStackTrace()
                }
            }
            imageUris = uris
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

    /**
     * Estructura principal con barra superior
     */
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Publicación") },
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
                     * Botón de logout (mantiene consistencia con el resto de pantallas)
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
        /** Interfaz visual del formulario */
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Nueva publicación",
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

            Spacer(modifier = Modifier.height(12.dp))

            /**
             * Campo: Descripción (más grande + contador)
             */
            OutlinedTextField(
                value = description,
                onValueChange = {
                    if (it.length <= maxDescriptionChars) {
                        description = it
                    }
                },
                label = { Text("Descripción") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 160.dp),
                minLines = 6,
                maxLines = 10,
                supportingText = {
                    Text(
                        text = "${description.length} / $maxDescriptionChars caracteres",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            /**
             * Botón para seleccionar múltiples imágenes desde la galería
             * Solicita permisos de acceso a fotos si es necesario
             */
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
                Text("Seleccionar imágenes")
            }

            Spacer(modifier = Modifier.height(8.dp))

            /**
             * Vista previa de las imágenes seleccionadas (en fila horizontal)
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

            Spacer(modifier = Modifier.height(16.dp))

            /**
             * Botón para guardar la publicación
             */
            Button(
                onClick = {
                    if (title.isNotEmpty() && description.isNotEmpty()) {

                        //runBlocking 'bloquea' el hilo actual y ejecuta una corrutina
                        //de forma asíncrona. "Espera" el valor de SessionManager.getUserEmail()
                        //antes de continuar.
                        val ownerEmail = runBlocking {
                            //first() -> Obtiene el primer valor emitido por el Flow
                            SessionManager.getUserEmail().first()
                        }

                        val postDto = PostDto(
                            title = title,
                            description = description,
                            date = date,
                            ownerEmail = ownerEmail
                        )

                        viewModel.createPostWithImages(
                            post = postDto,
                            imageUris = imageUris,
                            context = context
                        ) {
                            showConfirmationDialog = true
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar publicación")
            }
        }
    }

    /**
     * Diálogo de confirmación de publicación
     */
    if (showConfirmationDialog) {
        AlertDialog(
            onDismissRequest = {
                showConfirmationDialog = false
                title = ""
                description = ""
                imageUris = emptyList()
                navController.popBackStack()
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Confirmación",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            title = { Text("¡Publicación confirmada!") },
            text = { Text("Tu publicación ha sido publicada exitosamente.") },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmationDialog = false
                        title = ""
                        description = ""
                        imageUris = emptyList()
                        navController.popBackStack()
                    }
                ) {
                    Text("Aceptar")
                }
            }
        )
    }
}
