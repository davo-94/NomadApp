# Actualización de NomadApp - Panel Admin + Permisos de Cámara

## ✅ Cambios Realizados

### 1. **Panel de Administración**

#### Archivos Creados:
- **AdminViewModel.kt** - ViewModel para gestionar usuarios y roles
  - Métodos: `loadUsers()`, `updateUserRole()`, `enableDisableUser()`, `deleteUser()`
  - State flows: users, loading, error, success
  - Uso: Gestión centralizada del estado admin

- **AdminScreen.kt** - Interfaz de administración
  - TopAppBar con botón atrás
  - Componente UserRoleCard: muestra usuario, rol, estado, botón eliminar
  - Dropdown para cambiar roles (USER, MODERATOR, ADMIN)
  - Indicadores visuales: verde (activo), naranja (inactivo)
  - AlertDialog para confirmación de eliminación
  - Auto-limpiar mensajes de error/éxito después de 3 segundos

#### Archivos Modificados:
- **AppNavigation.kt**
  - Agregado: Import de AdminScreen
  - Agregado: Ruta `composable("admin_panel")` que llama a AdminScreen

- **HomeScreen.kt**
  - Agregado: Import de `collectAsState` y `androidx.compose.runtime`
  - Agregado: Botón "Panel de Administración" que solo aparece si `userRole == "ADMIN"`
  - Lógica: Obtiene el rol del usuario desde SessionManager

### 2. **Permisos de Cámara y Galería**

#### Archivos Creados:
- **PhotoPickerUtils.kt** - Utilidades para solicitar permisos
  - `remotePhotoPickerLauncher()`: Solicita permisos de galería y abre selector de fotos
  - `remoteCameraLauncher()`: Solicita permiso de cámara
  - Manejo automático de versiones de Android (API 33+: READ_MEDIA_IMAGES, anteriores: READ_EXTERNAL_STORAGE)

#### Archivos Modificados:
- **AndroidManifest.xml**
  - Agregados permisos:
    - `android.permission.CAMERA`
    - `android.permission.WRITE_EXTERNAL_STORAGE`
    - `android.permission.READ_MEDIA_IMAGES`

- **PostFormScreen.kt**
  - Reemplazado: Launcher de galería con `remotePhotoPickerLauncher()`
  - Botón "Seleccionar imágenes" ahora solicita permisos antes de abrir galería

## 🔄 Flujo de Funcionalidad

### Panel Admin:
1. Usuario logueado ve botón "Panel de Administración" en HomeScreen (solo si es ADMIN)
2. Navega a `admin_panel`
3. AdminScreen carga lista de usuarios desde backend
4. Puede:
   - Ver detalles de cada usuario (nombre, email, rol)
   - Cambiar rol mediante dropdown
   - Habilitar/deshabilitar usuario
   - Eliminar usuario (con confirmación)
5. Mensajes de error/éxito se muestran y auto-limpian

### Permisos de Foto:
1. Usuario toca botón "Seleccionar imágenes" en PostFormScreen
2. App solicita permiso READ_MEDIA_IMAGES (Android 13+) o READ_EXTERNAL_STORAGE (Android 12-)
3. Si es concedido:
   - Se abre selector de múltiples imágenes
   - URIs se procesan y se toman permisos persistentes
   - Imágenes se muestran en preview
4. Si es denegado:
   - El selector no se abre
   - Usuario puede reintentar

## ⚠️ Notas Importantes

### EndpointsAPI que requiere verificación/implementación en backend:
```
GET /api/admin/users - Obtener lista de usuarios
PUT /api/admin/users/{id}/role - Cambiar rol
PUT /api/admin/users/{id}/status - Habilitar/deshabilitar
DELETE /api/admin/users/{id} - Eliminar usuario
```

**Estado actual**: AdminViewModel usa endpoints placeholder que deben conectarse a los reales del backend.

### Versión de compilación:
- API minSdk: Probablemente 24+
- targetSdk: Probablemente 34+
- Compatible con Android 13+ (Tiramisu) y anteriores

### Permisos runtime:
Los permisos ahora se solicitan en tiempo de ejecución (no solo en manifest) cuando el usuario intenta usar galería/cámara.

## 📝 Tareas Pendientes

### De Alta Prioridad:
1. [ ] Verificar/crear endpoints admin en backend Spring Boot
2. [ ] Actualizar URLs de endpoints en AdminViewModel
3. [ ] Integrar PhotoPickerUtils en ContactFormScreen
4. [ ] Pruebas en emulador/dispositivo

### De Media Prioridad:
1. [ ] Mejorar UI con iconos
2. [ ] Agregar loading spinners
3. [ ] Manejo de errores de red mejorado
4. [ ] Validación de roles en frontend

### De Baja Prioridad:
1. [ ] Optimizar imágenes de preview
2. [ ] Caché de usuarios admin
3. [ ] Historial de cambios

## 🧪 Compilación y Estado

**Última compilación**: ✅ EXITOSA (BUILD SUCCESSFUL in 8s)
- APK generado: `app/build/outputs/apk/debug/app-debug.apk`
- Errores: 0
- Advertencias: 2 (deprecation de IconsIcons.Filled - no críticas)

## 🚀 Pasos Siguientes Recomendados

1. Iniciar backend Spring Boot
2. Crear usuario admin de prueba
3. Desplegar APK en emulador/dispositivo
4. Probar:
   - Login con usuario admin
   - Acceso al panel admin
   - Modificación de roles
   - Solicitud de permisos al seleccionar foto
5. Investigar y crear endpoints admin si no existen
