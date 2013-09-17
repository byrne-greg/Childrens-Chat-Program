/* 
 * Children's Chat Program
 * 
 * Created by Greg Byrne, December 2012 (College Thesis Project)
 * Email: byrne.greg@gmail.com
 * Copyright remains reserved
 * 
 * Package Description:
 * A multi-threaded client/server application that provides text communication between a server and multiple clients over TCP/IP.
 * Child-centric features include;
 * - Server: IP User Logging
 * - Server: Save Chat and Event Files
 * - Server: Block Word Use
 * - Server: Remove Users
 * - Client: Alias changer
 * - Client: Color Theme changer
 * - Client: Text look changer
 * 
 * Package Content:
 * - ChatClient
 * - ChatClientGUI
 * - ChatClientStarter (Executable)
 * - MessagesThread
 * - ChatServer
 * - ChatServerGUI
 * - ChatServerStarter (Executable)
 * - ClientHandler
 * 
 */
package childchatprog.gregbyrne;

import java.io.*;
import java.net.*;

public class ClientHandler extends Thread
{	//open CLASS ClientHandler

	//create variable placeholders
	private String name = "";
	private String line = "";
	private String arrivalMessage = "";
	private ObjectInputStream inputFromClient;
	private ObjectOutputStream outputToClient;

	//create ChatServer variable placeholder
	public ChatServer chatServer;

//---------------------------------------------------------
//			Constructor & Methods
//---------------------------------------------------------
	
	//METHOD: ClientHandler (Socket client, ChatServer chatServer) Constructor
	//PRECONDITION:		instance of Socket client and ChatServer chatServer
	//POSTCONDITION:	modified instance of ClientHandler that;
	//					* creates input and output streams
	//					* adds ChatClient user name to Vector userNameList
	//					* register user name with event log upon connection
	public ClientHandler(Socket client, ChatServer chatServer) throws Exception 
	{	//open CONSTRUCTOR ClientHandler
    
		//passed ChatServer arg is referenced to variable chatServer
		this.chatServer = chatServer;
		
		//create input and output streams
		inputFromClient = new ObjectInputStream( client.getInputStream() ) ;
		outputToClient = new ObjectOutputStream ( client.getOutputStream() );
	 
		//add user name to vector
		name  = (String) inputFromClient.readObject();
		chatServer.userNameList.add(name); // add to vector
		
		//register user name with event log upon connection
		arrivalMessage = (String) inputFromClient.readObject();
		chatServer.displayEventMessage(arrivalMessage);
		start();
    
	}	//close CONSTRUCTOR ClientHandler

	//METHOD: sendMessage(String userName, String message)
	//PRECONDITION:		String userName, String message and output stream 
	//POSTCONDITION:	sent object containing String userName and String message
    public void sendMessage(String userName, String message)  
    {	//open METHOD sendMessage with userName and message args
    	
    	try
    	{	//open TRY
    		
				//append message with user-name and >>> then send
    			outputToClient.writeObject(userName + ChatServer.userMessageSeparator + message);
    			outputToClient.flush();
    		
    	}	//close TRY
    	
    	catch(IOException ioe)
    	{	//open CATCH
    		
    		//catch SEND.MESSAGE error
    		chatServer.displayEventMessage("\nAn error has occured - please restart or contact the software developer.\nError Code: [IO]SEND.MESSAGE\n");
    		
    	}	//close TRY
    	
    }	//close METHOD sendMessage

    //METHOD: getUserName()
	//PRECONDITION:		String name
	//POSTCONDITION:	returns String name
    public String getUserName() 
    {	//open METHOD getUserName
    
    	return name; 
    
    }	//close METHOD getUserName
    
