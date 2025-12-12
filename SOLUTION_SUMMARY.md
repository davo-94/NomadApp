# NomadApp - Solución Backend con Oracle y Control de Roles

## 📋 Resumen Ejecutivo

Se ha completado la migración arquitectónica de NomadApp de una aplicación Android con base de datos local (Room) a una arquitectura moderna de **cliente-servidor** con:

- ✅ **Backend REST API** en Spring Boot 3.3
- ✅ **Base de datos Oracle** con Wallet seguro
- ✅ **Autenticación JWT** para seguridad
- ✅ **Control de Roles** con panel administrativo
- ✅ **Docker** para despliegue containerizado
- ✅ **Documentación completa** para integración

---

## 🏗️ Arquitectura de la Solución

```
┌─────────────────────┐
│   APP MÓVIL ANDROID │  (Kotlin + Jetpack Compose)
│    (NomadApp)       │
└──────────┬──────────┘
           │ HTTP/REST
           │ JWT Token
           ▼
┌─────────────────────┐
│   BACKEND API REST  │  (Spring Boot 3.3)
│   - Auth            │  - Autenticación JWT
│   - Posts           │  - CRUD de Posts
│   - Contacts        │  - CRUD de Contactos
│   - Admin Panel     │  - Gestión de Roles
└──────────┬──────────┘
           │ SQL
           ▼
┌─────────────────────┐
│   ORACLE DATABASE   │  (21c +)
│   - Con Wallet      │  - Conexión segura TCPS
│   - Encriptado      │  - Tablas normalizadas
└─────────────────────┘
```

---

## 📁 Estructura de Carpetas

### Backend
```
backend/
├── src/main/java/cl/vasquez/nomadapp/
│   ├── NomadAppBackendApplication.java
│   ├── controller/              # Endpoints REST
│   │   ├── AuthController.java
│   │   ├── PostController.java
│   │   ├── ContactController.java
│   │   └── AdminController.java
│   ├── entity/                  # Modelos JPA
│   │   ├── User.java
│   │   ├── Post.java
│   │   ├── Contact.java
│   │   └── Role.java
│   ├── repository/              # Acceso a datos
│   ├── service/                 # Lógica de negocio
│   ├── security/                # Seguridad
│   │   ├── jwt/
│   │   │   ├── JwtTokenProvider.java
│   │   │   └── JwtAuthenticationFilter.java
│   │   └── config/
│   │       └── SecurityConfig.java
│   └── dto/                     # Objetos de transferencia
├── src/main/resources/
│   └── application.yml          # Configuración
├── Dockerfile
└── build.gradle.kts
```

### App Móvil (nuevos archivos)
```
app/src/main/java/cl/vasquez/nomadapp/
├── network/
│   ├── ApiService.kt           # Definición de endpoints
│   └── ApiProvider.kt          # Configuración de Retrofit
├── model/
│   └── RemoteRepository.kt     # Conexión con backend
└── utils/
    └── TokenManager.kt         # Gestión de tokens JWT
```

---

## 🔐 Características de Seguridad

### 1. **Autenticación JWT**
- Tokens con expiración de 24 horas
- Almacenamiento seguro en DataStore (Android)
- Incluye roles en el token

### 2. **Control de Acceso por Roles**

| Rol | Posts | Contactos | Usuarios | Roles |
|-----|-------|-----------|----------|-------|
| USER | ✅ | ✅ | ❌ | ❌ |
| ADMIN | ✅ | ✅ | ✅ | ✅ |
| MODERATOR | ✅ | ✅ | ⚠️ | ❌ |

### 3. **Conexión a Oracle con Wallet**
- Encriptación TCPS (TLS 1.3)
- Wallet descargable desde Oracle Cloud
- Variables de entorno seguras

### 4. **Validación de Datos**
- Validación en servidor (Jakarta Validation)
- Validación en cliente (Android)
- Protección contra inyección SQL (JPA)

---

## 📊 Modelos de Datos

### User
```
id (PK)
username (UNIQUE)
email (UNIQUE)
password (encriptada)
firstName
lastName
bio
profileImageUrl
enabled
roles (Many-to-Many)
posts (One-to-Many)
contacts (One-to-Many)
createdAt
updatedAt
```

### Post
```
id (PK)
title
content (CLOB)
imageUrl
author (FK → User)
published
likes
createdAt
updatedAt
```

### Contact
```
id (PK)
name
email
phone
address (CLOB)
user (FK → User)
createdAt
updatedAt
```

### Role
```
id (PK)
name (ENUM: ADMIN, USER, MODERATOR)
description
```

---

## 🚀 Endpoints Disponibles

### Autenticación (Públicos)
- `POST /auth/register` - Registrar usuario
- `POST /auth/login` - Iniciar sesión

### Posts
- `GET /posts` - Obtener posts (público)
- `POST /posts` - Crear post (autenticado)
- `PUT /posts/{id}` - Actualizar post (autor/admin)
- `DELETE /posts/{id}` - Eliminar post (autor/admin)
- `POST /posts/{id}/like` - Dar like (autenticado)

### Contactos
- `GET /contacts` - Obtener mis contactos
- `POST /contacts` - Crear contacto
- `PUT /contacts/{id}` - Actualizar contacto
- `DELETE /contacts/{id}` - Eliminar contacto

### Administración (ADMIN ONLY)
- `GET /admin/users` - Listar usuarios
- `GET /admin/users/{id}` - Obtener usuario
- `POST /admin/users/{userId}/roles` - Asignar rol
- `DELETE /admin/users/{userId}/roles/{roleId}` - Remover rol
- `PUT /admin/users/{id}/disable` - Deshabilitar usuario
- `PUT /admin/users/{id}/enable` - Habilitar usuario
- `DELETE /admin/users/{id}` - Eliminar usuario
- `GET /admin/roles` - Listar roles
- `POST /admin/roles` - Crear rol

