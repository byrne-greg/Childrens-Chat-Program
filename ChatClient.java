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
import javax.swing.*;

@SuppressWarnings("serial")
public class ChatClient extends JFrame 
{	//open CLASS
	
	//create connection-required variable placeholders
    private String userName;
    private String serverName;
    private int port;
    
    //create socket and streams placeholders
    public Socket client;
    public ObjectInputStream inputFromServer;
    public ObjectOutputStream outputToServer;
   
    //create ChatClientGUI variable placeholder
    public ChatClientGUI gui;
    
 //--------------------------------------------------------
 //			Constructors & Methods
 //--------------------------------------------------------
    
	//METHOD: ChatClient(String serverName)
	//PRECONDITION:		String serverName
	//POSTCONDITION:	instantiated ChatClient object
    public ChatClient(String serverName, int port)
    {	//open CONSTRUCTOR with userName arg
    	
    	this.serverName = serverName;
    	this.port = port;
    	
    	//set the LookAndFeel to user's native GUI
    	try
    	{	//open TRY
    		
    		String systemUI = UIManager.getSystemLookAndFeelClassName();
    		UIManager.setLookAndFeel(systemUI);
    				
    	}	//close TRY
    	catch(Exception e)
    	{	//open CATCH
    				
    		//catch UI.ERROR error
    		displayChatMessage("\nAn error has occured - please restart or contact the software developer.\nError Code: [E]UI.ERROR\n");
    				
    	}	//close CATCH
    	
    	//instantiate ChatClientGUI object
    	gui = new ChatClientGUI(this, serverName);
    
    }	//close CONSTRUCTOR
    
	//METHOD: runClient(String serverName, int port)
	//PRECONDITION:		ChatClient object, String serverName and int port
	//POSTCONDITION:	ChatClient object that has;
    //					* set user-name
    //					* connected to server
    //					* instantiated a messages thread
    //					* announce entrance
    public void runClient(String serverName, int port)	throws Exception
    {	//open METHOD runClient with userName, serverName and port args
 
    	//Chat is loading
    	displayChatMessage("Your chat client is loading...\n");
    	
    	//call setUserName()
    	userName = setUserName();
    	
    	//call ConnectToServer() method
    	connectToServer();
    	
    	//call getStreams() method
    	getStreams();
        
    	//call sendCommand(userName) method
    	sendCommand(userName);
    	
        //instantiate MessagesThread class and start
        new MessagesThread(this).start();  // create thread for listening for messages
    
        //Chat is now ready
    	displayChatMessage("Your chat client is now ready\n-------------------\n");
    
    	//call announceEntrance
    	announceEntrance();
    	
    }	//close METHOD runClient
    
    //METHOD: connectToServer()
    //PRECONDITION:		ChatClient object
    //POSTCONDITION:	ChatClient object connected to ChatServer
    public void connectToServer()
    {	//open METHOD connectToServer with serverName, port args
    	
    	try
    	{	//open TRY
    		
    		//Display info and connect client to server
    		displayChatMessage("Attempting connection to " + serverName + " on port " + port + "\n");
    		client  = new Socket(serverName , port);
    		displayChatMessage("Server connection found: \n" + client + "\n");

    	}	//close TRY
    	
    	catch(IOException ioe)
    	{	//open CATCH
    		
    		displayChatMessage("\nAn error has occured - please restart or contact the software developer.\nError Code: [IO]SERVER.NOT.FOUND\n");
    		
    	}	//close CATCH
    	
    }	//close METHOD connectToServer
    
    //METHOD: connectToServer()
    //PRECONDITION:		ChatClient object
    //POSTCONDITION:	ChatClient object with created input output streams
    public void getStreams()
    {	//open METHOD getStreams()
    	
    	try
    	{	//open TRY
    		
    		//create input and output streams
    		outputToServer = new ObjectOutputStream(client.getOutputStream() );
    		outputToServer.flush();
       	
    		inputFromServer = new ObjectInputStream(client.getInputStream() ) ;
    		
    	}	//close TRY
    	catch(IOException ioe)
    	{	//open CATCH
    		
    		displayChatMessage("\nAn error has occured - please restart or contact the software developer.\nError Code: [IO]CANNOT.CREATE.STREAMS\n");
    	
    	}	//close CATCH
    	
    }	//close METHOD getStreams()
    
    //METHOD: sendCommand(String commandAndUserName)
    //PRECONDITION:		ChatClient object and String commandAndUserName
    //POSTCONDITION:	response from ChatServer based on sent command
    public void sendCommand(String commandAndUserName)
    {	//open METHOD sendCommand with userName arg
    	
    	try
    	{	//open TRY
    		
    		outputToServer.writeObject(commandAndUserName);  // send name to server
    		outputToServer.flush();
    		displayChatMessage("Command request sent to server\n");
    		
    	}	//close TRY
    	catch(IOException ioe)
    	{	//open CATCH
    		
    		displayChatMessage("\nAn error has occured - please restart or contact the software developer.\nError Code: [IO]CANNOT.SEND.COMMAND\n");
    		
    	}	//close CATCH
        
    }	//close METHOD sendCommand
    
