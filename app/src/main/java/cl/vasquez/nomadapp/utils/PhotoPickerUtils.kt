package cl.vasquez.nomadapp.utils

import android.Manifest
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * Composable que solicita permisos de galería y luego abre el selector de fotos
 * Maneja automáticamente los permisos necesarios para acceder a la galería en diferentes versiones de Android
 */
@Composable
fun remotePhotoPickerLauncher(
    onPhotosSelected: (List<android.net.Uri>) -> Unit
): () -> Unit {
    val context = LocalContext.current
    
    // Launcher para seleccionar múltiples imágenes de la galería
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris.isNotEmpty()) {
            // Tomar permisos persistentes sobre los URIs
            uris.forEach { uri ->
                try {
                    context.contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            onPhotosSelected(uris)
        }
    }
    
    // Launcher para solicitar permisos (manejo simplificado)
    val permissionLauncher = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        // Android 13+
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions.values.all { it }) {
                galleryLauncher.launch("image/*")
            }
        }
    } else {
        // Android 12 y anteriores
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                galleryLauncher.launch("image/*")
            }
        }
    }
    
    // Retornar función que solicita permisos y abre la galería
    return {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+: solicitar READ_MEDIA_IMAGES
            @Suppress("UNCHECKED_CAST")
            val launcher = permissionLauncher as androidx.activity.result.ActivityResultLauncher<Array<String>>
            launcher.launch(arrayOf(Manifest.permission.READ_MEDIA_IMAGES))
        } else {
            // Android 12 y anteriores: solicitar READ_EXTERNAL_STORAGE
            @Suppress("UNCHECKED_CAST")
            val launcher = permissionLauncher as androidx.activity.result.ActivityResultLauncher<String>
            launcher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }
}

/**
 * Composable que solicita permiso de cámara y retorna un launcher para tomar foto
 */
@Composable
fun remoteCameraLauncher(
    onPhotoCapture: () -> Unit
): () -> Unit {
    // Launcher para solicitar permiso de cámara
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permiso concedido
            onPhotoCapture()
        }
    }
    
    return {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }
}

