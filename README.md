# 📸 NomadApp – Bitácora Nómada
Aplicación móvil desarrollada en **Kotlin + Jetpack Compose + Room + MVVM**, que permite registrar experiencias de viaje, gestionar publicaciones con imágenes y explorar contenido tanto como usuario autenticado como invitado. Incluye validación de formularios, persistencia local, navegación modular y uso de recursos nativos del dispositivo.

---

## Equipo de desarrollo
- **Nicolás Lorca**
- **David Vásquez**


---

## Objetivo del proyecto
Crear una aplicación móvil funcional que permita:
- Publicar experiencias de viaje con múltiples imágenes.
- Visualizar publicaciones con carrusel de fotos.
- Utilizar modo invitado y modo usuario autenticado.
- Integrar validaciones de formulario.
- Usar almacenamiento local (Room + DataStore).
- Implementar navegación fluida con Jetpack Compose.
- Aplicar diseño visual consistente basado en Material Design 3.

---

##  Arquitectura utilizada: MVVM
El proyecto sigue el patrón **Model–View–ViewModel**, permitiendo una separación clara de responsabilidades:

### ** data/**
- `AppDatabase.kt`: configuración de Room + DAOs  
- `Post.kt`, `User.kt`, `Contact.kt`: entidades  
- `Converters.kt`: convierte `List<String>` (URIs) en JSON para Room  
- `PostDao.kt`, `UserDao.kt`, `ContactDao.kt`: CRUD  
- `SessionManager.kt`: manejo de sesión con DataStore  

### ** model/**
- Repositorios que abstraen DAOs:  
  `PostRepository.kt`, `LoginRepository.kt`, `ContactRepository.kt`  

### ** viewmodel/**
- Lógica de negocio + estados con StateFlow:  
  `PostViewModel.kt`, `LoginViewModel.kt`, `ContactViewModel.kt`

### ** view/**
- Todas las pantallas en Jetpack Compose:  
  `LoginScreen.kt`, `RegisterScreen.kt`, `HomeScreen.kt`,  
  `GuestHomeScreen.kt`, `PostListScreen.kt`,  
  `GuestPostListScreen.kt`, `PostFormScreen.kt`, etc.

### ** navigation/**
- `AppNavigation.kt`: rutas modulares para usuario y guest.

### ** utils/**
- `ValidationUtils.kt`: validaciones reutilizables (email, campos vacíos, etc.)

### ** ui/theme/**
- Colores, tipografía y estilos Material 3.

---

##  Funcionalidades implementadas

###  **Autenticación y sesión**
- Login y registro con validaciones  
- Manejo de sesión persistente con `DataStore`  
- Logout funcional

###  **CRUD de publicaciones**
- Crear publicaciones con múltiples imágenes  
- Selección de imágenes desde la galería (recurso nativo)  
- Carrusel horizontal con `HorizontalPager`  
- Editar y eliminar publicaciones  
- Persistencia en Room

###  **Navegación**
- Flujo principal (usuario autenticado)  
- Modo invitado (solo lectura)  
- Navegación modular vía `NavHost`

###  **Formulario de contacto**
- Validación visual  
- Guardado local en Room

###  **Recursos nativos**
- Acceso a galería mediante `ActivityResultLauncher`  
- Carga de imágenes con `AsyncImage` (Coil)  
- Guardado de URIs como `List<String>`

###  **Diseño visual**
- Material Design 3  
- Fondos semitransparentes  
- Componentes reutilizables (HeaderSection, PrimaryButton, etc.)  
- Animaciones leves (fadeIn, fadeOut, HorizontalPager)

---

##  Requisitos técnicos
- Android Studio Giraffe o superior  
- Android 9.0 (API 28) o superior  
- Gradle configurado automáticamente por Android Studio  

---

##  Cómo ejecutar el proyecto

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/tuusuario/NomadApp.git

2. Abrir en Android Studio

3. Esperar sincronización de Gradle

4. Ejecutar en un dispositivo físico o emulador

5. Credenciales de prueba:
Admin
user: admin@nomadapp.com
pass: abc1234

Guest
user: user@nomadapp.com
pass: password123

## Estructura del repositorio

- /app — código fuente completo

- /gradle — configuración de Gradle

- build.gradle — configuración raíz

- README.md — este archivo

## Estado del proyecto

Funcional y estable

Probado completamente en entorno local

Listo para evaluación EVA2

## Nota final

* Este proyecto forma parte de la Evaluación Parcial 2 del ramo Desarrollo de Aplicaciones Móviles, siguiendo los criterios de diseño, validación, navegación, almacenamiento local, arquitectura y uso de recursos nativos establecidos por la rúbrica EVA2.