    //METHOD: announceEntrance()
    //PRECONDITION:		ChatClient object
    //POSTCONDITION:	sent message to ChatServer object announcing entrance
    public void announceEntrance()
    {	//open METHOD announceEntrance()
    	
    	try
    	{	//open TRY

    		String newInMessage = "\nClient Connected\nUserName:  " + userName + "\n";
    	
    		//send special message to server event log 
   	 		outputToServer.writeObject(newInMessage);
   	 		outputToServer.flush();
    	
   	 		//send data so everyone can see new arrival in chat area
   	 		sendMessage(userName + " has entered the chat room");
   	 		
    	}	//close TRY
    	catch(IOException ioe)
    	{	//open CATCH
    		
    		displayChatMessage("\nAn error has occured - please restart or contact the software developer.\nError Code: [IO]CANNOT.ANNOUNCE.ENTRANCE\n");
    		
    	}	//close CATCH
 
    }	//close METHOD announceEntrance

    //METHOD: sendMessage(String message)
    //PRECONDITION:		ChatClient object and String message
    //POSTCONDITION:	sent message to ChatServer object
    public void sendMessage(String message)
    {	//open sendMessage with message arg
    	
        try 
        {	//open TRY
        	
        	userName = getUserName();
            outputToServer.writeObject(message);
            outputToServer.flush(); //flush output to client
            displayChatMessage(userName + ChatClientGUI.userMessageSeparator + message + "\n");
        
        }	//close TRY
        catch (IOException ioException)
        {	//open CATCH
        	
            displayChatMessage("\nAn error has occured - please restart or contact the software developer.\nError Code: [IO]CANNOT.SEND.MESSAGE\n");
            
        }	//close CATCH
        
    }	//close METHOD sendMessage
    
    //METHOD: closeConnectionsAndExit()
    //PRECONDITION:		ChatClient object
    //POSTCONDITION:	exit application
    public void closeConnectionsAndExit()
    {	//open METHOD closeConnectionsAndExit
    	
    	try
    	{	//open TRY
    		
    		sendCommand(".quit");
    		displayChatMessage("Quit Command entered\nEnding Chat Session ...\n");
        	inputFromServer.close();
        	outputToServer.close();
        	client.close();
        	System.exit(getDefaultCloseOperation());
    		
    	}	//close TRY
    	
    	catch(IOException ioe)
    	{	//open CATCH
    		
    		displayChatMessage("\nAn error has occured - please restart or contact the software developer.\nError Code: [IO]CANNOT.CLOSE.CONNECTION\n");
    		
    	}	//close CATCH
    	
    }	//close METHOD closeConnectionsAndExit
    
    //METHOD: displayChatMessage(String messageToDisplay)
    //PRECONDITION:		ChatArea JTextArea, ChatClient object and String messageToDisplay
    //POSTCONDITION:	append messageToDisplay to ChatArea
	public void displayChatMessage(String messageToDisplay)
	{   //open METHOD displayChatMessage
		
		gui.displayChatArea.append(messageToDisplay);
	
	}   //close METHOD displayChatMessage
    
    //METHOD: setUserName()
    //PRECONDITION:		ChatClient object
    //POSTCONDITION:	return String userName with user-input
	private String setUserName()
	{	//open METHOD setUserName()
		
		//prompt user for userName input
		String userName = JOptionPane.showInputDialog(null,
				"Enter your Chat Room Name:\n(SAFETY TIP: Do NOT use your real name!)", "Username",
	             JOptionPane.PLAIN_MESSAGE);
		if(userName == null)
		{	//open IF
			
			JOptionPane.showMessageDialog(null, "No Username Entered. \nEnding Session - Please restart and enter a valid user name");
			System.exit(getDefaultCloseOperation());
			
		}	//close IF
		
		return userName;
		
	}	//close METHOD setUserName
	
    //METHOD: getUserName()
    //PRECONDITION:		ChatClient object
    //POSTCONDITION:	return String userName
	public String getUserName()
	{	//open METHOD getUsername()
		
		return userName;
		
	}	//close METHOD getUserName()
	
    //METHOD: changeUsername()
    //PRECONDITION:		ChatClient object
    //POSTCONDITION:	changed userName variable
	public void changeUserName()
	{	//open METHOD changeUserName
	
			String oldUserName = userName;
			userName = gui.changeNameField.getText();
		
			if(userName.isEmpty() || userName == "" || userName.contains(" ") || userName == null)
			{	//open IF
				
				displayChatMessage("Invalid Username\nName is either empty or contains spaces\n");
				userName = oldUserName;
			
			}	//close IF
			else
			{	//open ELSE
			
				sendCommand(".changeName " + userName);
				sendCommand(".removeName " + oldUserName);
			
			}	//close ELSE
			
			displayChatMessage("Username *" + userName + "* registered with server\n");
			
	}	//close METHOD changeUserName
   
}	//close CLASS ChatClient

