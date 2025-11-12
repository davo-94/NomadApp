package cl.vasquez.nomadapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = MagentaPrimary,
    secondary = CyanAccent,
    tertiary = TealAccent,
    background = DarkBackground,
    surface = DarkCardBackground,
    surfaceVariant = SurfaceDark,
    onPrimary = TextLight,
    onSecondary = DarkBackground,
    onTertiary = TextLight,
    onBackground = TextLight,
    onSurface = TextLight,
    onSurfaceVariant = TextGrey
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun NomadAppTheme(
    darkTheme: Boolean = true,  // Siempre usar tema oscuro
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,  // Desactivar colores dinámicos
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme  // Usar siempre el esquema oscuro

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}