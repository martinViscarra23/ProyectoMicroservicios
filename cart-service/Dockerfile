FROM eclipse-temurin:21-jdk-jammy
ARG JAR_FILE=target/cart-service-0.0.1.jar
COPY ${JAR_FILE} app_cart.jar
ENTRYPOINT ["java", "-jar", "app_cart.jar"]