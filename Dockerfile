# Etapa 1: Construcción (Build)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
# Copiamos el pom.xml y descargamos dependencias (esto ahorra tiempo en futuras subidas)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos el código fuente y generamos el .jar
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución (Runtime)
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
# Copiamos solo el archivo .jar desde la etapa de construcción
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto (Render suele usar el 8080 por defecto)
EXPOSE 8085

# Comando para ejecutar la aplicación
# Agregamos parámetros para optimizar la RAM en planes gratuitos
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-Xmx400m", "-jar", "app.jar"]