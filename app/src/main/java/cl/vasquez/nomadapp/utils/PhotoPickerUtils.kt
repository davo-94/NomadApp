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
 * Utiliza PermissionManager para manejar permisos de manera centralizada
 * Compatible con Android 11 y versiones posteriores
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
    
    // Launcher para solicitar permisos usando el PermissionManager
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            galleryLauncher.launch("image/*")
        }
    }
    
    // Retornar función que solicita permisos y abre la galería
    return {
        // Usar PermissionManager para solicitar solo permisos de fotos
        PermissionManager.requestPhotoPermission(permissionLauncher)
    }
}

/**
 * Composable que solicita permiso de ubicación y retorna un launcher
 * para acceder a la ubicación
 */
@Composable
fun remoteLocationPermissionLauncher(
    onPermissionGranted: () -> Unit
): () -> Unit {
    // Launcher para solicitar permisos de ubicación
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            onPermissionGranted()
        }
    }
    
    return {
        // Usar PermissionManager para solicitar permisos de ubicación
        PermissionManager.requestLocationPermission(permissionLauncher)
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