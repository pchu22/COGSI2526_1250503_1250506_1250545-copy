# CA4 - Configuration Management
The goal of this assignment is to **install Ansible** and use it to **provision the two VMs** (`web-app` and 
`db-server`) that were created in CA3. All playbooks should be **idempotent**, ensuring that running them multiple times 
maintains the desired system state without unintended changes.

In this assignment, you will:
- Use Ansible to **enforce complex password policies** on the VMs using **PAM** with `libpam-pwquality`.
- **Create user groups and accounts** on the VMs through Ansible.

By completing this assignment, you will gain hands-on experience in using Ansible for system configuration, security 
enforcement, and user management, while applying best practices for repeatable and maintainable infrastructure 
automation.

## Install Ansible
Ansible is an open-source automation tool designed for configuration management, application deployment, and IT 
orchestration. It enables **infrastructure as code**, allowing you to define and manage your IT environment 
programmatically. 

Unlike much automation tool, Ansible is an agentless. You only need to install it on a single machine, called the 
control node, which can manage multiple machines or devices (managed nodes) remotely using SSH, Powershell remoting, or 
other supported protocols. Ansible operate from a simple command-line interface and does not require daemons or 
databases.

### Control Node requirements
Your control node can be any UNIX-like machine with Python installed, including:
- Red Hat;
- Debian;
- Ubuntu;
- macOS;
- BSDs;
- Windows under a Windows Subsystem for Linux (WSL) distribution

**NOTE**: Windows without WSL is not supported as a control node.

### How Ansible Works
Ansible uses YAML-based playbooks to define automation workflows. These playbooks are:
- Human-readable and easy to learn, even for users without extensive programming experience.
- Idempotent, meaning that running them multiple times ensures the system reaches the same desired state without 
unintended changes.

Ansible can manage a wide range of environments, including bare-metal servers, virtual machines, cloud platforms, and 
hybrid infrastructures. 

### Installing Ansible
You can install Ansible using either `pip` or `pipx`. Using `pipx` is recommended as it **isolates Ansible and its 
dependencies from your system Python environment**.

Since we used `pipx`, bellow you'll find an example of the required steps to complete the installation:

```bash
# Install pipx if not already installed
sudo apt-get install pipx -y

# Install Ansible with dependencies
pipx install --include-deps ansible

# Upgrade Ansible (optional)
pipx upgrade --include-injected ansible

# Enable shell completion for Ansible
pipx inject --include-apps ansible argcomplete
```

After this, Ansible is ready to use, and you can start managing your infrastructure with playbooks.

## Provision `web-app` and `db-server` VMs with Ansible
Before configuring your `playbook.yaml`, you need to **update the Vagrantfile from the previous class assignment** 
(**CA3**). In the last CA, the VMs were provisioned using inline shell scripts. To switch to Ansible as the provisioner, 
you can use the following approach:

### Example Vagrantfile Changes
Replace the old provisioning method entirely with Ansible for your `db-server` and `web-app` VMs, respectively. Take the 
examples below as reference:

```bash
db.vm.provision "ansible" do |ansible|
  ansible.playbook = "./playbook.yaml"
  ansible.compatibility_mode = "2.0"
end
```

```bash
web.vm.provision "ansible" do |ansible|
  ansible.playbook = "./playbook.yaml"
  ansible.compatibility_mode = "2.0"
end
```

### Explanation of Attributes
- **ansible.playbook** – Specifies the playbook file that Ansible will use to provision the guest machines.
- **ansible.compatibility_mode** – Ensures the Ansible provisioner generates an inventory file compatible with Ansible 
2.0 and later versions.

By making these changes, Vagrant will use Ansible to configure your VMs instead of the previous shell scripts, 
streamlining your workflow and leveraging Ansible’s automation capabilities.

## Ensuring your `playbook.yaml` is idempotent
Your `playbook.yaml` should be **idempotent**. This means it can be executed **multiple times**, and each execution will 
result in the same desired state as the initial run, without unintended changes or side effects.

Most of Ansible’s core modules (like `user`, `group`, `apt`, `yum`, etc.) are **inherently idempotent**, automatically 
checking the current system state before making changes.

However, the `shell` and `command` modules **are not idempotent by default**, as they execute commands regardless of 
the existing state. If you need to use these modules, you can ensure idempotency by adding conditions such as:
- `creates` – Skip execution if a file or directory already exists.
- `removes` – Skip execution if a file or directory has been removed.
- `changed_when` – Explicitly control when a task should be considered “changed.”

