# CA 6 - CI/CD Pipelines
This class assignment (CA) focuses on designing and implementing **CI/CD pipelines** using **Jenkins**, along with 
automated infrastructure provisioning using **Vagrant** and **Ansible**. 

You'll build the CI/CD Pipelines using concepts learned in previous assignments, specifically CA3 and CA5.

## Jenkins Overview
**Jenkins** is a self-contained, open source automation server used to automate tasks related to building, testing, and 
delivering or deploying software.

It can be installed through:
- Native system packages.
- Docker containers.
- A standalone Java Runtime Environment (JRE).

### Installing Jenkins

## Part I
The first part of CA6 requires creating a pipeline that:
1. Builds the Gradle version of the Spring Boot Rest Application
2. Deploys the built application to a local virtual machine (VM). 
This task mirrors the structure of Part 1 of CA3, where both the application and the H2 database run inside the same VM.

### Automate Infrastructure Setup
You'll use **Vagrant** to create and manage two VMs:
- **blue**
- **green**

The result can be achieved by following the steps outlined in the `Vagrantfile` below.

```bash
Vagrant.configure("2") do |config|
    config.vm.box = "bento/ubuntu-20.04"

    config.ssh.forward_agent = true
    config.ssh.insert_key = false

    config.vm.define "blue" do |blue|

        blue.vm.provider "virtualbox" do |vb|
            vb.name = "blue"
            vb.memory = 1024
            vb.cpus = 2
        end

        blue.vm.network "private_network", ip: "192.168.56.10"
        blue.vm.network "forwarded_port", guest: 8080, host: 8081

        blue.vm.provision "ansible" do |ansible|
            ansible.playbook = "./playbook.yaml"
            ansible.compatibility_mode = "2.0"
        end
    end

    config.vm.define "green" do |green|

        green.vm.provider "virtualbox" do |vb|
            vb.name = "green"
            vb.memory = 1024
            vb.cpus = 2
        end

        green.vm.network "private_network", ip: "192.168.56.11"
        green.vm.network "forwarded_port", guest: 8080, host: 8083

        green.vm.provision "ansible" do |ansible|
            ansible.playbook = "./playbook-green.yaml"
            ansible.compatibility_mode = "2.0"
        end
    end
end
```

The Vagrantfile begins by specifying the base box used to create all virtual machines: `bento/ubuntu-20.04`, an Ubuntu
20.04 image maintained for development environments.

#### blue VM
Firstly, it's defined the first VM, named **blue**. This machine uses the VirtualBox provider, where its VM name is set 
to `blue`, and it is allocated `1 GB of RAM` and `2 CPU cores`, giving it enough resources to run the Spring Boot Rest 
Application. Networking is configured in two ways: first, a private network assigns the machine the static IP 
`192.168.56.10`, second, a port forwarding rule exposes the application running inside the VM on port `8080`, mapping it 
to port `8081` on the host.

After networking, an Ansible provisioner is defined, instructing Vagrant to run the playbook `playbook.yaml` to 
automatically provision the machine.

#### green VM
The configuration defines the second machine, named **green**, which mirrors the structure of the blue VM but serves 
as a parallel deployment environment. Using the VirtualBox provider, the green VM is assigned the same hardware 
resources, and is named `green`. It also receives its own private IP address, `192.168.56.11`, and a different port 
forwarding rule, mapping its internal port `8080` to port `8083` on the host system. 

The green VM is provisioned with a separate Ansible playbook, `playbook-green.yaml`, allowing a different version or 
configuration of the application to be deployed.

#### Ansible Provisioning
These machines must be provisioned automatically using **Ansible**. Your first version of the Ansible `playbook.yaml` 
should deploy the current version of the Spring Boot Rest Application, targeting the **blue** VM for deployment.

The result can be achieved by following the steps outlined in the `playbook.yaml` below.

