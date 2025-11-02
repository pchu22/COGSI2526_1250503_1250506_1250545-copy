# CA3
This class assignment consists of creating and managing virtual machines (VMs) using Vagrant.

Vagrant is a tool for building and managing VM environments in a single workflow. With an **easy-to-use** workflow and
**focus on automation**, Vagrant lowers development environment setup time and increases production parity.

# CA3 - Virtualization (Part I)
The initial step to begin utilizing Vagrant, is to have Vagrant installed. Visit the URL 
`https://developer.hashicorp.com/vagrant/install`, and download the appropriate executable for your system 
(in this instance was the Windows `AMD64` binary file). 

## Creating VMs using Vagrant
Once you have downloaded and installed Vagrant, navigate to the project directory you intend to work in. Because we will 
create a VM for each application we utilized in the previous class assignment, we ran the following commands 
to access the intended directories: 
- `cd ../COGSI2526_1250503_1250506_1250545/CA2/PART-I/gradle_basic_demo` 
- `cd ../COGSI2526_1250503_1250506_1250545/CA2/PART-II/gradle-tut-rest`

The command `Vagrant init bento\ubuntu-20.04` was executed in both directories to generate the `Vagrantfile` utilized
for creating and configuring the VMs for each application. We choose bento's box due to its excellent compatibility with
various cpu architectures and different providers.

<img src="PART-I/Images/01_03.PNG" alt="bento's Ubuntu-20.04 compatible providers"/>

Here is the execution of the command discussed earlier along with the resulting output.

<img src="PART-I/Images/01_01.PNG" alt="Vagrant init in gradle_basic_demo application" width="700"/>
<img src="PART-I/Images/01_02.PNG" alt="Vagrant init in gradle-tut-rest application" width="700"/>

### Automate the installation of all the dependencies of the project (e.g Git, JDK, etc...)
Vagrant automate tasks inside the VMs, such as installing packages, updating existing packages, running 
services, etc... To accomplish that, you can either develop an inline (or external) shell script or utilize different 
tools like Ansible, Puppet, Chef, Docker, and so on...

To streamline the installation of the required dependencies for the project, within the `Vagrantfile` develop a shell 
scrip similar to the following:

<img src="PART-I/Images/02.PNG" alt="Inline SHELL Script to automate the installation of project dependencies"/>

The script mentioned above is a basic shell script that runs `echo` and `install` commands upon the VM provisioning
and verify the version of the installed packages - In this particular case, it solely verifies the version of `java` and
`git`.

## Clone a private repository inside a VM
Cloning a private repository within a VM created with Vagrant poses challenges on Windows since the standard SSH Agent 
cannot be forward keys between the host and guest machine even with `config.ssh.forward_agent = true`. To address that
issue you can utilize **Pageant** and **PuTTYgen**.

**Pageant** is a PuTTY authentication agent that holds SSH private keys in memory, enabling seamless and secure 
authentication when connecting to servers. It eliminates the need to manually specify keys for each connection or 
repeatedly enter passphrases for passphrase-protected keys, streamlining the SSH login process.

**PuTTY Key Generator**, commonly known as **PuTTYgen**, is a tool used to generate pairs of public and private SSH keys 
for secure network communication. PuTTYgen supports generating various key types, including RSA, DSA, ECDSA, and EdDSA 
(including Ed25519), which are used for secure data transmission and digital signatures.

This solution significantly eases the management of SSH keys comparing to how challenging it could be. You'll begin by 
generating a pair of public and private keys that will allow you to easily access your GitHub from within the VMs. We 
executed the command `ssh-keygen -t ed25519 -C "dev.pedrocarreiras@gmail.com"`.
- The **-t** flag is utilized to select the encryption algorithm for our key. We chose to utilize `ed25519` since it is 
the recommended standard SSH key encryption recommended by GitHub for Git operations.
- The **-C** flag is utilized to specify one of the email addresses you have linked to your GitHub account.

Upon successfully executing the command, your result should resemble the example shown below:

<img src="PART-I/Images/03_01.PNG" alt="Creation of Private/Public Key pair to access GitHub from inside the VMs"/>

Once you have generated the key pair, you must include the **public key** in your GitHub account under 
`Settings > SSH and GPG Keys`, then click on the **green** button located in the **top right** labeled **"New SSH Key"**
. Inside you must assign a name to your key (**Title**), keep **Key Type** as **Authentication Key** and enter the key 
value under the section **key**. Refer to the illustration provided below as a guideline:

<img src="PART-I/Images/03_02.PNG" alt="Adding the public key to GitHub"/>

Once the public key is added to GitHub, the **private key** must be added to the local SSH Agent. To achieve this, 
execute the command `ssh-add ~/.ssh/id_ed25519` and then you'll be prompted to enter the passphrase you previously 
defined when creating the public and private key pair. Once you enter the passphrase, the resulting output should 
resemble the example provided below:

<img src="PART-I/Images/03_03.PNG" alt="Adding the private key to the local SSH Agent"/>

Run the command `ssh-add -l` to verify that the key has been added to your SSH Agent successfully. If the output 
resembles the one shown below, it indicates that everything has been executed properly.

<img src="PART-I/Images/03_04.PNG" alt="Confirming the key was added to the local SSH Agent"/>

**NOTE**: In the image above, there are two keys. The initial key was the first trial we conducted when we initially 
attempted to configure Pageant. Unfortunately, that attempt didn't proceed as anticipated, and we were required to start 
over completely.

Moreover, as you're utilizing Pageant as the SSH service provider, you must also include it to Pageant via its 
Graphical User Interface (GUI), but first, you must convert your private key file into a `.ppk` file. To accomplish 
this, launch PuTTYgen and load your key by clicking the "Load" button. Your PuTTY Key Generator should resemble the 
following:

<img src="PART-I/Images/03_05.PNG" alt="Converting the key to .ppk using PuTTYgen"/>

