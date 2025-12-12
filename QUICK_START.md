# Quick Start - NomadApp Backend

## ✅ Configuración Completada

Tu backend está configurado para conectarse a **Oracle Cloud** con los siguientes datos:

- **Host**: adb.sa-santiago-1.oraclecloud.com
- **Puerto**: 1522
- **Usuario**: admin
- **Servicio**: gec91f46eadff57_personal_medium.adb.oraclecloud.com
- **Wallet**: C:\Users\Nicolas\.oracle\wallets

---

## 🚀 Ejecutar el Backend Localmente

### 1. Desde la carpeta raíz del proyecto

```bash
cd c:\Users\Nicolas\OneDrive\Documentos\GitHub\NomadApp
```

### 2. Compilación ya completada ✅

El backend ya está compilado. El JAR se encuentra en:
```
backend/build/libs/nomadapp-1.0.0.jar
```

### 3. Ejecutar - Opción A: Directamente desde JAR

```bash
java -jar backend/build/libs/nomadapp-1.0.0.jar
```

**Salida esperada:**
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_|\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v3.2.5)

2025-12-12 ... : Starting NomadAppBackendApplication v1.0.0
...
2025-12-12 ... : Started NomadAppBackendApplication in 5.123s
```

### 3b. Ejecutar - Opción B: Con Gradle

```bash
./gradlew :backend:bootRun
```

### 3c. Ejecutar - Opción C: Script batch (Windows)

```bash
./run-backend.bat
```

```bash
./gradlew bootRun
```

O alternativamente:

```bash
./gradlew build
java -jar build/libs/nomadapp-backend-1.0.0.jar
```

### 4. Verificar que está funcionando

```bash
curl http://localhost:8080/api/posts
```

**Respuesta esperada:** `[]` (array vacío de posts)

---

## 🧪 Probar la API

### Registrar un usuario

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "firstName": "Test",
    "lastName": "User"
  }'
```

**Respuesta:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "testuser",
  "email": "test@example.com",
  "roles": "USER",
  "message": "Registro exitoso"
}
```

### Iniciar sesión

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

### Crear un post (requiere token)

```bash
TOKEN="tu_token_aqui"

curl -X POST http://localhost:8080/api/posts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "title": "Mi primer post",
    "content": "Este es el contenido de mi primer post en NomadApp",
    "published": true
  }'
```

### Obtener todos los posts

```bash
curl http://localhost:8080/api/posts
```

---

## 📁 URLs importantes

| Recurso | URL |
|---------|-----|
| **API REST** | http://localhost:8080/api |
| **Posts** | http://localhost:8080/api/posts |
| **Autenticación** | http://localhost:8080/api/auth/login |

---

## 🐳 Ejecutar con Docker (Alternativo)

### Construir imagen

```bash
docker build -f backend/Dockerfile -t nomadapp-backend:latest .
```

### Ejecutar contenedor

```bash
docker run -p 8080:8080 \
  -e ORACLE_HOST=adb.sa-santiago-1.oraclecloud.com \
  -e ORACLE_PORT=1522 \
  -e ORACLE_SERVICE_NAME=gec91f46eadff57_personal_medium.adb.oraclecloud.com \
  -e ORACLE_USER=admin \
  -e ORACLE_PASSWORD=Salocingamer99 \
  -e ORACLE_WALLET_PATH=file:/opt/oracle/wallets \
  -v C:\Users\Nicolas\.oracle\wallets:/opt/oracle/wallets \
  nomadapp-backend:latest
```

---

## ⚠️ Solución de Problemas

### Error: "No se puede conectar a Oracle"

**Causa**: Firewall o credenciales incorrectas

**Solución**:
1. Verificar que la máquina tenga acceso a internet
2. Verificar que las credenciales sean correctas
3. Reintentar la conexión

### Error: "Wallet not found"

**Causa**: Wallet no está en la ruta correcta

**Solución**:
```bash
# Verificar que el wallet existe
ls C:\Users\Nicolas\.oracle\wallets

# Debería mostrar:
# - cwallet.sso
# - ewallet.p12
# - ewallet.pem
# - keystore.jks
# - etc...
```

### Puerto 8080 ya está en uso

**Solución**: Cambiar el puerto en `application.yml`:

```yaml
server:
  port: 8081
```

---

## 📚 Documentación Completa

- [Backend README](backend/README.md) - Documentación técnica del backend
- [API Reference](API_REFERENCE.md) - Documentación de todos los endpoints
- [Integration Guide](INTEGRATION_GUIDE.md) - Cómo integrar con la app móvil
- [Deployment Guide](DEPLOYMENT_GUIDE.md) - Guía de despliegue en producción

---

## ✨ Próximos Pasos

1. ✅ **Backend configurado y corriendo**
2. 📱 **Integrar con la app móvil** (Ver INTEGRATION_GUIDE.md)
3. 🧪 **Pruebas de integración**
4. 🚀 **Despliegue en producción** (Ver DEPLOYMENT_GUIDE.md)

---

**¿Problemas?** Revisa los logs en la consola o crea un issue en el repositorio.
