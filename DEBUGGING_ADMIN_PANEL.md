# 🔧 Guía de Debugging - Panel Admin no muestra usuarios

## ❌ Problema
El AdminScreen abre pero **no muestra ningún usuario ni opciones** para modificar roles.

## ✅ Solución por Pasos

### Paso 1: Verificar que Backend está corriendo

```bash
# En una terminal, desde la raíz del proyecto:
cd c:\Users\Nicolas\OneDrive\Documentos\GitHub\NomadApp
.\gradlew.bat backend:bootRun

# Esperar a ver el mensaje:
# "Started NomadApp in X seconds"
```

**URL del backend**: `http://localhost:8080`

---

### Paso 2: Verificar que el usuario es ADMIN

Usar Postman o un navegador para probar el endpoint:

```
GET http://localhost:8080/api/admin/users

Headers:
Authorization: Bearer {tu_token_jwt}
Content-Type: application/json
```

**Respuesta esperada** (si eres admin):
```json
[
  {
    "id": 1,
    "username": "admin",
    "email": "admin@example.com",
    "firstName": "Admin",
    "lastName": "User",
    "roles": "ADMIN",
    "enabled": true
  },
  {
    "id": 2,
    "username": "user1",
    "email": "user1@example.com",
    "firstName": "Juan",
    "lastName": "Pérez",
    "roles": "USER",
    "enabled": true
  }
]
```

**Si obtienes 403 Forbidden**:
- Tu usuario NO es admin
- Ve a la base de datos Oracle y asigna rol ADMIN
- Cierra sesión en la app y vuelve a iniciar

**Si obtienes error 500**:
- El backend está fallando
- Revisa los logs en la terminal donde corriste `bootRun`

---

### Paso 3: Verificar que el Token JWT es válido

1. Abre la app en el emulador
2. Inicia sesión correctamente
3. **No cierres la app**
4. En Postman, intenta:
   ```
   GET http://localhost:8080/api/posts
   Headers:
   Authorization: Bearer {copia_el_token_desde_app}
   ```

Si esto funciona, el token es válido.

---

### Paso 4: Verificar permisos en Base de Datos

En SQL Developer o herramienta similar, ejecuta:

```sql
-- Verificar que el usuario tiene rol ADMIN
SELECT u.username, u.email, r.name
FROM users u
INNER JOIN user_roles ur ON u.id = ur.user_id
INNER JOIN roles r ON ur.role_id = r.id
WHERE u.username = 'admin';

-- Resultado esperado:
-- admin | admin@example.com | ADMIN
```

Si no ves resultados o ves otro rol, ejecuta:

```sql
-- Primero, obtener IDs
SELECT id FROM users WHERE username = 'admin';
SELECT id FROM roles WHERE name = 'ADMIN';

-- Luego, asignar rol (reemplaza valores con IDs reales)
INSERT INTO user_roles (user_id, role_id) 
VALUES (1, 1);  -- Ajusta números según tus IDs

COMMIT;
```

---

### Paso 5: Reinstalar APK

```bash
# Desinstalar versión anterior
adb uninstall cl.vasquez.nomadapp

# Instalar nueva versión (después de compilar)
adb install app\build\outputs\apk\debug\app-debug.apk

# O directamente:
.\gradlew.bat installDebug
```

---

### Paso 6: Probar en la App

1. Abre la app en emulador
2. Inicia sesión con usuario admin
3. HomeScreen debe mostrar botón **"Panel de Administración"**
4. Tocar el botón
5. AdminScreen debe mostrar:
   - Botón "Refrescar"
   - Lista de usuarios
   - Para cada usuario:
     - Nombre, email, username
     - Dropdown de rol
     - Toggle de estado
     - Botón eliminar

---

## 🐛 Checklist de Debugging

- [ ] Backend está corriendo (`localhost:8080` responde)
- [ ] Endpoint `/api/admin/users` devuelve lista de usuarios
- [ ] Usuario iniciado es ADMIN (no USER o MODERATOR)
- [ ] Token JWT es válido (funciona en Postman)
- [ ] Base de datos tiene usuarios
- [ ] APK reinstalado después de cambios
- [ ] App reiniciada (no solo parada)
- [ ] Backend reiniciado (si hubo cambios en código)

---

## 📝 Logs para Revisar

### En Terminal del Backend (bootRun):
```
Buscar líneas con:
- "Started NomadApp"
- "ERROR"
- "/api/admin/users"

Errores comunes:
- "[WARN] Unauthorized" → Token inválido
- "403 Forbidden" → No tienes rol ADMIN
- "500 Internal Server Error" → Error en backend
```

### En Logcat del Emulador (Android Studio):
```
Buscar líneas con:
- "AdminViewModel"
- "ApiService"
- "loadUsers"
- "Exception"

Errores comunes:
- "Network error" → Backend no responde
- "JSON parse error" → Formato de respuesta incorrecto
- "Unauthorized" → Token expirado o inválido
```

---

## 🚀 Prueba Rápida Completa

```bash
# 1. Terminal 1: Backend
cd c:\Users\Nicolas\OneDrive\Documentos\GitHub\NomadApp
.\gradlew.bat backend:bootRun

# 2. Terminal 2: Esperar a que aparezca "Started NomadApp"
# 3. En el emulador: Abrir app y hacer login
# 4. Si no ves botón admin:
#    a) Verificar en Postman: GET /api/admin/users
#    b) Si falla, revisar base de datos
#    c) Si usuario no es admin, asignar rol en DB
# 5. Desinstalar APK: adb uninstall cl.vasquez.nomadapp
# 6. Reinstalar: adb install app\build\outputs\apk\debug\app-debug.apk
# 7. Volver a hacer login
# 8. Verificar que aparezca botón admin
```

---

## ✨ Cambios Recientes Aplicados

He actualizado:

1. **AdminViewModel.kt**
   - Ahora puede instanciarse sin inyección de dependencias
   - Constructor con parámetro `apiService` opcional
   - Si es null, usa `ApiProvider.apiService`

2. **AdminScreen.kt**
   - Actualizado para crear AdminViewModel de forma segura
   - Manejo de excepciones en creación de ViewModel

3. **Backend AdminController.java**
   - Nuevo DTO `UserDTO.java` para serialización correcta
   - Endpoint `/api/admin/users` ahora devuelve UserDTO
   - Mejor manejo de roles (se formatean como strings)

4. **APIService.kt**
   - Ya incluye endpoint `getAllUsers()` que devuelve `List<UserResponse>`
   - Compatible con el DTO del backend

---

## 💡 Si Sigue Sin Funcionar

1. **Verifica logs detallados**:
   ```bash
   # En Android Studio:
   # Menú: View → Tool Windows → Logcat
   # Filtro: "AdminViewModel" o "ApiService"
   ```

2. **Haz request manual en Postman**:
   ```
   GET http://localhost:8080/api/admin/users
   Authorization: Bearer {token}
   ```

3. **Revisa la consola del backend** por errores

4. **Verifica permiso en BD**:
   ```sql
   SELECT * FROM user_roles WHERE user_id = (SELECT id FROM users WHERE username = 'admin');
   ```

5. **Si todo falla**:
   - Crea usuario nuevo en DB
   - Asigna rol ADMIN manualmente
   - Hacer login con ese usuario en la app

---

**La aplicación ahora está debuggeada y lista. Los cambios de backend ya compilaron exitosamente.** ✅