	//METHOD: searchForBlockedWord(String checkString)
	//PRECONDITION:		Vector blockedWordsList and String checkString
	//POSTCONDITION:	returned boolean whether blockedWordsList contains String checkString
    public boolean searchForBlockedWord(String checkString)
    {	//open METHOD searchForBlockedWord
    	
    	//takes blockedWordsList vector and transfers contents into a String[]
    	String[] checkBlockedWords = new String[chatServer.blockedWordsList.size()];
    	chatServer.blockedWordsList.toArray(checkBlockedWords);
    	
    	for (int i = 0; i < checkBlockedWords.length; i++)
    	{	//open FOR
    		
    		if(checkString.contains(checkBlockedWords[i]))
    		{	//open IF
    			
    			return true;
    			
    		}	//close IF
    		
    	}	//close FOR
    	
    	return false;
    	
    }	//close METHOD searchForBlockedWord
    
	//METHOD: run()
	//PRECONDITION:		instance of ClientHandler
	//POSTCONDITION:	* read object from client, cast as String and display
    //					* check for incoming commands and process
    //					* broadcast call with read object from client
    public void run() 
    {	//open METHOD run
    	
	     try    
	     {	//open TRY
         
	    	while(true)   
	    	{	//open WHILE
	     		
	    		//readObject from client and cast as String object, then display
	    		line = (String) inputFromClient.readObject();
	    		chatServer.displayChatMessage(name + ChatServer.userMessageSeparator + line + "\n");
	    		
	 //Command List	
	    
	    		//quit command    		
	    		if ( line.equals(".quit") ) 
	    		{	//open IF
	    		
	    			chatServer.clientHandlerList.remove(this);
	    			chatServer.userNameList.remove(name);
	    			chatServer.displayEventMessage("\n" + name + " client has left\n");
	    			chatServer.displayChatMessage(name + " has left the chat room\n");
			    	chatServer.broadcast(name, name + " has left the chat room\n");
	    			break;
	    		
	    		}	//close IF

	    		//changeName command
	    		if (line.startsWith(".changeName"))
	    		{	//open IF
	   
	    			String changeName = line.substring(12);
	    			chatServer.userNameList.add(changeName);
	    			chatServer.displayEventMessage(changeName + " is now registered\n");
	    			chatServer.broadcast("[SERVER]", name + " has changed their user name to " + changeName);
	    			name = changeName;
	    					    			
	   			}	//close IF
	    		
	    		//removeName command
	    		else if(line.startsWith(".removeName"))
	    		{	//open IF
	    			
	    			String removeName = line.substring(12);
	    			chatServer.userNameList.remove(removeName);
	    			chatServer.displayEventMessage(removeName + " has now been removed\n");
	    			
	    		}	//close IF
	    		
	    		//usersInAttendance command
	    		else if(line.equals(".usersInAttendance"))
	    		{	//open IF
	    
	    			String usersInAttendance = chatServer.usersInChat();
	    			outputToClient.writeObject(usersInAttendance);
	    			outputToClient.flush();
	    			
	    		}	//close IF
	    		
	    		//remove request made by a user
	    		else if(line.startsWith(".remove"))
	    		{	//open IF
	    	
	    			chatServer.displayChatMessage("[SERVER] " + name + " has used a remove command\n");
			    	
	    		}	//close IF

	    		
	    		//blocked word check
	    		else if(searchForBlockedWord(line))
	    		{	//open IF
	   
	    			String warning = ChatServer.serverUserName + ChatServer.userMessageSeparator + "You have used a blocked word. Your message was not broadcast.\n";
	    			outputToClient.writeObject(warning);
	    			outputToClient.flush();
	   
	    		}	//close IF
    			
	    		//no command - default broadcast text
	    		else
	    			chatServer.broadcast(name, line); // method  of outer class - send messages to all
	       
	    	}	//close WHILE
	     
	     }	//close TRY
	     
	     catch(Exception ex) 
	     {	//open CATCH
	     
	    	 //in case client decides to exit from his window
	    	 chatServer.userNameList.remove(name);
	    	 chatServer.clientHandlerList.remove(this);
	    	 chatServer.broadcast(name, name + " has left the chat room\n");
	    	 chatServer.displayEventMessage("\n" + name + " client has left\n");
	    	 chatServer.displayChatMessage(name + " has left the chat room\n");
	    	 
	     }	//close CATCH
    
    }	//close METHOD run

}	//close CLASS ClientHandler