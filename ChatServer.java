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

import java.net.*;
import java.util.*;
import java.io.*;

import javax.swing.*;

public class ChatServer
{   //open CLASS ChatServer
	
	//create array(Vector) that contains list of user names and connected clients
	public Vector<String> userNameList = new Vector<String>();
	public Vector<String> blockedWordsList = new Vector<String>();
	public Vector<ClientHandler> clientHandlerList = new Vector<ClientHandler>();
	
	//create connection-required variable placeholders
	private ServerSocket chatServer;
	private Socket clientConnection;
	private int port = 0;	
	
	//create command-request variable placeholder
	public String userNameForRemoval;
	public String usersInChat;
	public String listBlockedWords;
	
	//create final strings for message separation and server user-name
	public static final String userMessageSeparator = " >>> ";
	public static final String serverUserName = "CHAT MODERATOR";
	
	//time-stamp variables
	private Calendar cal = Calendar.getInstance();
	private Date fullTimeStamp = new Date();
	private String currentTimeStamp = cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
			
	//create ChatServer & ChatServerGUI variable placeholders
	public ChatServer program;
	public ChatServerGUI gui;
	
//---------------------------------------------------------
//		Constructor & Methods
//---------------------------------------------------------
	
	//METHOD: ChatServer(int port) Constructor
	//PRECONDITION:		port number variable
	//POSTCONDITION:	modified instance of ChatServer that; 
	//						* includes a window title 
	//						* sets the look and feel to system default
	//						* creates instance of ChatServerGUI
	public ChatServer(int port)
	{   //open CONSTRUCTOR with port arg
		
		//reference passed arg with port variable
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
			displayEventMessage("\nAn error has occured - please restart or contact the software developer.\nError Code: [E]UI.ERROR\n");
			
		}	//close CATCH
		
