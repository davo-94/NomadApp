package cl.vasquez.nomadapp.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cl.vasquez.nomadapp.data.User
import cl.vasquez.nomadapp.data.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
fun RegisterScreen(navController: NavController) {
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
            
            // Email field
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = null
                },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                isError = emailError != null,
                supportingText = { emailError?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            // Password field
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = null
                },
                label = { Text("Contraseña (mín. 6 caracteres)") },
                modifier = Modifier.fillMaxWidth(),
                isError = passwordError != null,
                supportingText = { passwordError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            // Confirm password field
            OutlinedTextField(
                value = passwordConfirm,
                onValueChange = { passwordConfirm = it },
                label = { Text("Confirmar Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(24.dp))
            
            // Register button
            Button(
                onClick = {
                    var isValid = true
                    
                    // Validate email format
                    if (email.isBlank()) {
                        emailError = "El email no puede estar vacío"
                        isValid = false
                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        emailError = "Formato de email inválido"
                        isValid = false
                    }
                    
                    // Validate password
                    if (password.isBlank()) {
                        passwordError = "La contraseña no puede estar vacía"
                        isValid = false
                    } else if (password.length < 6) {
                        passwordError = "La contraseña debe tener al menos 6 caracteres"
                        isValid = false
                    } else if (password != passwordConfirm) {
                        passwordError = "Las contraseñas no coinciden"
                        isValid = false
                    }
                    
                    if (!isValid) return@Button
                    
                    // Check for duplicate email in database
                    isLoading = true
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val dao = AppDatabase.getDatabase(LocalContextProvider.context).userDao()
                            val existingUser = dao.getByEmail(email)
                            
                            CoroutineScope(Dispatchers.Main).launch {
                                isLoading = false
                                if (existingUser != null) {
                                    // Email already registered
                                    emailError = "Este email ya está registrado"
                                    showErrorDialog = true
                                    dialogMessage = "El email '$email' ya está asociado a otra cuenta.\n\nIntentar con otro email o usar 'Iniciar sesión'."
                                } else {
                                    // Register new user
                                    CoroutineScope(Dispatchers.IO).launch {
                                        dao.insert(User(email = email, password = password, role = "guest"))
                                        CoroutineScope(Dispatchers.Main).launch {
                                            showSuccessDialog = true
                                            dialogMessage = "¡Registro exitoso!\n\nAhora puedes iniciar sesión con tu email y contraseña."
                                        }
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            CoroutineScope(Dispatchers.Main).launch {
                                isLoading = false
                                showErrorDialog = true
                                dialogMessage = "Error al registrar: ${e.message}"
                            }
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
            
            // Back to login link
            TextButton(onClick = { navController.popBackStack() }) {
                Text("¿Ya tienes cuenta? Inicia sesión")
            }
        }
    }
    
    // Success dialog
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
    
    // Error dialog
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

// Small helper to obtain a Context inside non-Activity composables where needed
object LocalContextProvider {
    // Will be replaced at runtime by generated Compose Local when used
    // Provide an accessible Application Context via AndroidViewModel or other callers if needed.
    lateinit var context: android.content.Context
}
