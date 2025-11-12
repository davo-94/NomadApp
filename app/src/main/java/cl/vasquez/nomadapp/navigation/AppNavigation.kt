package cl.vasquez.nomadapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.vasquez.nomadapp.view.HomeScreen
import cl.vasquez.nomadapp.view.PostFormScreen
import cl.vasquez.nomadapp.view.PostListScreen
import cl.vasquez.nomadapp.view.ContactFormScreen
import cl.vasquez.nomadapp.view.LoginScreen
import cl.vasquez.nomadapp.view.RegisterScreen
import cl.vasquez.nomadapp.view.GuestHomeScreen

/**
 * Composable principal que define las rutas de navegación de la app
 */
@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login" // pantalla inicial
    ) {
        // Pantalla de login
        composable("login") {
            LoginScreen(navController = navController)
        }
        // Registro de usuario
        composable("register") {
            RegisterScreen(navController = navController)
        }
        // Home principal (admin o usuario logueado)
        composable("home") {
            HomeScreen(navController = navController)
        }
        // Home para invitados
        composable("home_guest") {
            GuestHomeScreen(navController = navController)
        }
        // Crear nueva publicación
        composable("post_form") {
            PostFormScreen(navController = navController)
        }
        // Listado de publicaciones
        composable("post_list") {
            PostListScreen(navController = navController)
        }
        // Formulario de contacto
        composable("contact_form") {
            ContactFormScreen(navController = navController)
        }
    }
}
