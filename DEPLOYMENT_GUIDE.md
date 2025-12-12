# Guía de Despliegue - NomadApp Backend

## Despliegue Local

### Prerequisitos

1. Java 17+ instalado
2. Oracle Database (local o cloud)
3. Wallet de Oracle descargado
4. Gradle (incluido con el proyecto)

### Pasos

1. **Configurar variables de entorno**

```bash
# Linux/Mac
export ORACLE_HOST="localhost"
export ORACLE_PORT="1521"
export ORACLE_SERVICE_NAME="XE"
export ORACLE_USER="nomadapp"
export ORACLE_PASSWORD="password123"
export ORACLE_WALLET_PATH="file:/home/usuario/.oracle/wallets"
export JWT_SECRET="MiClaveJWT_QueDebeTener_MenosTreeintayDosCaracteres_AqW9xZcVbNmPoLkJhGfD"
```

2. **Compilar el proyecto**

```bash
cd backend
./gradlew clean build
```

3. **Ejecutar la aplicación**

```bash
./gradlew bootRun
```

La aplicación estará disponible en: `http://localhost:8080/api`

## Despliegue con Docker

### Configuración

1. **Copiar y actualizar variables de entorno**

```bash
cp .env.example .env
# Editar .env con tus valores reales
```

2. **Construir imagen Docker**

```bash
docker build -f backend/Dockerfile -t nomadapp-backend:latest .
```

3. **Ejecutar con Docker Compose**

```bash
docker-compose up -d
```

### Verificar el servicio

```bash
# Ver logs
docker-compose logs -f nomadapp-backend

# Verificar estado
docker-compose ps

# Detener servicio
docker-compose down
```

## Despliegue en Oracle Cloud Infrastructure (OCI)

### 1. Usar OCI Container Registry

```bash
# Loguear en OCI Registry
docker login <region>.ocir.io

# Tagear imagen
docker tag nomadapp-backend:latest <region>.ocir.io/<namespace>/nomadapp-backend:latest

# Push a registry
docker push <region>.ocir.io/<namespace>/nomadapp-backend:latest
```

### 2. Crear instancia en Compute

```bash
# Desde OCI Console:
1. Compute → Instances → Create Instance
2. Seleccionar imagen Ubuntu 22.04
3. Configurar redes y security groups
4. Crear instance SSH keys
```

### 3. Instalar en la instancia

```bash
# SSH a la instancia
ssh ubuntu@<instance_ip>

# Instalar Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# Instalar Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# Copiar archivos del proyecto
scp -r docker-compose.yml ubuntu@<instance_ip>:/home/ubuntu/
scp -r .env ubuntu@<instance_ip>:/home/ubuntu/

# Ejecutar
ssh ubuntu@<instance_ip>
cd /home/ubuntu
docker-compose up -d
```

## Despliegue en Heroku (Alternativa gratuita)

### 1. Configurar Heroku

```bash
# Instalar Heroku CLI
# https://devcenter.heroku.com/articles/heroku-cli

# Loguear
heroku login

# Crear app
heroku create nomadapp-backend

# Agregar variables de entorno
heroku config:set ORACLE_HOST=... -a nomadapp-backend
heroku config:set ORACLE_USER=... -a nomadapp-backend
# etc...
```

### 2. Crear Procfile

```bash
web: java -jar backend/build/libs/nomadapp-backend-1.0.0.jar
```

### 3. Deploy

```bash
git add .
git commit -m "Deploy backend"
git push heroku main
```

## Monitoreo en Producción

### Health Check

```bash
curl http://localhost:8080/api/posts
```

### Ver logs

```bash
# Local
tail -f backend/logs/application.log

# Docker
docker-compose logs -f nomadapp-backend

# Heroku
heroku logs --tail -a nomadapp-backend
```

### Métricas importantes

- **Tiempo de respuesta API**: < 500ms
- **Uptime**: > 99.9%
- **Errores HTTP 5xx**: < 0.1%
- **Conexiones activas**: Monitorear pool de conexiones Oracle

## Base de datos en Producción

### Backup

```bash
# Exportar datos de Oracle
expdp nomadapp/password123 DIRECTORY=backup_dir DUMPFILE=nomadapp.dmp

# Usar Oracle Cloud Backup
# https://www.oracle.com/database/technologies/backup-options.html
```

### Mantenimiento

```sql
-- Actualizar estadísticas
ANALYZE TABLE users COMPUTE STATISTICS;
ANALYZE TABLE posts COMPUTE STATISTICS;
ANALYZE TABLE contacts COMPUTE STATISTICS;

-- Ver tamaño de tablas
SELECT table_name, ROUND(bytes/1024/1024) mb 
FROM user_tables 
ORDER BY bytes DESC;
```

## Escalado Horizontal

### Usar Load Balancer

Para múltiples instancias del backend:

1. Levantar múltiples contenedores
2. Configurar nginx como load balancer

```nginx
upstream backend {
    server backend1:8080;
    server backend2:8080;
    server backend3:8080;
}

server {
    listen 80;
    location /api {
        proxy_pass http://backend;
    }
}
```

## Seguridad en Producción

### 1. Certificados SSL/TLS

```bash
# Usar Let's Encrypt con Certbot
sudo certbot certonly --standalone -d api.tudominio.com

# Configurar en nginx o load balancer
```

### 2. Firewall

```bash
# Abrir solo puertos necesarios
sudo ufw allow 443/tcp  # HTTPS
sudo ufw allow 80/tcp   # HTTP (redirigir a HTTPS)
sudo ufw allow 22/tcp   # SSH
```

### 3. Secrets Management

Usar Oracle Vault o AWS Secrets Manager:

```bash
# En lugar de .env, obtener secrets de vault
heroku config:set ORACLE_PASSWORD=<valor_desde_vault>
```

## Actualizar aplicación

### CI/CD con GitHub Actions

Crear `.github/workflows/deploy.yml`:

```yaml
name: Deploy

on:
  push:
    branches: [ main ]

jobs:
  build-and-deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK 17
        uses: actions/setup-java@v2
        with:
          java-version: '17'
      - name: Build with Gradle
        run: cd backend && ./gradlew build
      - name: Deploy to Heroku
        run: git push heroku main
        env:
          HEROKU_API_KEY: ${{ secrets.HEROKU_API_KEY }}
```

## Rollback en caso de problemas

```bash
# Docker
docker-compose down
docker-compose up -d  # Volverá a la última versión funcionando

# Heroku
heroku releases
heroku rollback v123

# Manual (mantener versiones anteriores)
git revert <commit_hash>
git push
```

## Checklist de despliegue

- [ ] Variables de entorno configuradas correctamente
- [ ] Base de datos Oracle accesible y con datos iniciales
- [ ] Wallet de Oracle correctamente instalado
- [ ] SSL/TLS configurado
- [ ] Firewall permite conexiones necesarias
- [ ] Backup de base de datos configurado
- [ ] Monitoreo y alertas configurados
- [ ] Plan de rollback documentado
- [ ] Documentación actualizada
- [ ] Tests pasando en staging
- [ ] Performance acceptable
- [ ] Seguridad auditada

## Soporte

Para reportar problemas de despliegue, crear un issue con:
- Descripción del problema
- Logs de error (sin información sensible)
- Pasos para reproducir
- Entorno (local/cloud, OS, versiones)
