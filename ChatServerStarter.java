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

public class ChatServerStarter 
{	//open CLASS ChatServerStarter

	//port variable used only to pass to ChatServer object
	public static int port = 0;

//---------------------------------------------------------	
//	Constructors & Methods
//---------------------------------------------------------
	
	//METHOD: main()
	//PRECONDITION: 	null
	//POSTCONDITION:	instance of ChatServer object with port variable
	public static void main (String[] args)
	{	//open METHOD main
		
		try
		{	//open TRY
			
			//call method to ask user to set port number
			startWithPortNumber();
		
		}	//close TRY
		catch(NumberFormatException nfe)
		{	//open CATCH
			
			JOptionPane.showMessageDialog(null, "Invalid input \nPlease restart application and enter using ONLY numbers");
			System.exit(JFrame.DISPOSE_ON_CLOSE);
			
		}	//close CATCH
		
    	//instantiate chat server and call startChatServer method
		ChatServer program = new ChatServer(port);
		program.startChatServer();
		
	}	//close METHOD main
	
	//METHOD: startWithPortNumber()
	//PRECONDITION: 	port variable
	//POSTCONDITION:	initialised port variable with user input
	private static void startWithPortNumber()
	{	//open METHOD startPortNumber
		
		port = ChatServer.setPortNumber();
		
	}	//close METHOD startPortNumber
	
}	//close CLASS ChatServerStart
