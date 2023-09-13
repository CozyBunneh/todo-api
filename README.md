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

first do this so that linux/amd64 images can be built:
```sh
podman build -f src/main/docker/Dockerfile.graalvm -t graalvm .
```

#### Creating a native executable

You can create a native executable using: 

```sh
quarkus build --native --skip-tests # file generate a 64-bit ARM image
# or
./mvnw package -Pnative -Dmaven.test.skip=true -Dquarkus.native.container-build=true -Dquarkus.native.container-runtime=podman
```
You can then execute your native executable with: `./target/todo-1.0.0-SNAPSHOT-runner`

The runner created will be 64-bit ARM aarch64.
```sh
./mvnw package -Pnative -Dmaven.test.skip=true -Dquarkus.native.container-build=true -Dquarkus.native.builder-image=graalvm
```

### Others

#### Creating a native executable

You can create a native executable using: 

```sh
quarkus build --native --skip-tests
# or
./mvnw package -Pnative -Dmaven.test.skip=true -Dquarkus.native.container-build=true -Dquarkus.native.container-runtime=podman
```
You can then execute your native executable with: `./target/todo-1.0.0-SNAPSHOT-runner`

### Create a native docker image

```sh
podman build -f src/main/docker/Dockerfile.native-micro -t todo/todo-api-native-micro .
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

