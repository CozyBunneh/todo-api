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

### Creating a native executable

You can create a native executable using: 

```sh
quarkus build --native --skip-tests
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
quarkus build --native -Dquarkus.native.container-build=true
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

