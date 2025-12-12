# Backend NomadApp - Guía de Configuración

## Descripción

Backend de la aplicación NomadApp construido con Spring Boot 3.3 que se conecta a Oracle Database con Wallet y proporciona API REST para autenticación, gestión de posts, contactos y control de roles administrativos.

## Requisitos Previos

- Java 17 o superior
- Oracle Database 21c o superior
- Gradle 8.0 o superior (o usar el wrapper gradlew)
- Oracle Wallet para conexión segura

## Estructura del Proyecto

```
backend/
├── src/main/java/cl/vasquez/nomadapp/
│   ├── NomadAppBackendApplication.java       # Clase principal
│   ├── controller/                            # Controladores REST
│   │   ├── AuthController.java               # Login y Registro
│   │   ├── PostController.java               # Gestión de Posts
│   │   ├── ContactController.java            # Gestión de Contactos
│   │   └── AdminController.java              # Panel de Administración
│   ├── entity/                                # Entidades JPA
│   │   ├── User.java
│   │   ├── Post.java
│   │   ├── Contact.java
│   │   └── Role.java
│   ├── repository/                            # Repositorios JPA
│   ├── service/                               # Servicios de lógica
│   ├── security/                              # Configuración de seguridad
│   │   ├── jwt/
│   │   │   ├── JwtTokenProvider.java
│   │   │   └── JwtAuthenticationFilter.java
│   │   └── config/
│   │       └── SecurityConfig.java
│   └── dto/                                   # Objetos de transferencia
└── src/main/resources/
    ├── application.yml                        # Configuración de la app
    └── application-dev.yml                    # Configuración para desarrollo
```

## Configuración de Oracle Database

### 1. Obtener el Wallet de Oracle

Si usas Oracle Cloud, descarga el wallet desde:
1. Ir a Oracle Cloud Console → Autonomous Database
2. Seleccionar tu base de datos
3. Descargar wallet (archivo .zip)

### 2. Extraer y configurar el Wallet

```bash
# Crear directorio para el wallet
mkdir -p ~/.oracle/wallets

# Extraer el contenido del wallet
unzip Wallet_XXXX.zip -d ~/.oracle/wallets
```

### 3. Configurar variables de entorno

```bash
# Linux/Mac
export ORACLE_WALLET_PATH="file:/home/usuario/.oracle/wallets"
export ORACLE_HOST="basedatos.region.oraclecloud.com"
export ORACLE_PORT="1521"
export ORACLE_SERVICE_NAME="nomaddb_medium"
export ORACLE_USER="admin"
export ORACLE_PASSWORD="tu_contraseña_segura"
export JWT_SECRET="tu_clave_secreta_muy_larga_minimo_32_caracteres"

# Windows (PowerShell)
$env:ORACLE_WALLET_PATH = "file:C:\Users\Usuario\.oracle\wallets"
$env:ORACLE_HOST = "basedatos.region.oraclecloud.com"
$env:ORACLE_PORT = "1521"
$env:ORACLE_SERVICE_NAME = "nomaddb_medium"
$env:ORACLE_USER = "admin"
$env:ORACLE_PASSWORD = "tu_contraseña_segura"
$env:JWT_SECRET = "tu_clave_secreta_muy_larga_minimo_32_caracteres"
```

### 4. Configurar archivos de la app

Actualizar `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCPS)(HOST=${ORACLE_HOST})(PORT=${ORACLE_PORT}))(CONNECT_DATA=(SERVICE_NAME=${ORACLE_SERVICE_NAME})))
    username: ${ORACLE_USER}
    password: ${ORACLE_PASSWORD}
  
  jpa:
    hibernate:
      ddl-auto: create-drop  # O 'update' para mantener datos
```

## Construcción e Instalación

### Compilar el proyecto

```bash
./gradlew clean build
```

### Ejecutar la aplicación

```bash
./gradlew bootRun
```

La aplicación estará disponible en: `http://localhost:8080/api`

## Endpoints disponibles

### Autenticación (Públicos)

- **POST** `/api/auth/register` - Registrar nuevo usuario
- **POST** `/api/auth/login` - Iniciar sesión

### Posts (Públicos - lectura, Autenticados - escritura)

- **GET** `/api/posts` - Obtener todos los posts publicados
- **GET** `/api/posts/{id}` - Obtener un post específico
- **POST** `/api/posts` - Crear nuevo post (Autenticado)
- **PUT** `/api/posts/{id}` - Actualizar post (Autor o Admin)
- **DELETE** `/api/posts/{id}` - Eliminar post (Autor o Admin)
- **POST** `/api/posts/{id}/like` - Dar like a un post (Autenticado)

### Contactos (Autenticados)

