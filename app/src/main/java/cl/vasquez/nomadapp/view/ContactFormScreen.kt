package cl.vasquez.nomadapp.view

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cl.vasquez.nomadapp.utils.ValidationUtils
import cl.vasquez.nomadapp.viewmodel.ContactViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader
import kotlinx.coroutines.launch

data class Pais(val nombre: String, val codigo: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactFormScreen(
    navController: NavController,
    viewModel: ContactViewModel = viewModel()
) {
    val context = LocalContext.current
    val paises = remember { cargarPaisesDesdeAssets(context) }

    var nombre by remember { mutableStateOf(TextFieldValue("")) }
    var correo by remember { mutableStateOf(TextFieldValue("")) }
    var mensaje by remember { mutableStateOf(TextFieldValue("")) }
    var paisSeleccionado by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    var isSending by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Formulario de Contacto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val nombreError = ValidationUtils.getNombreErrorMessage(nombre.text)
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                isError = nombreError != null,
                supportingText = {
                    if (nombreError != null) {
                        Text(nombreError, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            val emailError = ValidationUtils.getEmailErrorMessage(correo.text)
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo electrónico") },
                isError = emailError != null,
                supportingText = {
                    if (emailError != null) {
                        Text(emailError, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            ) {
                OutlinedTextField(
                    value = paisSeleccionado,
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    label = { Text("Selecciona un país") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = true }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp)
                ) {
                    paises.forEach { pais ->
                        DropdownMenuItem(
                            text = { Text(pais.nombre) },
                            onClick = {
                                paisSeleccionado = pais.nombre
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            val mensajeError = ValidationUtils.getMensajeErrorMessage(mensaje.text)
            OutlinedTextField(
                value = mensaje,
                onValueChange = { mensaje = it },
                label = { Text("Mensaje") },
                isError = mensajeError != null,
                supportingText = {
                    if (mensajeError != null) {
                        Text(mensajeError, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 4
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    if (viewModel.validarContacto(
                            nombre.text,
                            correo.text,
                            mensaje.text
                        )
                    ) {
                        isSending = true

                        viewModel.addContact(
                            nombre.text,
                            correo.text,
                            paisSeleccionado,
                            mensaje.text
                        )

                        viewModel.sendContactToBackend(
                            nombre = nombre.text,
                            correo = correo.text,
                            pais = paisSeleccionado,
                            mensaje = mensaje.text
                        ) { success ->
                            scope.launch {
                                isSending = false
                                if (success) {
                                    snackbarHostState.showSnackbar("Mensaje enviado con éxito")
                                    nombre = TextFieldValue("")
                                    correo = TextFieldValue("")
                                    mensaje = TextFieldValue("")
                                    paisSeleccionado = ""
                                } else {
                                    snackbarHostState.showSnackbar("Error al enviar el mensaje")
                                }
                            }
                        }
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("Por favor revisa los campos del formulario")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSending
            ) {
                if (isSending) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Enviando...")
                } else {
                    Text("Enviar")
                }
            }
        }
    }
}

// Carga países desde assets
fun cargarPaisesDesdeAssets(context: Context): List<Pais> {
    val inputStream = context.assets.open("paises.json")
    val reader = InputStreamReader(inputStream)
    val tipoLista = object : TypeToken<List<Pais>>() {}.type
    return Gson().fromJson(reader, tipoLista)
}