```yaml
---
- name: Provision the blue VM
  hosts: blue
  become: true
  become_method: sudo
  tasks:
    - name: Update apt cache
      become: true
      apt:
        update_cache: yes
        cache_valid_time: 3600

    - name: Install Git
      package:
        name: git
        state: present

    - name: Install Java
      apt:
        name: default-jre
        state: present

    - name: Install JDK
      apt:
        name: openjdk-17-jdk
        state: present

    - name: Ensure if the repository exists
      stat:
        path: /home/vagrant/COGSI2526_1250503_1250506_1250545/.git
      register: repo_status

    - name: Clone the Git repository
      git:
        repo: https://github.com/pchu22/COGSI2526_1250503_1250506_1250545-copy
        dest: /home/vagrant/COGSI2526_1250503_1250506_1250545
        version: main
        update: no
      register: git_clone
      when: not repo_status.stat.exists

    - name: Pull from the Git repository
      git:
        repo: https://github.com/pchu22/COGSI2526_1250503_1250506_1250545-copy
        dest: /home/vagrant/COGSI2526_1250503_1250506_1250545
        version: main
        update: yes
        force: yes
      register: git_pull
      when: git_clone is not changed or git_clone is skipped

    - name: Repository clone debug message
      debug:
        msg: "Repository successfully cloned!"
      when: git_clone.changed

    - name: Repository existence debug message
      debug:
        msg: "Repository already exists. Skipping clone.."
      when: git_clone is skipped

    - name: Up-to-dated project debug message
      debug:
        msg: "Repository successfully updated!"
      when: not git_pull.changed

    - name: Ensure project directory has the right ownership
      file:
        path: /home/vagrant/COGSI2526_1250503_1250506_1250545
        state: directory
        owner: vagrant
        group: vagrant

    - name: Ensure gradlew has correct permissions
      file:
        path: /home/vagrant/COGSI2526_1250503_1250506_1250545/CA2/PART-II/gradle-tut-rest/gradlew
        state: file
        owner: vagrant
        group: vagrant
        mode: '0744'

    - name: Run Spring Boot Rest Application
      shell: |
        ./gradlew bootRun
      args:
        chdir: /home/vagrant/COGSI2526_1250503_1250506_1250545/CA2/PART-II/gradle-tut-rest/
```

### Define the Pipeline Logic
You will implement all the CI/CD automation for this CA using a `Jenkinsfile`, stored at the **root** of your 
application. This file defines the workflow executed by Jenkins whenever the pipeline is triggered.

The goal is to model a full build-and-deploy pipeline that compiles the Spring Boot Rest Application, **verifies it 
through automated tests**, **archives the artifacts**, and **deploys it to the green VM** using `Ansible`.

To structure the pipeline, you must include the following stages:

1. **Checkout** - This stage pulls the latest source code from your repository's **main** branch, ensuring Jenkins 
always builds the **most up-to-date version** of the Spring Boot Rest Application.

2. **Assemble** – In this stage, Jenkins compiles the project using `Gradle`, producing the executable artifact. The 
assembly process verifies that the code compiles successfully and that all dependencies are correctly defined.

3. **Test** – Run unit tests to verify the application’s correctness. Publish the test results in Jenkins.

4. **Archive** – Archive the artifacts in Jenkins for later use.

5. **Deploy to Production** – Request manual approval to deploy the application to the production environment. Only 
proceed if the deployment is approved.

6. **Deploy** – Uses an Ansible playbook to deploy and start the application on the green VM.

Below you will find a `Jenkinsfile` you can use as example to achieve the pretended result.

