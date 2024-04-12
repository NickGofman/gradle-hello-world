# CI/CD workflow with Gradle Wrapper and the Kotlin DSL


Build & Run Java Programs using Github actions, MultiStage Dockerfile, and Gradle Wrapper.


## Program Source code
This is the source code of the program:
```bash
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello World!\nNick Gofman");
    }
}
```


## GitHub Actions CI/CD Stages (gradle.yml)
- Activate Pipeline on Push event on mater Branch.

```bash
on:
  push:
    branches: [ "master" ]
```
- Versioning, use the build number as the PATCH number.

```bash
env:
  buid-number: ${{ github.run_number }}

```


###  1. Job - Build


- Build a docker image and tag it as a new version.
- Test the new Image.
- Add the latest Tag.
- Login to DockerHub Registery (configure your secrets in GitHub).
- Push both versions to DockerHub Registery.

###  2. Job - run-app
- Run Image from DockerHub.



## Multi-Stage Docker

### Build and Tag Image
```bash
docker build -t nickgofman/gradle:helloWorld-1.0.${{env.buid-number}} --build-arg="VERSION_NUM=1.0.${{env.buid-number}} .

```
### Inside Dockerfile
### Arguments & Environment variables
  
  ```yml
  ARG VERSION_NUM=1.0.0 - If no argument passes set the version to 1.0.0 .
  ENV VERSION_NUM=$VERSION_NUM - Set environment variable for execution.
  ```

#### Stage 1: Build 

>>> Copy source code.
```yml
COPY . .

```

>>> Compiles Hello World and creates JAR files with new version numbers. 
```yml
RUN ./gradlew build -Pversion=$VERSION_NUM

```

#### Stage 2:
>>> Ensures the final image includes only the jar files.
> 
```yml
COPY --from=package /hello-world-nick/build/libs.

```
>>> Execute the Program when running the image.
>
```yml
CMD java -jar hello-world-nick-$VERSION_NUM-all.jar

```


## Plugin Used
  #### Github Action:
   -  ***actions/checkout@v4*** - Allow you to take actions on your source code.
   -  ***docker/login-action@v3*** - Allows you to log in against a Docker registry.
   
  #### DockerFile:
   -  ***openjdk:23-slim*** - Docker Image for running Java applications.
   

## Run Program Locally

```bash
   docker run nickgofman/gradle:latest
```

## Links

- [DockerHub Project Registry](https://hub.docker.com/repository/docker/nickgofman/gradle/general)