		//create new instance of ChatServerGUI
		gui = new ChatServerGUI(this, port);
				
	}   //close CONSTRUCTOR
	
	//METHOD: setPortNumber()
	//PRECONDITION:		port number variable (which is set to 0)
	//POSTCONDITION:	sets port number to user-input number and returns it to variable port
	public static int setPortNumber()
	{	//open METHOD setPortNumber
		
		//prompt user for port assignment
		String portNumber = JOptionPane.showInputDialog(null,
    		"Enter the Port number for server creation:\n(HELP TIP: Ask an adult to read the instructions if you need help)", "Server Connection\n",
               JOptionPane.PLAIN_MESSAGE);
		//assign port number if user cancels input dialog
		if(portNumber == null)
		{	//open IF
    		
			JOptionPane.showMessageDialog(null, "Default Port Number Used.\nPort in use: 12345");
			portNumber = "12345";
    		
		}	//close IF
			
		//take input dialog entry and change to type int
	    int port = Integer.parseInt(portNumber);
		
		return port;
		
	}	//close METHOD setPortNumber
	
	//METHOD: startChatServer()
	//PRECONDITION:		ChatServer object
	//POSTCONDITION:	initiated ChatServer object by:
	//					* time-stamping the event log and chat area
	//					* calling methods to;
	//						* create server socket
	//						* initialise blocked words list
	//						* enter indefinite loop to accept incoming connections
	public void startChatServer()
	{   //open METHOD startChatServer
		
		//start message in Event and Chat areas with a time-stamp
		displayEventMessage(fullTimeStamp + "\nStarting Chat Server - Event Log...\n\n");
		displayChatMessage(fullTimeStamp + "\nStarting Chat Server - Chat Area...\n\n");
		
		try
		{	//open TRY
		
			//call create socket, initialise blocked words list and accept connections methods
			createServerSocket();
			initialiseBlockedWordsList();
			acceptConnections();	
				
		}	//close TRY
		catch(Exception e)
		{	//open CATCH
			
			//catch START.SERVER error
			displayEventMessage("\nAn error has occured - please restart or contact the software developer.\nError Code: [E]START.SERVER\n");
			
		}	//close CATCH
		
	}   //close METHOD startChatServer
	
	//METHOD: createServerSocket()
	//PRECONDITION:		ChatServer object
	//POSTCONDITION:	ChatServer object that contains a ServerSocket
	private void createServerSocket()
	{	//open METHOD createServerSocket
		
		try
		{	//open TRY
			
			//create ServerSocket with class' port arg and give max connected clients as 30
			displayEventMessage("Binding server to port " + port + ", please wait .... \n");
			chatServer = new ServerSocket(port, 30);   //maxQueue -> 30
			displayEventMessage("Chat Server started: \n" + chatServer + "\n");
		
		}	//close TRY
		catch(IOException ioe)
		{	//open CATCH
			
			//catch CREATE.SERVERSOCKT error 
			displayEventMessage("\nAn error has occured - please restart or contact the software developer.\nError Code: [IO]CREATE.SERVERSOCKET\n");
			
		}	//close CATCH
		
	}	//close METHOD createServerSocket
	
	//METHOD: acceptConnections()
	//PRECONDITION:		ChatServer object with Server Socket
	//POSTCONDITION:	ChatServer object with Server Socket that;
	//					* accepts incoming connections
	//					* creates a ClientHandler object for each connection
	private void acceptConnections()
	{	//open METHOD acceptConnections
		
		try
		{	//open TRY
		
			do
			{   //open DO-WHILE
				
				displayEventMessage("Awaiting new connections .... \n\n");
				
				//accept incoming connections, assign a client handler,
				//	add clientHandler to clientHandlerList vector and
				//	register userName to variable of client (sent from ChatClient class) 
				clientConnection = chatServer.accept();
				ClientHandler cHandler = new ClientHandler(clientConnection, this);
				clientHandlerList.add(cHandler);
				
				//announceEntrance() from ChatClient class will add the user name and 
				//	connection found message before the following message to event log
				displayEventMessage("Inet Details:  " + clientConnection + "\n");	
		
			}  	//close DO-WHILE
			while(clientConnection != null);	//do indefinitely
			
		}	//close TRY
		catch(Exception ex)
		{	//open CATCH
			
			//catch ACCEPT.CONNECTIONS error
			displayEventMessage("\nAn error has occured - please restart or contact the software developer.\nError Code: [E]ACCEPT.CONNECTIONS\n");
			
		}	//close CATCH
		
	}	//close METHOD acceptConnections

	//METHOD: closeConnectionsAndExit()
	//PRECONDITION:		ChatServer object
	//POSTCONDITION:	Exit from ChatServer application
	public void closeConnectionsAndExit() throws IOException
	{	//open METHOD closeConnectionsAndExit
	
		String exitLine = "Server Termination in Progress";
		displayEventMessage("\n" + exitLine + "\n");
		displayChatMessage("\n" + exitLine + "\n");
		broadcast(serverUserName, exitLine);
		System.exit(gui.getDefaultCloseOperation());
		
	}	//close METHOD closeConnectionsAndExit

	//METHOD: broadcast(String user, String message)
	//PRECONDITION:		ChatServer object, Client Handler object, String user and String message
	//POSTCONDITION:	call to ClientHandler method sendMessage
	public synchronized void broadcast(String user, String message) 
	{	//open METHOD broadcast with user and message args
		
	    //send message to all connected users
	    for ( ClientHandler cHandler : clientHandlerList )
	       if ( ! cHandler.getUserName().equals(user) )
	          cHandler.sendMessage(user, message);
	    
	}	//close METHOD broadcast

	//METHOD: displayEventMessage(String messageToDisplay)
	//PRECONDITION:		event log JTextArea and String messageToDisplay
	//POSTCONDITION:	appended event log JTextArea with messageToDisplay
	public void displayEventMessage(String messageToDisplay)
	{   //open METHOD displayEventMessage with messageToDisplay arg
		
		//display Event message with a time-stamp to EventLogArea
		gui.displayEventLogArea.append(currentTimeStamp + " - " + messageToDisplay);
	
	}   //close METHOD displayEventMessage

	//METHOD: displayChatMessage(String messageToDisplay)
	//PRECONDITION:		chat area JTextArea and String messageToDisplay
	//POSTCONDITION:	appended chat area JTextArea with messageToDisplay
	public void displayChatMessage(String messageToDisplay)
	{	//open METHOD displayChatMessage with messageToDisplay arg
		
		//display Chat message to ChatArea
		gui.displayChatArea.append(messageToDisplay);
		
	}	//close METHOD displayChatMessage

	//METHOD: removeUser(String user)
	//PRECONDITION: 	Vector userNameList, String userNameForRemoval, Socket clientConnection
	//					and instance of ClientHandler
	//POSTCONDITION:	ended instance of ClientHandler and clientConnection
	public void removeUser(String userToRemove)
	{	//open METHOD removeUser with user arg
		
		try
		{	//open TRY
			
			while(true)
			{	//open WHILE
				
				//check if userNameForRemoval exists
				if(!userNameList.contains(userToRemove))
				{	//open IF
					
					displayEventMessage("Cannot kick - " + userToRemove + " does not exist\n");
					break;
					
				}	//close IF
				
				//check if userNameForRemoval is the only one in the chat room
				
				else if(userNameList.size() <= 1)
				{	//open ELSE IF
					
					displayEventMessage("Cannot kick from chat room - " + userToRemove + " only client left\n");
					break;
					
				}	//close ELSE IF
				
				//remove user - delete userName from both vectors, close their connection,
				//	broadcast to clients about users' removal and display event message
				else
				{	//open ELSE IF
					
					//remove user from vectors
					userNameList.remove(userToRemove);
					clientHandlerList.remove(userToRemove);
					
					//display on server char area
					displayChatMessage(serverUserName + userMessageSeparator + ".remove " + userToRemove + "\n");
					
					//send command to MessagesThread to remove user
					broadcast(serverUserName, ".remove " + userToRemove);
					broadcast(serverUserName, userToRemove + " is being removed from the chat room");
		    		displayEventMessage("\n" + userToRemove + " client has been removed from chat\n");
		    		displayChatMessage(serverUserName + userMessageSeparator + userToRemove + " client has been removed from chat\n");
		    		break;
		    		
				}	//close ELSE IF
		
			}	//close WHILE
	    	
		}	//close TRY
		catch(Exception ex)
		{	//open CATCH
			
			//catch NO.USER error
			displayEventMessage("Cannot find user. Error.Code: [E]NO.USER\n");
			
		}	//close CATCH
   	 		
	}	//close METHOD removeUser

	//METHOD: usersInChat()
	//PRECONDITION:		Vector userNameList
	//POSTCONDITION:	returned String usersInChat
	public String usersInChat()
	{	//open METHOD usersInChat
		
		if(userNameList.isEmpty())
		{	//open IF
			
			usersInChat ="\n[SERVER] There are no user's in the chat room\n\n";
			return usersInChat;
		
		}	//close IF
		else
		{	//open ELSE
			
			usersInChat = "\n[SERVER] User's in Chat Room:\n" + userNameList.toString() + "\n\n";
			return usersInChat;
			
		}	//close ELSE
	
	}	//close METHOD usersInChat

	//METHOD: initialiseBlockedWords
	//PRECONDITION: 	Vector blockedWordsList
	//POSTCONDITION:	added elements to Vector blockedWordsList
	private void initialiseBlockedWordsList()
	{	//open METHOD initialiseBlockedWordsList
		
		blockedWordsList.add("http");
		blockedWordsList.add("www");
		blockedWordsList.add("//");
		blockedWordsList.add("@");
		blockedWordsList.add(".com");
		blockedWordsList.add(".org");
		blockedWordsList.add("porn");
		blockedWordsList.add("fuck");
		
	}	//close METHOD initialiseBlockedWordsList
		
	//METHOD: addBlockedWord(String wordToBlock)
	//PRECONDITION:		Vector blockedWordsList and String wordToBlock
	//POSTCONDITION:	added wordToBlock to Vector blockedWordsList
	public void addBlockedWord(String wordToBlock)
	{	//open METHOD addBlockedWord
		
		if(wordToBlock.isEmpty() || wordToBlock == "" || wordToBlock.startsWith(" ") || wordToBlock == null)
		{	//open IF
			
			displayEventMessage("Cannot block words that are empty or start with spaces\n");
			
		}	//open IF
		else
		{	//open ELSE
		
			blockedWordsList.add(wordToBlock);
			displayEventMessage(wordToBlock + " has been added to the Blocked Words list.\n");
		
		}	//close ELSE
		
	}	//close METHOD addBlockedWord
	
	//METHOD: removeBlockedWord(String wordToUnblock)
	//PRECONDITION:		Vector blockedWordsList and String wordToUnblock
	//POSTCONDITION:	removed wordToUnblock from Vector blockedWordsList
	public void removeBlockedWord(String wordToUnblock)
	{	//open METHOD removeBlockedWord
		
		if(blockedWordsList.contains(wordToUnblock))
		{	//open IF
		
			blockedWordsList.remove(wordToUnblock);
			displayEventMessage(wordToUnblock + " has been removed from the Blocked Words list.\n");
		
		}	//close IF
		else
			displayEventMessage(wordToUnblock + " does not exits in the Blocked Words list.\n");
		
	}	//close METHOD removeBlockedWord
		
	//METHOD: getBlockedWordsList()
	//PRECONDITION:		Vector blockedWordsList and String listBlockedWords
	//POSTCONDITION: 	returns amended String listBlockedWords
	public String getBlockedWordsList()
	{	//open METHOD getBlockedWordsList
		
		listBlockedWords = blockedWordsList.toString();
		displayEventMessage("Blocked Words List:\n" + listBlockedWords + "\n");
		
		return listBlockedWords;
		
	}	//close METHOD getBlockedWordsList
	
}	//close CLASS ChatServer