Once you've converted the private key into a `.ppk` file, you'll need to load it into the SSH Provider. To accomplish 
this, launch Pageant, then click the button in the bottom left corner labeled "Add Key". Upon clicking that button, 
file explorer will appear, and you need to navigate to `~/.ssh` directory (or the location where you saved your private 
key), and choose the key you generated for SSH access to GitHub. Once you've completed that, your Pageant should 
resemble the image shown below:

<img src="PART-I/Images/03_06.PNG" alt="Adding the .ppk key to Pageant"/>

Once you've completed all the steps outlined above, you'll be able to clone from your private repository. However, 
before proceeding, let's conduct some tests to double-check that everything went according the plan.
Begin by executing the command `vagrant ssh` to access the VM. After entering the VM execute the command `ssh-add -l`. 
If the result is identical to what you obtained when you executed the command in the host machine, indicating that 
Pageant  is successfully forwarding the keys. Locate an instance of the result shown below:

<img src="PART-I/Images/03_07.PNG" alt="Output of the command ssh-add -l inside the guest machine"/>

Execute the command `ssh -T git@github.com` on both the host and guest machines. This command is utilized to establish a 
connection between your machine and GitHub using the SSH key you provided previously. If you're effectively 
communicating with GitHub, your output should resemble the example provided below:

<img src="PART-I/Images/03_08.PNG" alt="Output of the command ssh -T git@github.com in the host machine"/>
<img src="PART-I/Images/03_09.PNG" alt="Output of the command ssh -T git@github.com inside the guest machine"/>

**NOTE**: On the **host machine**, you'll be prompted to enter the passphrase you previously configured for the key 
pair.

You can clone your repository to the VM, but instead of using the repository URL for a simple git clone, run the command
`git clone git@github.com:username/repository_name` to successfully clone it for the guest machine. By doing this, you 
ensure that you are cloning the repository using the SSH keys that had been generated earlier. We executed the command 
`git clone git@github.com:pchu22/COGSI2526_1250503_1250506_1250545` to clone our repository demonstrated below:

<img src="PART-I/Images/03_10.PNG" alt="Cloning our repository using the command git@github.com:pchu22/COGSI2526_1250503_1250506_1250545"/>

## Interacting with both applications from the host machine
It was intended to interact with the guest machines through the host machine. 
- For the **Simple Chat Application**, the **server was meant to run inside the VM** while the **clients were to run on the 
host machine**.
- For the **Spring Boot Rest Application**, the **web app was intended to operate within the VM** while it was expected 
to access it via the browser of the host machine.

### Simple Chat Application
To have the server operating in the VM and interact with clients directly on the host machine, it is essential to fo
rward the port `59001` from the guest to the client (we opted for the port `59001` as well), and to set up a`private 
network`, enabling **host-only** access to the machine through a designated IP address. To accomplish this, incorporate 
the following lines of code into your `Vagrantfile`:
- config.vm.network "forwarded_port", guest: 59001, host: 59001
- config.vm.network "private_network", ip: "192.168.33.10"

When operating the server within the VM and the clients on your machine, you should achieve a similar result to the one 
displayed below:

<img src="PART-I/Images/04_01.PNG" alt="Simple Chat App running in both host and guest machine"/>

### Spring Boot Rest Application
To run the Spring Boot Rest Application on the VM and directly interact with it from the host machine's web browser, it 
is essential to forward the port `8080` from the guest to the client (we opted for the port `8080` as well) and to set 
up a `private network`, enabling **host-only** access to the machine with a designated IP address. To accomplish this,
add the subsequent lines of code into your `Vagrantfile`:
- config.vm.network "forwarded_port", guest: 8080, host: 8080
- config.vm.network "private_network", ip: "192.168.33.11"

When executing the spring boot application in the VM and attempting to reach the service your machine's browser, you 
should see a result akin to the one shown below:

<img src="PART-I/Images/04_02.PNG" alt="Spring Boot Application running in both host and guest machine"/>

## Automation the cloning, building, and starting of applications
Automation plays a significant role in this class assignment. Vagrant allows to automate tasks within VMs through shell 
scripts (either inline or external), Ansible, Chef, Puppet, and more...
In this class assignment, we utilized inline shell scripts to automate tasks like updating installed packages, cloning 
and retrieving data from an existing repository, modifying permissions and starting services for both the Simple Chat 
Application and the Spring Boot Rest Application.

### Simple Chat Application
To automate all the previously mentioned tasks in the Simple Chat Application VM, we developed the following inline 
script:

```shell
APP_ROOT_DIR=COGSI2526_1250503_1250506_1250545
APP_DIR=CA2/PART-I/gradle_basic_demo/

GH_USERNAME=pchu22
echo "GitHub username: $GH_USERNAME"

GH_REPO=COGSI2526_1250503_1250506_1250545
echo "GitHub repository: $GH_REPO"

REPO_URL=git@github.com:$GH_USERNAME/$GH_REPO

echo "============================="
echo " Updating system packages..."
echo "============================="
sudo apt-get update -y

echo "============================="
echo " Installing Git..."
echo "============================="
sudo apt-get install git -y

echo "============================="
echo " Installing JDK..."
echo "============================="
sudo apt-get install openjdk-17-jdk -y

echo "============================="
echo " Installation Complete..."
echo "============================="
java -version
git --version

echo "============================="
echo " Cloning data from $REPO_URL..."
echo "============================="
if [ ! -d "$APP_ROOT_DIR/.git" ]; then
  echo "-> Changing .ssh directory owner and permissions..."
  sudo chown vagrant:vagrant /home/vagrant/.ssh
  sudo chmod 700 /home/vagrant/.ssh
  sudo chown vagrant:vagrant /home/vagrant/.ssh
  sudo chmod 600 /home/vagrant/.ssh/id_ed25519
  git clone $REPO_URL
else
  echo "-> Repository already exists. Skipping clone..."
fi

echo "============================="
echo " Pulling data from the repository..."
echo "============================="
cd $APP_ROOT_DIR
echo "-> Inside directory $APP_ROOT_DIR..."
echo "-> Switching to main branch..."
git switch main
git pull $REPO_URL

echo "-> Changing $APP_ROOT_DIR owner to vagrant..."
sudo chown -R vagrant:vagrant .

echo "============================="
echo " Starting Server..."
echo "============================="
cd $APP_DIR
echo "-> Inside directory $APP_ROOT_DIR/$APP_DIR..."
echo "-> Changing gradlew permissions..."
sudo chmod 744 gradlew gradlew.bat
echo "-> Running Server..."
./gradlew runServer
```

