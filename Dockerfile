FROM openjdk:23-slim as package

# defaul version number
ARG VERSION_NUM=1.0.0

WORKDIR /hello-world-nick

COPY . .

# Build
RUN ./gradlew  build  -Pversion=$VERSION_NUM

#run fat-jar
FROM openjdk:23-slim as run-app

ARG VERSION_NUM=1.0.0
ENV VERSION_NUM=$VERSION_NUM

WORKDIR /hello-world-nick

COPY --from=package /hello-world-nick/build/libs .

RUN adduser non-root
# set non-root user
USER non-root

CMD java -jar hello-world-nick-$VERSION_NUM-all.jar