```bash
pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                echo "Pulling source code..."

                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/main']],
                    userRemoteConfigs: [[url: 'https://github.com/pchu22/COGSI2526_1250503_1250506_1250545-copy']],
                    gitTool: isUnix() ? 'DefaultLinuxGit' : 'DefaultWindowsGit'
                ])
            }
        }

        stage('Assemble') {
            steps {
                echo "Building project..."

                dir('CA2/PART-II/gradle-tut-rest') {
                    script {
                        if (isUnix()) {
                            sh './gradlew clean build'
                        }
                        else {
                            bat 'gradlew.bat clean build'
                        }
                    }
                }
            }
        }

        stage('Test') {
            steps {
                echo "Running test..."

                dir('CA2/PART-II/gradle-tut-rest') {
                    script {
                        if (isUnix()) {
                            sh './gradlew test'
                        } else {
                            bat 'gradlew.bat test'
                        }
                    }
                    junit 'build/test-results/test/*.xml'
                }
            }
        }

         stage('Archiving') {
            steps {
                echo 'Archiving artifacts...'

                archiveArtifacts 'CA2/PART-II/gradle-tut-rest/build/libs/*.jar'
            }
         }


         stage('Deploy to Production') {
            steps {
                echo "Deploying to production..."

                script {
                    timeout(time: 60, unit: 'SECONDS') {
                        input(message: 'Deploy application to PRODUCTION?', ok: 'Approve Deployment')
                    }
                }
            }
         }
    }
}
```

The `Checkout` stage is responsible for pulling the latest source code from the remote repository into the Jenkins 
workspace. It begins by printing a message to the console indicating that the checkout process is starting. The checkout 
step uses the **Git SCM plugin** to interact with the repository and is configured to ensure the correct branch and 
repository are used, while also handling differences between operating systems.

- `$class: 'GitSCM'`: Specifies that the **Git Source Control Management** plugin is being utilized.
- `branches: [[name: '*/main']]`: Indicates the checkout branch.
- `userRemoteConfigs: [[url: 'https://github.com/pchu22/COGSI2526_1250503_1250506_1250545-copy']]`: Defines the remote 
repository URL to pull the code from.
- `gitTool: isUnix() ? 'DefaultLinuxGit' : 'DefaultWindowsGit'`: Dynamically selects the Git executable based on the 
agent's operating system.

The `Assemble` stage is responsible for building the project. It prints a message to indicate that the build is 
starting. The `dir` block navigates to the `CA2/PART-II/gradle-tut-rest` directory where the Gradle project is located. 
Inside a **script** block, the pipeline dynamically chooses the command depending on the agent’s operating system:
- On **Unix/Linux/macOS nodes**, it runs `sh './gradlew clean build'` to clean any previous build and assemble a fresh 
build.
- On **Windows nodes**, it runs `bat 'gradlew.bat clean build'` to achieve the same effect.

The `Test` stage is responsible for running automated tests. It navigates to the project directory and **executes the 
Gradle test task**:
- On **Unix/Linux/macOS nodes**, it runs `sh './gradlew test'`.
- On **Windows nodes**, it runs `bat 'gradlew.bat test'`.

After running the tests, the junit step collects the test results from `build/test-results/test/*.xml` so that Jenkins 
can display them in the UI. This ensures that test failures are tracked and visible in the pipeline.

The `Archiving` stage handles storing the build artifacts. It prints a message to indicate archiving and then uses 
`archiveArtifacts 'CA2/PART-II/gradle-tut-rest/build/libs/*.jar'` to save the compiled JAR files. This makes them 
available for future stages, downloads, or deployments.

Finally, the `Deploy to Production` stage is a manual approval step. It prints a message about deployment and uses a 
timeout combined with input to pause the pipeline and request user confirmation:
- `timeout(time: 60, unit: 'SECONDS')` ensures the input prompt will only wait for 60 seconds before failing the step if 
nobody approves.
- `input(message: 'Deploy application to PRODUCTION?', ok: 'Approve Deployment')` prompts the user to approve the 
deployment, adding a safety check before pushing changes to production.

### Tag Stable Builds
Next, you're going to tag the **stable builds in Jenkins**. To tag these builds, you can use a consistent naming 
convention such as `stable-v1.0`,`stable-v1.1`, and so on. These tags should only be created for builds that 
**successfully pass all pipeline stages**.

