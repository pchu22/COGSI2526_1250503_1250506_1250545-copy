# CA 1 - Version Control (Part 1)

This document provides step-by-step instructions on using Git for version control. It covers cloning, committing changes, tagging versions, reverting commits, and checking commit history.


##  Copy the code of Spring Petclinic application under the CA1 folder

```bash
git clone https://github.com/spring-petclinic/spring-framework-petclinic # Copies the code of spring petclinic application to the current directory (CA1).
git add . # Adds all changes to the staging area
git commit -m "CA1_1st-week: petclinic code copied to CA1; Tag added" # Commits staged changes with a descriptive message.
git push origin main # Push the changes to "main" branch of the repository "COGSI2526_1250503_1250506_1250545".
```

## Use tags to mark the versions of the application

```bash
git tag -a v1.1.0 c1100f2 # Create the tag "v1.1.0" and add that same tag to the c1100f2 commit.
git push origin v1.1.0 # Push the tag "v1.1.0" to the repository "COGSI2526_1250503_1250506_1250545"
```

## Add new field **professionalLicenseNumber** to the application, commit and push changes to GitHub, and tag the commit

```bash
**Change the source code of Spring Petclinic application**

git add . # Adds all changes to the staging area.
git commit -m "Professional License Number field added to Vet" # Commits staged changes with a descriptive message.
git push origin main # Push the changes to "main" branch of the repository "COGSI2526_1250503_1250506_1250545".
git tag -a v1.2.0 686a61b # Create the tag "v1.2.0" and add that same tag to the 686a61b commit.
git push origin v1.2.0 # Push the tag "v1.2.0" to the repository "COGSI2526_1250503_1250506_1250545"
```

## Check commit history of the repository

```bash
git log --oneline # Logs the commit history in a condensed format (each commit is displayed in a single line).
git log --graph # Logs a graphical commit tree.
```

## Revert the changes to a specific commit

```bash
git revert c1100f2 # Creates a new commit that undoes the changes from c1100f2.
git add . # Adds all changes to the staging area.
git commit -m "Reverting to commit c1100f2" # Commits staged changes with a descriptive message.
git push origin main # Push the changes to "main" branch of the repository "COGSI2526_1250503_1250506_1250545".

**Note**
git revert creates a new commit that undoes the changes introduced by a previous commit. Unlike git reset, it does not rewrite history. Instead, it adds a new commit that applies the inverse of the selected commit.

Key points:
- Only the files that were changed in the original commit will be touched.
- Files not changed by the original commit will not appear in the revert commit.
- This makes revert a surgical and safe way to undo changes in shared/public branches.
```

## Get repository default branch and check when was the last commit

```bash
git config --get init.deafaultBranch # Shows the default branch name.
git log --graph # Logs a graphical commit tree.
```

## Show how many contributors made commits to the repository and what commits they made

```bash
git shortlog # Summarizes commits grouped by author.
```

## Adding the tag "ca1-part1" to the last commit

```bash
git tag -a ca1-part1 c7a8d53 # Create the tag "ca1-part1" and add that same tag to the c7a8d53.
git push origin ca1-part1 # Push the tag "ca1-part1" to the repository "COGSI2526_1250503_1250506_1250545"
```

# CA1 - Version Control (Part II)

## Create a branch named email-field

```bash
git branch email-field # Create a new branch named "email-field".
git branch # Verify the branches and confirm which one is active.
git checkout email-field # Switch from current branch ("main") to the "email-field" branch.

**Change the source code of Spring Petclinic to support email-field in the Vet model**

git add . # Add all changes to the staging area.
git commit -m "Add email field support in the vet model" # Commits staged changes with a descriptive message.
git push origin email-field # Push the changes to "email-filed" branch of the repository "COGSI2526_1250503_1250506_1250545".
git tag -a v1.3.0 5175f8c # Create the tag "v1.3.0" and add that same tag to the 5175f8c commit.
git push origin v1.3.0 # Push the tag "v1.3.0" to the repository "COGSI2526_1250503_1250506_1250545".
```
## Create conflicting edits on the two branches