---

## 🛠️ Configuración Inicial

### 1. Oracle Database

```bash
# Descargar wallet desde Oracle Cloud
# Extraer en ~/.oracle/wallets

# Crear usuario (si no existe)
CREATE USER nomadapp IDENTIFIED BY password123;
GRANT CONNECT, RESOURCE TO nomadapp;
```

### 2. Variables de Entorno

```bash
export ORACLE_HOST=basedatos.region.oraclecloud.com
export ORACLE_PORT=1521
export ORACLE_SERVICE_NAME=nomaddb_medium
export ORACLE_USER=admin
export ORACLE_PASSWORD=tu_password
export ORACLE_WALLET_PATH=file:/home/usuario/.oracle/wallets
export JWT_SECRET=clave_larga_minimo_32_caracteres
```

### 3. Compilar y Ejecutar

```bash
# Backend
cd backend
./gradlew bootRun

# La API estará disponible en: http://localhost:8080/api
```

---

## 📱 Integración en App Móvil

### Pasos para actualizar

1. **Agregar dependencias** (Retrofit, OkHttp, Coroutines)
2. **Crear ApiService** con endpoints
3. **Crear RemoteRepositories** que usen la API
4. **Actualizar ViewModels** para usar repositorios remotos
5. **Guardar token JWT** en DataStore
6. **Implementar TokenManager** para acceso seguro

Ver `INTEGRATION_GUIDE.md` para detalles completos.

---

## 🐳 Despliegue con Docker

### Construir y ejecutar localmente

```bash
# Copiar variables de entorno
cp .env.example .env
# Editar .env con tus valores

# Ejecutar
docker-compose up -d

# Ver logs
docker-compose logs -f nomadapp-backend

# Detener
docker-compose down
```

### Despliegue en producción

- **Docker en OCI Compute Instance**
- **Heroku** (opción económica)
- **Kubernetes** (escalado horizontal)

Ver `DEPLOYMENT_GUIDE.md` para instrucciones detalladas.

---

## 📚 Documentación

| Documento | Descripción |
|-----------|------------|
| `backend/README.md` | Guía completa del backend |
| `INTEGRATION_GUIDE.md` | Cómo integrar en la app móvil |
| `API_REFERENCE.md` | Documentación detallada de endpoints |
| `DEPLOYMENT_GUIDE.md` | Guía de despliegue en producción |

---

## ✅ Checklist de Implementación

### Backend
- [x] Estructura del proyecto creada
- [x] Entidades JPA definidas
- [x] Repositorios implementados
- [x] Servicios de lógica de negocio
- [x] Controladores REST
- [x] Autenticación JWT
- [x] Control de roles
- [x] Validación de datos
- [x] Manejo de errores
- [x] Documentación

### App Móvil (próximas tareas)
- [ ] Agregar dependencias
- [ ] Crear ApiService
- [ ] Crear RemoteRepositories
- [ ] Actualizar ViewModels
- [ ] Implementar TokenManager
- [ ] Crear UI para login/register
- [ ] Crear pantalla de admin
- [ ] Agregar manejo offline
- [ ] Tests de integración
- [ ] Optimización de performance

### DevOps
- [x] Dockerfile configurado
- [x] docker-compose.yml creado
- [ ] CI/CD pipeline (GitHub Actions)
- [ ] Monitoring y alertas
- [ ] Backup automático
- [ ] Plan de disaster recovery

---

## 🔄 Flujos principales

### 1. Registro e Inicio de Sesión

```
Usuario → App Móvil → POST /auth/register → Backend → Oracle DB
                                              ↓
                        Retorna JWT Token → App guarda en DataStore
```

### 2. Crear Post

```
Usuario → App Móvil → POST /posts (con JWT) → Backend
                                                 ↓
                                            Valida Token
                                                 ↓
                                            Crea en DB
                                                 ↓
                                            Retorna Post
```

### 3. Admin asigna rol

```
Admin → Panel Web → POST /admin/users/{id}/roles → Backend
                                                     ↓
                                              Valida que sea ADMIN
                                                     ↓
                                              Asigna rol en DB
                                                     ↓
                                              Retorna confirmación
```

---

## 🎯 Próximos Pasos Recomendados

### Corto Plazo (1-2 semanas)
1. Completar integración en app móvil
2. Tests de integración
3. Desplegar en ambiente de staging
4. UAT con usuarios finales

### Mediano Plazo (1 mes)
1. Agregar paginación
2. Implementar búsqueda avanzada
3. Notificaciones push
4. Caché de datos locales

### Largo Plazo (2-3 meses)
1. Agregar mensajes directos
2. Followers/Following
3. Trending topics
4. Analytics y reportes

---

## 🆘 Soporte y Troubleshooting

### Problemas Comunes

**Error de conexión a Oracle**
- Verificar Wallet está en la ruta correcta
- Validar variables de entorno
- Comprobar firewall abierto en puerto 1521

**Token expirado**
- Implementar refresh token
- Usuario debe hacer login nuevamente

**CORS errors**
- Backend ya permite todos los orígenes (`@CrossOrigin(origins = "*")`)
- Restringir en producción

Ver `backend/README.md` para más soluciones.

---

## 📞 Contacto

Para preguntas o reportar problemas:
- Crear issue en el repositorio
- Documentar pasos para reproducir
- Incluir versiones relevantes (Java, Spring, Oracle, etc.)

---

**Última actualización:** 12 de Diciembre de 2024
**Versión:** 1.0.0
**Estado:** ✅ Listo para desarrollo

