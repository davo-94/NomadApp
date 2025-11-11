package cl.vasquez.nomadapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.vasquez.nomadapp.view.HomeScreen
import cl.vasquez.nomadapp.view.PostFormScreen
import cl.vasquez.nomadapp.view.PostListScreen

/**
 * Composable principal que define las rutas de navegación de la app
 * Composable le indica a Kotlin que esa función dibuja UI en pantalla.
 */
@Composable
fun AppNavigation() {
    //recordamos el controlador de navegación -> permite moverse entre pantallas
    val navController: NavHostController = rememberNavController()

    //NavHost define el contenedor que gestiona las rutas
    NavHost(
        navController = navController,
        startDestination = "home" //pantalla inicial
    ){
        //Ruta home (hub principal)
        composable("home") {
            HomeScreen(navController = navController)
        }
        //Ruta Formulario (nueva publicación)
        composable("post_form") {
            PostFormScreen(navController = navController)
        }
        //Ruta Lista (mis publicaciones)
        composable("post_list") {
            PostListScreen(navController = navController)
        }
    }
}
