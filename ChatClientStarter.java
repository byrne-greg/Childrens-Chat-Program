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

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ChatClientStarter
{	//open CLASS ChatClientStarter
	
	//port and serverName used to pass to instance of ChatClient
	private static String serverName;
    private static int port;

//---------------------------------------------------------
//	Constructors & Methods
//---------------------------------------------------------
	
	//METHOD: main()
	//PRECONDITION: 	null
	//POSTCONDITION:	instance of ChatClient object with port and serverName variable
	public static void main(String[] args) 
    {	//open METHOD main
		
			serverName = setServerName();
		
		try
		{	//open TRY
			
			port = setPortNumber();
     
		}	//close TRY
		catch(NumberFormatException nfe)
		{	//open CATCH
			
			JOptionPane.showMessageDialog(null, "Invalid input \nPlease restart application and enter using ONLY numbers");
			System.exit(JFrame.DISPOSE_ON_CLOSE);
			
		}	//close CATCH
		
		try
		{	//open TRY
			
        	//instantiate ChatClient class with server connection
        	ChatClient chatClient = new ChatClient(serverName, port);
        	//call method runClient
            chatClient.runClient(serverName, port);
            
		}	//close TRY
		catch(Exception e)
		{	//open CATCH
			
			System.exit(JFrame.DISPOSE_ON_CLOSE);
			
		}	//close CATCH
             
    }	//close MAIN
	
	//METHOD: setPortNumber()
    //PRECONDITION:		port number variable
    //POSTCONDITION:	set port number variable to user input and returns
	private static int setPortNumber()
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
	
    //METHOD: setServerName()
    //PRECONDITION:		serverName variable
    //POSTCONDITION:	set serverName variable to user input and return serverName
	private static String setServerName()
	{	//open METHOD setPortNumber
		
		 //prompt user for server connection
    	serverName = JOptionPane.showInputDialog(null,
    			"Enter the Server IP for connection:\n(HELP TIP: Ask an adult to read the instructions if you need help)", "Server Connection\n",
                JOptionPane.PLAIN_MESSAGE);
    	if(serverName == null)
    	{	//open IF
    		
    		JOptionPane.showMessageDialog(null, "Default Server Address Used\nServer on: 127.0.0.1 (localhost)");
    		serverName = "localhost";
    		
    	}	//close IF
		
    	return serverName;
		
	}	//close METHOD setPortNumber
	
}	//close CLASS ChatClientStarter
