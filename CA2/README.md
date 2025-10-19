# CA2 - Build Tools (Part I)
After downloading the `gradle_basic_demo` source code from **"https://github.com/lmpnogueira/gradle_basic_demo"**, create a new branch to address the first part of the Class Assignment II. The following commands were executed to create the branch and start working in it:
```bash
git branch Gradle-W1 # Create a new branch named "Gradle-W1".
git branch # Verify the branches and confirm which one is active.
git switch Gradle-W1 # Switch from current branch ("main") to the "Gradle-W1" branch.
```

## Add a tag to mark the application version
It is recommended to use a **major.minor.revision** pattern for your tags (e.g., `ca2-v1.1.0`).
```bash
git tag -a ca2-v1.1.0 4b6bc11 
```
This command stages the tag `ca2-v1.1.0` on commit `4b6bc11`. To push the tag to the remote repository **COGSI2526_1250503_1250506_1250545**, execute:
```bash
git push origin ca2-v1.1.0 
```

## Add a runServer task to build.gradle
First, create the following task inside `build.gradle`.

<img src="PART-I/Images/01_01.PNG" alt="runServer task source code" width="500"/>

This task will execute the code in `ChatServerApp.java` and has `59001` as argument (which will be the server port).
 
Then, in the terminal, run the command **./gradlew runServer** to rune the previously created task. The result should look like this:

<img src="PART-I/Images/01_02.PNG" alt="runServer task running on terminal" width="500"/>

The output indicates the server is up and running.

When a chatter joins the server, the terminal output should be something like this:

<img src="PART-I/Images/01_03.PNG" alt="renServer Message after a chatter joins" width="500"/>

## Add a simple unit test to the source code and change build.gradle to accomodate the test
Create `test/java/basic_demo` directories inside the `src` folder of the source code and create a java file (i.e AppTest). Write the following test:

<img src="PART-I/Images/02_03.PNG" alt="AppTest code" width="500"/>

This unit test verifies that the `getGreeting()` method inside App.java returns the string `Welcome to Multi-User Chat Application!`. If it does, the test succeds and the application is executed.

You'll need to make some changes to your `build.gradle` in order to add **JUnit dependencies**. For that purpose, make sure your `build.gradle` contains these lines of code:

<img src="PART-I/Images/02_01.PNG" alt="build.gradle JUnit dependencies" width="500"/>

To run the test, inside the terminal, use the command **./gradlew test**. The output should be something like this:

<img src="PART-I/Images/02_04.PNG" alt="./gradlew test - command output" width="500"/>

## Add a new task of type Copy, to be used to make a backup of the source code of the application 
To add a new task **"runBackupRoutine"**, you must place the following code in your `build.gradle` file:

<img src="PART-I/Images/03_01.PNG" alt="runBackupRoutine - task source code" width="500"/>

This task copies the code from `src/` directory and pastes it into the `build/backup` directory. If the folder `backup` doesn't exist, a new one is created.

After adding the code to the `build.gradle` file, execute the command **./gradlew runBackupRoutine**, in the terminal. The output should look like this:

<img src="PART-I/Images/03_02.PNG" alt="./gradlew task runBackupRoutine - command output" width="500"/>

The **runBackupRoutine** task will create a new folder named `backup`, if it doesn't exist, inside the `build` directory.
<img src="PART-I/Images/03_03.PNG" alt="folder backup" width="500"/>

## Add a new task of type Zip, to be used to make an archive (i.e .zip) of the backup of the application
To add a new task **"zipBackup"**, you must place the following code in your `build.gradle` file:

<img src="PART-I/Images/04_01.PNG" alt="zipBackup - task source code" width="500"/>

This snippet of code will include all the `.class` file in the newly created archive, and save it in the `build/backup-zipped` directory.

After adding the code to the `build.gradle` file, execute the command **./gradlew zipBackup**. The output should look like this:

<img src="PART-I/Images/04_02.PNG" alt="./gradlew task zipBackup - command output" width="500"/>

The zipBackup task will create a new folder named `backup-zipped` inside the `build` directory.
<img src="PART-I/Images/04_03.PNG" alt="folder backup-zipped" width="500"/>

## Explanation of how the Gradle Wrapper and the JDK Toolchain ensure the correct versions of the Gradle and the JDK are used without requiring manual installation
When you run the command **./gradlew -q javaToolchain** in the terminal you'll get a similar result to the one in the image below.

<img src="PART-I/Images/05.PNG" alt="./gradlew -q javaToolchain command result" width="500"/>

### Gradle Wrapper
The **Gradle Wrapper** and the **JDK Toolchain** work together to ensure the correct versions of Gradle and JDK are used for a project without requiring manual installation on each machine.

The Graddle Wrapper is a script (`gradlew`/ `gradlew.bat`) included in the project that:

