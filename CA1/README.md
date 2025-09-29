## CA 1 - Version Control (Part 1)

This document provides step-by-step instructions on using Git for version control. It covers cloning, committing changes, tagging versions, reverting commits, and checking commit history.


##  Copy the code of Spring Petclinic application under the CA1 folder

```bash
git clone <repo-url> # Copies the code of spirng petclinic application to the current directory (CA1). **Note**: Replace <repo-url> for the actual repository url you want to clone the application from.
git add . # Adds all files to the stagin area
git commit -m "<message>" # Commit the changes to the staging area. **Note**: Replace <message> for the actual message you want to include when commiting the changes.
git push origin main # Push the changes to main branch of the repository
```

## Use tags to mark the versions of the application

```bash
git tag -a <tag-name> <hash> # Create the tag <tag-name> and add that same tag to the <hash> commit. **Note**: Replace <tag-name> for the actual tag name you want to add to the commit, and replace <hash> for the actual commits hash.
```

## Development of a new field (professionalLicenseNumber) to the application + commit and push the changes to GitHub + Add adition of a tag to the commit

```bash
git add . # Adds all files to the staging area
git commit -m "<message>" # This command was used to commit all changes to the stagin area
git push origin main # This command was used to push all the changes to main branch of the repository
git tag -a v1.2.0 <hash> # This command was used to create the tag "v1.2.0" and add it to the <hash> commit
```

## Check commit history of the repository using git log and testing it with multiple fomatting options

```bash
git log --oneline # This commad is used to log all the changes done to the repo, each one using only one line
git log --graph # This command display all the changes done to the repository with more information
```

## Reverting the changes to a specific commit

```bash
git revert <hash> 
git add .
git commit -m "<message>"
git push origin main
```

## Get repository default branch and check when was the last commit

```bash
git config --get init.deafultBranch # Shows the default branch name
git log --graph # Logs the information of all the commits
```

## Show how many contributors made commits to the repository and what commits they made

```bash
git shortlog # Shows all commits and what contributor made which commit
```

## Adding a tag to a specific commit

```bash
git tag -a ca1-part1 <hash> # Adds a tag to the <hash> commit
```
