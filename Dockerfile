FROM openjdk:23-slim

ARG VERSION_NUM=1.0.0
ENV VERSION_NUM=$VERSION_NUM

WORKDIR /hello-world-nick

COPY  /build/libs/ .

RUN adduser non-root
# set non-root user
USER non-root


CMD java -jar  gradle-hello-world-$VERSION_NUM-all.jar