To enhance comprehension of the script, it will be explained in the following paragraphs.

Begin the script by executing update and install commands to set up the previously installed packages, and install 
packages such as `git` and `jdk`, then verify the installed versions of git and jdk using the `--version` flag. Next, 
within an if/else loop, verify whether the repository has been previously cloned. If it has already been cloned already, 
show some form of information (we opted for the message `-> Repository already exists. Skipping clone...`). If the 
repository does not exist on the guest machine, begin by assigning the ownership of the `.ssh` directory to the user 
`vagrant` granting read, write and execute permission only to the owner (**700**). Perform the same action for the key 
imported through the provision file - (but give the **600** permission `rw-------`). This was configured within the 
`Vagrantfile` with the following line of code: `config.vm.provision "file", source: "C:/Users/pmdc2/.ssh/id_ed25519", 
destination: "/home/vagrant/.ssh/id_ed25519"`. Finally, clone the repository utilizing the SSH keys that were generated 
earlier. 

Next, retrieve the data from the repository - We chose this method because if the service has been updated, we switch to 
the main branch and pull the latest version of the service onto the VM.

Once you've pulled the latest version of the service, change the application owner to vagrant and navigate through the 
directories until you reach the project's directory. Next, modify the permissions of the `gradlew` and `gradlew.bat` 
files to `744 (rwxr-xr-x)`. Finish by executing the application with the command `./gradlew runServer` to carry out the 
`runServer` task.

### Spring Boot Rest Application
To automate all the previously mentioned tasks in the Spring Boot Rest Application VM, we developed the following inline
script:

```shell
APP_ROOT_DIR=COGSI2526_1250503_1250506_1250545
APP_DIR=CA2/PART-II/gradle-tut-rest/

GH_USERNAME=pchu22
echo "GitHub username: $GH_USERNAME"

GH_REPO=COGSI2526_1250503_1250506_1250545
echo "GitHub repository: $GH_REPO"

REPO_URL=git@github.com:$GH_USERNAME/$GH_REPO

echo "============================="
echo " Updating system packages..."
echo "============================="
sudo apt-get update -y

echo "============================="
echo " Installing Git..."
echo "============================="
sudo apt-get install git -y

echo "============================="
echo " Installing JDK..."
echo "============================="
sudo apt-get install openjdk-17-jdk -y

echo "============================="
echo " Installation Complete..."
echo "============================="
java -version
git --version

echo "============================="
echo " Cloning data from $REPO_URL..."
echo "============================="
if [ ! -d "$APP_ROOT_DIR/.git" ]; then
  echo "-> Changing .ssh directory owner and permissions..."
  sudo chown vagrant:vagrant /home/vagrant/.ssh
  sudo chmod 700 /home/vagrant/.ssh
  sudo chown vagrant:vagrant /home/vagrant/.ssh
  sudo chmod 600 /home/vagrant/.ssh/id_ed25519
  git clone $REPO_URL
else
  echo "-> Repository already exists. Skipping clone..."
fi

echo "============================="
echo " Pulling data from the repository..."
echo "============================="
cd $APP_ROOT_DIR
echo "-> Inside directory $APP_ROOT_DIR..."
echo "-> Switching to main branch..."
git switch main
git pull $REPO_URL

echo "-> Changing $APP_ROOT_DIR owner to vagrant..."
sudo chown -R vagrant:vagrant .

echo "============================="
echo " Starting Server..."
echo "============================="
cd $APP_DIR
echo "-> Inside directory $APP_ROOT_DIR/$APP_DIR..."
echo "-> Changing gradlew permissions..."
sudo chmod 744 gradlew gradlew.bat
echo "-> Running Server..."
./gradlew bootRun
```

To enhance comprehension of the script, it will be explained in the following paragraphs.

Begin the script by executing update and install commands to set up the previously installed packages, and install
packages such as `git` and `jdk`, then verify the installed versions of git and jdk using the `--version` flag. Next,
within an if/else loop, verify whether the repository has been previously cloned. If it has already been cloned already,
show some form of information (we opted for the message `-> Repository already exists. Skipping clone...`). If the
repository does not exist on the guest machine, begin by assigning the ownership of the `.ssh` directory to the user
`vagrant` granting read, write and execute permission only to the owner (**700**). Perform the same action for the key
imported through the provision file (but give the **600** permission `rw-------`) - This was configured within the 
`Vagrantfile` with the following line of code: `config.vm.provision "file", source: "C:/Users/pmdc2/.ssh/id_ed25519", 
destination: "/home/vagrant/.ssh/id_ed25519"`. Finally, clone the repository utilizing the SSH keys that were generated 
earlier.

Next, retrieve the data from the repository - We chose this method because if the service has been updated, we switch to
the main branch and pull the latest version of the service onto the VM.

Once you've pulled the latest version of the service, change the application owner to vagrant and navigate through the
directories until you reach the project's directory. Next, modify the permissions of the `gradlew` and `gradlew.bat`
files to `744 (rwxr-xr-x)`. Finish by executing the application with the command `./gradlew bootRun`.

## Ensuring the H2 database in the VM stores the data on disk and retains it across restarts

To make sure the database stores the data on disk and retain it through restarts rather than holding it in memory, 
create a new directory within `src > main` named `resources` and within that directory create a file named 
`applciation.properties`. This file will handle the definition of different configuration settings that customize the 
application's behavior without needing code recompilation. In this file, include the following lines of code:

