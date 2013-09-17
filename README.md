Childrens-Chat-Java-Program
===========================

Children's Chat Program - Client-Server Model - Thesis Project for HDip

Created by Greg Byrne, December 2012 (College Thesis Project).
Email: byrne.greg@gmail.com

Copyright remains reserved

Package Description:
A multi-threaded client/server application that provides text communication between a server and multiple clients over TCP/IP.
Child-centric features include;
- Server: IP User Logging
- Server: Save Chat and Event Files
- Server: Block Word Use
- Server: Remove Users
- Client: Alias changer
- Client: Color Theme changer
- Client: Text look changer

Package Content:
- ChatClient
- ChatClientGUI
- ChatClientStarter (Executable)
- MessagesThread
- ChatServer
- ChatServerGUI
- ChatServerStarter (Executable)
- ClientHandler

To Use:
Run the Children's Chat Program - Server and enter a port number on your computer that is currently not being used. Numbers above 1023 should be fine, (if you are not sure, cancel the request and a default port of 12345 is used).
Once the Server is operating, run the Children's Chat Program - Client. You will be asked to enter the IP number of the server (you may need to configure firewalls) and the port. If you are running the Server and Client on a local computer, you can cancel the request and the IP will default to 127.0.0.1 (localhost). You may run as many instances of the Client program as you wish.
