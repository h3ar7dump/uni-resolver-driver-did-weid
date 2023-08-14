# Dockerfile for universalresolver/driver-did-sov

FROM gradle:8.2.1-jdk17 AS build
MAINTAINER h3ar7dump <awenluo@outlook.com>

# build driver-did-sov

WORKDIR /opt/driver-did-weid
ADD . /opt/driver-did-weid
RUN gradle war

# build image

FROM jetty:11.0.15-jre17-eclipse-temurin
MAINTAINER h3ar7dump <awenluo@outlook.com>

# install dependencies

USER root

USER jetty

# copy from build stage

COPY --from=build --chown=jetty /opt/driver-did-weid/build/libs/*.war /var/lib/jetty/webapps/ROOT.war
# COPY --from=build --chown=jetty /opt/driver-did-weid/sovrin/ /var/lib/jetty/sovrin/

# done

EXPOSE 8080
CMD java -Djetty.http.port=8080 -jar /usr/local/jetty/start.jar