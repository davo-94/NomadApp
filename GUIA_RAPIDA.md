# 🚀 Guía de Inicio Rápido - NomadApp Admin Panel

## ¿Qué se agregó?

### ✨ Panel de Administración
Un panel donde los administradores pueden:
- Ver lista completa de usuarios
- Cambiar roles de usuarios (USER → MODERATOR → ADMIN)
- Habilitar/Deshabilitar usuarios
- Eliminar usuarios de la plataforma

### 📱 Permisos de Cámara y Galería
Cuando el usuario intenta seleccionar fotos:
- La app solicita permiso automáticamente
- Android muestra dialog pidiendo permiso
- Si acepta → se abre selector de fotos
- Si rechaza → puede reintentar después

---

## 🏃 Pasos para Probar

### 1️⃣ Iniciar Backend (Spring Boot)

```bash
cd c:\Users\Nicolas\OneDrive\Documentos\GitHub\NomadApp\backend

# En PowerShell:
.\gradlew.bat bootRun

# Esperar hasta ver: "Started NomadApp in X seconds"
```

**URL Backend**: `http://localhost:8080`

### 2️⃣ Desplegar App en Emulador

```bash
# En otra terminal PowerShell:
cd c:\Users\Nicolas\OneDrive\Documentos\GitHub\NomadApp

# Instalar APK (requiere emulador Android corriendo)
adb install app\build\outputs\apk\debug\app-debug.apk

# O compilar y instalar directo:
.\gradlew.bat installDebug
```

### 3️⃣ Crear Usuario Admin (Opcionalmente)

Usar herramienta como Postman:

```
POST http://localhost:8080/api/auth/register

Body (JSON):
{
  "username": "admin",
  "email": "admin@example.com",
  "password": "Admin123456!",
  "firstName": "Admin",
  "lastName": "User"
}
```

Luego, en la base de datos Oracle, asignar rol ADMIN al usuario.

### 4️⃣ Iniciar Sesión en la App

1. Abrir la app en emulador
2. Ir a "Registrarse" o "Iniciar Sesión"
3. Usar credenciales:
   - Usuario: `admin`
   - Contraseña: `Admin123456!`

### 5️⃣ Acceder al Panel Admin

1. En HomeScreen, si viste un botón **"Panel de Administración"** → ¡Eres admin! 🎉
2. Tocar el botón
3. Esperar a que cargue la lista de usuarios
4. Ahora puedes:
   - Tocar dropdown en "Rol" para cambiar (USER/MODERATOR/ADMIN)
   - Tocar toggle "Estado" para habilitar/deshabilitar
   - Tocar botón rojo "Eliminar" para remover usuario

### 6️⃣ Probar Permisos de Cámara

1. En HomeScreen, tocar **"Nueva Publicación"**
2. Completar campos (Título, Descripción)
3. Tocar botón **"Seleccionar imágenes"**
4. Android pedirá permiso → Tocar **"Permitir"**
5. Se abre selector de fotos → Seleccionar imagen
6. Ver preview en pantalla

---

## 🎯 Flujo Completo de Ejemplo

```
┌─────────────────────────────────────────┐
│  INICIAR SESIÓN                         │
│  Usuario: admin                         │
│  Contraseña: Admin123456!              │
└──────────┬──────────────────────────────┘
           ↓
┌─────────────────────────────────────────┐
│  HOME SCREEN (Usuario Admin)            │
│  - Nueva Publicación                    │
│  - Mis Publicaciones                    │
│  - Contacto                             │
│ ► PANEL DE ADMINISTRACIÓN ◄ (SOLO ADMIN)│
└──────────┬──────────────────────────────┘
           ↓
┌─────────────────────────────────────────┐
│  ADMIN PANEL                            │
│  ┌─────────────────────────────────────┐│
│  │ Usuario 1                           ││
│  │ Rol: USER → [dropdown: MODERATOR] ││
│  │ Estado: Activo [toggle] │ [Eliminar]││
│  ├─────────────────────────────────────┤│
│  │ Usuario 2                           ││
│  │ Rol: MODERATOR                      ││
│  │ Estado: Inactivo [toggle] │ [Eliminar]││
│  └─────────────────────────────────────┘│
└─────────────────────────────────────────┘
```

---

## 🛠️ Solución de Problemas

### Problema: "Panel de Administración" no aparece

**Causa**: Usuario no es admin
**Solución**: 
1. Verificar en DB Oracle que el usuario tenga rol ADMIN
2. Cerrar sesión y volver a iniciar

### Problema: AdminScreen no carga usuarios

**Causa**: Backend no está corriendo
**Solución**:
1. Verificar que `.\gradlew.bat bootRun` esté corriendo
2. Verificar URL en ApiProvider: `http://localhost:8080/api/`
3. Revisar logs de backend en terminal

### Problema: Permiso de galería no aparece