- Automatically downloads and uses the specific Gradle version configured for the project.
- Ensures that all developers and CI environments use the same Gradle version, avoiding version conflicts.
- Works independently of any system-installed Gradle. 

In the image above, Gradle has auto-detected and provisioned:
- Eclipse Temurin JDK 17.0.16+8 
- Location: /home/pchu/.gradle/jdks/eclipse_adoptium-17-amd64-linux.2
- Language Version: 17
- Vendor: Eclipse Temurin
- Architecture: amd64
- Is JDK: true
- Detected by: **Auto-provisioned by Gradle**

### JDK Toolchain
Gradle's JDK Toolchain feature allow specifying the exact version required for compiling and running the project, regardless of the system default.

In the output **two** JDKs are available:
- **Eclipse Temurin JDK 17.0.16+8**, auto-provisioned by Gradle
- **Ubuntu JDK 21.0.8+9**, system installed

Gradle can choose the correct JDK (e.g 17) for the project build, even though the system has JDK 21 installed globally.

### How they work together
1. The Graddle Wrapper ensures the right Gradle version is used.
2. The JDK Toolchain ensures the correct Java version is used for compiling and running the code.
3. Both tools eliminate the need for developers to manually install or configure Gradle and JDK versions.

This ensures **consistency** across all environments, as demonstrated in the output where Gradle auto-provisions JDK 17 for the project, even though JDK 21 is installed on the system.

## Add a new tag, to mark commit d12d44b, as last of the CA2-Part I
To mark the first part of the Class Assignment II as concluded, create the tag `ca2-part1` with the command. The `-a` attribute stages the tag to the `d12d44b` commit.
```bash
git tag -a ca2-part1 d12d44b
```
To push the tag to the remote repository **COGSI2526_1250503_1250506_1250545**, execute:
```bash
git push origin ca2-part1
```

# CA2 - Build Tools (Part II)
After downloading the `tut_rest` source code from **"https://github.com/spring-guides/tut_rest"**, create a new branch to address the second part of the Class Assignment II. The following commands were executed to create the branch and start working in it:
```bash
git branch Gradle-W2 # Create a new branch named "Gradle-W2".
git branch # Verify the branches and confirm which one is active.
git switch Gradle-W2 # Switch from current branch ("Gradle-W1") to the "Gradle-W2" branch.
```
## Move to the `links` folder and execute the application from the command line 
In order to run the application from the command line, execute the code `../mvnw spring-boot:run`. In your command line you should see something like what is shown in the image below:

<img src="PART-II/Images/02_01.PNG" alt="../mvnw spring-boot:run command result" width="500"/> 

Then, in you browser enter the URL `localhost:808/employees`, and the result must be the following:
<img src="PART-II/Images/02_02.PNG" alt="application employees page" width="500"/>

## Build a new Gradle project and replace the App.java with `tut_rest` source code. Make sure all dependencies and plugins are added to the `build.gradle` file
Start by running the command `gradle init` on your terminal. After that, a new project is going to be created and you're going to swap your source code (Hello World) with `tut_rest` source code.

<img src="PART-II/Images/03_01.PNG" alt="tut_rest files in the newly created gradle_tut_rest project" width="500"/>

After doing that, you need to add all the needed dependencies and plugins to your `build.gradle`. Your build file should look something like this:
```bash
import org.apache.tools.ant.filters.ReplaceTokens

// Apply the java plugin to add support for Java
plugins {
    id 'java'
    id 'application'
    id 'org.springframework.boot' version '3.5.6'
    id 'io.spring.dependency-management' version '1.1.7'
}

version = '1.1.0'

// In this section you declare where to find the dependencies of your project
repositories {
    // Use jcenter for resolving your dependencies.
    // You can declare any Maven/Ivy/file repository here.
    mavenCentral()
}

dependencies {
    // This dependency is found on compile classpath of this component and consumers.
    implementation 'com.google.guava:guava:23.0'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-hateoas'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'jakarta.persistence:jakarta.persistence-api'


    // Use JUnit test framework
    testImplementation 'org.springframework.boot:spring-boot-starter-test'

    runtimeOnly 'com.h2database:h2'
}

test{
    useJUnitPlatform()
}
```
After performing these changes, in the terminal run the command `./gradlew clean build`. The result should look like this:
<img src="PART-II/Images/04_01.PNG" alt="./gradlew clean build command result" width="500"/>

When the build process is finished, you're going to execute the command `./gradlew bootRun`, and after the command is executed, your terminal must look like the following:

<img src="PART-II/Images/04_02.PNG" alt="./gradlew bootRun command result" width="500"/>

To fully confirm your code is successfully running, in your browser, visit the URL `localhost:8080/employees`. The final result should be this:
<img src="PART-II/Images/04_03.PNG" alt="application employees page" width="500"/>

