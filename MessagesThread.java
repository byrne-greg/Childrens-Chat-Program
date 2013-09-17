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

public class MessagesThread extends Thread 
{	//open CLASS MessagesThread
	
	//create ChatClient variable placeholder
	public ChatClient chatClient;

//---------------------------------------------------------	
//			Constructors & Methods
//---------------------------------------------------------
	    
	//METHOD: MessagesThread Constructor
	//PRECONDITION:		ChatClient object
	//POSTCONDITION:	instantiated MessagesThread object
	public MessagesThread(ChatClient chatClient)
	{	//open CONSTRUCTOR MessagesThread
		
		this.chatClient = chatClient;
		
	}	//close CONSTRUCTOR MessagesThread
	
	//METHOD: run()
	//PRECONDITION:		MessagesThread object
	//POSTCONDITION:	MessagesThread object that reacts to inputs from server
	public void run() 
	{	//open METHOD run()
		
        //line placeholder variable
		String line;
		
        try 
        {	//open TRY
        	
        	//loop forever
            while(true) 
            {	//open WHILE   	
                
            	line = (String) chatClient.inputFromServer.readObject();
                
            	//remove request command
                if(line.contains(".remove"))
                {	//open IF
                	
                	if(line.contains(chatClient.getUserName()))
                	{	//open IF
                		
                		chatClient.closeConnectionsAndExit();                	
                	
                	}	//close IF
                	
                }	//close IF
              
                //if no command request, just show message
                else
                	chatClient.displayChatMessage(line + "\n");
                
            }	//close WHILE
            
        }	//close TRY 
        catch(Exception ex) 
        {	//open CATCH
        	
        	chatClient.displayChatMessage("\nAn error has occured - please restart or contact the software developer.\nError Code: [E]SERVER.TERMINATED\n");
        	
        }	//close CATCH
   
	}	//close METHOD run
	
}	//close CLASS MessagesThread