Use the example presented below as reference:

```bash
    post {
        success {
            echo "Tagging this stable build..."
            
            def tag = "stable-v${env.BUILD_NUMBER}"
            
            if (isUnix()) {
                sh """
                    git config user.name "jenkins"
                    git config user.email "jenkins@cogsi"
                    git tag ${tag}
                    git push origin ${tag}
                """
            } else {
                bat """
                    git config user.name "jenkins"
                    git config user.email "jenkins@cogsi"
                    git tag ${tag}
                    git push origin ${tag}
                """
            }
            
            echo "Tag ${tag} successfully created and pushed."
        }
    }
```

In the example you provided above, a tag is created during the `post { success { ... } }` block. Using 
`${env.BUILD_NUMBER}`, you automatically generate a unique tag for each successful build. Inside the block, Jenkins sets 
a Git **username** and **email**, creates the tag locally using the command `git tag`, and then pushes it to the remote 
repository using `git push`. The logic handles both Unix and Windows agents by switching between sh and bat steps, 
ensuring compatibility across environments.

### Include `post-actions` Messages in the Pipeline:
Include the following post-actions in your pipeline:
1. **Notification** – Print a message with the result of the pipeline’s execution
2. **Deployment Verification** – Add automated health-checks after deployment to verify that the application is 
functioning correctly in production

Below you will find the utilized `post-actions` on the `Jenkinsfile`. You can use these `post-action` as an example to 
achieve the pretended result.

```bash
post {
    success {
        echo "Build succeeded!"
        echo "Running health-check..."

        def response = ''

        if (isUnix()) {
            response = sh(
                script: "curl -s -o /dev/null -w \"%{http_code}\" http://192.168.56.10:8082/employees",
                returnStdout: true
            ).trim()
        } else {
            response = bat
            }(
                script: """
                    powershell -Command "(Invoke-WebRequest -Uri 'http://192.168.56.10:8082/employees' -UseBasicParsing).StatusCode"
                """,
                returnStdout: true
            ).trim()
        }

        if (response == '200') {
            echo "Application is healthy!"
        } else {
            echo "Application may be unstable or unreachable!"
        }
    }

    failure {
        echo "Build failed!"
    }

    always {
        echo "Pipeline execution completed."
    }
}
```

The above snippets of code define the following post-actions:

1.  `success`: This block is only executed if **every stage completes without errors**. It performs two tasks, being the 
first one a `Build Success` notification, and the second one a deployment health-check - After deployment, the pipeline 
verifies if the application is healthy and reachable by sending an HTTP request and evaluating the **status code**. If 
the server responds with `200`, Jenkins reports that the application is healthy, otherwise, it warns that the service 
may be unstable or unreachable.
2. `failure`: This section only runs when one or more stages fail. A "Build failed!" message is printed to the console 
to indicate that the pipeline didn't complete.
3. `always`: This block runs at the end of the pipeline whether the result is success, failure, or abort. It prints a 
final message to indicate that the entire pipeline execution has concluded.

## Part II
The goal of Part 2 is to create a pipeline that builds a Docker image of the Gradle version of the Building REST 
services with Spring application, publishes it in Docker Hub, and deploys it on a production VM. This task is designed 
to follow a structure similar to Part 1 of CA5, where both the application and the H2 database are hosted and executed 
within the same container.

### Automate Infrastructure Setup
To ensure consistent and repeatable environment creation, this setup uses `Vagrant` with an `Ansible` provisioner. By 
automating the entire process, it is possible to guarantee that the development environment can be recreated reliably on 
any machine with minimal manual intervention.

