After the repository has been created we executed the following commands:
git init - Initiates the repository
git remote add origin https://github.com/pchu22/COGSI2526_1250503_1250506_1250545.git - 
git branch -M main
git pull --rebase origin main - Because we had a README.md file that was not in the directory
git add . - to add all the files to be commited 
git commit -m "" - to commit the files to the repository
git push -u origin main - to send the files to the repository

Then, we procedded with the creation of a new directory "CA0" in the root of the project. In order to create the new
directory, the following command was executed:

mkdir CA0

We proceeded to make all the necessary changes to the spring-petclinic code in order to accomodate a new collumn
(NIF) in the table "Owner"
After all the files have been changed and tested we executed the following commands:

cp /mnt/c/MCS/COGSI/spring-framework-petclinic/src/main/java/org/springframework/samples/petclinic/model/Owner.java  /mnt/c/MCS/COGSI/CA0/
cp /mnt/c/MCS/COGSI/spring-framework-petclinic/src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcOwnerRepositoryImpl.java  /mnt/c/MCS/COGSI/CA0/
cp /mnt/c/MCS/COGSI/spring-framework-petclinic/src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaOwnerRepositoryImpl.java  /mnt/c/MCS/COGSI/CA0/
cp /mnt/c/MCS/COGSI/spring-framework-petclinic/src/main/java/org/springframework/samples/petclinic/repository/OwnerRepository.java  /mnt/c/MCS/COGSI/CA0/

*CREATE DIRECTORIES FOR EACH DB TECHNOLOGY AND COPYING THE DATA AND SCHEMAS TO THE CORRECT DESTINATION DIRECTORY*
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

*COPYING .jsp FILES to CA0 DIRECTORY*
cp /mnt/c/MCS/COGSI/spring-framework-petclinic/src/main/webapp/WEB-INF/jsp/owners/createOrUpdateOwnerForm.jsp  /mnt/c/MCS/COGSI/CA0/
cp /mnt/c/MCS/COGSI/spring-framework-petclinic/src/main/webapp/WEB-INF/jsp/owners/findOwners.jsp  /mnt/c/MCS/COGSI/CA0/
cp /mnt/c/MCS/COGSI/spring-framework-petclinic/src/main/webapp/WEB-INF/jsp/owners/ownerDetails.jsp  /mnt/c/MCS/COGSI/CA0/
cp /mnt/c/MCS/COGSI/spring-framework-petclinic/src/main/webapp/WEB-INF/jsp/owners/ownersList.jsp  /mnt/c/MCS/COGSI/CA0/

*COPYING TESTS TO CA0 DIRECTORY*
cp /mnt/c/MCS/COGSI/spring-framework-petclinic/src/test/java/org/springframework/samples/petclinic/service/AbstractClinicServiceTests.java  /mnt/c/MCS/COGSI/CA0/
cp /mnt/c/MCS/COGSI/spring-framework-petclinic/src/test/java/org/springframework/samples/petclinic/web/OwnerControllerTests.java  /mnt/c/MCS/COGSI/CA0/

