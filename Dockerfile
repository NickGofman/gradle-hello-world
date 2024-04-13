FROM openjdk:23-slim as package

WORKDIR /hello-world-nick

COPY . .

# Build
RUN ./gradlew  build 

#run fat-jar
FROM openjdk:23-slim as run-app

ARG VERSION_NUM
ENV VERSION_NUM=$VERSION_NUM

WORKDIR /hello-world-nick

COPY --from=package /hello-world-nick/build/libs .

RUN adduser non-root
# set non-root user
USER non-root

CMD java -jar hello-world-nick-$VERSION_NUM-all.jar

