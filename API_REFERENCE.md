# API Reference - NomadApp Backend

## Base URL

```
http://localhost:8080/api
```

En producción:
```
https://api.tudominio.com/api
```

## Autenticación

Todos los endpoints excepto `/auth/*` y `/posts` (GET) requieren un token JWT en el header:

```
Authorization: Bearer <token>
```

## Códigos de Estado HTTP

- `200` - OK
- `201` - Created (Recurso creado)
- `204` - No Content
- `400` - Bad Request (Datos inválidos)
- `401` - Unauthorized (Token requerido o inválido)
- `403` - Forbidden (Sin permisos)
- `404` - Not Found (Recurso no encontrado)
- `409` - Conflict (Recurso ya existe)
- `500` - Internal Server Error

---

## Authentication Endpoints

### POST /auth/register

Registrar nuevo usuario.

**Request Body:**
```json
{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe"
}
```

**Response (201 Created):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "johndoe",
  "email": "john@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "roles": "USER",
  "message": "Registro exitoso"
}
```

**Errores:**
- `409` - Username o email ya existen

---

### POST /auth/login

Iniciar sesión.

**Request Body:**
```json
{
  "username": "johndoe",
  "password": "password123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "johndoe",
  "email": "john@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "roles": "USER",
  "message": "Inicio de sesión exitoso"
}
```

**Errores:**
- `401` - Usuario no encontrado o contraseña incorrecta
- `401` - Usuario deshabilitado

---

## Posts Endpoints

### GET /posts

Obtener todos los posts publicados (Público).

**Query Parameters:**
- (Ninguno actualmente)

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "title": "Mi primer post",
    "content": "Contenido del post...",
    "imageUrl": "https://...",
    "author": {
      "id": 1,
      "username": "johndoe",
      "firstName": "John",
      "lastName": "Doe"
    },
    "likes": 5,
    "published": true,
    "createdAt": "2024-12-12T10:30:00",
    "updatedAt": "2024-12-12T10:30:00"
  }
]
```

---

### GET /posts/{id}

Obtener post específico (Público).

**Path Parameters:**
- `id` (Long) - ID del post

**Response (200 OK):**
```json
{
  "id": 1,
  "title": "Mi primer post",
  "content": "Contenido del post...",
  "imageUrl": "https://...",
  "author": {
    "id": 1,
    "username": "johndoe",
    "firstName": "John",
    "lastName": "Doe"
  },
  "likes": 5,
  "published": true,
  "createdAt": "2024-12-12T10:30:00",
  "updatedAt": "2024-12-12T10:30:00"
}
```

**Errores:**
- `404` - Post no encontrado

---

### POST /posts

Crear nuevo post (Autenticado - USER, ADMIN).

**Request Body:**
```json
{
  "title": "Mi primer post",
  "content": "Contenido detallado del post...",
  "imageUrl": "https://...",
  "published": true
}
```

**Response (201 Created):**
```json
{
  "id": 2,
  "title": "Mi primer post",
  "content": "Contenido detallado del post...",
  "imageUrl": "https://...",
  "author": {
    "id": 1,
    "username": "johndoe"
  },
  "likes": 0,
  "published": true,
  "createdAt": "2024-12-12T10:35:00",
  "updatedAt": "2024-12-12T10:35:00"
}
```

**Validaciones:**
- `title` - Requerido, 3-200 caracteres
- `content` - Requerido, mínimo 10 caracteres
- `published` - Opcional, por defecto true

**Errores:**
- `400` - Validación fallida
- `401` - No autenticado

---

### PUT /posts/{id}

Actualizar post (Autor o ADMIN).

**Path Parameters:**
- `id` (Long) - ID del post

**Request Body:**
```json
{
  "title": "Título actualizado",
  "content": "Contenido actualizado...",
  "imageUrl": "https://...",
  "published": true
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "title": "Título actualizado",
  "content": "Contenido actualizado...",
  "imageUrl": "https://...",
  "author": {
    "id": 1,
    "username": "johndoe"
  },
  "likes": 5,
  "published": true,
  "createdAt": "2024-12-12T10:30:00",
  "updatedAt": "2024-12-12T11:00:00"
}
```

**Errores:**
- `400` - Validación fallida
- `401` - No autenticado
- `403` - No eres el autor
- `404` - Post no encontrado

---

### DELETE /posts/{id}

Eliminar post (Autor o ADMIN).

**Path Parameters:**
- `id` (Long) - ID del post