```bash
**Change source code of Spring Petclinic in email-field branch**

git add . # Add all changes to the staging area.
git commit -m "Change Owner.java to cause conflicts when merging" # Commits staged changes with a descriptive message.
git switch main # Switches from "email-field" branch to "main" branch.

**Change source code of Spring Petclinic in main branch**

git add . # Add all changes to the staging area.
git commit -m "Change Owner.java to cause conflicts when merging" # Commits staged changes with a descriptive message.
git merge email-field # Initiates the merge process of branch "email-field" into branch "main"

**After merge conflict appears, open conflicting files and resolve manually. Once resolved**

git add . # Add all changes to the staging area.
git commit -m "Fix Owner.java" # Commits staged changes with a descriptive message.
git push origin main # Push the changes to "main" branch of the repository "COGSI2526_1250503_1250506_1250545".
git branch -d email-field # Removes "email-field" from the local repository
```

## Indetification of remote branches and their respective local branches

```bash
main -> origin/main
email-field -> origin/email-field
```

## Add the tag "ca1-part2" to the last commit

```bash
git tag -a ca1-part2 dd87f41 # Create the tag "v1.3.0" and add that same tag to the 5175f8c commit.
git push origin ca1-part2 # Push the tag "v1.3.0" to the repository "COGSI2526_1250503_1250506_1250545".
```

# Alternative technologies that could be used to implement class assignment 1

## Mercurial

```bash
In order to manage software development projects, especially the Linux Kernel (even though the Linux Kernel project eventually selected Git), Matt Mackall developed Mercurial, a free and open-source distributed version control system, in 2005. Mercurial has been widely utilized by groups including Mozilla, Facebook, Google, and the W3C. Uses a decentralized architecture, it functions locally and independently of the network. Supports both HTTP and SSH protocols in order to access repositories and uniquely identifies revisions using SHA-1 hashes. Its main tool, hg, provides consistent and intuitive commands. It was mainly written in Python, with some performance-critical parts in C and a growing amount in Rust.
```

## Apache Subversion

```bash
Also known as SVN, is a open-source version management system, widely used by the open-source comunity and corporate sector. SVN created in C by CollabNet, Inc. in 2000 to replace the Concurrent Versions System, and later maintained by Apache Software Foundation. It lets developers maintain both recent and older versions of documents and uses a centralized architecture that prioritizs stability, simplicity, and data integrity. The Apache Software Foundation, FreeBSD, SourceForge, and, from 2006 to 2019, the GCC compiler project are some of the projects that have made use of Subversion.
```

## Concurrent Version System

```bash
Also know as CVS, is a version control system written in C and originally developed by Dick Grune in July 1986, extends its capabilities by adding repository-level change tracking and a client–server model for collaborative development. It uses delta compression to efficiently store multiple file versions. Operates through a centralized architecture, and allows multiple developers to work concurrently from the main repository. It excludes symbolic links for security reasons and manages related files as “modules” within its repository. CVS also supports branching for parallel development and can run user-defined scripts. Although later superseded by more modern systems, CVS laid the groundwork for many version control concepts still used today.
```

## Comparison of Git, Mercurial, SVN and CVS

### Speed

```bash
Git and Mercurial are generally considered the fastest version control systems, as they utilize a decentralized model and store data in the form of hashed values, allowing for faster data transfer and retrieval. SVN and CVS are both centralized systems and may not be as fast as Git and Mercurial in terms of data transfer and retrieval.
```

### Flexibility

```bash
Git and Mercurial are the most flexible systems, allowing developers to easily create multiple branches, experiment new features, and merge changes efficiently. The decentralized architecture allows each developer to work independently. SVN, while versatile, functions on a centralized structure that may make branching and merging more difficult and slower compared to Git and Mercurial. In contrast, CVS is rigid and seen as obsolete by today’s criteria, with limited branching and merging functions that result in increased conflicts.
```

### Security

```bash
Git and Mercurial both employ a decentralized approach, indicating that there is no central repository for the data. This complicates the process of losing or compromising data. SVN features robust security protocols, utilizing SSL/TLS encryption for data transmission and offering various options for authentication and authorization. CVS exhibits less robust security protocols than other version control systems since it lacks encryption for data transmission and offers few choices for authentication and authorization.
``` 

### Ease of Use

```bash
Git and Mercurial can be quite challenging for new users, as they employ a CLI and present a steep learning curve. SVN is more accessible to users since it features a GUI and is simpler for beginners. CVS is also straightforward, featuring a basic CLI and is easy for beginners.
```