```properties
spring.datasource.url=jdbc:h2:file:./CA2-Part2/PayrollDB
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.hibernate.ddl-auto=update
```

In your `Vagrantfile`, incorporate a code similar to the following to guarantee that you share the directory where the 
database information will be stored/generated: 
`config.vm.synced_folder "C:/MCS/COGSI/CA2/PART-II/gradle-tut-rest/CA2-Part2", 
"/home/vagrant/COGSI2526_1250503_1250506_1250545/CA2/PART-II/gradle-tut-rest/CA2-Part2"`

## Add a new tag, to mark commit bab1b55, as last of the first part of CA3
To mark the completion of the first part of the CA3, create the tag `ca3-part1` using the command 
`git tag -a ca3-part1 bab1b55`. The `-a` flag stages the tag to the `bab1b55` commit. To push the tag to the remote 
repository `COGSI2526_1250503_1250506_1250545`, the command `git push origin ca3-part1` was executed.

# CA3 - Virtualization (Part II)
Vagrant is able to define and control multiple guest machines per `Vagrantfile`. This is known as a "**multi-machine**" 
environment. These machines are generally able to work together or are somehow associated with each other. 

In this class assignment you're going to develop a multi-machine `Vagrantfile` in order to accurately separate a web and 
database server. This setup will allow you to simulate a real-world scenario where the application and database are 
hosted on separate servers, facilitating better understanding and management of inter-service communication

## H2 Database Set Up
By default, Spring Boot configures the application to connect to an in-memory store with the **username** `sa` and an 
**empty password**. This setup is ideal for quick testing since the data is lost once the application stops.

### Change the H2 Database to Run in Server Mode
To ensure your data to persist between runs or to connect to the database from external tools, you can run H2 in Server 
Mode. In server mode, an instance of H2 database engine operates as the server in a separate process, while your Spring 
Boot Rest Application connects as a client via JDBC (over TCP).

**Advantages of Server Mode**:
- Data persists beyond application restarts.
- Multiple applications or tools can connect concurrently.
- Simplified debugging via H2 Console or SQL clients.
- Behavior that is more similar to production databases (connection pooling, multi-session).

To run your H2 database instance in server mode, your `application.properties` (or `application.yaml`) file should be 
formatted to appear as: 

```properties
spring.datasource.url=jdbc:h2:tcp://192.168.33.11:9092/PayrollDB
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

- `jdbc:h2:tcp://192.168.33.11:9092/PayrollDB`: Connects to an H2 database named PayrollDB using TCP.
- `spring.h2.console.enabled=true`: Together with `spring.h2.console.path=/h2-console` allows you to access the H2 web
console at `http://<your-ip>:8080/h2-console` (in our case the URL was `http://192.168.33.11:8080/h2-console`).
- `spring.jpa.hibernate.ddl-auto=update`: This setting instructs Hibernate to automatically update your database schema 
at each application startup, according to your JPA entity definitions (in our case `update` - That alters the schema 
without dropping existing data).

## Ensuring that your VMs are Allocated Sufficient Resources
Configuration is done using a `vm.provider` block. It takes as a single parameter the name of the provider being 
configured, next, an inner block with custom configuration options is exposed that can be used to configure that 
provider.

A VM lacking adequate resources like `CPU`, `memory`, or `disk space` suffers from **performance decline** because of 
multiple interrelated issues. When a VM lack sufficient CPU resources, it may experience **lag** and **delayed 
application response times**. Another significant reason for VM slowdowns is inadequate memory allocation. When a VM 
lacks sufficient RAM, the guest OS utilizes a **swap file on the disk**, **which is significantly slower** than physical 
memory, resulting in **poor performance**.

To ensure that the `web-app` and `db-server` VMs would deliver adequate performance, both were configured with the 
following specifications, respectively:

````bash
web.vm.provider "virtualbox" do |vb|
    vb.name = "web-app"
    vb.memory = 1024
    vb.cpus = 2
end
````

````bash
db.vm.provider "virtualbox" do |vb|
    vb.name = "db-server"
    vb.memory = 1024
    vb.cpus = 2
end
````

## Ensuring that you use Custom SSH Keys for a Secure Access
As SSH provides access to a host, misconfiguration or default setups may result in unauthorized access. Default 
configurations are generally secure, but it’s crucial to understand how to strengthen and automate SSH authentication. 
Creating custom SSH keys in Vagrant is advisable since:
- Enhances overall security.
- Provides flexibility for multi-VM configurations.
- Facilitates seamless automation in development and CI workflows.

Begin by generating your custom SSH key pair. Execute the command
`ssh-keygen -t rsa -b 2048 -f ~/.ssh/my_vagrant_key`. We executed the command
`ssh-keygen -t rsa -b 2048 -f ~/.ssh/vagrant_web-app_rsa` along with 
`ssh-keygen -t rsa -b 2048 -f ~/.ssh/vagrant_db_rsa`, to create the public and private key pairs for the web-app and db 
respectively. Examine the images provided below as an example:

<img src="PART-II/Images/03_01.PNG" alt="Generating web-app SSH key pair"/>
<img src="PART-II/Images/03_02.PNG" alt="Generating db SSH key pair"/>

As we utilized Pageant for this section of the class assignment as our SSH Agent, we once more needed to load the keys 
into PuTTYgen to convert the private keys into a `.ppk` file, as illustrated in them images below:

### web-app
<img src="PART-II/Images/03_04.PNG" alt="Converting vagrant_web-app_rsa key to a .ppk file"/>

Add the `vagrant_web-app_rsa` public key to the designated VM by including the following line of code in your 
`Vagrantfile`: `web.vm.provision "file", source: "~/.ssh/vagrant_web-app_rsa.pub", 
destination: "~/.ssh/authorized_keys"` to include the generated public key to the `authorized_hosts` file.

### db-server
<img src="PART-II/Images/03_05.PNG" alt="Converting vagrant_db_rsa key to a .ppk file"/>

