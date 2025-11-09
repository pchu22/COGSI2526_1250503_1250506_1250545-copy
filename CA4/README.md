# CA4 - Configuration Management
The goal for this class assignment is to install Ansible and provision both the VMs (`web-app` and `db-server`) that 
were created during CA3. You should keep your playbooks idempotent.

You'll use Ansible to enforce complex password policies using PAM (with `libpam-pwquality`). To finish CA4, you'll 
create groups and users using Ansible.

## Install Ansible
Ansible is an open-source automation tool designed for configuration management, application deployment, and IT 
orchestration, enabling infrastructure as code. Ansible is an agentless, and it only needs to be installed on a single 
host (referred to as the control node). From the control node, Ansible can manage an entire fleet of machines and other 
devices (referred to as managed nodes) remotely with SSH, Powershell remoting, and numerous other transports, all from a 
simple command-line interface with no databases or daemons required.

For your control node (the machine that runs Ansible), you can use nearly any UNIX-like machine with Python installed. 
This includes Red Hat, Debian, Ubuntu, macOS, BSDs, and Windows under a Windows Subsystem for Linux (WSL) distribution. 
Windows without WSL is not natively supported as a control node.

Ansible uses YAML-based playbooks to define automation workflows, making its configuration language human-readable and 
easy to learn, even for those without extensive programming experience. These playbooks are idempotent, ensuring that 
running them multiple times results in the same desired state without unintended side effects. The tool supports a wide 
range of environments, including bare metal, virtual machines, cloud platforms, and hybrid infrastructures.

You can install Ansible either by using `pipx` or using `pip`. We decided to use `pipx`, therefore, if you follow the 
example below, you'll be able to successfully install Ansible.

```bash
sudo apt-get install pipx -y
pipx install --include-deps ansible
pipx upgrade --include-injected ansible
pipx inject --include-apps ansible argcomplete
```

## Provision `web-app` and `db-server` VMs with Ansible

## Grant that `playbook.yaml` is idempotent
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

### Enforce a minimum length of 12 characters, and require at least three of the four-character classes: uppercase letters, lowercase letters, digits, and symbols
```yaml
    - name: Configure libpam-pwquality
      lineinfile:
        path: "/etc/pam.d/common-password"
        regexp: "pam_pwquality.so"
        line: "password required pam_pwquality.so minlen=12 lcredit=-1 ucredit=-1 dcredit=-1 ocredit=-1 minclass=3 
        dictpath=/usr/share/dict/words usercheck=1 usersubstr=4"
        state: present
```

### Reject passwords containing common dictionary words and prevent reuse of the last five passwords
```yaml
    - name: Configure libpam-pwquality
      lineinfile:
        path: "/etc/pam.d/common-password"
        regexp: "pam_pwquality.so"
        line: "password required pam_pwquality.so minlen=12 lcredit=-1 ucredit=-1 dcredit=-1 ocredit=-1 minclass=3 
        dictpath=/usr/share/dict/words usercheck=1 usersubstr=4"
        state: present
    - name: Configure pam_pwhistory
      lineinfile:
        path: "/etc/pam.d/common-password"
        regexp: "pam_pwhistory.so"
        line: "pam_pwhistory.so retry=5 remember=5 use_authtok"
        state: present
```

### Lock the account for 10 minutes after five consecutive failed login attempts
```yaml
    - name: Configure pam_faillock
      lineinfile:
        path: "/etc/pam.d/common-auth"
        regexp: "pam_faillock.so"
        line: "pam_faillock.so audit deny=5 unlock_time=600"
        state: present
```

### Deny the use of passwords that contain the username or parts of it
```yaml
    - name: Configure libpam-pwquality
      lineinfile:
        path: "/etc/pam.d/common-password"
        regexp: "pam_pwquality.so"
        line: "password required pam_pwquality.so minlen=12 lcredit=-1 ucredit=-1 dcredit=-1 ocredit=-1 minclass=3 
        dictpath=/usr/share/dict/words usercheck=1 usersubstr=4"
        state: present
```

## List of hosts known to Ansible
```bash
[web-app]
192.168.33.10 ansible_user=vagrant

[db-server]
192.168.33.11 ansible_user=vagrant
```
<img src="Images/04_01.PNG" alt="Ansible hosts list"/>

## Create groups and users inside both of `web-app` and `db-server` VMs using Ansible

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