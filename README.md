# Multi-threaded Dictionary Server
Using client-server architecture, implemented a multi-threaded server that allows concurrent clients to query the meaning(s) of an existing word, add a new word and its meaning, as well as delete any existing word.

### Running the Application

To run the application on the server side, enter the following on the command line: 

`java -jar DictionaryServer.jar <port> <dictionary-file>`

here, <port> is the port number where the server will listen for incoming client connections, and <dictionary-file> is the path to the file containing the dictionary data.

To run the application on the client side, enter the following on the command line (in a new tab): 

`java -jar DictionaryClient.jar <server-IP-address> <server-port>`

here, <server-IP-address> is the IP address of the server, and <server-port> is the port number where the server will listen for incoming client connections.
