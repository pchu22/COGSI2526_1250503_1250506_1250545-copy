# CA2 - Build Tools (Part I)

After downloading the gradle_basic_demo source code from **"https://github.com/lmpnogueira/gradle_basic_demo"**, we proceeded with the creation of a new branch, in order to solve the first part of class assignment II. To create and start operating in the new branch, the following commands were executed:

```bash
git branch Gradle-W1
git branch
git switch Gradle-W1
```

## Add a tag to mark the application version

```bash
git tag -a ca2-v1.1.0 <hash>
git push origin ca2-v1.1.0
```

## Add a runServer task to build.gradle

Start by creating the following task inside build.gradle.
<img src="Images/01_01.PNG" alt="runServer task source code"/>

Then, run the command **./gradlew task runServer**. The result should look like this:
<img src="Images/01_02.PNG" alt="runServer task running on terminal"/>

When a chatter joins the server, the terminal output should be something like this:
<img src="Images/01_03.PNG" alt="renServer Message after a chatter joins"/>
## 
## 
## 



# Self-Evaluation

```bash
Daniel (1250503) - 80%
Diogo (1250506) - 80%
Pedro (1250545) - 100%
```