```bash
Vagrant.configure("2") do |config|
  config.vm.box = "bento/ubuntu-20.04"
  config.vm.hostname = "ca6-p2"
  
  config.vm.provider "virtualbox" do |vb|
    vb.name = "ca6-p2"
    vb.memory = 1024
    vb.cpus = 2
  end

  config.vm.network "private_network", ip: "192.168.56.12"
  config.vm.network "forwarded_port", guest: 8080, host: 8085

  config.vm.provision "ansible" do |ansible|
    ansible.playbook = "playbook.yml"
  end
end
```

The `Vagrantfile` begins by defining the base image used to build the virtual environment: `bento/ubuntu-20.04`. The VM 
**ca6-p2** is then configured using the VirtualBox provider. Its name is set to `ca6-p2`, and it is assigned `1 GB` of 
RAM and `2 CPU cores`, providing sufficient resources to run the Spring Boot Rest Application within a Docker container.

Networking is configured in two ways:
- A private network assigns the VM a static IP address: `192.168.56.12`.
- A port forwarding rule exposes the VM’s internal port `8080` on the host machine via port `8085`, allowing local 
access to the application running inside the VM.

Finally, the VM is provisioned using `Ansible`, with Vagrant automatically executing the `playbook.yml` file.

### Ansible Provisioning
This machine must be provisioned automatically using **Ansible**. Your first version of the Ansible `playbook.yaml` 
should deploy the current version of the Spring Boot Rest Application, targeting the **blue** VM for deployment.

The result can be achieved by following the steps outlined in the `playbook.yaml` below.

```yaml
---
- name: Provision the ca6-p2 VM
  hosts: ca6-p2
  become: true
  become_method: sudo
  vars_files:
    - secrets/dockerhub.yml

  tasks:
    - name: Install Docker
      apt:
        name: docker.io
        state: present
        update_cache: yes

    - name: Ensure Docker is running
      service:
        name: docker
        state: started
        enabled: yes

    - name: Login to Docker Hub
      docker_login:
        username: "{{ dockerhub_user }}"
        password: "{{ dockerhub_pass }}"

    - name: Pull latest Docker image
      docker_image:
        name: "devpchu01/gradle-tut-rest:4.0"
        source: pull

    - name: Stop any container that's running (if exists)
      docker_container:
        name: "spring-boot-rest-application"
        state: absent
        force_kill: yes

    - name: Run the container
      docker_container:
        name: "spring-boot-rest-application"
        image: "devpchu01/gradle-tut-rest:4.0"
        state: started
        restart_policy: always
        ports:
          - "8082:8086"
```

### Define the Pipeline Logic
Below is the CI/CD workflow that must be implemented in the `Jenkinsfile`. A **GitHub webhook** must be configured so 
that any push to either the **main** or **development** branches automatically triggers the pipeline. The pipeline must 
include the following stages:
1. **Checkout**: Retrieve the latest version of the source code from your remote repository.
2. **Assemble**: Compile the application and generate all necessary artifacts.
3. **Test**: Execute **unit** and **integration** tests. Test results **must be published in Jenkins**, and failed tests 
should mark the build as **unstable** or **failed** depending on severity.
4. **Tag Docker Image**: Build the Docker image for the application and apply an appropriate tag.
5. **Archive**: Archive the `Dockerfile` and any relevant build metadata in Jenkins.
6. **Push Docker Image**: Authenticate to Docker Hub and push the tagged Docker image to the registry.
7. **Deploy**: Execute an Ansible playbook to deploy the newly built Docker image.

Below you will find a `Jenkinsfile` you can use as example to achieve the pretended result.