- **POST** `/api/contacts` - Crear contacto
- **GET** `/api/contacts` - Obtener mis contactos
- **GET** `/api/contacts/{id}` - Obtener contacto específico
- **PUT** `/api/contacts/{id}` - Actualizar contacto
- **DELETE** `/api/contacts/{id}` - Eliminar contacto

### Administración (Solo Admin)

- **GET** `/api/admin/users` - Obtener todos los usuarios
- **GET** `/api/admin/users/{id}` - Obtener usuario específico
- **DELETE** `/api/admin/users/{id}` - Eliminar usuario
- **PUT** `/api/admin/users/{id}/disable` - Deshabilitar usuario
- **PUT** `/api/admin/users/{id}/enable` - Habilitar usuario
- **POST** `/api/admin/users/{userId}/roles` - Asignar rol
- **DELETE** `/api/admin/users/{userId}/roles/{roleId}` - Remover rol
- **GET** `/api/admin/roles` - Obtener todos los roles
- **POST** `/api/admin/roles` - Crear nuevo rol

## Autenticación con JWT

### Registro
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "usuario",
    "email": "usuario@ejemplo.com",
    "password": "password123",
    "firstName": "Juan",
    "lastName": "Pérez"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "usuario",
    "password": "password123"
  }'
```

**Respuesta:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "usuario",
  "email": "usuario@ejemplo.com",
  "roles": "USER"
}
```

### Usar el token

En headers de cada petición autenticada:
```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

## Roles disponibles

- **ADMIN** - Acceso total a la administración
- **USER** - Usuario regular con acceso a posts y contactos
- **MODERATOR** - Moderador con permisos especiales

## Estructura de entidades

### Usuario
- `id` - ID único
- `username` - Nombre de usuario único
- `email` - Email único
- `password` - Contraseña encriptada
- `firstName` - Nombre
- `lastName` - Apellido
- `bio` - Biografía (CLOB)
- `profileImageUrl` - URL de imagen de perfil
- `enabled` - Habilitado/Deshabilitado
- `roles` - Roles asignados
- `posts` - Posts creados
- `contacts` - Contactos del usuario

### Post
- `id` - ID único
- `title` - Título
- `content` - Contenido (CLOB)
- `imageUrl` - URL de imagen
- `author` - Autor del post
- `published` - Publicado/Borrador
- `likes` - Contador de likes
- `createdAt` - Fecha de creación
- `updatedAt` - Fecha de actualización

### Contacto
- `id` - ID único
- `name` - Nombre
- `email` - Email
- `phone` - Teléfono
- `address` - Dirección (CLOB)
- `user` - Usuario propietario
- `createdAt` - Fecha de creación
- `updatedAt` - Fecha de actualización

### Rol
- `id` - ID único
- `name` - Nombre del rol (ENUM)
- `description` - Descripción

## Desarrollo

### Crear scripts SQL (opcional)

Para inicializar datos de prueba, crear `src/main/resources/data.sql`:

```sql
-- Insertar roles
INSERT INTO roles (id, name, description) VALUES (seq_role_id.NEXTVAL, 'ADMIN', 'Administrador');
INSERT INTO roles (id, name, description) VALUES (seq_role_id.NEXTVAL, 'USER', 'Usuario Regular');
INSERT INTO roles (id, name, description) VALUES (seq_role_id.NEXTVAL, 'MODERATOR', 'Moderador');
COMMIT;
```

### Configurar para desarrollo

Crear `src/main/resources/application-dev.yml`:

```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@localhost:1521:XE
    username: testuser
    password: testpass123
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
  
logging:
  level:
    root: INFO
    cl.vasquez.nomadapp: DEBUG
```

Ejecutar:
```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```

## Solución de problemas

### Error de conexión a Oracle
- Verificar que el Wallet esté correctamente extraído
- Validar las variables de entorno
- Comprobar firewall y puertos (1521 por defecto)

### Error de autenticación JWT
- Generar una nueva clave JWT más larga (mínimo 32 caracteres)
- Verificar que el token no haya expirado
- Revisar el formato del header Authorization

### Errores de JPA/Hibernate
- Ejecutar con `ddl-auto: create-drop` para recrear tablas
- Verificar anotaciones en entidades
- Revisar logs de Hibernate

## Próximos pasos

1. ✅ Backend API REST creado
2. ⏳ Integración con aplicación móvil Android
3. ⏳ Validación de datos mejorada
4. ⏳ Paginación de resultados
5. ⏳ Tests unitarios y de integración
6. ⏳ Documentación con Swagger/OpenAPI

## Licencia

Este proyecto es parte de la aplicación NomadApp.

## Contacto y Soporte

Para reportar problemas o sugerencias, crear un issue en el repositorio.