### Steps to Achieve an Idempotent Playbook

1. Use core modules whenever possible.
2. Check the current state before executing tasks with shell or command.
3. Test your playbook multiple times on each VM to ensure it consistently reaches the same state.


### web-app
#### first run
```bash
==> web-app: Running provisioner: ansible...
    web-app: Running ansible-playbook...
[WARNING]: Deprecation warnings can be disabled by setting `deprecation_warnings=False` 
in ansible.cfg.
[DEPRECATION WARNING]: The '--inventory-file' argument is deprecated. This feature will 
be removed from ansible-core version 2.23.

PLAY [Provision db-server VM] **************************************************
skipping: no hosts matched

PLAY [Provision web-app VM] ****************************************************

TASK [Gathering Facts] *********************************************************
ok: [web-app]
[WARNING]: Host 'web-app' is using the discovered Python interpreter at 
'/usr/bin/python3.8', but future installation of another Python interpreter could cause 
a different interpreter to be discovered. See 
https://docs.ansible.com/ansible-core/2.19/reference_appendices/interpreter_discovery.html 
for more information.

TASK [Update apt cache] ********************************************************
ok: [web-app]

TASK [Install Git] *************************************************************
ok: [web-app]

TASK [Install Java] ************************************************************
ok: [web-app]

TASK [Install JDK] *************************************************************
ok: [web-app]

TASK [Ensure if the repository exists] *****************************************
ok: [web-app]

TASK [Clone the Git repository] ************************************************
skipping: [web-app]

TASK [Pull from the Git repository] ********************************************
changed: [web-app]

TASK [Repository clone debug message] ******************************************
skipping: [web-app]

TASK [Repository existence debug message] **************************************
ok: [web-app] => {
    "msg": "Repository already exists. Skipping clone.."
}

TASK [Up-to-dated project debug message] ***************************************
skipping: [web-app]

TASK [Ensure project directory has the right ownership] ************************
ok: [web-app]

TASK [Ensure gradlew has correct permissions] **********************************
changed: [web-app]

TASK [Wait for DB to be ready] *************************************************
ok: [web-app]

TASK [Run Spring Boot Rest Application] ****************************************
changed: [web-app]

PLAY RECAP *********************************************************************
web-app: ok=12   changed=3    unreachable=0    failed=0    skipped=3    rescued=0    
ignored=0
```

#### second run
```bash
==> web-app: Running provisioner: ansible...
    web-app: Running ansible-playbook...
[WARNING]: Deprecation warnings can be disabled by setting `deprecation_warnings=False` 
in ansible.cfg.
[DEPRECATION WARNING]: The '--inventory-file' argument is deprecated. This feature will 
be removed from ansible-core version 2.23.

PLAY [Provision db-server VM] **************************************************
skipping: no hosts matched

PLAY [Provision web-app VM] ****************************************************

TASK [Gathering Facts] *********************************************************
ok: [web-app]
[WARNING]: Host 'web-app' is using the discovered Python interpreter at 
'/usr/bin/python3.8', but future installation of another Python interpreter could cause a 
different interpreter to be discovered. See 
https://docs.ansible.com/ansible-core/2.19/reference_appendices/interpreter_discovery.html 
for more information.

TASK [Update apt cache] ********************************************************
ok: [web-app]

TASK [Install Git] *************************************************************
ok: [web-app]

TASK [Install Java] ************************************************************
ok: [web-app]

TASK [Install JDK] *************************************************************
ok: [web-app]

TASK [Ensure if the repository exists] *****************************************
ok: [web-app]

TASK [Clone the Git repository] ************************************************
skipping: [web-app]

TASK [Pull from the Git repository] ********************************************
changed: [web-app]

TASK [Repository clone debug message] ******************************************
skipping: [web-app]

TASK [Repository existence debug message] **************************************
ok: [web-app] => {
    "msg": "Repository already exists. Skipping clone.."
}

TASK [Up-to-dated project debug message] ***************************************
skipping: [web-app]

TASK [Ensure project directory has the right ownership] ************************
ok: [web-app]

TASK [Ensure gradlew has correct permissions] **********************************
changed: [web-app]

TASK [Wait for DB to be ready] *************************************************
ok: [web-app]

TASK [Run Spring Boot Rest Application] ****************************************
changed: [web-app]

PLAY RECAP *********************************************************************
web-app: ok=12   changed=3    unreachable=0    failed=0    skipped=3    rescued=0    
ignored=0
```

