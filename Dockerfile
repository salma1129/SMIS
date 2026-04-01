FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM tomee:10-jre17-webprofile

WORKDIR /usr/local/tomee

# Remove default TomEE apps
RUN rm -rf /usr/local/tomee/webapps/*

# Deploy app as ROOT
COPY --from=build /app/target/SMIS.war /usr/local/tomee/webapps/ROOT.war

# TomEE datasource config
COPY tomee.xml /usr/local/tomee/conf/tomee.xml

# MySQL JDBC driver
COPY --from=build /root/.m2/repository/com/mysql/mysql-connector-j/8.4.0/mysql-connector-j-8.4.0.jar /usr/local/tomee/lib/

EXPOSE 8080

CMD ["catalina.sh", "run"]