# Build a partir da pasta "projetos/" (o back-end agora depende do modulo omce-reuso):
#   cd projetos
#   docker build -f back-end/Dockerfile -t omce-back-end .
FROM maven:3.9.9-eclipse-temurin-23 AS build

WORKDIR /app
COPY pom.xml .
COPY omce-reuso ./omce-reuso
COPY back-end ./back-end
# -pl back-end -am: compila o back-end e, antes, os modulos de que ele depende (omce-reuso)
RUN mvn -q -DskipTests -pl back-end -am package

FROM eclipse-temurin:23-jre

WORKDIR /app
COPY --from=build /app/back-end/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