### db-server
#### first run
```bash
==> db-server: Running provisioner: file...
    db-server: /mnt/c/MCS/COGSI/CA2/PART-II/gradle-tut-rest/CA2-Part2/PayrollDB.mv.db => 
    /home/vagrant/
==> db-server: Running provisioner: ansible...
    db-server: Running ansible-playbook...
[WARNING]: Deprecation warnings can be disabled by setting `deprecation_warnings=False` 
in ansible.cfg.
[DEPRECATION WARNING]: The '--inventory-file' argument is deprecated. This feature will 
be removed from ansible-core version 2.23.

PLAY [Provision db-server VM] **************************************************

TASK [Gathering Facts] *********************************************************
[WARNING]: Host 'db-server' is using the discovered Python interpreter at 
'/usr/bin/python3.8', but future installation of another Python interpreter could cause 
a different interpreter to be discovered. See 
https://docs.ansible.com/ansible-core/2.19/reference_appendices/interpreter_discovery.html 
for more information.
ok: [db-server]

TASK [Update apt cache] ********************************************************
ok: [db-server]

TASK [Install Git] *************************************************************
ok: [db-server]

TASK [Install Java] ************************************************************
ok: [db-server]

TASK [Install JDK] *************************************************************
ok: [db-server]

TASK [Ensure UFW is installed] *************************************************
ok: [db-server]

TASK [Deny incoming] ***********************************************************
ok: [db-server]

TASK [Allow outgoing] **********************************************************
ok: [db-server]

TASK [Allow SSH on port 22] ****************************************************
ok: [db-server]

TASK [Allow incoming form 192.168.56.10:9092 via TCP] **************************
ok: [db-server]

TASK [Ensure UFW is running] ***************************************************
ok: [db-server]

TASK [Run H2 Database] *********************************************************
changed: [db-server]

PLAY [Provision web-app VM] ****************************************************
skipping: no hosts matched

PLAY RECAP *********************************************************************
db-server: ok=12   changed=1    unreachable=0    failed=0    skipped=0    rescued=0    
ignored=0
```

#### second run
```bash
==> db-server: Running provisioner: file...
    db-server: /mnt/c/MCS/COGSI/CA2/PART-II/gradle-tut-rest/CA2-Part2/PayrollDB.mv.db => 
    /home/vagrant/
==> db-server: Running provisioner: ansible...
    db-server: Running ansible-playbook...
[WARNING]: Deprecation warnings can be disabled by setting `deprecation_warnings=False` 
in ansible.cfg.
[DEPRECATION WARNING]: The '--inventory-file' argument is deprecated. This feature will 
be removed from ansible-core version 2.23.

PLAY [Provision db-server VM] **************************************************

TASK [Gathering Facts] *********************************************************
ok: [db-server]
[WARNING]: Host 'db-server' is using the discovered Python interpreter at 
'/usr/bin/python3.8', but future installation of another Python interpreter could cause 
a different interpreter to be discovered. See 
https://docs.ansible.com/ansible-core/2.19/reference_appendices/interpreter_discovery.html 
for more information.

TASK [Update apt cache] ********************************************************
ok: [db-server]

TASK [Install Git] *************************************************************
ok: [db-server]

TASK [Install Java] ************************************************************
ok: [db-server]

TASK [Install JDK] *************************************************************
ok: [db-server]

TASK [Ensure UFW is installed] *************************************************
ok: [db-server]

TASK [Deny incoming] ***********************************************************
ok: [db-server]

TASK [Allow outgoing] **********************************************************
ok: [db-server]

TASK [Allow SSH on port 22] ****************************************************
ok: [db-server]

TASK [Allow incoming form 192.168.56.10:9092 via TCP] **************************
ok: [db-server]

TASK [Ensure UFW is running] ***************************************************
ok: [db-server]

TASK [Run H2 Database] *********************************************************
changed: [db-server]

PLAY [Provision web-app VM] ****************************************************
skipping: no hosts matched

PLAY RECAP *********************************************************************
db-server: ok=12   changed=1    unreachable=0    failed=0    skipped=0    rescued=0    
ignored=0
```

## Configure PAM to enforce a complex password policy within both VMs
In this section, you will use **Ansible** to configure **Pluggable Authentication Modules** (**PAM**) to enforce a 
strong password policy on both the `web-app` and `db-server` VMs. The goal is to improve system security by setting 
requirements for password complexity, history, and login attempt restrictions.


### Enforce password complexity

Passwords must meet the following criteria:

- Minimum length: 12 characters
- Must include at least three of four character classes: uppercase letters, lowercase letters, digits, and symbols
- Reject passwords containing common dictionary words
- Deny passwords that include the username or parts of it

Take the task below as reference to configure libpam-pwquality:

```yaml
    - name: Configure libpam-pwquality
      lineinfile:
        path: "/etc/pam.d/common-password"
        regexp: "pam_pwquality.so"
        line: "password required pam_pwquality.so minlen=12 lcredit=-1 ucredit=-1 dcredit=-1 ocredit=-1 minclass=3 
        dictpath=/usr/share/dict/words usercheck=1 usersubstr=4"
        state: present
```

### Prevent reuse of recent passwords
To enforce password history and prevent users from reusing their last five passwords, use the configuration below as 
example:

```yaml
    - name: Configure pam_pwhistory
      lineinfile:
        path: "/etc/pam.d/common-password"
        regexp: "pam_pwhistory.so"
        line: "pam_pwhistory.so retry=5 remember=5 use_authtok"
        state: present
```
`remember=5` ensures the **last five passwords cannot be reused**, and `retry=5` allows users **five attempts before 
failing**.

### Lock the account after failed login attempts
To protect against brute-force attacks, lock accounts after **five consecutive failed login attempts** for 10 minutes:

```yaml
    - name: Configure pam_faillock
      lineinfile:
        path: "/etc/pam.d/common-auth"
        regexp: "pam_faillock.so"
        line: "pam_faillock.so audit deny=5 unlock_time=600"
        state: present
```
`deny=5` specifies the **number of allowed failed attempts**, and `unlock_time=600` sets the **lockout duration to 
10 minutes** (**600 seconds**).

### Deny passwords containing the username or parts of it
To prevent users from choosing weak passwords that include their username or parts of it, you can configure PAM’s 
`libpam-pwquality` module to reject such passwords automatically. This enhances protection against predictable password 
choices.

```yaml
    - name: Configure libpam-pwquality
      lineinfile:
        path: "/etc/pam.d/common-password"
        regexp: "pam_pwquality.so"
        line: "password required pam_pwquality.so minlen=12 lcredit=-1 ucredit=-1 dcredit=-1 ocredit=-1 minclass=3 
        dictpath=/usr/share/dict/words usercheck=1 usersubstr=4"
        state: present
```
The parameters `usercheck=1` and `usersubstr=4` instruct PAM to check the password against the username and reject 
passwords containing four or more consecutive characters from it.

### Notes for Idempotency

- Using `lineinfile` ensures that tasks are **idempotent**, modifying the configuration only if the desired line is 
missing or different.
- Avoid duplicating tasks - each `regexp` ensures that the specific line is managed correctly.

## Verify Ansible inventory with `ansible-inventory`
Ansible uses an **inventory file** to define which hosts it manages and how they are grouped. This can be either a 
**static inventory file** (e.g., `inventory.ini`), or the **Vagrant auto-generated inventory file**, which Vagrant 
provides automatically when using the Ansible provisioner.

For this assignment, we decided to use a static inventory file named `inventory.ini`. Take the configuration below as an
example:

```bash
[web-app]
192.168.56.10 ansible_user=vagrant

[db-server]
192.168.56.11 ansible_user=vagrant
```

This file defines two groups - `web-app` and `db-server` - each containing one VM with its corresponding IP address 
(**192.168.56.10** and **192.168.56.11**, respectively) and the SSH user.

### Verify inventory configuration
To verify that Ansible recognizes the defined hosts, the command `ansible-inventory --list -i inventory.ini` was 
executed. This command outputs the parsed structure of your inventory in JSON format, confirming that Ansible correctly 
detects both the `web-app` and `db-server` hosts.

After executing the command mentioned above, your output should resemble the reference below.

<img src="Images/04_01.PNG" alt="Ansible hosts list"/>

## Create groups and users inside both of `web-app` and `db-server` VMs using Ansible

### Create group `developers`

```yaml
    - name: Ensure group 'developers' exists
      group:
        name: developers
        state: present
```

**NEVER INCLUDE PASSWORD HASHES OR OTHER SECRETS DIRECTLY IN PLAYBOOKS OR VERSION CONTROL**

## Add a Health-Check to `playbook.yaml` to verify that both services are running correctly

# Alternative technologies to Ansible
## Chef

## Puppet


# Self-Evaluation
```bash
Daniel (1250503) - ??%
Diogo (1250506) - ??%
Pedro (1250545) - 100%
```