## Custom Gradle Task: deployToDev – Automated Deployment Process
This task can be implemented as a single block of code, or divided in smaller, modular tasks. We opted for a modular approach to improve readability, maitainability and reusability. To achieve this goal, we created **four** separate tasks - **cleanDeployment**, **copyJar**, **copyRuntimeDependencies**, and **copyConfigurations**. The implementation of this tasks is shown below.

```bash
tasks.register("cleanDeployment", Delete){
    group = 'deployment'
    description = 'Deletes the deployment directory for dev environment'

    delete layout.buildDirectory.dir("deployment/dev")
}
```

```bash
tasks.register("copyJar", Copy){
    group = 'deployment'
    description = 'Copies the built jar into the deployment directory'
    dependsOn tasks.named("build")

    from layout.buildDirectory.dir("libs/${project.name}-${project.version}.jar")
    into layout.buildDirectory.dir("deployment/dev")
}
```

```bash
tasks.register("copyRuntimeDependencies", Copy){
    group = 'deployment'
    description = 'Copies runtime dependencies into the deployment directory'
    dependsOn tasks.named("build")

    from configurations.runtimeClasspath
    into layout.buildDirectory.dir("deployment/dev/lib")
}
```

```bash
tasks.register("copyConfigurations", Copy){
    group = 'deployment'
    description = 'Copies configurations (and replaces tokens) into the deployment directory'
    dependsOn tasks.named("build")

    from ('src/main/resources/*.properties') {
        filter(ReplaceTokens, tokens: [
                timestamps: new Date().format('dd-MM-yyyy HH:mm:ss'),
                version   : project.version
        ])
    }
    into layout.buildDirectory.dir("deployment/dev")
}
```

If you decide to follow a modular approach like ours, your final `deployToDev` task should resemble the example shown below.
```bash
tasks.register("deployToDev"){
    group = 'deployment'
    description = 'Deploys the dev build with JARs and comfigurations'

    dependsOn tasks.named("build")
    dependsOn tasks.named("cleanDeployment")
    dependsOn tasks.named("copyJar")
    dependsOn tasks.named("copyRuntimeDependencies")
    dependsOn tasks.named("copyConfigurations")
}
```

To confirm the task is working correctly, execute `./gradlew deployToDev` in the terminal. The output should resemble the image shown below:
<img src="PART-II/Images/05_01.PNG" alt="./gradlew deployToDev command result" width="500"/>

After a successful execution, a new `.jar` file will be generated, and the deployment directory should contain the structure ilustrated below:
<img src="PART-II/Images/05_02.PNG" alt="gradle-tut-res-<version>.jar file" width="500"/>

## Custom Gradle task: generateScript 

```bash
tasks.register("generateScript", Exec){
    group = 'application'
    description = 'Generates an executable script based on the operating system'
    dependsOn tasks.named("installDist")

    def exe

    if(System.getProperty("os.name").toLowerCase().contains("windows")){
        exe = file("${buildDir}/install/${project.name}/bin/${project.name}.bat")
        commandLine 'cmd.exe', '/d', '/c', exe
    } else {
        exe = file("${buildDir}/install/${project.name}/bin/${project.name}")
        commandLine exe
    }
}
```

## Custom Gradle task: packageJavadoc

```bash
tasks.register("packageJavadoc", Zip){
    group = 'documentation'
    description = 'Generates Javadoc and packages it into a zip file'
    dependsOn tasks.named("javadoc")

    from layout.buildDirectory.dir("docs/javadoc")
    destinationDirectory.set(layout.buildDirectory.dir("docs"))

    def date = new SimpleDateFormat("ddMMyyyy-HHmmss").format(new Date())

    archiveBaseName = "${project.name}-javadoc-${date}"
    archiveExtension = 'zip'
}
```

## Create a new SourceSet for Integration Tests
```bash
sourceSets {
    integrationTest {
        java {
            srcDir 'src/integrationTest/java'
        }
        resources {
            srcDir 'src/integrationTest/resources'
        }

        compileClasspath += sourceSets.main.output
        runtimeClasspath += sourceSets.main.output

        configurations {
            integrationTestImplementation.extendsFrom implementation
            integrationTestRuntimeOnly.extendsFrom runtimeOnly
        }

        dependencies {
            integrationTestImplementation 'org.junit.jupiter:junit-jupiter'
            integrationTestRuntimeOnly 'org.junit.platform:junit-platform-launcher'
        }
    }
}

tasks.register("integrationTest", Test){
    group = 'verification'
    description = 'Runs yhe integration tests'

    testClassesDirs = sourceSets.integrationTest.output.classesDirs
    classpath = sourceSets.integrationTest.runtimeClasspath

    shouldRunAfter(tasks.named("test"))
}

check.dependsOn(tasks.named("integrationTest"))
```

# Self-Evaluation
```bash
Daniel (1250503) - 80%
Diogo (1250506) - 80%
Pedro (1250545) - 100%
```
