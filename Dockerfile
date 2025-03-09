FROM eclipse-temurin

RUN apt-get update
RUN apt-get install -y maven

COPY . /job-platform

WORKDIR /job-platform

RUN chmod 777 ./mvnw

#RUN ./mvnw dependency:go-offline

ENV WAIT_VERSION 2.7.2
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/$WAIT_VERSION/wait /wait
RUN chmod +x /wait

CMD ["/wait", "mvn", "spring-boot:run"]