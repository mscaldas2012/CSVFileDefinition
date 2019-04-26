# build stage
FROM maven:3-jdk-8 as builder
RUN mkdir -p /usr/src/app
COPY . /usr/src/app
WORKDIR /usr/src/app
RUN mvn clean package -DskipTests=true

# create Image stage
FROM openjdk:8-jre-alpine

VOLUME /tmp

COPY --from=builder  /usr/src/app/target/mdedefinitions-0.0.1-1.jar ./MDEDefinitions.jar

RUN sh -c 'touch ./MDEDefinitions.jar'
ENTRYPOINT ["java","-Dspring.profiles.active=DOCKER","-Djava.security.egd=file:/dev/./urandom","-jar","./MDEDefinitions.jar"]