Add the `vagrant_db_rsa` public key to the designated VM by including the following line of code in your `Vagrantfile`:
`db.vm.provision "file", source: "~/.ssh/vagrant_db_rsa.pub", destination: "~/.ssh/authorized_keys"` to include
the generated public key to the `authorized_hosts` file.

### Pageant
Once you have completed the steps outlined in the above sections, open Pageant, click the "Load" button, and choose the 
converted `.ppk` files to load the keys into it. Use the image below as a reference for how your Pageant ought to 
appear:

<img src="PART-II/Images/03_03.PNG" alt="Pageant with github, web-app and db private keys loaded"/>

## Ensuring `web-app` VM Waits for `db` VM’s H2 Service Before Starting Spring Boot Rest Application
To ensure the web-app functions correctly when the database is hosted on a different machine, you must verify that the 
database is operational before starting the application. to accomplish this, you need include code similar to the 
example provided below in the inline shell script.   

```shell
until nc -z 192.168.33.11 9092; do
  echo "-> DB not ready yet, waiting..."
  sleep 2
done
```

This code enables the `web-app` VM to scan its connectivity to the `db-server` VM machine (with the IP address 
`192.168.33.11` on port `9092`). It displays the message "**DB not ready yet, waiting...**" every two seconds until
a successful connection is established.

## Adding Firewall Rules to Restrict Access Only to the web-app VM
`Uncomplicated Firewall (ufw)`  is an intuitive command-line interface designed to make the administration of 
netfilter-based firewalls on Linux systems easier.

To protect the database and allow connections solely from the web-app and Vagrant it is essential to create 
firewall rules that restrict all incoming traffic while allowing all outgoing traffic. Next, enable allow SSH 
connections so Vagrant can connect to the VM, and allow TCP  connections exclusively for the web-app IP address 
on port `9092`.

Utilize the subsequent inline shell script code as a guideline to achieve what was stated earlier:

```shell
echo "============================="
echo " Adding FW Rules..."
echo "============================="
echo "-> Blocking incoming traffic..."
sudo ufw default deny incoming
echo "-> Allowing outgoing traffic..."
sudo ufw default allow outgoing
echo "-> Allowing SSH on port 22..."
sudo ufw allow 22
echo "-> Allowing TCP from 192.168.33.10 on por 9092..."
sudo ufw allow from 192.168.33.10 to any port 9092 proto tcp
```

## Final `Vagrantfile` explanation
This section provides you with an illustration of how your `Vagrantfile` should look like to successfully complete 
CA3. The same configuration will be elaborated upon, and an appropriate explanation of the inline shell scripts will be 
provided following the file itself. Use the example below as reference:

```bash
Vagrant.configure("2") do |config|
    config.vm.box = "bento/ubuntu-20.04"

    config.ssh.forward_agent = true
    config.ssh.insert_key = false

    config.vm.define "web-app" do |web|

        web.vm.provider "virtualbox" do |vb|
            vb.name = "web-app"
            vb.memory = 1024
            vb.cpus = 2
        end

        web.vm.network "private_network", ip: "192.168.33.10"
        web.vm.network "forwarded_port", guest: 8080, host: 8080

        web.ssh.private_key_path = ["~/.ssh/vagrant_web-app_rsa", "~/.vagrant.d/insecure_private_key"]

        web.vm.provision "file", source: "~/.ssh/vagrant_web-app_rsa.pub", destination: "~/.ssh/authorized_keys"
        web.vm.provision "file", source: "C:/Users/pmdc2/.ssh/id_ed25519", destination: "/home/vagrant/.ssh/id_ed25519"
        web.vm.provision "shell",
        privileged: false,
        inline: <<-SHELL
            APP_ROOT_DIR=COGSI2526_1250503_1250506_1250545
            APP_DIR=CA2/PART-II/gradle-tut-rest/

            GH_USERNAME=pchu22
            echo "GitHub username: $GH_USERNAME"

            GH_REPO=COGSI2526_1250503_1250506_1250545
            echo "GitHub repository: $GH_REPO"

            REPO_URL=git@github.com:$GH_USERNAME/$GH_REPO

            echo "============================="
            echo " Updating system packages..."
            echo "============================="
            sudo apt-get update -y

            echo "============================="
            echo " Installing Git..."
            echo "============================="
            sudo apt-get install git -y

            echo "============================="
            echo " Installing JDK..."
            echo "============================="
            sudo apt-get install openjdk-17-jdk -y

            echo "============================="
            echo " Installation Complete..."
            echo "============================="
            java -version
            git --version

            echo "============================="
            echo " Cloning data from $REPO_URL..."
            echo "============================="
            if [ ! -d "$APP_ROOT_DIR/.git" ]; then
                echo "-> Changing .ssh directory owner and permissions..."
                sudo chown vagrant:vagrant /home/vagrant/.ssh
                sudo chmod 700 /home/vagrant/.ssh
                sudo chown vagrant:vagrant /home/vagrant/.ssh
                sudo chmod 600 /home/vagrant/.ssh/id_ed25519
                git clone $REPO_URL
            else
                echo "-> Repository already exists. Skipping clone..."
            fi

            echo "============================="
            echo " Pulling data from the repository..."
            echo "============================="
            cd $APP_ROOT_DIR
            echo "-> Inside directory $APP_ROOT_DIR..."
            echo "-> Switching to main branch..."
            git switch main
            git pull $REPO_URL

            echo "-> Changing $APP_ROOT_DIR owner to vagrant..."
            sudo chown -R vagrant:vagrant .

            echo "============================="
            echo " Starting Server..."
            echo "============================="
            cd $APP_DIR
            echo "-> Inside directory $APP_ROOT_DIR/$APP_DIR..."
            echo "-> Changing gradlew permissions..."
            sudo chmod 744 gradlew gradlew.bat
            echo "-> Waiting for H2 database on 192.168.0.11:9092..."
            until nc -z 192.168.33.11 9092; do
                echo "-> DB not ready yet, waiting..."
                sleep 2
            done
            echo "-> Running Server..."
            ./gradlew bootRun
        SHELL
    end

    config.vm.define "db" do |db|

        db.vm.provider "virtualbox" do |vb|
            vb.name = "db-server"
            vb.memory = 1024
            vb.cpus = 2
        end

        db.vm.network "private_network", ip: "192.168.33.11"

        db.ssh.private_key_path = ["~/.ssh/vagrant_db_rsa", "~/.vagrant.d/insecure_private_key"]

        db.vm.provision "file", source: "~/.ssh/vagrant_db_rsa.pub", destination: "~/.ssh/authorized_keys"
        db.vm.provision "file", source: "C:/Users/pmdc2/.gradle/caches/modules-2/files-2.1/com.h2database/h2/2.3.232/4fcc05d966ccdb2812ae8b9a718f69226c0cf4e2/h2-2.3.232.jar", destination: "/home/vagrant/libs/h2-2.3.232.jar"
        db.vm.provision "shell", inline: <<-SHELL
            echo "============================="
            echo " Updating system packages..."
            echo "============================="
            sudo apt-get update -y

            echo "============================="
            echo " Installing Git..."
            echo "============================="
            sudo apt-get install git -y

            echo "============================="
            echo " Installing JDK..."
            echo "============================="
            sudo apt-get install openjdk-17-jdk -y

            echo "============================="
            echo " Installation Complete..."
            echo "============================="
            java -version
            git --version

            echo "============================="
            echo " Adding FW Rules..."
            echo "============================="
            echo "-> Blocking incoming traffic..."
            sudo ufw default deny incoming
            echo "-> Allowing outgoing traffic..."
            sudo ufw default allow outgoing
            echo "-> Allowing SSH on port 22..."
            sudo ufw allow 22
            echo "-> Allowing TCP from 192.168.33.10 on por 9092..."
            sudo ufw allow from 192.168.33.10 to any port 9092 proto tcp

            echo "============================="
            echo " Starting the DB..."
            echo "============================="
            mkdir -p /home/vagrant/logs/h2
            java -cp /home/vagrant/libs/h2-2.3.232.jar org.h2.tools.Server \
                -tcp -tcpAllowOthers -tcpPort 9092 -web -webAllowOthers > /home/vagrant/logs/h2/h2.log 2>&1 &
        SHELL
    end
end
```

