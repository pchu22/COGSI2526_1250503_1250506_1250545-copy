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

<img src="Images/01_01.PNG" alt="runServer task source code" width="500"/>

Then, run the command **./gradlew task runServer**. The result should look like this:

<img src="Images/01_02.PNG" alt="runServer task running on terminal" width="500"/>

When a chatter joins the server, the terminal output should be something like this:

<img src="Images/01_03.PNG" alt="renServer Message after a chatter joins" width="500"/>

## Add a simple unit test to the source code and change build.gradle to accomodate the test
Create `test/java/basic_demo` directories inside the `src` folder of the source code and create a java file (i.e AppTest). Write the following test:

<img src="Images/02_03.PNG" alt="AppTest code" width="500"/>

You'll need to make some changes to your build.gradle in order to add JUnit dependencies. To achieve that, make sure your build.gradle has the following lines of code:

<img src="Images/02_01.PNG" alt="build.gradle JUnit dependencies" width="500"/>

To run the test, use the command **./gradlew test**. The output should be something like this:

<img src="Images/02_04.PNG" alt="./gradlew test - command output" width="500"/>

## Add a new task of type Copy, to be used to make a backup of the source code of the application 
To add a new task **"runBackupRoutine"**, inside your build.gradle file you'll have to add the following code:

<img src="Images/03_01.PNG" alt="runBackupRoutine - task source code" width="500"/>

After adding the code to the build.gradle file, execute the command **./gradlew task runBackupRoutine**. The output should look like this:

<img src="Images/03_02.PNG" alt="./gradlew task runBackupRoutine - command output" width="500"/>

The runBackupRoutine task will create a new folder named **backup** inside the **build** directory.
<img src="Images/03_03.PNG" alt="folder backup" width="500"/>

## Add a new task of type Zip, to be used to make an archive (i.e .zip) of the backup of the application
To add a new task **"zipBackup"**, inside your build.gradle file you'll have to add the following code:

<img src="Images/04_01.PNG" alt="zipBackup - task source code" width="500"/>

After adding the code to the build.gradle file, execute the command **./gradlew task zipBackup**. The output should look like this:

<img src="Images/04_02.PNG" alt="./gradlew task zipBackup - command output" width="500"/>

The zipBackup task will create a new folder named **backup-zipped** inside the **build** directory.
<img src="Images/04_03.PNG" alt="folder backup-zipped" width="500"/>

## Explanation of how the Gradle Wrapper and the JDK Toolchain ensure the correct versions of the Gradle and the JDK are used without requiring manual installation
When you run the command **./gradlew -q javaToolchain** in the terminal you'll get a similar result to the one in the image below.

<img src="Images/05.PNG" alt="./gradlew -q javaToolchain command result" width="500"/>

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
``bash
git tag -a ca2-part1 d12d44b #  Create the tag "ca2-part1" and add that same tag to the d12d44b commit.
git push origin ca2-part1 #  Push the tag "ca2-part1" to the remote repository "COGSI2526_1250503_1250506_1250545".
```

# Self-Evaluation
```bash
Daniel (1250503) - 80%
Diogo (1250506) - 80%
Pedro (1250545) - 100%
```
