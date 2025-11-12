# NomadApp - Bitácora Nómada

## Descripción General

**NomadApp** es una aplicación Android moderna desarrollada en **Kotlin** con **Jetpack Compose** que permite a usuarios compartir y explorar historias de viajes. La aplicación implementa un sistema de autenticación basado en roles donde los **administradores** pueden crear publicaciones y los **usuarios invitados** pueden visualizarlas.

---

## Funcionalidades Principales

### 🔐 Autenticación y Gestión de Sesiones

- **LoginScreen**: Formulario de inicio de sesión con validación de email y contraseña
- **RegisterScreen**: Registro de nuevos usuarios con:
  - Validación de email (formato correcto)
  - Validación de contraseña (mínimo 6 caracteres)
  - Validación de confirmación de contraseña
  - **Verificación de emails duplicados** (impide registros con emails existentes)
  - Diálogos de éxito/error para feedback del usuario
- **SessionManager**: Gestión de sesiones persistente usando **DataStore Preferences**
  - Guardado automático de email y rol al login exitoso
  - Auto-logout en cierre de sesión
  - Verificación de sesión activa

### 👥 Sistema de Roles

- **Admin**: Acceso a funcionalidades de publicación
  - Crear nuevas publicaciones (título, descripción, imagen, fecha)
  - Ver lista personal de publicaciones
- **Guest**: Acceso de lectura únicamente
  - Ver publicaciones existentes en formato tarjeta (BlogCard)

### 📱 Interfaz de Usuario

- **Componentes Reutilizables**:
  - `HeaderSection`: Encabezado con título y subtítulo
  - `BlogCard`: Tarjeta de publicación con:
    - Imagen de fondo (via **Coil**)
    - **Gradiente semi-transparente** (overlay)
    - Texto superpuesto en blanco
    - Información: título, fecha, descripción
  - `PrimaryButton` / `SecondaryButton`: Botones estilizados con Material3

- **Pantallas**:
  - **LoginScreen**: Autenticación con modo prueba
  - **RegisterScreen**: Registro con validaciones completas
  - **HomeScreen**: Hub admin con opciones (Nueva Publicación / Mis Publicaciones)
  - **PostFormScreen**: Formulario para crear publicaciones
  - **PostListScreen**: Lista de publicaciones del usuario actual
  - **GuestHomeScreen**: Vista simplificada para usuarios invitados

### 🎨 Diseño Visual

- **Material Design 3**: Tema moderno y consistente
- **Jetpack Compose**: UI declarativa y reactiva
- **Overlay Gradient**: BlogCard con degradado transparente→negro para mejor legibilidad
- **TopAppBar mejorada**: Muestra email del usuario y botón de logout

---

## Arquitectura

### Patrón MVVM

```
Data Layer (Repository + DAO + Entities)
    ↓
ViewModel Layer (State Management)
    ↓
View Layer (Jetpack Compose Screens)
```

### Componentes Principales

#### Data Layer
- **User.kt**: Entity Room con email, password, role
- **UserDao.kt**: Data Access Object para operaciones CRUD de usuarios
- **Post.kt**: Entity Room para publicaciones (existente)
- **PostDao.kt**: DAO para publicaciones (existente)
- **AppDatabase.kt**: Room Database con pre-población de usuarios
- **SessionManager.kt**: Singleton para gestión de sesiones con DataStore

#### ViewModel Layer
- **LoginViewModel**: Manejo de lógica de login, validación, test mode
- **PostViewModel**: Manejo de listado y creación de publicaciones

#### View Layer
- **LoginScreen**: UI de autenticación
- **RegisterScreen**: UI de registro
- **HomeScreen**: Hub admin
- **PostFormScreen**: Creación de publicaciones
- **PostListScreen**: Visualización de publicaciones
- **CommonComponents**: Componentes reutilizables

#### Navigation
- **AppNavigation.kt**: Rutas y flujo de navegación basado en roles

---

## Stack Tecnológico

| Componente | Versión | Propósito |
|-----------|---------|----------|
| Kotlin | 2.0+ | Lenguaje principal |
| Jetpack Compose | 1.8+ | Framework UI |
| Room | 2.6.1 | ORM SQLite |
| Material3 | - | Design system |
| Navigation Compose | 2.8.3 | Enrutamiento |
| DataStore Preferences | 1.0.0 | Almacenamiento de sesión |
| Coil | 2.4.0 | Carga de imágenes |
| Lifecycle ViewModels | - | State management |
| Coroutines | - | Programación asíncrona |

---

## Usuarios Pre-Poblados

La base de datos viene con dos usuarios de demostración:

| Email | Contraseña | Rol |
|-------|-----------|-----|
| admin@nomadapp.com | 123456 | admin |
| user@nomadapp.com | password | guest |

### Modo Prueba
En LoginScreen existe un botón "Modo prueba" que permite autenticarse sin acceso a BD (útil para testing).

---

## Persistencia de Datos

### Datos de Usuario y Posts
- **SQLite via Room**: Almacenamiento persistente en Device
- **Pre-población**: Usuarios iniciales creados en AppDatabase.kt
- **Versionado**: Database v2 (incluye tabla User)

