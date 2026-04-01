FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM tomee:10-jre17-webprofile

WORKDIR /usr/local/tomee

RUN rm -rf /usr/local/tomee/webapps/*

COPY --from=build /app/target/SMIS.war /usr/local/tomee/webapps/SMIS.war
COPY tomee.xml /usr/local/tomee/conf/tomee.xml
COPY --from=build /root/.m2/repository/com/mysql/mysql-connector-j/8.4.0/mysql-connector-j-8.4.0.jar /usr/local/tomee/lib/

EXPOSE 8080

CMD ["catalina.sh", "run"]