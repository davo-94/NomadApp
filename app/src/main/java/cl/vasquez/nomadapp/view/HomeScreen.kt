package cl.vasquez.nomadapp.view

//Importaciones básicas de Jetpack Compose
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

/**
 * Pantalla principal (hub) de la app
 * donde el usuario podrá elegir entre crear o ver publicaciones
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {

    /**
     * Scaffold permite definir estructura base:
     * AppBar arriba, contenido en el centro, etc.
     */
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bitácora Nómada") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        //Columna central: centra los botones vertical y horizontalmente
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
                ) {
            //Mensaje de bienvenida
            Text(
                text = "¡Bienvenido a tu Bitácora Nómada!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            //Botón para crear nueva publicación
            Button(
                onClick = {
                    //Navegamos hacia la pantalla del formulario
                    navController.navigate("post_form")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Nueva Publicación")
            }
            //Botón para ver publicaciones existentes
            Button(
                onClick = {
                    //Navegamos hacia la lista de publicaciones
                    navController.navigate("post_list")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ){
                Text("Mis publicaciones")
            }
        }
    }
}