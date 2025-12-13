package cl.vasquez.nomadapp.view

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cl.vasquez.nomadapp.data.AppDatabase
import cl.vasquez.nomadapp.ui.theme.Purple40
import cl.vasquez.nomadapp.viewmodel.AdminViewModel
import cl.vasquez.nomadapp.viewmodel.UserItem

/**
 * Pantalla de administración de usuarios y roles
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    navController: NavController,
    viewModel: AdminViewModel? = null
) {
    val context = LocalContext.current
    
    // Crear ViewModel con constructor por defecto que funcione sin inyección
    val adminViewModel = remember(viewModel) { 
        viewModel ?: try {
            // Usar la instancia singleton de AppDatabase
            val db = AppDatabase.getDatabase(context)
            AdminViewModel(db)
        } catch (e: Exception) {
            // Si falla, crear con null
            AdminViewModel(null)
        }
    }
    val users by adminViewModel.users.collectAsState()
    val loading by adminViewModel.loading.collectAsState()
    val error by adminViewModel.error.collectAsState()
    val success by adminViewModel.success.collectAsState()
    
    LaunchedEffect(Unit) {
        adminViewModel.loadUsers()
    }
    
    LaunchedEffect(error, success) {
        if (error != null || success != null) {
            // Auto-limpiar mensajes después de 3 segundos
            kotlinx.coroutines.delay(3000)
            adminViewModel.clearMessages()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Usuarios y Roles") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Purple40,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Mensajes de éxito
            if (success != null) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFE8F5E9)
                ) {
                    Text(
                        text = success!!,
                        color = Color(0xFF2E7D32),
                        modifier = Modifier.padding(12.dp),
                        fontSize = 14.sp
                    )
                }
            }
            
            // Botón refrescar
            Button(
                onClick = { adminViewModel.loadUsers() },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 16.dp),
                enabled = !loading
            ) {
                Text(if (loading) "Cargando..." else "Refrescar")
            }
            
            // Lista de usuarios
            if (users.isEmpty() && !loading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                ) {
                    Text(
                        text = "No hay usuarios para mostrar",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(users) { user ->
                        UserRoleCard(
                            user = user,
                            onChangeRole = { newRole ->
                                adminViewModel.updateUserRole(user.id, newRole)
                            },
                            onToggleStatus = {
                                adminViewModel.enableDisableUser(user.id, !user.enabled)
                            },
                            onDelete = {
                                adminViewModel.deleteUser(user.id)
                            },
                            isLoading = loading
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UserRoleCard(
    user: UserItem,
    onChangeRole: (String) -> Unit,
    onToggleStatus: () -> Unit,
    onDelete: () -> Unit,
    isLoading: Boolean
) {
    var showRoleMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF2d2d2d),
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Nombre del usuario
            Text(
                text = "${user.firstName} ${user.lastName}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            
            // Email y username
            Text(
                text = user.email,
                fontSize = 13.sp,
                color = Color(0xFFB0B0B0),
                modifier = Modifier.padding(bottom = 2.dp)
            )
            Text(
                text = "@${user.username}",
                fontSize = 13.sp,
                color = Color(0xFFB0B0B0),
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            // Rol y estado
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Rol con botón dropdown
                Box(modifier = Modifier.weight(1f)) {
                    Surface(
                        modifier = Modifier
                            .clickable(!isLoading) { showRoleMenu = true }
                            .padding(end = 8.dp),
                        shape = RoundedCornerShape(6.dp),
                        color = Purple40
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Rol: ${user.roles}",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Editar rol",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    
                    // Dropdown menu
                    DropdownMenu(
                        expanded = showRoleMenu,
                        onDismissRequest = { showRoleMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("USER") },
                            onClick = {
                                onChangeRole("USER")
                                showRoleMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("MODERATOR") },
                            onClick = {
                                onChangeRole("MODERATOR")
                                showRoleMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("ADMIN") },
                            onClick = {
                                onChangeRole("ADMIN")
                                showRoleMenu = false
                            }
                        )
                    }
                }
                
                // Estado (habilitado/deshabilitado)
                Surface(
                    modifier = Modifier
                        .clickable(!isLoading) { onToggleStatus() },
                    shape = RoundedCornerShape(6.dp),
                    color = if (user.enabled) Color(0xFF4CAF50) else Color(0xFFFF9800)
                ) {
                    Text(
                        text = if (user.enabled) "Activo" else "Inactivo",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
            }
            
            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { showDeleteDialog = true },
                    enabled = !isLoading,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = Color(0xFFC62828)
                    )
                }
            }
        }
    }
    
    // Dialog de confirmación de eliminación
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar Usuario") },
            text = { Text("¿Estás seguro de que deseas eliminar a ${user.firstName}?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828))
                ) {
                    Text("Eliminar", color = Color.White)
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
