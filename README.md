## Synopsis

This application provides functionality to perform a transaction between two bank accounts.

## Functionalities

It provides the following functionalities as REST endpoints:

### Add Account:

Type : POST
URL : /add
Sample Payload : 

```
{
    "name": "Test",
    "balance": 5
}
```
Sample Response :

```
{
    "accountNumber": 1,
    "name": "Test",
    "balance": 5
}
```

### Get Account Info:

Type : GET
URL : /get/{accountNumber}
Sample Response : 

```
{
    "accountNumber": 1,
    "name": "Test",
    "balance": 5
}
```

### Perform Transaction:

Type : POST
URL : /transfer
Sample Payload : 

```
{
	"sourceAccountNumber" : 1,
	"destinationAccountNumber" : 1,
	"amount" : 5
}
```
Sample Response :

```
Sucessfully performed funds transfer. New balance in source account : x
```

In all the requests, necessary validations are performed. Below are sample validations:
* Balance is non negative
* Account number is valid
* Account has sufficient funds to make the transfer

## Installation

To install the app, run through the following steps:

1. Clone the repo and change the directory to be the cloned directory (i.e. app-bank)
2. Make sure the machine has [Java](http://www.oracle.com/technetwork/java/javase/downloads/index-jsp-138363.html) and [Maven](https://maven.apache.org/download.cgi) installed.
3. From the command line, type `mvn clean install`, this will start the installation process
4. Once installation is complete, the `jar` file will be present inside `target` folder. This file is an executable file and can be run via `java -jar target/app-bank.jar` command
5. The app will run with default specs (i.e. `/accounts` as context path and `8085` as port number). If we need to change these, we can copy `application.properties` file (present inside `src/main/resource` directory), change these values and place it in the same directory as `jar` file (i.e `target` in this case)