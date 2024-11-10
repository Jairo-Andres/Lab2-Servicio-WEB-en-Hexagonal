# PersonApp - Hexagonal Architecture with Spring Boot
Este proyecto es una aplicación de ejemplo que sigue una arquitectura hexagonal utilizando Spring Boot y Docker, con soporte para bases de datos MongoDB y MariaDB.

## Tabla de Contenidos
* Requisitos Previos
* Configuración del Ambiente
* Compilación y Despliegue
* Uso
* Configuración Adicional

## Requisitos Previos
* Docker y Docker Compose deben estar instalados en tu sistema.
* Java 11 o superior.
* Maven para gestionar las dependencias y compilar el proyecto.
* Git para clonar el repositorio (opcional si ya tienes el proyecto en tu máquina local).

## Configuración del Ambiente
### Clonar el Repositorio:
Primero, clona el repositorio de GitHub
```bash
git clone https://github.com/Jairo-Andres/personapp-hexa-spring-boot.git
cd personapp-hexa-spring-boot
```
### Configuración de Bases de Datos
Este proyecto usa MongoDB y MariaDB. La configuración está en el archivo application.properties. Docker Compose se encargará de levantar ambas bases de datos en contenedores separados.
### Configuración de Docker Compose
El proyecto incluye un archivo docker-compose.yml que configura y levanta los servicios de la aplicación y de las bases de datos.

### Configuración del Archivo application.properties
Asegúrate de que los valores en src/main/resources/application.properties coincidan con los siguientes:
```bash
# Configuración de Logging
logging.level.root=INFO
logging.file.name=logs/persona.log

# Configuración de MariaDB
spring.datasource.url=jdbc:mariadb://mariadb:3306/persona_db
spring.datasource.username=persona_db
spring.datasource.password=persona_db
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.open-in-view=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MariaDBDialect

# Configuración de MongoDB
spring.data.mongodb.authentication-database=admin
spring.data.mongodb.username=persona_db
spring.data.mongodb.password=persona_db
spring.data.mongodb.database=persona_db
spring.data.mongodb.port=27017
spring.data.mongodb.host=mongodb
```

## Compilación y Despliegue
### Paso 1: Compilar el Proyecto:
Primero, compila el proyecto usando Maven:
```bash
mvn clean install
```
### Paso 2: Levantar el Entorno con Docker Compose:
Levanta los contenedores necesarios para la aplicación y las bases de datos:
```bash
docker-compose up -d
```
Esto iniciará los contenedores en segundo plano. La primera vez puede tomar algunos minutos mientras Docker descarga las imágenes.
### Paso 3: Ejecutar el CLI de la Aplicación:
Para interactuar con la aplicación mediante la interfaz de línea de comandos (CLI), ejecut
```bash
docker-compose run -it cli
```
Este comando abrirá una sesión de CLI interactiva para el módulo cli-input-adapter.

## Uso
### 1. Ejecutar la aplicación:
La aplicación estará disponible en localhost en el puerto configurado en application.properties.
El puerto predeterminado es el 3000 para el servicio REST.
### 2. Interacción con la aplicación:
Puedes acceder a los endpoints de la aplicación a través de un cliente REST o directamente desde Swagger en http://localhost:3000/swagger-ui/index.html.

## Configuración Adicional
### Logs
Los logs de la aplicación se guardan en el archivo logs/persona.log. Puedes revisar los logs para depuración o monitoreo del sistema.

### Apagar los Contenedores
Para detener y eliminar los contenedores cuando hayas terminado, usa:
```bash
docker-compose down
```

## Notas
* Si deseas cambiar el puerto o cualquier configuración adicional, puedes modificar el archivo docker-compose.yml o application.properties.
* En caso de errores de conexión a las bases de datos, revisa los permisos y la autenticación de MongoDB y MariaDB.
