FROM maven:3.9.6-amazoncorretto-21 as builder
WORKDIR /project
COPY . ./
RUN mvn clean install

FROM amazoncorretto:21
WORKDIR /project
COPY --from=builder /project/target/ChargeFactoryApp.jar run.jar
CMD ["java", "-jar", "run.jar"]