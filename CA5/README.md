# CA5 - Containers
This class assignment will teach you how to work with containerization tools. As a container orchestration tool we will 
be using **Docker**.

Docker is

### How to install Docker

## Part I
The goal for the first part of the class assignment is to create separate images and containers for each CA2 application 
(the Simple Chat Application and the Spring Boot Rest Application).

You'll have two provide two versions for each one of the implementations, being the **first version** the one where 
you're going to build the server inside the `Dockerfile` - clone your repository and compile the application within the 
container, and the **second version** the one where you're going to build the server on the host machine and copy the 
resulting JAR file into the Docker image

### Simple Chat Application
As previously mentioned, you'll be developing two versions of the Simple Chat Application for this part of the class 
assignment. Find below a reference for each one of the versions with the respective description of the used dockerfile 
and respective executed commands in the powershell console.

#### Version 1
In your `Dockerfile`, start by defining the **parser directive**. The **parser directive** specifies which `Dockerfile` 
syntax version to use, and is primarily required when building images with `BuildKit`.

Next, the `FROM` instruction to select the base image. In this, we are using `Ubuntu 22.04`.

Then, utilizing the `RUN` instruction, update the existing packages and install the packages you need - in this case it 
was `git`, `openjdk-17-jdk`, and `openssh-client`. These actions can be concatenated into a single command to reduce the 
number of layers in the final Docker image and to ensure that the package installation occurs only after updating the 
system is updated.

Afterward, still with `RUN`, execute the following commands:
```bash
mkdir -p ~/.ssh
ssh-keyscan github.com >> ~/.ssh/known_hosts
```

- The first command **creates the `.ssh` directory in the container's `home` directory**. The `-p` flag ensures the 
command doesn't fail if the directory already exists. This directory store SSH configuration, private keys, and known 
hosts.
- The second command **fetches the GitHub's public SSH host key and appends it to the `known_hosts` file**.

These commands were concatenated to create only one layer and to ensure the `ssh-keyscan` command runs only if the 
`~/.ssh` directory exists.

Then, still using `RUN`, clone your repository. For **private repositories**, use the `--mount=type=ssh` flag. **This 
temporarily mounts the SSH agent or the private key only for this build step, allowing the container to authenticate 
with GitHub without embedding your private key into the final image**.

The `WORKDIR /COGSI2526_1250503_1250506_1250545/CA2/PART-I/gradle_basic_demo` instruction set the working directory 
for any `RUN`, `CMD`, `ENTRYPOINT`, `COPY`, and `ADD` instructions that follow it within the `Dockerfile`.

The `EXPOSE` instruction doesn't open any port by itself; it is primarily used to documenting which ports the container  
is expected to expose.

Next, `RUN chmod +x gradlew` is utilized to add executable permissions to the `gradlew` file, allowing the root user to 
run this script. 

Finally, the `CMD` instruction defines the default program that is run once you start the container based on this image. 
Each `Dockerfile` only has one `CMD` instance.

For the first version, your `Dockerfile` should follow the structure outlined below.

```dockerfile
# syntax=docker/dockerfile:1
FROM ubuntu:22.04

RUN apt-get update && apt-get install -y git openjdk-17-jdk openssh-client
RUN mkdir -p ~/.ssh && ssh-keyscan github.com >> ~/.ssh/known_hosts
RUN --mount=type=ssh git clone git@github.com:pchu22/COGSI2526_1250503_1250506_1250545.git /COGSI2526_1250503_1250506_1250545

WORKDIR /COGSI2526_1250503_1250506_1250545/CA2/PART-I/gradle_basic_demo

RUN chmod +x gradlew

EXPOSE 59001

CMD ["./gradlew", "runServer"]
```

After having a robust and well-structured `Dockerfile`, run the following command to build your image: 

```bash
docker buildx build --ssh default --load -t gradle_basic_demo:1.0 .
```

This command uses `docker buildx` to build a Docker image.

