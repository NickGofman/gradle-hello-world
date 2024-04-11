# CI/CD workflow with Gradle Wrapper and the Kotlin DSL


Build & Run Java Programs using Github actions, MultiStage Dockerfile, and Gradle.


## Program Source code
This is the source code of the program:
```bash
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello World!\n Nick Gofman");
    }
}

```


## Multipule DockerFile

- Stage 1: Build Jar File and Fat Jar with the new version number using  Gradle Wrapper.
```bash

RUN ./gradlew  build -Pversion=$VERSION_NUM


```


- Stage 2: Run Program.

```bash
Java -jar fileName-$VERSION_NUM-all.jar

```


## GitHub Actions Stages
Two jobs, one for building and the second for running the image 
- Activate Pipeline on Push event on mater Branch

```bash
on:
  push:
    branches: [ "master" ]
```
###  1. Job - build


- Build a docker image and tag it as a new version
- Tag the docker image to the latest 
- Login to DockerHub Registery (configure your secrets in GitHub)
- Push new version to DockerHub
- Push the latest to DockeHub 



###  2. Job - run-app
- Run Program

## Plugin Used
  #### Github Action:
   -  ***actions/checkout@v4*** - Allow you to make actions on your source code
   -  ***docker/login-action@v3*** - Allows you to log in against a Docker registry.
   
  #### Docker MultiStage:
   -  ***openjdk:23-slim*** - Docker Image for running Java applications
   

## docker hub link

- [DockerHub Project Repo](https://hub.docker.com/repository/docker/nickgofman/gradle/general)


## Run Program Locally

```bash
   docker run nickgofman/gradle:latest
```