Both machines are configured with **1GB** of RAM and **2 threads**, as you can see. Each machine is designated a name 
and a private network IP address - `web-app` with the IP address `192.168.33.10`, and `db-server` with the IP address 
`192.168.33.11`. Additionally, both systems have been globally configured with bento/ubuntu-20.04 OS and to forward the 
ssh-agent.

### web-app VM
Begin the script by executing update and install commands to set up the previously installed packages, and install
packages such as `git` and `jdk`, then verify the installed versions of git and jdk using the `--version` flag. Next,
within an if/else loop, verify whether the repository has been previously cloned. If it has already been cloned already,
show some form of information (we opted for the message `-> Repository already exists. Skipping clone...`). If the
repository does not exist on the guest machine, begin by assigning the ownership of the `.ssh` directory to the user
`vagrant` granting read, write and execute permission only to the owner (**700**). Perform the same action for the key
imported through the provision file (but give the **600** permission `rw-------`) - This was configured within the
`Vagrantfile` with the following line of code: `config.vm.provision "file", source: "C:/Users/pmdc2/.ssh/id_ed25519",
destination: "/home/vagrant/.ssh/id_ed25519"`. Finally, clone the repository utilizing the SSH keys that were generated
earlier.

Next, retrieve the data from the repository - We chose this method because if the service has been updated, we switch to
the main branch and pull the latest version of the service onto the VM.

Once you've pulled the latest version of the service, change the application owner to vagrant and navigate through the
directories until you reach the project's directory. Next, modify the permissions of the `gradlew` and `gradlew.bat`
files to `744 (rwxr-xr-x)`. Before starting the application, ensure that the database service is ready by running a loop 
check that continuously tests the connection to the database, pausing for 2 seconds between attempts until the service 
becomes available. Finish by executing the application with the command `./gradlew bootRun`.

## db-server VM
Begin the script by executing update and install commands to set up the previously installed packages, and install
packages such as `git` and `jdk`, then verify the installed versions of git and jdk using the `--version` flag. 

Next, create firewall rules to block all the incoming and allow all outgoing traffic. Establish two extra rules: one 
for allowing SSH connections to allow access to the `db-server` VM using the command `vagrant ssh db-server`, and 
another rule to allow TCP traffic from the web-app IP (`192.168.33.10`) on port `9092`.

Finally, create all the non-existing directories in the path `/home/vagrant/logs/h2` and execute the **h2-2.3.232.jar** 
file, which contains the H2 Database service that has been imported through provision file using the command 
`db.vm.provision "file", source: "C:/Users/pmdc2/.gradle/caches/modules-2/files-2.1/com.h2database/h2/2.3.232/4fcc05d966ccdb2812ae8b9a718f69226c0cf4e2/h2-2.3.232.jar", 
destination: "/home/vagrant/libs/h2-2.3.232.jar"`. 

## Add a new tag, to mark commit 647ac44, as last of the second part of CA3
To mark the completion of the second (and last) part of the CA3, create the tag `ca3-part2` using the command
`git tag -a ca3-part2 647ac44`. The `-a` flag stages the tag to the `647ac44` commit. To push the tag to the remote
repository `COGSI2526_1250503_1250506_1250545`, the command `git push origin ca3-part2` was executed.

## Branch management for the development of the CA3 solution 
To develop the current solution, we began by creating a new branch named `Vagrant`, next we proceeded by checking 
the existing branches, and ultimately switched to the newly created branch. Execute the subsequent commands to achieve 
a similar result to what we accomplished.
```bash
git branch Vagrant
git branch
git switch Vagrant
```

Once we completed all the CA3 tasks, we deleted the `Vagrant` branch both locally and remotely, using the following 
commands:
```bash
git branch -d Vagrant
git push -d origin Vagrant
```

