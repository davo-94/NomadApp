package cl.vasquez.nomadapp.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.vasquez.nomadapp.data.AppDatabase
import cl.vasquez.nomadapp.data.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(navController: NavController) {
    // ✅ Contexto obtenido de forma segura (solo una vez)
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Registro de Usuario", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(24.dp))

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = null
                },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                isError = emailError != null,
                supportingText = {
                    emailError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = null
                },
                label = { Text("Contraseña (mín. 6 caracteres)") },
                modifier = Modifier.fillMaxWidth(),
                isError = passwordError != null,
                supportingText = {
                    passwordError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Confirmar contraseña
            OutlinedTextField(
                value = passwordConfirm,
                onValueChange = { passwordConfirm = it },
                label = { Text("Confirmar Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón Registrar
            Button(
                onClick = {
                    var isValid = true

                    // Validación de email
                    if (email.isBlank()) {
                        emailError = "El email no puede estar vacío"
                        isValid = false
                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        emailError = "Formato de email inválido"
                        isValid = false
                    }

                    // Validación de contraseña
                    if (password.isBlank()) {
                        passwordError = "La contraseña no puede estar vacía"
                        isValid = false
                    } else if (password.length < 6) {
                        passwordError = "Debe tener al menos 6 caracteres"
                        isValid = false
                    } else if (password != passwordConfirm) {
                        passwordError = "Las contraseñas no coinciden"
                        isValid = false
                    }

                    if (!isValid) return@Button

                    // Guardar usuario
                    isLoading = true
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val dao = AppDatabase.getDatabase(context).userDao()
                            val existingUser = dao.getByEmail(email)

                            if (existingUser != null) {
                                // Ya existe
                                emailError = "Este email ya está registrado"
                                dialogMessage = "El email '$email' ya está asociado a otra cuenta."
                                showErrorDialog = true
                            } else {
                                dao.insert(User(email = email, password = password, role = "guest"))
                                dialogMessage = "¡Registro exitoso! Ahora puedes iniciar sesión."
                                showSuccessDialog = true
                            }
                        } catch (e: Exception) {
                            dialogMessage = "Error al registrar: ${e.message}"
                            showErrorDialog = true
                        } finally {
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Registrar")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Volver al login
            TextButton(onClick = { navController.popBackStack() }) {
                Text("¿Ya tienes cuenta? Inicia sesión")
            }
        }
    }

    // Diálogo de éxito
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("¡Éxito!") },
            text = { Text(dialogMessage) },
            confirmButton = {
                Button(onClick = {
                    showSuccessDialog = false
                    navController.popBackStack()
                }) {
                    Text("Ir al Login")
                }
            }
        )
    }

    // Diálogo de error
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Error") },
            text = { Text(dialogMessage) },
            confirmButton = {
                Button(onClick = { showErrorDialog = false }) {
                    Text("Entendido")
                }
            }
        )
    }
}
