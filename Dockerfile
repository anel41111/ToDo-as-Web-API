FROM maven:3-adoptopenjdk-11

WORKDIR /usr/src/app

COPY . /usr/src/app
RUN mvn dependency:resolve-plugins && \
    mvn dependency:resolve

ENV PORT 8080
EXPOSE $PORT
CMD [ "sh", "-c", "mvn -Dserver.port=${PORT} spring-boot:run" ]