# Alternative technologies to Vagrant (Docker not included)
## Multipass + cloud init
Multipass is a lightweight VM manager for Linux, Windows and macOS. It's designed for developers who want to spin up a 
fresh Ubuntu environment with a single command. It uses KVM on Linux, Hyper-V on Windows and QEMU on macOS to run 
virtual machines with minimal overhead. It can also use VirtualBox on Windows and macOS. Multipass will fetch Ubuntu 
images for you and keep them up to date.

Cloud-init is an open-source initialization tool designed to automate the setup and configuration of Linux-based cloud 
instances during their first boot. It acts as a standard method for early-stage initialization across major public cloud 
providers, private cloud infrastructure, and bare-metal installations.

Cloud-init integrates with Multipass to automate the setup of VMs. When launching a VM with Multipass, user-data can be 
passed using the `--cloud-init` flag followed by a YAML file containing the configuration. This file defines settings 
such as users, packages to install, SSH keys, and system configurations. Multipass validates the user-data configuration 
before starting the VM, ensuring it adheres to the cloud-config format. 

The upcoming sections will guide you on how to manage a multi-machine system utilizing Multipass and cloud init in 
conjunction. Utilize the following commands and `.yaml` files as a guide.

### web-app
Prior to configuring the `.yaml` file you're going to execute the following command in order to create the web-app 
VM with 2 CPU cores, 1 GB of RAM and Ubuntu 20.04 operating system.

```bash
multipass launch \
  --name web-app \
  --cpus 2 \
  --mem 1024M \
  --cloud-init web-app.yaml \
  ubuntu:20.04
```

**web-app.yaml**:
```yaml
network:
  version: 2
  ethernets:
    eth0:
      dhcp: no
      addresses: [192.168.33.10/24]
      nameservers: [8.8.8.8, 8.8.4.4]
 
packages:
  - git
  - openjdk-17.jdk
  - netcat
    
write-file:
  - path: /home/ubuntu/.ssh/id_ed25519
    permissions: '0600'
    owner: vagrant:vagrant
    content: |
      -----BEGIN OPENSSH PRIVATE KEY-----
      b3BlbnNzaC1rZXktdjEAAAAACmFlczI1Ni1jdHIAAAAGYmNyeXB0AAAAGAAAABDlDzkLw/
      WdIDuyDK6YmtjfAAAAGAAAAAEAAAAzAAAAC3NzaC1lZDI1NTE5AAAAIJbOpzFr2oU2Jbbu
      vw/Z7IiOaY+cOhLbUNm+5c2PCp9PAAAAoBGALJQpBOfAlgecU4O27kbZSueGXuiANz6bXR
      ufJg6aob33DCfjhM6Rk3j6BVNXVo4EzIHTtdHY+Hup/sCYw9YcSBN7ywt9F/LHTz1KZBki
      hnYXOc4XYyqRZhLwzcOJZUui6+OF3zIfuJlu2yXBZJRvtT9Ailqyuy+Bpapt0yOpfojlqR
      w/7qP6AK2r0BI97ljHNlg07E3fML0WQBu/i9o=
      -----END OPENSSH PRIVATE KEY-----

  - path: /home/ubuntu/.ssh/
    permissions: '0600'
    owner: vagrant:vagrant
    content: |
      ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCx7+La7D18vAUG/St7S3ApItstXSzGOB
      gK+EmdZpW9+yYFVbHPK25Mqn9dXtyC7JCmaCOKfKCQE+D2zRajLU48gccv9oHhRPydmjjY
      1dw0UYa7g059MBv+kyxURoPHMopGITPuXWK2XPtzaBLJj/VhQisSbTFNZXFKqDP+gfyhtm
      ksbOSK0RkGxTaL8vysR033f63Ryx8TnXrnHB384MyajSfqGoDGLsg7hQrca9IVqT0YIOAp
      uzXICkt42NkdAHhEB4yf0ALVpuGdunSyf+bKZIupOHruob4hmdkfsB4EfPedeMmdmujPVF
      7PGQE/bRqvktDa9A7Lw2pPPQhrkkLf pmdc2@DESKTOP-SDTA0AS
  
runcmd:
  - |
    APP_ROOT_DIR=COGSI2526_1250503_1250506_1250545
    APP_DIR=CA2/PART-II/gradle-tut-rest/

    GH_USERNAME=pchu22
    echo "GitHub username: $GH_USERNAME"

    GH_REPO=COGSI2526_1250503_1250506_1250545
    echo "GitHub repository: $GH_REPO"

    REPO_URL=git@github.com:$GH_USERNAME/$GH_REPO

    echo "============================="
    echo " Updating system packages..."
    echo "============================="
    sudo apt-get update -y

    echo "============================="
    echo " Cloning data from $REPO_URL..."
    echo "============================="
    if [ ! -d "$APP_ROOT_DIR/.git" ]; then
        echo "-> Changing .ssh directory owner and permissions..."
        sudo chown vagrant:vagrant /home/vagrant/.ssh
        sudo chmod 700 /home/vagrant/.ssh
        git clone $REPO_URL
    else
        echo "-> Repository already exists. Skipping clone..."
    fi

    echo "============================="
    echo " Pulling data from the repository..."
    echo "============================="
    cd $APP_ROOT_DIR
    echo "-> Inside directory $APP_ROOT_DIR..."
    echo "-> Switching to main branch..."
    git switch main
    git pull $REPO_URL

    echo "-> Changing $APP_ROOT_DIR owner to vagrant..."
    sudo chown -R vagrant:vagrant .

    echo "============================="
    echo " Starting Server..."
    echo "============================="
    cd $APP_DIR
    echo "-> Inside directory $APP_ROOT_DIR/$APP_DIR..."
    echo "-> Changing gradlew permissions..."
    sudo chmod 744 gradlew gradlew.bat
    echo "-> Waiting for H2 database on 192.168.0.11:9092..."
    until nc -z 192.168.33.11 9092; do
        echo "-> DB not ready yet, waiting..."
        sleep 2
    done
    echo "-> Running Server..."
    ./gradlew bootRun --args='--server.address=0.0.0.0 --server.port=8080'

```