**Response (200 OK):**
```json
{
  "message": "Post eliminado exitosamente"
}
```

**Errores:**
- `401` - No autenticado
- `403` - No eres el autor
- `404` - Post no encontrado

---

### POST /posts/{id}/like

Dar like a un post (Autenticado).

**Path Parameters:**
- `id` (Long) - ID del post

**Response (200 OK):**
```json
{
  "id": 1,
  "title": "Mi primer post",
  "content": "Contenido...",
  "author": {...},
  "likes": 6,
  "published": true,
  "createdAt": "2024-12-12T10:30:00",
  "updatedAt": "2024-12-12T11:05:00"
}
```

**Errores:**
- `401` - No autenticado
- `404` - Post no encontrado

---

## Contacts Endpoints

### POST /contacts

Crear nuevo contacto (Autenticado).

**Request Body:**
```json
{
  "name": "Carlos Pérez",
  "email": "carlos@example.com",
  "phone": "+56912345678",
  "address": "Calle Principal 123, Santiago"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "name": "Carlos Pérez",
  "email": "carlos@example.com",
  "phone": "+56912345678",
  "address": "Calle Principal 123, Santiago",
  "user": {
    "id": 1,
    "username": "johndoe"
  },
  "createdAt": "2024-12-12T10:40:00",
  "updatedAt": "2024-12-12T10:40:00"
}
```

**Validaciones:**
- `name` - Requerido
- `email` - Requerido, formato válido
- `phone` - Requerido
- `address` - Opcional

**Errores:**
- `400` - Validación fallida
- `401` - No autenticado

---

### GET /contacts

Obtener mis contactos (Autenticado).

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Carlos Pérez",
    "email": "carlos@example.com",
    "phone": "+56912345678",
    "address": "Calle Principal 123, Santiago",
    "user": {
      "id": 1,
      "username": "johndoe"
    },
    "createdAt": "2024-12-12T10:40:00",
    "updatedAt": "2024-12-12T10:40:00"
  }
]
```

**Errores:**
- `401` - No autenticado

---

### GET /contacts/{id}

Obtener contacto específico (Autenticado).

**Path Parameters:**
- `id` (Long) - ID del contacto

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "Carlos Pérez",
  "email": "carlos@example.com",
  "phone": "+56912345678",
  "address": "Calle Principal 123, Santiago",
  "user": {
    "id": 1,
    "username": "johndoe"
  },
  "createdAt": "2024-12-12T10:40:00",
  "updatedAt": "2024-12-12T10:40:00"
}
```

**Errores:**
- `401` - No autenticado
- `404` - Contacto no encontrado

---

### PUT /contacts/{id}

Actualizar contacto (Propietario).

**Path Parameters:**
- `id` (Long) - ID del contacto

**Request Body:**
```json
{
  "name": "Carlos Pérez Actualizado",
  "email": "carlos.updated@example.com",
  "phone": "+56987654321",
  "address": "Nueva dirección 456"
}
```

**Response (200 OK):** Similar al GET

**Errores:**
- `400` - Validación fallida
- `401` - No autenticado
- `403` - No eres el propietario
- `404` - Contacto no encontrado

---

### DELETE /contacts/{id}

Eliminar contacto (Propietario).

**Path Parameters:**
- `id` (Long) - ID del contacto

**Response (200 OK):**
```json
{
  "message": "Contacto eliminado exitosamente"
}
```

**Errores:**
- `401` - No autenticado
- `403` - No eres el propietario
- `404` - Contacto no encontrado

---

## Administration Endpoints

### GET /admin/users

