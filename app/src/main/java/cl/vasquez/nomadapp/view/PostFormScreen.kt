package cl.vasquez.nomadapp.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.vasquez.nomadapp.viewmodel.PostViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.res.painterResource



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostFormScreen(
    modifier: Modifier = Modifier,
    viewModel: PostViewModel = viewModel()
) {
    /**
     * Estados locales para los campos del formulario
     */
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val date = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()) }

    /**
     * Estado para la imagen seleccionada
     */
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    /**
     * Lanza el selector de imágenes(galería del teléfono)
     */
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri // Guarda el URI seleccionado
    }

    /** Interfaz visual del formulario
     */
    Column(
        modifier = modifier
            .fillMaxSize()
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

        Spacer(modifier = Modifier.height(8.dp))

        /**
         * Campo descripción
         */
        OutlinedTextField(
            value = description,
            onValueChange =  { description = it },
            label = {Text("Descripción") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(8.dp))

        /**
         * Botón para seleccionar imagen desde galería
         */
        Button(onClick = { launcher.launch("image/*") }) {
            Text("Seleccionar imagen")
        }

        Spacer(modifier = Modifier.height(8.dp))

        /**
         * Vista previa de laimagen seleccionada
         */
        imageUri?.let {
            /**
             * Muestra una miniatura; en proyectos reales se usaría Coil
             */
            Image(
                painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                contentDescription = "Imagen seleccionada",
                modifier = Modifier
                    .size(120.dp)
                    .padding(4.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        /**
         * Botón para guardar la publicación
         */
        Button(
            onClick = {
                /**
                 * Validación simple: no dejar campos vacíos
                 */
                if (title.isNotEmpty() && description.isNotEmpty()) {
                    viewModel.addPost(title, description, date, imageUri?.toString())
                    //Limpia todos los campos después de guardar
                    title = ""
                    description = ""
                    imageUri = null
                }
            },
            modifier = Modifier.fillMaxWidth()
        ){
            Text("Guardar publicación")
        }
    }
}


