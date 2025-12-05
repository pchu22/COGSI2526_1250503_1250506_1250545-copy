# CA 6
For this class assignment you'll have to 

### Jenkins
Jenkins is a self-contained, open source automation server which can be used to automate all sorts of tasks related to 
building, testing, and delivering or deploying software.

Jenkins can be installed through native system packages, Docker, or even run standalone by any machine with a Java 
Runtime Environment (JRE) installed.

#### Installing Jenkins

## Part I
The goal of the first part of CA6 is to create a pipeline that builds the Gradle version of the Building REST services 
with Spring application and deploys it to a local virtual machine (VM). This task is designed to follow a structure 
similar to Part 1 of CA3, where both the application and the H2 database are hosted and executed within the same VM.

## Automate Infrastructure Setup
You're going to create two VMs using Vagrant - one named **blue**, and another named **green** - and provision the using 
Ansible. 

The "first version" of your `playbook.yaml` should deploy the current version of the Spring Boot Rest Application on the 
**blue** machine.

## Define the Pipeline Logic
Create a `Jenkinsfile`, where all the **pipeline logic** is stored. **Define the following stages in your Jenkins 
pipeline**:

1. **Checkout** – Pull the latest source code from the development branch in your repository.
2. **Assemble** – Compile the code and produce the artifact files.
3. **Test** – Run unit tests to verify the application’s correctness. Publish the test results in Jenkins.
4. **Archive** – Archive the artifacts in Jenkins for later use.
5. **Deploy to Production** – Request manual approval to deploy the application to the production environment. Only 
proceed if the deployment is approved.
6. **Deploy** – Uses an Ansible playbook to deploy and start the application on the green VM.

### Tag Stable Builds
Tag stable builds in Jenkins using a consistent naming convention - e.g., `stable-v1.0`, `stable-v1.1`.

Ensure these tags are applied only to the artifacts that pass all tests.

### Include `post-actions` in the Pipeline:
Include the following post-actions in your pipeline:
1. **Notification** – Print a message with the result of the pipeline’s execution
2. **Deployment Verification** – Add automated health-checks after deployment to verify that the application is 
functioning correctly in production

## Rollback to Previous Versions
Create an Ansible `playbook` to roll back to a previous stable version of the Spring Boot Rest Application stored as an 
artifact in Jenkins. The `playbook.yamp` should automate the rollback process by retrieving the artifact from Jenkins 
and deploying it to the **green** VM.
- Connect to Jenkins using the `Jenkins API` or `CLI` to **download the tagged artifact**.
- Stop the application currently running on the **green** VM, ensuring the resources in use are properly released.
- Replace the current application with the retrieved stable artifact and restart the service.
- Run **automated health checks** to confirm that the stable version is running as expected.

## Part II
The goal of Part 2 is to create a pipeline that builds a Docker image of the Gradle version of the Building REST 
services with Spring application, publishes it in Docker Hub, and deploys it on a production VM. This task is designed 
to follow a structure similar to Part 1 of CA5, where both the application and the H2 database are hosted and executed 
within the same container.