Obtener todos los usuarios (ADMIN).

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "username": "johndoe",
    "email": "john@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "enabled": true,
    "roles": ["USER"],
    "createdAt": "2024-12-12T10:30:00",
    "updatedAt": "2024-12-12T10:30:00"
  }
]
```

**Errores:**
- `401` - No autenticado
- `403` - No eres administrador

---

### GET /admin/users/{id}

Obtener usuario específico (ADMIN).

**Path Parameters:**
- `id` (Long) - ID del usuario

**Response (200 OK):** Similar a GET /admin/users (un elemento)

**Errores:**
- `401` - No autenticado
- `403` - No eres administrador
- `404` - Usuario no encontrado

---

### POST /admin/users/{userId}/roles

Asignar rol a usuario (ADMIN).

**Path Parameters:**
- `userId` (Long) - ID del usuario

**Request Body:**
```json
{
  "role": "ADMIN"
}
```

**Response (200 OK):**
```json
{
  "message": "Rol asignado exitosamente"
}
```

**Roles disponibles:** `ADMIN`, `USER`, `MODERATOR`

**Errores:**
- `400` - Rol inválido
- `401` - No autenticado
- `403` - No eres administrador
- `404` - Usuario o rol no encontrado

---

### DELETE /admin/users/{userId}/roles/{roleId}

Remover rol de usuario (ADMIN).

**Path Parameters:**
- `userId` (Long) - ID del usuario
- `roleId` (Long) - ID del rol

**Response (200 OK):**
```json
{
  "message": "Rol removido exitosamente"
}
```

**Errores:**
- `401` - No autenticado
- `403` - No eres administrador
- `404` - Usuario o rol no encontrado

---

### PUT /admin/users/{id}/disable

Deshabilitar usuario (ADMIN).

**Path Parameters:**
- `id` (Long) - ID del usuario

**Response (200 OK):**
```json
{
  "message": "Usuario deshabilitado exitosamente"
}
```

**Errores:**
- `401` - No autenticado
- `403` - No eres administrador
- `404` - Usuario no encontrado

---

### PUT /admin/users/{id}/enable

Habilitar usuario (ADMIN).

**Path Parameters:**
- `id` (Long) - ID del usuario

**Response (200 OK):**
```json
{
  "message": "Usuario habilitado exitosamente"
}
```

**Errores:**
- `401` - No autenticado
- `403` - No eres administrador
- `404` - Usuario no encontrado

---

### DELETE /admin/users/{id}

Eliminar usuario (ADMIN).

**Path Parameters:**
- `id` (Long) - ID del usuario

**Response (200 OK):**
```json
{
  "message": "Usuario eliminado exitosamente"
}
```

**Errores:**
- `401` - No autenticado
- `403` - No eres administrador
- `404` - Usuario no encontrado

---

### GET /admin/roles

Obtener todos los roles (ADMIN).

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "ADMIN",
    "description": "Administrador"
  },
  {
    "id": 2,
    "name": "USER",
    "description": "Usuario Regular"
  }
]
```

**Errores:**
- `401` - No autenticado
- `403` - No eres administrador

---

### POST /admin/roles

Crear nuevo rol (ADMIN).

**Request Body:**
```json
{
  "name": "MODERATOR",
  "description": "Moderador de contenido"
}
```

**Response (201 Created):**
```json
{
  "id": 3,
  "name": "MODERATOR",
  "description": "Moderador de contenido"
}
```

**Errores:**
- `400` - Rol inválido
- `401` - No autenticado
- `403` - No eres administrador

---

## Ejemplos de Uso

### cURL

**Registro:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johndoe",
    "email": "john@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe"
  }'
```

**Login:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johndoe",
    "password": "password123"
  }'
```

**Crear Post:**
```bash
TOKEN="eyJhbGciOiJIUzUxMiJ9..."

curl -X POST http://localhost:8080/api/posts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "title": "Mi post",
    "content": "Contenido del post...",
    "published": true
  }'
```

### JavaScript/Fetch

```javascript
// Registro
async function register() {
  const response = await fetch('http://localhost:8080/api/auth/register', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      username: 'johndoe',
      email: 'john@example.com',
      password: 'password123',
      firstName: 'John',
      lastName: 'Doe'
    })
  });
  
  return await response.json();
}

// Login
async function login() {
  const response = await fetch('http://localhost:8080/api/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      username: 'johndoe',
      password: 'password123'
    })
  });
  
  const data = await response.json();
  localStorage.setItem('token', data.token);
  return data;
}

// Crear Post
async function createPost(title, content) {
  const token = localStorage.getItem('token');
  
  const response = await fetch('http://localhost:8080/api/posts', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify({
      title,
      content,
      published: true
    })
  });
  
  return await response.json();
}
```

### Kotlin/Retrofit

Ver archivo [ApiService.kt](app/src/main/java/cl/vasquez/nomadapp/network/ApiService.kt)

---

## Rate Limiting (Futuro)

Actualmente sin límite de tasa. Se recomienda implementar en producción.

---

## Versionamiento de API

Versión actual: **v1**

Los endpoints pueden cambiar. Se notificará con anticipación antes de cambios breaking.

---

## Support

Para reportar problemas o sugerencias sobre la API, crear un issue en el repositorio.
