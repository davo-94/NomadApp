package cl.vasquez.nomadapp.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat

object PermissionManager {

    // Permisos necesarios para ubicación
    val LOCATION_PERMISSIONS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    } else {
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    // Permisos necesarios para acceder a fotos
    val PHOTO_PERMISSIONS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
    } else {
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    /**
     * Verifica si los permisos de ubicación están otorgados
     */
    fun hasLocationPermission(context: Context): Boolean {
        return LOCATION_PERMISSIONS.all { permission ->
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * Verifica si los permisos de fotos están otorgados
     */
    fun hasPhotoPermission(context: Context): Boolean {
        return PHOTO_PERMISSIONS.all { permission ->
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * Verifica si todos los permisos necesarios están otorgados
     */
    fun hasAllPermissions(context: Context): Boolean {
        return hasLocationPermission(context) && hasPhotoPermission(context)
    }

    /**
     * Solicita los permisos de ubicación y fotos
     */
    fun requestAllPermissions(launcher: ActivityResultLauncher<Array<String>>) {
        val allPermissions = (LOCATION_PERMISSIONS + PHOTO_PERMISSIONS).distinct().toTypedArray()
        launcher.launch(allPermissions)
    }

    /**
     * Solicita solo permisos de ubicación
     */
    fun requestLocationPermission(launcher: ActivityResultLauncher<Array<String>>) {
        launcher.launch(LOCATION_PERMISSIONS)
    }

    /**
     * Solicita solo permisos de fotos
     */
    fun requestPhotoPermission(launcher: ActivityResultLauncher<Array<String>>) {
        launcher.launch(PHOTO_PERMISSIONS)
    }
}