```bash
pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials')
        EMAIL_RECIPIENTS = "1250503@isep.ipp.pt, 1250506@isep.ipp.pt, 1250545@isep.ipp.pt"
    }

    stages {
        stage('Checkout') {
            steps {
                echo "Pulling source code..."

                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/main']],
                    userRemoteConfigs: [[url: 'https://github.com/pchu22/COGSI2526_1250503_1250506_1250545-copy']],
                    gitTool: isUnix() ? 'DefaultLinuxGit' : 'DefaultWindowsGit'
                ])
            }
        }

        stage('Assemble') {
            steps {
                echo "Building project..."

                dir('CA2/PART-II/gradle-tut-rest') {
                    script {
                        if (isUnix()) {
                            sh './gradlew clean build'
                        }
                        else {
                            bat 'gradlew.bat clean build'
                        }
                    }
                }
            }
        }

        stage('Test') {
            steps {
                echo "Running test..."

                dir('CA2/PART-II/gradle-tut-rest') {
                    script {
                        if (isUnix()) {
                            sh './gradlew test'
                        } else {
                            bat 'gradlew.bat test'
                        }
                    }
                    junit 'build/test-results/test/*.xml'
                }
            }
        }

         stage('Archiving') {
            steps {
                echo 'Archiving artifacts...'

                archiveArtifacts artifacts: 'Dockerfile'
                archiveArtifacts artifacts: 'CA2/PART-II/gradle-tut-rest/build/libs/*.jar'
            }
         }

        stage('Build and Tag Docker Image') {
            steps {
                echo "Building Docker image..."

                script {
                    if (isUnix()){
                        sh """
                            docker build -t gradle-tut-rest:4.0 -f Dockerfile .
                            docker tag gradle-tut-rest:4.0 gradle-tut-rest:latest
                        """
                    } else {
                        bat """
                            docker build -t gradle-tut-rest:4.0 -f Dockerfile .
                            docker tag gradle-tut-rest:4.0 gradle-tut-rest:latest
                        """
                    }

                }
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                echo "Pushing Image to Docker Hub..."

                script {
                    if (isUnix()) {
                        sh """
                            docker push gradle-tut-rest:4.0
                            docker push gradle-tut-rest:latest
                        """
                    } else {
                        bat """
                            docker push gradle-tut-rest:4.0
                            docker push gradle-tut-rest:latest
                        """
                    }
                }
            }
        }

         stage('Deploy to Production') {
            steps {
                echo "Deploying to production..."

                script {
                    timeout(time: 60, unit: 'SECONDS') {
                        input(message: 'Deploy application to PRODUCTION?', ok: 'Approve Deployment')
                    }
                }
            }
         }
    }
}
```

Execute parallel tests as part of the pipeline, reducing the overall runtime
▪ Consider using different Jenkins nodes for running these parallel tests to demonstrate efficient resource 
utilization and scalability

Ensure the application is only deployed to production when a commit is pushed to the main branch
▪ Use logic in your `Jenkinsfile` to verify the branch name before triggering deployment actions

The deployment playbook must:
- Ensure Docker is installed
- Login to Docker Hub and pull the latest Docker image
- Stop and remove the old container if it exists
- Run the new Docker container

Apply (in your `Jenkinsfile`) the changes presented below to achieve the desired result:

```bash
        stage('Test') {
            steps {
                echo "Running test..."
                
                stage ('Unit Test') {
                    agent { label 'node-1' }

                    echo "Running unit tests..."

                    dir('CA2/PART-II/gradle-tut-rest') {
                        script {
                            if (isUnix()) {
                                sh './gradlew test'
                            } else {
                                bat 'gradlew.bat test'
                            }
                        }
                        junit 'build/test-results/test/*.xml'
                    }
                }
                
                stage (Integration Test) {
                    agent { label 'node-2' }

                    echo "Running integration tests..."

                    dir('CA2/PART-II/gradle-tut-rest') {
                        script {
                            if (isUnix()) {
                                sh './gradlew integrationTest'
                            } else {
                                bat 'gradlew.bat integrationTest'
                            }
                        }
                        junit 'build/test-results/integrationTest/*.xml'
                    }
                }
            }
        }
```

These changes run the automated tests **in parallel** to reduce pipeline runtime and demonstrate efficient resource
usage.

