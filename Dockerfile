# build stage
FROM maven:3-jdk-8 as builder
RUN mkdir -p /usr/src/app
COPY . /usr/src/app
WORKDIR /usr/src/app
RUN mvn clean package -DskipTests=true

# create Image stage
FROM openjdk:8-jre-alpine

VOLUME /tmp

COPY --from=builder  /usr/src/app/target/CSVFileDefinitions-1.0.0.jar ./fileDefinitions.jar

RUN sh -c 'touch ./fileDefinitions.jar'
ENTRYPOINT ["java","-Dspring.profiles.active=DOCKER","-Djava.security.egd=file:/dev/./urandom","-jar","./fileDefinitions.jar"]