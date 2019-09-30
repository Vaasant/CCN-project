# CCN-project

## Requirement :
You need Java 8 or latest version to execute the project

## Execution :

### Setting up connection
To compile and execute HttpServer , Please run following command

```
javac HttpServer.java
java HttpServer 6000
```
To compile HttpClient , please run follwing command
```
javac HttpClient.java
```

### Test GET 
Execute client with following command
```
java HttpClient 127.0.0.1 6000 GET index.html
```
### Test PUT
Execute client with following command
```
java HttpClient 127.0.0.1 6000 PUT client_to_server.html
```
### Test with external server
Execute client with following command
```
java HttpClient www.google.com 80 GET /
```
### Test with external client
Execute server with following command
```
java HttpServer 12000
```
Copy www.localhost:12000/index.html in browser and check the display .

### Testing with client and server on separate system
```
java HttpClient <Servers IP Address> 6000 GET index.html
```
### Multithreaded Server
Server executes in infinite loop . Accepts a client connection .
The process of GET/PUT command is done in separate thread.
Server again starts listening on port for client connections.

### Termination Signal
On termination signal (like Ctrl+c /sudo kill -2) is captured Using java shutdownHook function. 
Current threads interuppted signal is set. Then threads executes normally for termination closing all client sockets.



