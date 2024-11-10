# Usa Maven con OpenJDK 11
FROM maven:3.8.1-openjdk-11-slim AS build

# Establece el directorio de trabajo en /app
WORKDIR /app

# Copia el archivo de configuración de Maven y el archivo pom.xml del proyecto raíz
COPY ./pom.xml .

# Copia la estructura completa del proyecto al contenedor
COPY . .

# Descarga dependencias y construye todo el proyecto
RUN mvn clean install -DskipTests

# Cambia al módulo `rest-input-adapter` y ejecuta la aplicación
WORKDIR /app/rest-input-adapter

CMD ["mvn", "spring-boot:run"]