It contains two parallel stages, the `Unit Tests`, that runs the Gradle test task, executing it on a node
labeled `node-1` and collects the test results using `junit 'build/test-results/test/*.xml'`. The `Integration Tests`
runs the Gradle integrationTest task, executing it on a node labeled node-2 and collects the test results using
`junit 'build/test-results/integrationTest/*.xml'`.

This parallelization allows different tests to run concurrently, leveraging multiple nodes if available.

### Include `post-actions` Messages in the Pipeline:
Include the following post-actions in your pipeline:
1. **Notification**: Instead of just printing a message, consider integrating with tools like `email` to send 
**success**, **failure**, or **unstable** build notifications.
2. **Deployment Verification**: Add automated health-checks after deployment to verify that the application is 
functioning correctly in production

Below you will find the utilized `post-actions` on the `Jenkinsfile`. You can use these `post-action` as an example to 
achieve the pretended result.

```bash
    post {
        success {
            echo "Build succeeded!"
            echo "Sending email notification!"

            script {
                emailext (
                    to: "${EMAIL_RECIPIENTS}",
                    subject: "Jenkins Pipeline SUCCESS",
                    body: "Pipeline completed successfully."
                )
            }

            echo "Running health-check..."

            def response = ''

            if (isUnix()) {
                response = sh(
                    script: "curl -s -o /dev/null -w \"%{http_code}\" http://192.168.56.12:8082/employees",
                    returnStdout: true
                ).trim()
            } else {
                response = bat
                }(
                    script: """
                        powershell -Command "(Invoke-WebRequest -Uri 'http://192.168.56.12:8082/employees' -UseBasicParsing).StatusCode"
                    """,
                    returnStdout: true
                ).trim()
            }

            if (response == '200') {
                echo "Application is healthy!"
            } else {
                echo "Application may be be unstable or unreachable!"
            }
        }

        unstable {
            echo "The pipeline completed with unstable results due to test failures."
        }

        failure {
            echo "Build failed!"
            script {
                emailext (
                    to: "${EMAIL_RECIPIENTS}",
                    subject: "Jenkins Pipeline FAILED",
                    body: "The pipeline failed"
                )
            }
        }

        always {
            echo "Pipeline execution completed."
        }
    }
```

## Alternative Technologies
In this section you'll be introduced to some alternative technologies to Jenkins and how one could implement
**CA6-Part2** - In our case it was `TeamCity`.

### Hudson

### TeamCity
`TeamCity` is a build management and CI server **developed by JetBrains**, and was released on 2/10/2006. Open-source 
projects can apply for a free license, giving them access to professional-grade CI/CD capabilities. TeamCity offers a 
web-based interface that simplifies navigation and makes setup and configuration straightforward. It works with many 
build and test tools, creating a cohesive development ecosystem. The **system tracks and analyses the build history**, 
which can help improve software quality and predictability.

Advantages
- TeamCity is relatively simple to set up and guides through the project.
- TeamCity connects with existing development tools and platforms.
- TeamCity's platform provides clear insights into your DevOps pipeline.
- Developers can work with builds without leaving their IDE.
- TeamCity manage CI/CD configurations using Kotlin.

**Disadvantages**
- Scaling may require additional license as your workloads grow and more people join the team.
- Compared to Jenkins, TeamCity has a fewer plugin.
- The smaller community means fewer community-created resources.
- While basic setup is simple, advanced features can take time to master.

#### Licensing and Costs
<table>
  <thead>
    <tr>
      <th>TeamCity</th>
      <th>Jenkins</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Commercial product with a free tier supporting 100 build configurations and 3 build agents</td>
      <td>Open-source with license fees</td>
    </tr>
    <tr>
      <td>Tiered enterprise pricing model based on build configurations and agents</td>
      <td>Infrastructure costs are the primary expense</td>
    </tr>
    <tr>
      <td>Additional build agents require separate licensing</td>
      <td>Hidden costs include maintenance engineering time and potential downtime</td>
    </tr>
    <tr>
      <td>Annual subscription covers version upgrades and basic support</td>
      <td>Total investment primarily in engineering resources rather than licenses</td>
    </tr>
  </tbody>
