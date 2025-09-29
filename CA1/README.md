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
```

## Add new field **professionalLicenseNumber** to the application, commit and push changes to GitHub, and tag the commit

```bash
**Change the source code of Spring Petclinic application**

git add . # Adds all changes to the staging area.
git commit -m "Professional License Number field added to Vet" # Commits staged changes with a descriptive message.
git push origin main # Push the changes to "main" branch of the repository "COGSI2526_1250503_1250506_1250545".
git tag -a v1.2.0 686a61b # Create the tag "v1.2.0" and add that same tag to the 686a61b commit.
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
```
