Introduction:

HashiCorp Vault is a powerful tool for managing secrets and protecting sensitive data. It provides a secure and centralized way to store, access, and manage credentials, tokens, and other secrets. In this blog post, we will walk you through the process of installing Vault as a service on an Ubuntu server, enabling you to harness its capabilities to enhance security and manage secrets effectively.

Prerequisites:

Before we begin, ensure you have the following prerequisites in place:

    An Ubuntu server with sudo privileges.
    A basic understanding of the command line.
    A user account with sudo privileges.

Step 1: Import HashiCorp GPG Key:

HashiCorp provides a GPG key to sign their packages. You need to import this key to verify the package's authenticity. Run the following command:

curl -fsSL https://apt.releases.hashicorp.com/gpg | sudo gpg --dearmor -o /usr/share/keyrings/hashicorp-archive-keyring.gpg

Step 2: Add HashiCorp APT Repository:

Next, add the HashiCorp APT repository to your system's sources list:

echo "deb [signed-by=/usr/share/keyrings/hashicorp-archive-keyring.gpg] https://apt.releases.hashicorp.com $(lsb_release -cs)
 main"
 | sudo tee
 /etc/apt/sources.list.d/hashicorp.list

Step 3: Update Package List:

Update your package list to include the new HashiCorp repository:

sudo apt update

Step 4: Install Vault:

Finally, install Vault using the APT package manager:

sudo apt install vault

Step 5: Verify the Installation:

Check that Vault has been successfully installed by running:

vault --version

This should display the version number of Vault.

Step 6: Configure Vault:

Configuration file is /etc/vault.d/vault.hcl, change tls_cert_file and tls_key_file to your cert file and keystore. The default data paths are stored in /opt/vault/data as recommended. 

storage "raft" {
  path    = "/opt/vault/data"  //vault will store thing on this path
  node_id = "node1"
}
listener "tcp" {
  address     = "0.0.0.0:8200"
  tls_disable = "false"               //to enable tls
  tls_cert_file = "/home/administrator/vault-test/ssl-vault-test/cert.pem" //path to cert file
  tls_key_file = "/home/administrator/vault-test/ssl-vault-test/privkey.pem" //path to key file
}
api_addr = "http://127.0.0.1:8200"
cluster_addr = "https://127.0.0.1:8201"
ui = true

After configuration done, restart vault service.

service vault stop
service vault start

Step 7: Initialize vault:

This can be done by web ui or command line. For web gui you can navigate to http://127.0.0.1:8200 and following instruction. To initialize vault by command, do the folloing command

Before run initialize you need to export two environment variables as follow

$ export VAULT_ADDR='https://localhost:8200'
$ export VAULT_CACERT=/home/administrator/vault-test/ssl-vault-test/cert.pem

Then run the following command

$ vault operator init

Output of this command will contains unseal key and root token as show follow:

Unseal Key 1: NO/iZxf0pnVvB1lOIdCtPP2InS07DQM/zkrD+FasNuoI
Unseal Key 2: n/p4q7gDAbNis2Qm1l99Lrge43z8U/CSZYtodxTGhgpM
Unseal Key 3: +dEwUuGhUvLv4xSfnroGuZ2PKdhvYdlFrYy4iV8ME7/d
Unseal Key 4: oSYgEr0aWvX7AzJ96IYpIcyducqlWVy78KyvPfcsv/Tt
Unseal Key 5: B0MBC0wBYNoE+Io6LYkToNTCB2BAh7Q0Fhm53H5dbWzu

Initial Root Token: hvs.1KQB2h1HMsbrzTua8nUBZySg

Vault initialized with 5 key shares and a key threshold of 3. Please securely
distribute the key shares printed above. When the Vault is re-sealed,
restarted, or stopped, you must supply at least 3 of these keys to unseal it
before it can start servicing requests.

Vault does not store the generated root key. Without at least 3 keys to
reconstruct the root key, Vault will remain permanently sealed!

It is possible to generate new unseal keys, provided you have a quorum of
existing unseal keys shares. See "vault operator rekey" for more information.

When vault service start, it start with seal state. You cannot access to vault if vault in seal state. To unseal vault, you need unseal keys

The Initial Root Token use for access vault. It is recommend to generate new token and keep root token in secret.

Step 8: Unseal Vault:

Following command to unseal vault

$ vault operator unseal

Below are the output

Unseal Key (will be hidden):<type the unseal key>
Key            	Value
---            	-----
Seal Type      	shamir
Initialized    	true
Sealed         	true
Total Shares   	5
Threshold      	3
Unseal Progress	1/3
Unseal Nonce   	5c726945-e616-a6c3-504a-c938dcf25d5a
Version        	1.14.0
Build Date     	2023-06-19T11:40:23Z
Storage Type   	raft
HA Enabled     	true

Do this 3 times and use difference Unseal key until the “Sealed false”

Step 9: Generate new token:

Using root token from Step 7 to export one more environment variable as follow

$ export VAULT_TOKEN="hvs.1KQB2h1HMsbrzTua8nUBZySg"

Then use following command to generate new token.

Please see more information about how to manage vault token here: https://developer.hashicorp.com/vault/tutorials/tokens/token-management

$ vault token create -policy=default

Key                  Value
---                  -----
token                hvs.CAESIPTd9dl2cePaceCI8SLKjD-mEq8pVC4vy730D7m9jImjGh4KHGh2cy45Y2pyUU5rSTB4Y3hpeno0aVJEZld3U1E

