# todo

This project uses Quarkus, the Supersonic Subatomic Java Framework.

The project is setup with a simple vertically sliced architecture and at the moment there's no real domain since it only offers a CRUD api.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
quarkus dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Native

### MacOs M1

WARNING, there's an issue with M1 Mac's and Podman for native builds atm: https://github.com/quarkusio/quarkus/issues/34666

Optional step, you can use prepared build images as well but this guide assumes you do this:
```sh
podman build -f src/main/docker/Dockerfile.graalvm -t graalvm .
# or
docker build -f src/main/docker/Dockerfile.graalvm -t graalvm .
```

#### Creating a native executable

##### Podman

You can create a native executable using: 

```sh
./mvnw package -Pnative -Dmaven.test.skip=true -Dquarkus.native.container-build=true -Dquarkus.native.container-runtime=podman -Dquarkus.native.builder-iamge=graalvm
```
You can then execute your native executable with: `./target/todo-1.0.0-SNAPSHOT-runner` if you are on an environemnt where that works.

##### Docker

You can create a native executable using: 

```sh
./mvnw package -Pnative -Dmaven.test.skip=true -Dquarkus.native.container-build=true -Dquarkus.native.container-runtime=docker -Dquarkus.native.builder-iamge=graalvm
```
You can then execute your native executable with: `./target/todo-1.0.0-SNAPSHOT-runner` if you are on an environemnt where that works.

#### Create the native docker image

Native:
```sh
podman build -f src/main/docker/Dockerfile.native -t todo/todo-api-native .
# or
docker build -f src/main/docker/Dockerfile.native -t todo/todo-api-native .
```

Native Micro:
```sh
podman build -f src/main/docker/Dockerfile.native-micro -t todo/todo-api-native-micro .
# or
docker build -f src/main/docker/Dockerfile.native-micro -t todo/todo-api-native-micro .
```

## JVM

### build

```sh
quarkus build --clean --no-tests
mvn package -Dmaven.test.skip=true
```

### Create a JVM docker image

```sh
podman build -f src/main/docker/Dockerfile.jvm -t todo/todo-api-jvm .
```

## Podman

### Install

#### MacOS

```sh
brew install podman-desktop
```

### Setup Podman Machine


```sh
podman machine init # if you need to init the machine
podman machine start # to start
podman machine info # for info
```

If you need to recreate the machine:

```sh
podman machine rm # to remove machine
podman machine init # to init the machine
```

### Setup Postgres

```sh
podman run -d --name postgres -e POSTGRES_PASSWORD=test -e POSTGRES_USER=test -p 5432:5432 postgres:11-alpine
```

get the ip so you can set it in the prod conf in application.yml:
```sh
podman container inspect -f '{{.NetworkSettings.IPAddress}}' postgres
```

## Docker Desktop

### Install

Go here, download and install: https://www.docker.com/products/docker-desktop/

### Setup Postgres

```sh
podman run -d --name postgres -e POSTGRES_PASSWORD=test -e POSTGRES_USER=test -p 5432:5432 postgres:11-alpine
```

get the ip so you can set it in the prod conf in application.yml:
```sh
podman container inspect -f '{{.NetworkSettings.IPAddress}}' postgres
```

## Run Integration Tests

```sh
./mvnw integration-test -Pnative
```

## Troubleshooting

### MacOS

#### fish

```sh
brew install graalvm/tap/graalvm-ce-java17
set -Ux GRAALVM_HOME /Library/Java/JavaVirtualMachines/graalvm-ce-java17-xx.x.x/Contents/Home/
set -U fish_user_paths $GRAALVM_HOME/bin/
gu install native-image
quarkus build --native
```