</table>

#### User Interface and Configuration
<table>
  <thead>
    <tr>
      <th>TeamCity</th>
      <th>Jenkins</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Modern web interface with consistent navigation patterns</td>
      <td>Function-first interface design with extensive customization options</td>
    </tr>
    <tr>
      <td>Visual build chain visualization for dependency tracking</td>
      <td>Plugin-based dashboard customization</td>
    </tr>
    <tr>
      <td>Project-based organization with inheritance of build parameters</td>
      <td>View configuration through XML or web interface</td>
    </tr>
    <tr>
      <td>Built-in test reporting with failure trend analysis</td>
      <td>Job organization via folders and multibranch pipelines</td>
    </tr>
    <tr>
      <td>Simpler initial setup but less customizable UI than Jenkins</td>
      <td>Higher learning curve but more flexible for specific workflow needs</td>
    </tr>
  </tbody>
</table>

#### Plugin Ecosystem and Integrations
<table>
  <thead>
    <tr>
      <th>TeamCity</th>
      <th>Jenkins</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Around 300 plugins with consistent quality standards</td>
      <td>More than 1800 community plugins covering virtually all tools and platforms</td>
    </tr>
    <tr>
      <td>First-party JetBrains tool integration (IntelliJ)</td>
      <td>Extensive SCM support through dedicated plugins</td>
    </tr>
    <tr>
      <td>Strong VCS support, particularly for Git operations</td>
      <td>Kubernetes' integration via specialized plugins</td>
    </tr>
    <tr>
      <td>Built-in Docker support and container-based build agents</td>
      <td>Variable plugin quality and maintenance levels</td>
    </tr>
    <tr>
      <td>Plugin compatibility more predictable between versions</td>
      <td>Requires careful evaluation of plugin security and update frequency</td>
    </tr>
  </tbody>
</table>

#### Scalability and Performance
<table>
  <thead>
    <tr>
      <th>TeamCity</th>
      <th>Jenkins</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Efficient server-side optimization for large project databases</td>
      <td>Master-agent architecture for horizontal scaling</td>
    </tr>
    <tr>
      <td>Cloud agent support for AWS, GCP and Azure</td>
      <td>Can run thousands of concurrent jobs with proper infrastructure</td>
    </tr>
    <tr>
      <td>Agent requirements and compatibility functionality for build routing</td>
      <td>Potential performance bottlenecks at the controller level with high concurrency</td>
    </tr>
    <tr>
      <td>Build grid for distributed test execution</td>
      <td>Stateless agent design allows for container-based scaling</td>
    </tr>
    <tr>
      <td>License costs increase linearly with scale</td>
      <td>Memory/CPU requirements increase with plugin count and build complexity</td>
    </tr>
  </tbody>
</table>

#### Maintenance and Support
<table>
  <thead>
    <tr>
      <th>TeamCity</th>
      <th>Jenkins</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>JetBrains commercial support with SLAs for critical issues</td>
      <td>Community support through forums, IRC and mailing lists</td>
    </tr>
    <tr>
      <td>Incremental upgrade path with backward compatibility</td>
      <td>Requires dedicated expertise for upgrades and maintenance</td>
    </tr>
    <tr>
      <td>Built-in backup functionality for configuration and history</td>
      <td>Configuration-as-code plugin helps with version control of settings</td>
    </tr>
    <tr>
      <td>Clear documentation and knowledge base</td>
      <td>Extensive but sometimes fragmented documentation</td>
    </tr>
  </tbody>
</table>

## Self-Evaluation
```bash
Daniel (1250503) - 80%
Diogo (1250506) - 80%
Pedro (1250545) - 100%
```