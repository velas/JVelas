## Installation
## Use
#### Generate keys
generate keys from random: 
```java
HD keys = new Hd();
```
generate keys from seed: 
```java
HD keys = new Hd(seed, deriveIndex);
```
create keys from private key: 
```java
HD keys = new Hd(privateKey);
```

____

### Wallet

#### Create wallet
сreate wallet from hd keys:
```java
HD keys = new Hd();
Wallet wallet = keys.toWallet();
```
сreate wallet from public key:
```java
Wallet wallet = new Wallet(publicKey);
```
#### Get balance
get balance by wallet address:
```java
String nodeUrl = "https://testnet.velas.com" //testnet
VelasClient client = new VelasClient(nodeUrl);
BigInteger balance = client.wallet.getBalance(wallet.base58Address); 
```
#### Get unspents
get unspents by wallet address:
```java
VelasClient client = new VelasClient(nodeUrl);
List<TransactionInputOutpoint> unspent = 
        client.wallet.getUnspent(wallet.base58Address);
```
#### Create transaction
create and sign transaction:
```java
Transaction tx = new Transaction(unspents,
                                amount,
                                hd,
                                wallet.base58Address,
                                toAddress,
                                commission);
```

____

### Transactions

#### Validate transaction
validate transaction, return error if transaction incorrect
```java
VelasClient client = new VelasClient(testNetMasterNodeUrl);
HD hd = new HD(pk);
Wallet wallet = hd.toWallet();

List<TransactionInputOutpoint> unspents = 
        client.wallet.getUnspent(wallet.base58Address);

Transaction tx = new Transaction(unspents,
                                amount,
                                hd,
                                wallet.base58Address,
                                toAddress,
                                commission);

boolean res = client.tx.validate(tx); // true or false
```
#### Publish Transaction
Publish transaction to blockchain:
```java
VelasClient client = new VelasClient(testNetMasterNodeUrl);
HD hd = new HD(pk);
Wallet wallet = hd.toWallet();

List<TransactionInputOutpoint> unspents = 
        client.wallet.getUnspent(wallet.base58Address);

Transaction tx = new Transaction(unspents,
                                amount,
                                hd,
                                wallet.base58Address,
                                toAddress,
                                commission);

boolean res = client.tx.publish(tx); // true or false
```
if confirmed field is greater then 0 in transaction object(can get with method getByHashList), then transaction is confirmed 

#### Get transactions by block
Get an array of transaction hashes by range blocks from the given height to the highest block
```java
VelasClient client = new VelasClient(testNetMasterNodeUrl);
int height = 1500;
String[] hashes = client.tx.getHashListByHeight(height);
```

#### Get Detail of transactions by hashes
Get array of transaction objects by hash list, maximum hashes is 10000(can change later)
```java
var client = new Client(Url);
// Get hashes by wallet address for example
VelasClient client = new VelasClient(testNetMasterNodeUrl);
String[] hashes = client.tx.getHashListByAddress("VLa1hi77ZXD2BSWDD9wQe8vAhejXyS7vBM4");
```