- The `docker buildx` extends the functionality of the `docker build` command utilizing **BuildKit**. BuildKit offers 
improved performance, better caching, and the ability to build multi-architecture images. The `build` subcommand 
instructs `buildx` to perform a build operation.
- The `--ssh default` 
- The `--load` tells BuildKit to load the built image into the local Docker image cache. Without this flag, image 
will not appear in your local docker images list
- The `-t gradle_basic_demo:1.0` assigns a tag (name:version) to the final image.
- The `.` in the final part of the command indicates the current working directory. This directory is recursively sent 
to the Docker daemon/BuildKit builder.

Once the image is built, create and start a container utilizing  the following command:

```bash
docker run --name simple-chat-application -p 59001:59001 gradle_basic_demo:1.0
```

- The flag `--name` assigns a name to the newly created container.
- The flag `-p 59001:59001` set up port forwarding. Its format is The format is `[HOST_IP:]HOST_PORT:CONTAINER_PORT` 
(if HOST_IP isn't defined, it forwards the indicated por in every interface).

After your container is running, start one (or more) client(s) by executing the `runClient` task. Your output should 
resemble the example below:

<img src="PART-I/Images/01_01.PNG" alt="simple-chat-application v1.0 container running"/>

##### Container layers

By running the command `docker history <CONTAINER_NAME/CONTAINER_ID>` you can display the layer-by-layer history of an 
image, showing details such as the **creation time**, **size**, **command used to create each layer**, and **any 
associated comments**. 

This command helps to understand how an image is constructed, which is useful for **troubleshooting**, **auditing**, and 
**optimizing image builds**. 

Each layer corresponds to a step in the Dockerfile, and the output reveals the sequence of 
commands that built the image.

The following image is the expected output you should get after running the previously mentioned command.

<img src="PART-I/Images/01_02.PNG" alt="simple-chat-application v1.0 container layers"/>

##### Container resource usage
The `docker stats <CONTAINER_NAME/CONTAINER_ID>` command provides real-time resource usage statistics for running 
containers, including **CPU percentage**, **memory usage relative to the limit**, **network input/output**, **block 
device input/output**, and the **number of processes or threads (PIDs) created by each container**.

The following image is the expected output you should get after running the previously mentioned command.

<img src="PART-I/Images/01_03.PNG" alt="simple-chat-application v1.0 container resource usage"/>

#### Version 2

##### Container layers

<img src="PART-I/Images/" alt="simple-chat-application v2.0 container layers"/>

##### Container resource usage

<img src="PART-I/Images/" alt="simple-chat-application v2.0 container resource usage"/>

### Spring Boot Rest Application

#### Version 1

##### Container layers

<img src="PART-I/Images/" alt="spring-boot-rest-application v1.0 container layers"/>

##### Container resource usage

<img src="PART-I/Images/" alt="spring-boot-rest-application v1.0 container resource usage"/>

#### Version 2

##### Container layers

<img src="PART-I/Images/" alt="spring-boot-rest-application v2.0 container layers"/>

##### Container resource usage

<img src="PART-I/Images/" alt="spring-boot-rest-application v2.0 container resource usage"/>

## Part II
The goal for the second part of the class assignment is to create a containerized environment for running the Spring 
Boot Rest Application. You will implement a solution similar to CA3-P2, but this time using Docker instead of Vagrant.

To achieve this goal, you'll be using **Docker Compose** to create **two** containers. One of the containers will host 
the Spring Boot Rest Application and the other will run the H2 database server.

Additionally, ensure both containers can resolve each other's hostname, and configure a health-check on the db 
container, so that the web service only starts after the database is available.

Next, use a Docker volume in the db container to persist the database file. That way you ensure that the data is stored 
outside the container’s filesystem and can be retained and reused across container restarts.

Define environment variables in both containers to store sensitive information (database configurations to 
Spring Boot Rest Application, such as DB URL, username, password, etc...)

Finally, publish both images (db and web) to **Docker Hub**.

# Alternative Technologies

## Kubernetes

## Podman

# Self-Evaluation
```bash
Daniel (12500503) - ??%
Diogo (1250506) - ??%
Pedro (1250545) - 100%
```