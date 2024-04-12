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
- Activate Pipeline on Push event on different branches.

```bash
on:
  push:
    branches: [ master, development, features ]

```

###  1. Job - Build
- Increment version.

- Get new version from **build.gradle.kts**.
- Build a docker image and tag it as a new version.
- Test the new Image.
- Add the latest Tag.
- Push the new **build.gradle.kts** to the repo (new version). 
- Login to DockerHub Registery (configure your secrets in GitHub).
- Push both versions to DockerHub Registery.

###  2. Job - run-app
- Run Image from DockerHub.



## Multi-Stage Docker

### Build and Tag Image
```bash
docker build -t nickgofman/gradle:helloWorld-${{env.NEW_VERSION}} --build-arg="VERSION_NUM=${{env.NEW_VERSION}}" .

```
### Inside Dockerfile
### Arguments & envariment variables
  

  `ARG VERSION_NUM`

  `ENV VERSION_NUM=$VERSION_NUM` - Set environment variable for execution.

#### Stage 1: Build 

>>> Copy source code.
```yml
COPY . .

```

>>> Compiles Hello World and creates JAR files.
```yml
RUN ./gradlew build

```

#### Stage 2:
>>> Ensures the final image includes only the jar files.
> 
```yml
COPY --from=package /hello-world-nick/build/libs .

```
>>> Execute the Program when running the image.
>
```yml
CMD java -jar hello-world-nick-$VERSION_NUM-all.jar

```

## Versioning  Logic
- Set VERSION_TYPE (major, minor, patch) according to the branch

```bash
- name: Set environment for branch
   run: |
      if [[ $GITHUB_REF_NAME == 'master' ]]; then
         echo "VERSION_TYPE=major" >> "$GITHUB_ENV"
      elif [[ $GITHUB_REF_NAME == 'development' ]]; then
         echo "VERSION_TYPE=minor" >> "$GITHUB_ENV"
      else
         echo "VERSION_TYPE=patch" >> "$GITHUB_ENV"
      fi
```

- Increment version, this function changes the version number in the **build.gradle.kts** file, according to the type. 
```bash
- name: Build with Gradle
   run: ./gradlew incrementVersion -Pmode=${{env.VERSION_TYPE}}
```

 - Usage:
```bash
./gradlew incrementVersion [-P[mode=major|minor|patch]|[overrideVersion=x]]
```


 - Examples:
```bash
./gradlew incrementVersion -Pmode=patch

./gradlew incrementVersion -Pmode=minor

./gradlew incrementVersion -Pmode=major

```

#### Reference to incrementVersion function


***[Stackoverflow - Mahozad
](https://stackoverflow.com/questions/39824574/version-increment-using-gradle-task)***

- Save new version
   - After a succesfull test of the new image we commit and push the new **build.gradle.kts** file to the repo.
   ```bash
   - name: Commit changes
      uses: EndBug/add-and-commit@v9
      with:
         add: build.gradle.kts
         message: "increment the patch number after successful build, Build number:  ${{ github.run_number }}"
         pathspec_error_handling: exitImmediately #If add fail, the action will stop right away, and the step will fail

   ```
   ```bash
      val versionToChange = "1.0.2" ==> "1.0.3"

   ```


## Plugin Used
  #### Github Action:
   -  ***actions/checkout@v4*** - Allow you to take actions on your source code.
   -  ***[docker/login-action@v3](https://github.com/docker/login-action)*** - Allows you to log in against a Docker registry.
   -  ***[gradle/gradle-build-action@v3](https://github.com/gradle/gradle-build-action)*** - Allow to configure Gradle and optionally execute a Gradle build on any platform supported by GitHub Actions.
   -  ***[EndBug/add-and-commit@v9](https://github.com/EndBug/add-and-commit)*** - Allows to commit changes made in your workflow run directly to your repo.
   
  #### DockerFile:
   -  ***openjdk:23-slim*** - Docker Image for running Java applications.
   

## Run Program Locally

```bash
   docker run nickgofman/gradle:latest
```

## Links

- [DockerHub Project Registry](https://hub.docker.com/repository/docker/nickgofman/gradle/general)