### Sesión de Usuario
- **DataStore Preferences**: Almacenamiento encriptado de credenciales de sesión
- **Flow-based**: Reactivo a cambios (aunque se usa `runBlocking` en UI por simplicidad)
- **Auto-logout**: Limpieza de sesión al hacer logout

---

## Flujos de Usuario

### Flujo de Registro
1. Usuario toca "Registrarse" en LoginScreen
2. Navega a RegisterScreen
3. Ingresa email y contraseña con confirmación
4. Sistema valida:
   - Email no duplicado (verifica en BD)
   - Password ≥ 6 caracteres
   - Contraseñas coinciden
5. Si válido → Registro exitoso + diálogo confirmación + redirección a login
6. Si inválido → Diálogo de error con detalles

### Flujo de Login
1. Usuario ingresa email y contraseña
2. LoginViewModel valida formato
3. LoginRepository consulta BD o usa test mode
4. Si exitoso:
   - SessionManager guarda sesión (email + rol)
   - Navega a `home_admin` (si rol=admin) o `home_guest` (si rol=guest)
   - TopAppBar muestra email del usuario
5. Si falla → Muestra error en pantalla

### Flujo de Logout
1. Usuario toca ícono Logout en TopAppBar
2. SessionManager.logout() limpia sesión
3. NavController redirige a LoginScreen con `popUpTo(0)`
4. Sesión completamente limpiada

---

## Estructura de Proyecto

```
app/src/main/
├── java/cl/vasquez/nomadapp/
│   ├── data/
│   │   ├── AppDatabase.kt
│   │   ├── Post.kt
│   │   ├── PostDao.kt
│   │   ├── User.kt
│   │   ├── UserDao.kt
│   │   └── SessionManager.kt
│   ├── model/
│   │   ├── LoginRepository.kt
│   │   └── LoginResult.kt
│   ├── view/
│   │   ├── LoginScreen.kt
│   │   ├── RegisterScreen.kt
│   │   ├── HomeScreen.kt
│   │   ├── GuestHomeScreen.kt
│   │   ├── PostFormScreen.kt
│   │   ├── PostListScreen.kt
│   │   ├── LocalContextProvider.kt
│   │   └── components/
│   │       ├── HeaderSection.kt
│   │       └── CommonComponents.kt
│   ├── viewmodel/
│   │   ├── LoginViewModel.kt
│   │   └── PostViewModel.kt
│   ├── navigation/
│   │   └── AppNavigation.kt
│   ├── utils/
│   │   └── ValidationUtils.kt
│   └── MainActivity.kt
├── res/
└── AndroidManifest.xml
```

---

## Validaciones Implementadas

### Email
- No puede estar vacío
- Debe ser formato válido (patrón regex de Android)
- No puede estar duplicado en BD (en registro)

### Contraseña
- No puede estar vacía
- Mínimo 6 caracteres
- Debe ser igual a confirmación en registro
- Se oculta en TextField con PasswordVisualTransformation

### Publicación
- Título: requerido
- Descripción: requerido
- Imagen: opcional (utiliza URI)
- Fecha: se auto-genera del sistema

---

## Compilación y Ejecución

### Requisitos
- Android Studio Flamingo+
- JDK 11+
- Android SDK 30+ (minSdkVersion)
- Gradle 8.13+

### Build
```bash
./gradlew build
```

### Run en Emulador/Device
```bash
./gradlew installDebug
```

---

## Estado Actual

✅ **Funcionalidades Completadas:**
- Autenticación completa con validación
- Registro con verificación de emails duplicados
- Navegación basada en roles
- Gestión de sesiones con DataStore
- UI moderna con Material3 y Compose
- Componentes reutilizables
- BlogCard con overlay gradient
- Pre-población de usuarios
- Modo prueba para testing
- Logout con redirección
- Diálogos de éxito/error

⚠️ **Mejoras Futuras:**
- Usar Flow para sesión en lugar de runBlocking
- Implementar Hilt para inyección de dependencias
- Agregar vista de detalles de publicación
- Implementar búsqueda/filtrado
- Agregar comentarios/calificaciones
- Autenticación con servidor remoto
- Sincronización en nube

---

## Notas de Desarrollo

- **LocalContextProvider**: Solución temporal para acceso a Context. Idealmente usar Hilt o CompositionLocal.
- **Deprecated Icons**: Los iconos `ArrowBack` y `Logout` están deprecados. Considerar migrar a `Icons.AutoMirrored.Filled.*` en futuras versiones.
- **Kotlin 2.0+**: Kapt muestra warning por incompatibilidad, no afecta funcionalidad.
- **DataStore Preferences**: Almacenamiento seguro y encriptado automáticamente por framework.

---

## Autor
Desarrollado como extensión del proyecto **NomadApp** con integración de MVVM y autenticación.

**Última actualización**: 11 de Noviembre 2025  
**Estado**: ✅ Compilación Exitosa (BUILD SUCCESSFUL)
