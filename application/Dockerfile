FROM openjdk:11-jre-slim

RUN \
      addgroup --system pgsockets4s \
      && adduser --system --disabled-login --disabled-password --home /var/lib/pgsockets4s --shell /sbin/nologin --ingroup pgsockets4s pgsockets4s

COPY ./target/scala-2.12/application-assembly.jar /var/lib/pgsockets4s/

USER pgsockets4s
WORKDIR /var/lib/pgsockets4s

ENTRYPOINT ["java", "-jar", "application-assembly.jar"]