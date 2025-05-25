#Stage 1: build com Maven

FROM maven:3.9.9-amazoncorretto-21 AS build
WORKDIR /app

#Copia o pom e baixa dependências (caching otimizado)

COPY pom.xml .
RUN mvn dependency:go-offline -B

#Copia o código-fonte e empacota

COPY src ./src
RUN mvn clean package -DskipTests -B

#Stage 2: runtime com JRE enxuto

FROM openjdk:21-jdk-slim
WORKDIR /app

#Copia o JAR gerado no stage de build

COPY --from=build /app/target/*.jar app.jar

#Expõe a porta padrão do Spring Boot

EXPOSE 8080

#Define entrypoint para rodar o JAR

ENTRYPOINT ["sh", "-c", "java -jar /app/app.jar"]
