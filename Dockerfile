####
# Este Dockerfile é usado para construir a aplicação Quarkus em modo JVM
####

# Build stage
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
WORKDIR /build

# Copia os arquivos do projeto
COPY pom.xml .
COPY src ./src

# Faz o build da aplicação
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /work

# Instala curl para health checks
RUN apk add --no-cache curl

# Copia o JAR da aplicação
COPY --from=build /build/target/quarkus-app/lib/ /work/lib/
COPY --from=build /build/target/quarkus-app/*.jar /work/
COPY --from=build /build/target/quarkus-app/app/ /work/app/
COPY --from=build /build/target/quarkus-app/quarkus/ /work/quarkus/

# Define variáveis de ambiente
ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"

EXPOSE 8080

# Comando de inicialização
CMD ["java", "-jar", "/work/quarkus-run.jar"]
