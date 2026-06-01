FROM amazoncorretto:25 AS build

ARG SRC

RUN yum install -y tar gzip openssl \
    && yum clean all

WORKDIR /tmp/certs

RUN openssl genpkey -algorithm RSA \
        -out /tmp/certs/sp-key.pem \
        -pkeyopt rsa_keygen_bits:2048 \
 && openssl req -new -x509 \
        -key  /tmp/certs/sp-key.pem \
        -out  /tmp/certs/sp-cert.pem \
        -days 3650 \
        -subj "/CN=saml-sp"

WORKDIR /tmp/build

COPY .mvn .mvn
COPY mvnw mvnw

COPY $SRC/pom.xml .
RUN ./mvnw -B dependency:go-offline

COPY $SRC/src src
RUN ./mvnw -B package -DskipTests


FROM amazoncorretto:25

COPY --from=build /tmp/certs /opt/certs
COPY --from=build /tmp/build/target/*.jar /opt/app.jar

ENV SPRING_SECURITY_SAML2_RELYINGPARTY_REGISTRATION_KEYCLOAK_SIGNING_CREDENTIALS_0_PRIVATEKEYLOCATION="file:/opt/certs/sp-key.pem"
ENV SPRING_SECURITY_SAML2_RELYINGPARTY_REGISTRATION_KEYCLOAK_SIGNING_CREDENTIALS_0_CERTIFICATELOCATION="file:/opt/certs/sp-cert.pem"

EXPOSE 9009

ENTRYPOINT ["java", "-jar", "/opt/app.jar"]
