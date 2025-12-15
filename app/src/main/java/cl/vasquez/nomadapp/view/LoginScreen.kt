package cl.vasquez.nomadapp.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cl.vasquez.nomadapp.viewmodel.LoginViewModel
import cl.vasquez.nomadapp.model.LoginResult
import cl.vasquez.nomadapp.ui.theme.MagentaPrimary
import cl.vasquez.nomadapp.ui.theme.DarkBackground
import cl.vasquez.nomadapp.ui.theme.TextLight
import cl.vasquez.nomadapp.ui.theme.DarkCardBackground
import androidx.compose.ui.text.style.TextAlign

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(uiState.loginResult) {
        when (val r = uiState.loginResult) {
            is LoginResult.Success -> {
                when (r.user.role.uppercase()) {
                    "ADMIN" -> {
                        navController.navigate("home_admin") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                    "USER", "MODERATOR" -> {
                        navController.navigate("home_admin") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                    else -> {
                        navController.navigate("home_guest") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
            }
            else -> Unit
        }
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Título
            Text(
                text = "NomadApp",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MagentaPrimary,
                fontSize = 36.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Subtítulo
            Text(
                text = "Un blog de viajes donde mochila, fotos y relatos se mezclan para contar historias alrededor del mundo.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextLight.copy(alpha = 0.8f),
                modifier = Modifier.padding(bottom = 32.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            // Email
            OutlinedTextField(
                value = uiState.email,
                onValueChange = viewModel::onEmailChange,
                label = { Text("Email", color = TextLight.copy(alpha = 0.6f)) },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = MagentaPrimary) },
                singleLine = true,
                isError = uiState.emailError != null,
                supportingText = { uiState.emailError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MagentaPrimary,
                    unfocusedBorderColor = TextLight.copy(alpha = 0.3f),
                    focusedTextColor = TextLight,
                    unfocusedTextColor = TextLight,
                    focusedLabelColor = MagentaPrimary
                )
            )

            // Password
            OutlinedTextField(
                value = uiState.password,
                onValueChange = viewModel::onPasswordChange,
                label = { Text("Contraseña", color = TextLight.copy(alpha = 0.6f)) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = MagentaPrimary) },
                trailingIcon = {
                    IconButton(onClick = { viewModel.togglePasswordVisibility() }) {
                        Icon(
                            imageVector = if (uiState.passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = MagentaPrimary
                        )
                    }
                },
                visualTransformation = if (uiState.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                isError = uiState.passwordError != null,
                supportingText = { uiState.passwordError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MagentaPrimary,
                    unfocusedBorderColor = TextLight.copy(alpha = 0.3f),
                    focusedTextColor = TextLight,
                    unfocusedTextColor = TextLight,
                    focusedLabelColor = MagentaPrimary
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Checkbox: Recuerda la sesión
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = uiState.rememberSession,
                    onCheckedChange = { viewModel.toggleRememberSession() },
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Recuerda mi sesión",
                    color = TextLight,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Botón Login con gradiente
            Button(
                onClick = { viewModel.onLoginClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MagentaPrimary),
                enabled = !uiState.isLoading,
                shape = RoundedCornerShape(8.dp)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = TextLight, strokeWidth = 2.dp)
                } else {
                    Text("Iniciar sesión", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Registrarse (outline)
            OutlinedButton(
                onClick = { navController.navigate("register") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                border = BorderStroke(2.dp, MagentaPrimary),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Registrarse", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MagentaPrimary)
            }

            Spacer(modifier = Modifier.height(20.dp))

            //Botón 'Entrar como invitado'
            TextButton(
                onClick = {
                    navController.navigate("home_guest") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            ) {
                Text("Entrar como invitado")
            }


            // Error message
            AnimatedVisibility(visible = uiState.loginResult is LoginResult.Error, enter = fadeIn(), exit = fadeOut()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCardBackground),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                ) {
                    Text(
                        text = (uiState.loginResult as? LoginResult.Error)?.message ?: "",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(12.dp),
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Links
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                            }

            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}
