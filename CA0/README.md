# Spring PetClinic – NIF Column Extension

This project extends the **Spring PetClinic** application by adding a new column **NIF** to the `Owner` table.  
All changes are stored in the `CA0` directory for clarity.

---

## Repository Setup

```bash
git init
git remote add origin https://github.com/pchu22/COGSI2526_1250503_1250506_1250545.git
git branch -M main
git pull --rebase origin main   # fetch README.md from remote
git add .
git commit -m "Upload of spring-petclinic code"
git push -u origin main
```
---

## Creating CA0 Directory

```bash
mkdir CA0
```

---

## Modified Files

### Java Classes
```bash
cp /mnt/c/MCS/COGSI/spring-framework-petclinic/src/main/java/org/springframework/samples/petclinic/model/Owner.java  /mnt/c/MCS/COGSI/CA0/
cp /mnt/c/MCS/COGSI/spring-framework-petclinic/src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcOwnerRepositoryImpl.java  /mnt/c/MCS/COGSI/CA0/
cp /mnt/c/MCS/COGSI/spring-framework-petclinic/src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaOwnerRepositoryImpl.java  /mnt/c/MCS/COGSI/CA0/
cp /mnt/c/MCS/COGSI/spring-framework-petclinic/src/main/java/org/springframework/samples/petclinic/repository/OwnerRepository.java  /mnt/c/MCS/COGSI/CA0/
```

### Database Scripts
```bash
mkdir h2
mkdir hsqldb
mkdir mysql
mkdir postgresql
cp /mnt/c/MCS/COGSI/spring-framework-petclinic/src/main/resources/db/h2/data.sql  /mnt/c/MCS/COGSI/CA0/h2
cp /mnt/c/MCS/COGSI/spring-framework-petclinic/src/main/resources/db/h2/schema.sql  /mnt/c/MCS/COGSI/CA0/h2
cp /mnt/c/MCS/COGSI/spring-framework-petclinic/src/main/resources/db/hsqldb/data.sql  /mnt/c/MCS/COGSI/CA0/hsqldb
cp /mnt/c/MCS/COGSI/spring-framework-petclinic/src/main/resources/db/hsqldb/schema.sql  /mnt/c/MCS/COGSI/CA0/hsqldb
cp /mnt/c/MCS/COGSI/spring-framework-petclinic/src/main/resources/db/mysql/data.sql  /mnt/c/MCS/COGSI/CA0/mysql
cp /mnt/c/MCS/COGSI/spring-framework-petclinic/src/main/resources/db/mysql/schema.sql  /mnt/c/MCS/COGSI/CA0/mysql
cp /mnt/c/MCS/COGSI/spring-framework-petclinic/src/main/resources/db/postgresql/data.sql  /mnt/c/MCS/COGSI/CA0/postgresql
cp /mnt/c/MCS/COGSI/spring-framework-petclinic/src/main/resources/db/postgresql/schema.sql  /mnt/c/MCS/COGSI/CA0/postgresql
```

### JSP Views
```bash
cp /mnt/c/MCS/COGSI/spring-framework-petclinic/src/main/webapp/WEB-INF/jsp/owners/createOrUpdateOwnerForm.jsp  /mnt/c/MCS/COGSI/CA0/
cp /mnt/c/MCS/COGSI/spring-framework-petclinic/src/main/webapp/WEB-INF/jsp/owners/findOwners.jsp  /mnt/c/MCS/COGSI/CA0/
cp /mnt/c/MCS/COGSI/spring-framework-petclinic/src/main/webapp/WEB-INF/jsp/owners/ownerDetails.jsp  /mnt/c/MCS/COGSI/CA0/
cp /mnt/c/MCS/COGSI/spring-framework-petclinic/src/main/webapp/WEB-INF/jsp/owners/ownersList.jsp  /mnt/c/MCS/COGSI/CA0/
```

### Tests
```bash
cp /mnt/c/MCS/COGSI/spring-framework-petclinic/src/test/java/org/springframework/samples/petclinic/service/AbstractClinicServiceTests.java  /mnt/c/MCS/COGSI/CA0/
cp /mnt/c/MCS/COGSI/spring-framework-petclinic/src/test/java/org/springframework/samples/petclinic/web/OwnerControllerTests.java  /mnt/c/MCS/COGSI/CA0/
```

## Commit & Push Changes
```bash
git add .
git commit -m "CA0 -> spring-framework-petclinic code changed to accomodate Owner-NIF; All files copied to CA0 directory"
git push -u origin main
```

## CA0 Directory Structure
```
CA0/
├── Owner.java
├── OwnerRepository.java
├── JdbcOwnerRepositoryImpl.java
├── JpaOwnerRepositoryImpl.java
├── h2/
│   ├── data.sql
│   └── schema.sql
├── hsqldb/
│   ├── data.sql
│   └── schema.sql
├── mysql/
│   ├── data.sql
│   └── schema.sql
├── postgresql/
│   ├── data.sql
│   └── schema.sql
├── createOrUpdateOwnerForm.jsp
├── findOwners.jsp
├── ownerDetails.jsp
├── ownersList.jsp
├── AbstractClinicServiceTests.java
└── OwnerControllerTests.java
```