**Causa**: API < 30 o permiso ya otorgado
**Solución**:
1. En emulador API 30+, volver a abrir app
2. Ir a Configuración → Aplicaciones → NomadApp → Permisos
3. Revocar permisos de Almacenamiento
4. Reintentar en app

### Problema: APK no instala

**Causa**: Versión anterior instalada
**Solución**:
```bash
# Desinstalar primero
adb uninstall cl.vasquez.nomadapp

# Luego instalar
adb install app\build\outputs\apk\debug\app-debug.apk
```

---

## 📝 Notas Técnicas

### Arquitectura Admin
```
HomeScreen
  ├─ collectAsState(userRole)
  ├─ if (userRole == "ADMIN") 
  │   └─ Button("Panel Administración")
  │       └─ navigate("admin_panel")
  │
AdminScreen
  ├─ AdminViewModel
  │   ├─ loadUsers() → GET /api/admin/users
  │   ├─ updateUserRole() → POST /api/admin/users/{id}/roles
  │   ├─ enableUser() → PUT /api/admin/users/{id}/enable
  │   ├─ disableUser() → PUT /api/admin/users/{id}/disable
  │   └─ deleteUser() → DELETE /api/admin/users/{id}
  │
  └─ UI
      └─ UserRoleCard (por cada usuario)
          ├─ Dropdown Rol
          ├─ Toggle Estado
          └─ Botón Eliminar
```

### Flujo de Permisos
```
Usuario toca "Seleccionar imágenes"
  ↓
remotePhotoPickerLauncher()
  ├─ Verifica versión Android
  ├─ Solicita permiso (READ_MEDIA_IMAGES o READ_EXTERNAL_STORAGE)
  │  ↓
  │  Dialog Android: ¿Permitir acceso a galería?
  │  ├─ [Permitir] → abre selector
  │  └─ [Denegar] → cierra, usuario puede reintentar
  │
  └─ Si permitido:
     ├─ Abre MultipleContents picker
     ├─ Usuario selecciona fotos
     └─ Se toman permisos persistentes (takePersistableUriPermission)
```

---

## 🔐 Seguridad

✅ **Validación en Backend**:
- `@PreAuthorize("hasRole('ADMIN')")` en todos endpoints admin
- Token JWT validado en cada request

✅ **Validación en Frontend**:
- Botón admin solo visible si `userRole == "ADMIN"`
- Token almacenado seguro en DataStore (no SharedPreferences)

✅ **Permisos Android**:
- Solicitados en tiempo de ejecución (no solo manifest)
- Usuario controla acceso en cada operación

---

## 📊 Endpoints Admin (Backend)

| Método | Ruta | Body | Respuesta |
|--------|------|------|-----------|
| GET | `/api/admin/users` | - | `List<User>` |
| POST | `/api/admin/users/{id}/roles` | `{"role":"ADMIN"}` | `{"message":"..."}` |
| PUT | `/api/admin/users/{id}/enable` | - | `{"message":"..."}` |
| PUT | `/api/admin/users/{id}/disable` | - | `{"message":"..."}` |
| DELETE | `/api/admin/users/{id}` | - | `{"message":"..."}` |

**Autenticación**: Incluir header `Authorization: Bearer {token}`

---

## 💾 Base de Datos (Oracle)

### Tabla USERS (cambios)

```sql
-- Verificar que usuario sea ADMIN
SELECT username, roles, enabled FROM users WHERE username = 'admin';

-- Cambiar rol (si necesario)
UPDATE users 
SET roles = 'ADMIN' 
WHERE username = 'admin';
COMMIT;
```

---

## 📱 Pantallas Modificadas

### HomeScreen
- Agregado botón "Panel de Administración" (visible solo si userRole == "ADMIN")
- Colores: Purple40 (tema app)

### PostFormScreen  
- Botón "Seleccionar imágenes" ahora solicita permisos automáticamente
- No hay cambios visuales, solo funcionalidad mejorada

### AdminScreen (NUEVA)
- Lista de usuarios en LazyColumn
- Cada usuario en UserRoleCard con 3 acciones
- Error/Success messages con auto-limpiar (3 segundos)

---

## ✨ Resumen de Cambios

| Componente | Cambio |
|------------|--------|
| Backend | 5 endpoints admin ya existentes |
| Frontend (Android) | 4 archivos nuevos + 6 modificados |
| UI | 1 pantalla nueva (AdminScreen) |
| Permisos | 3 nuevos (CAMERA, READ_MEDIA_IMAGES, WRITE_EXTERNAL_STORAGE) |
| Compilación | ✅ Exitosa (0 errores) |

---

## 🎓 Tech Stack

- **Backend**: Spring Boot 3.2.5, Java 17, Oracle JDBC, JWT
- **Frontend**: Android, Kotlin, Jetpack Compose, Material 3
- **Network**: Retrofit 2.9.0, OkHttp 4.11.0
- **Local**: Room 2.6.1, DataStore 1.1.1
- **Async**: Coroutines 1.7.3

---

¡La app está lista para probar! 🚀
