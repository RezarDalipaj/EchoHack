# ecohack-template


# Reference application: Spring Boot + React

## Pipeline Configuration

### SonarQube Points

1. The `projectkey` is defined in the quality-check job in the pipeline file (__.gitlab-ci.yml__). There is a dynamic default set, if you want to change it, make sure to use the variable `$SONARQUBE_PREFIX` as prefix.
2. The token to your SONARQUBE group is saved as a protected variable. This variable is only read on protected branches (see __Settings>Repository>Protected Branches__). If you need another token, login to https://sonarqube.lhindts.io, navigate to Your __Account>Security__ and create the token. The value has to be saved in the variable SONAR_TOKEN in the gitlab group or project.
3. As soon as you have scanned your projects, you will find the link to the results in the log of the quality-check job.

### Images

1. You will find images that you can use in this project: https://gitlab.lhindts.io/registry/images
2. You can use these images in your pipeline for your build or in Dockerfiles for your deployment

### Proxy

1. Please note that you need a Proxy to access sources outside of the DevSpace environment. In this project you will find several examples where proxies can be set. (see .gitlab-ci.yml or .ci_settings.xml).

### Kubernetes Namespace Deployments

1. Your namespace is stored in the Group Variable `APPLICATION_NAMESPACE`. This namespace was created for your group and can be reached by your pipeline (your runner).
2. For deploying something to the cluster, you need to create a secret inside the cluster with credentials from a __deploy-token__ (see: [GitLab Documentation](https://docs.gitlab.com/ee/user/project/deploy_tokens/#gitlab-deploy-token)).
3. The Hostname for your deployment is `$APPLICATION_NAMESPACE".app.hackathon.lhindts.io`. In this project it is automatically set in the applications deployment file.
4. Each group has received a kubeconfig file. This can be used to access the namespace remotely with a local client (e.g. kubectl as command line client or LENS with a GUI).

### How To ...

Your Pipeline configuration is done in the .gitlab-ci.yml file. For what you can write in the pipeline the [.gitlab-ci.yml Reference](https://docs.gitlab.com/ee/ci/yaml/) is a good place to start.
Have a look at the [Cheat Sheet](cheatsheet.md) for some tipps and examples.

## Technical requirements

### Backend

- Apache Maven: [Download](https://maven.apache.org/download.cgi), [Installation Guide](https://maven.apache.org/install.html)
- Java 17: [Download Oracle OpenJDK](https://openjdk.java.net/)

### Frontend

- Node.js, [Download](https://nodejs.org/en/)

### Deployment

- Docker, [Download](https://www.docker.com/)

## Build applications

Both applications, frontend and backend, can be built with:

`mvn clean package`

## Start application

The application with all dependent components, like the database, can be started with `docker-compose`. The prerequisite for a successful start is the prior construction of the applications. This was described in the previous step.

```
# Start the application with all components
docker-compose up

# Start the application with all components and rebuild all images
docker-compose up --build

# Stop the application with all components
docker-compose down
```

## Start frontend application only

Change to sub-module `ecohack-ui` and start the application.

```
cd ecohack-ui/
npm start
```

## Start backend application only

The backend application can be started from the root directory or the sub-module `ecohack-api` .

When starting the backend application without Docker, please note that variables in the `.properties` files (e.g. `base.properties`) may have to be adjusted.

```
# Start application from root directory
java -jar ecohack-api/target/ecohack-api-1.1.0-SNAPSHOT-application.jar

# Start application from sub-module 'app'
cd app/
java -jar target/ecohack-api-1.1.0-SNAPSHOT-application.jar
```

By default, the web interface for Swagger UI is deactivated. To activate it, it is necessary to activate the spring boot profile `swagger`.

```
# Start application from root directory
java -jar ecohack-api/target/ecohack-api-1.1.0-SNAPSHOT-application.jar --spring.profiles.active=swagger

# Start application from sub-module 'app'
cd app/
java -jar target/ecohack-api-1.1.0-SNAPSHOT-application.jar --spring.profiles.active=swagger
```

## Application links

- Frontend React UI: https://localhost:3000
- Swagger UI: https://localhost:8443/app/swagger-ui.html