To better understand how Multipass and cloud init work, we are going to explain the thought process behind developing 
the `web-app.yaml` file. We started by launching the Multipass instance with the previously described specifications to 
ensure a consistent and reproducible environment.

Next, we defined the network configuration inside the `network` snippet of the `.yaml` file, assigning a static IP 
address and DNS servers to the `web-app` VM. In the `packages` section, we listed the packages that should be 
automatically installed on the guest machine upon initialization. Under the `write-file` section, we included the 
private SSH key to access the remote GitHub repository (`COGSI2526_1250503_1250506_1250545`) and the custom private key 
used access the VM via SSH.

Finally, in the `runcmd` section, we executed a shell script similar to the one utilized in the web-app's `Vagrantfile`,
but without the permission-setting commands for the SSH key, as cloud-init already handles file permissions defined in 
`write_files`.

**NOTE**: Since Multipass does not natively support a `private_network` configuration like vagrant, we assigned the 
machine a **static IP address** using the network configuration. When executing the service with `./gradlew bootRun`, we 
passed the arguments `--args='--server.addres=0.0.0.0 --server.port=8080'` to ensure the application binds to all the 
network interfaces and listens on port 8080. This allows the service to be accessed from host machine using the VM’s 
static IP address on port 8080.

### db-server
Prior to configuring the `.yaml` file you're going to execute the following command in order to create the db-server
VM with 2 CPU cores, 1 GB of RAM and Ubuntu 20.04 operating system.

```bash
multipass launch \
  --name db-server \
  --cpus 2 \
  --mem 1024M \
  --cloud-init db-server.yaml \
  ubuntu:20.04
```

Run the subsequent command to upload the database `.jar` file to the db-server VM:

```bash
multipass transfer C:/Users/pmdc2/.gradle/caches/modules-2/files-2.1/com.h2database/h2/2.3.232/4fcc05d966ccdb2812ae8b9a718f69226c0cf4e2/h2-2.3.232.jar dn-server:/home/vagrant/libs/h2-2.3.232.jar
```

**bd-server.yaml**:
```yaml
network:
  version: 2
  ethernets:
    eth0:
      dhcp: no
      addresses: [192.168.33.11/24]
      nameservers: [8.8.8.8, 8.8.4.4]

packages:
  - netcat

write-file:
  - path: /home/ubuntu/.ssh/
    permissions: '0600'
    owner: vagrant:vagrant
    content: |
      ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDWntgUW6dpDiA7is3JOAUq/E3zy5ZVEt
      rnyNmKm4cHkav50ChXe/oHMC8DHduePhfo8gBA2VKoiWumPDX7VOCdDAMQKqnOATJggw74
      fHZ5JIx52ZaA/TuNNwhuSx5XGaIt+DpnzjPym3SG5YXWaSiFodUw5WU1AnOHx8avGlGadv
      dZzCjZ+RMNFHqkZda9nFroHB3B7ux0lnrx7OulQ2TOX/2L9EdeOMtW5YjL4HTe7LsgmkL8
      NHjDnWmyPzd4hNWH67xXGzu/acxwV1DbN8bZqBoSUeLvs7QANYqWrfVA8Rw9sz7ODuMCj0
      pnMNlSMg3Yfu8MgyBEIncJ475LcFSH pmdc2@DESKTOP-SDTA0AS
      
runcmd:
  - |
    echo "============================="
    echo " Updating system packages..."
    echo "============================="
    sudo apt-get update -y

    echo "============================="
    echo " Adding FW Rules..."
    echo "============================="
    echo "-> Blocking incoming traffic..."
    sudo ufw default deny incoming
    echo "-> Allowing outgoing traffic..."
    sudo ufw default allow outgoing
    echo "-> Allowing SSH on port 22..."
    sudo ufw allow 22
    echo "-> Allowing TCP from 192.168.33.10 on por 9092..."
    sudo ufw allow from 192.168.33.10 to any port 9092 proto tcp

    echo "============================="
    echo " Starting the DB..."
    echo "============================="
    mkdir -p /home/vagrant/logs/h2
    java -cp /home/vagrant/libs/h2-2.3.232.jar org.h2.tools.Server \
        -tcp -tcpAllowOthers -tcpPort 9092 -web -webAllowOthers > /home/vagrant/logs/h2/h2.log 2>&1 &
```

To better understand how Multipass and cloud init work, we are going to explain the thought process behind developing
the `db-server.yaml` file. We started by launching the Multipass instance with the previously described specifications 
to ensure a consistent and reproducible environment.

Next, we defined the network configuration inside the `network` snippet of the `.yaml` file, assigning a static IP
address and DNS servers to the `db-server` VM. In the `packages` section, we listed the package that should be
automatically installed on the guest machine upon initialization. Under the `write-file` section, we included the
the custom private key used access the VM via SSH.

Finally, in the `runcmd` section, we executed a shell script similar to the one utilized in the web-app's `Vagrantfile`,
but without the permission-setting commands for the SSH key, as cloud-init already handles file permissions defined in
`write_files`.

## Terraform 
Terraform is an infrastructure as code tool that lets you define both cloud and on-prem resources in human-readable 
configuration files that you can version, reuse, and share. You can then use a consistent workflow to provision and 
manage all of your infrastructure throughout its lifecycle. Terraform can manage low-level components like compute, 
storage, and networking resources, as well as high-level components like DNS entries and SaaS features.

Since Terraform is an Infrastructure orchestrator and not a full provisioning tool, it is best used in conjunction with 
a provisioning or image-building system such as **cloud init**, **packer** or **configuration management tools** 
(e.g, Ansible).

# Self-Evaluation
```bash
Daniel (1250503) - 80%
Diogo (1250506) - 80%
Pedro (1250545) - 100%
```
