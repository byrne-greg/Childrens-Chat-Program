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

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class ChatServerGUI extends JFrame 
{	//open CLASS ChatServerGUI
	
	private String versionID = "3.0";
	
	//create port variable
	public int port;
	
	//create GUI variable placeholders
	private JMenuBar menuBar;
	private JTextField enterField;
	private JTextField removeUserField;
	private JTextField blockedWordsField;
	public JTextArea displayEventLogArea;
	public JTextArea displayChatArea;
	private JFileChooser fileChooser;
	public String userNameForRemoval;
	
	//create ChatServer variable placeholder
	public ChatServer chatServer;

//---------------------------------------------------------
//			Constructors & Methods
//---------------------------------------------------------

	//METHOD: ChatServerGUI() constructor
	//PRECONDITION:		null
	//POSTCONDITION:	modified instance of ChatServerGUI that;
	//					* sets up GUI for server user
	public ChatServerGUI(final ChatServer chatServer, int port)
	{	//open CONSTRUCTOR ChatServerGUI
		
		//title window with port number
		super("Children's Chat Program - Server (on Local Port " + port + ")");
		
		//reference passed arg as variable chatServer
		this.chatServer = chatServer;
		
		//some housekeeping variables - close operation and layout manager
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		
		//chatArea panel and subcomponents
		JPanel chatArea = new JPanel();
		chatArea.setLayout(new BorderLayout() );
		chatArea.setBorder(new TitledBorder("Chat Area"));
			
			//displayChatArea - text area
		displayChatArea = new JTextArea();
		displayChatArea.setEditable(false);
		displayChatArea.setLineWrap(true);
		chatArea.add(new JScrollPane(displayChatArea), BorderLayout.CENTER);
			
			//enterField - text field
		enterField = new JTextField(); //create enter field
		enterField.setEditable(true);
		enterField.addActionListener( new ActionListener()
		{   //new INNER CLASS anonymous
				
			//register actionPerformed
			public void actionPerformed(ActionEvent ev)
			{	//open METHOD actionPerformed
				
				//allows Server to talk in chat room
				String message = ev.getActionCommand();	
				chatServer.broadcast(ChatServer.serverUserName, message);
				chatServer.displayChatMessage(ChatServer.serverUserName + ChatServer.userMessageSeparator + message + "\n");
				enterField.setText("");			
					
			}   //close METHOD actionPerformed
				
		}   //close INNER CLASS anonymous
		);   //close actionListener call)
		chatArea.add(enterField,BorderLayout.SOUTH);
			
		//eventLog panel and subcomponents
		JPanel eventLog = new JPanel();
		eventLog.setLayout(new BorderLayout() );
		eventLog.setBorder(new TitledBorder("Event Log"));
		
			//displayEventLogArea - text area
		displayEventLogArea = new JTextArea(); //create DisplayArea
		displayEventLogArea.setEditable(false);
		displayEventLogArea.setLineWrap(true);
		eventLog.add(new JScrollPane(displayEventLogArea), BorderLayout.CENTER);
		
		//controlArea panel and subcomponents
		JPanel controlArea = new JPanel();
		controlArea.setLayout(new GridLayout(2, 0, 25, 25) );
		controlArea.setBorder(new TitledBorder("Control Area"));
		
			//buttonsSubPanel - panel
		JPanel buttonsSubPanel = new JPanel();
		buttonsSubPanel.setLayout(new FlowLayout() );
		controlArea.add(buttonsSubPanel);
			
			//clientListButton - button
		JButton clientListButton = new JButton("List Users in Room");
		clientListButton.addActionListener( new ActionListener()
		{   //new INNER CLASS anonymous
				
			//register actionPerformed method
			public void actionPerformed(ActionEvent ev)
			{	//open METHOD actionPerformed
					
				chatServer.displayChatMessage(chatServer.usersInChat());
				
			}   //close METHOD actionPerformed()
				
		}   //close INNER CLASS anonymous
		);   //close actionListener call)
		buttonsSubPanel.add(clientListButton);
			
			//blockedWordsSubPanel - panel
		JPanel blockedWordsSubPanel = new JPanel();
		blockedWordsSubPanel.setLayout(new FlowLayout() );
		blockedWordsSubPanel.setBorder(new TitledBorder("Blocked Words Panel"));
		controlArea.add(blockedWordsSubPanel);
		
			//blockedWordsFieldLabel - label
		JLabel blockedWordsFieldLabel = new JLabel("Enter Word for Blocking");
		blockedWordsSubPanel.add(blockedWordsFieldLabel);
		
			//blockedWordsField - text field
		blockedWordsField = new JTextField(15);
		blockedWordsField.setEditable(true);
		blockedWordsSubPanel.add(blockedWordsField);
		
			//addBlockedWordButton - button
		JButton addBlockedWordButton = new JButton("Add Blocked Word");
		addBlockedWordButton.addActionListener( new ActionListener()
		{   //new INNER CLASS anonymous
				
			//register actionPerformed method
			public void actionPerformed(ActionEvent ev)
			{	//open METHOD actionPerformed
					
				//get text from removeUserField and pass it as arg to removeUser method
				String wordToBlock = blockedWordsField.getText();
				chatServer.addBlockedWord(wordToBlock);
				blockedWordsField.setText("");
							
			}   //close METHOD actionPerformed
				
		}   //close INNER CLASS anonymous
		);   //close actionListener call)
		blockedWordsSubPanel.add(addBlockedWordButton);
			
			//removeBlockedWordButton - button
		JButton removeBlockedWordButton = new JButton("Remove Blocked Word");
		removeBlockedWordButton.addActionListener( new ActionListener()
		{   //new INNER CLASS anonymous
				
			//register actionPerformed method
			public void actionPerformed(ActionEvent ev)
			{	//open METHOD actionPerformed
					
				//get text from removeUserField and pass it as arg to removeUser method
				String wordToBlock = blockedWordsField.getText();
				chatServer.removeBlockedWord(wordToBlock);
				blockedWordsField.setText("");
							
			}   //close METHOD actionPerformed
			
		}   //close INNER CLASS anonymous
		);   //close actionListener call)
		blockedWordsSubPanel.add(removeBlockedWordButton);
			
			//getBlockedWordsButton - button
		JButton getBlockedWordsButton = new JButton("Display Blocked Words");
		getBlockedWordsButton.addActionListener( new ActionListener()
		{   //new INNER CLASS anonymous
				
			//register actionPerformed method
			public void actionPerformed(ActionEvent ev)
			{	//open METHOD actionPerformed
					
				//get text from removeUserField and pass it as arg to removeUser method
				chatServer.getBlockedWordsList();
							
			}   //close METHOD actionPerformed
			
		}   //close INNER CLASS anonymous
		);   //close actionListener call)
		blockedWordsSubPanel.add(getBlockedWordsButton);
			
			//removeUserSubPanel - panel
		JPanel removeUserSubPanel = new JPanel();
		removeUserSubPanel.setLayout(new FlowLayout() );
		removeUserSubPanel.setBorder(new TitledBorder("Remove User Panel"));
		controlArea.add(removeUserSubPanel);
			
			//removeUserFieldLabel - label
		JLabel removeUserFieldLabel = new JLabel("Enter User Name for Removal");
		removeUserSubPanel.add(removeUserFieldLabel);
		
			//removeUserField - text field
		removeUserField = new JTextField(15);
		removeUserField.setEditable(true);
		removeUserSubPanel.add(removeUserField);
		
			//removeButton - button
		JButton removeButton = new JButton("Remove User");
		removeButton.addActionListener( new ActionListener()
		{   //new INNER CLASS anonymous
				
			//register actionPerformed method
			public void actionPerformed(ActionEvent ev)
			{	//open METHOD actionPerformed
				
				//get text from removeUserField and pass it as arg to removeUser method
				userNameForRemoval = removeUserField.getText();
				chatServer.removeUser(userNameForRemoval);
				removeUserField.setText("");
						
			}   //close METHOD actionPerformed
			
		}   //close INNER CLASS anonymous
		);   //close actionListener call)
		removeUserSubPanel.add(removeButton);
			
		//JMenuBar and subcomponents
		menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu saveMenu = new JMenu("Save");
		JMenu quitMenu = new JMenu("Quit");
		JMenu helpMenu = new JMenu("Help");
		JMenuItem saveEventLogSubMenu = new JMenuItem("Event Log");
		JMenuItem saveChatAreaSubMenu = new JMenuItem("Chat Area");
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		JMenuItem aboutMI = new JMenuItem("About");
		
			//FileChooser
		fileChooser = new JFileChooser(new File("."));
			//set default saving filter to .txt files
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt files", "txt");
		fileChooser.setFileFilter(filter);
		//add exit menu item to quit menu
		quitMenu.add(exitMenuItem);
		
		//add save menu to file menu
		fileMenu.add(saveMenu);
		//add save text area choices to save menu
		saveMenu.add(saveEventLogSubMenu);
		saveMenu.add(saveChatAreaSubMenu);
		//add about MI to help menu
		helpMenu.add(aboutMI);

		//add ActionListener to exitMenuItem
		exitMenuItem.addActionListener( new ActionListener()
		{   //new INNER CLASS anonymous
						
			//register actionPerformed method
			public void actionPerformed(ActionEvent ev)
			{	//open METHOD actionPerformed
					
				try
				{	//open TRY
						
					//call to saveEventLog method
					chatServer.closeConnectionsAndExit();
						
				}	//close TRY
				catch(IOException ioe)
				{ 	
					//system exit - no need to record error
				}
						
			}   //close actionPerformed method
								
		}   //close INNER CLASS anonymous
		);   //close actionListener call
					
		//add ActionListener to saveEventLogSubMenu
		saveEventLogSubMenu.addActionListener( new ActionListener()
		{   //new INNER CLASS anonymous
				
			//register actionPerformed method
			public void actionPerformed(ActionEvent ev)
			{	//open METHOD actionPerformed
					
				//call to saveEventLog method
				saveEventLog();
					
			}   //close actionPerformed method
						
		}   //close INNER CLASS anonymous
		);   //close actionListener call
				
		//add ActionListener to saveEventLogSubMenu
		saveChatAreaSubMenu.addActionListener( new ActionListener()
		{   //new INNER CLASS anonymous
					
			//register actionPerformed method
			public void actionPerformed(ActionEvent ev)
			{	//open METHOD actionPerformed
					
				//call to saveChatArea
				saveChatArea();
				
			}   //close actionPerformed method
							
		}   //close INNER CLASS anonymous
		);   //close actionListener call
		
		//add ActionListener to about menu item
		aboutMI.addActionListener(new ActionListener() 
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				
				JOptionPane.showMessageDialog(null, "Created by Greg Byrne, December 2012\nEmail: byrne.greg@gmail.com\nVersion: " + versionID);
				
			}
			
			
		});
					
		//add "File" menu to JMenuBar
		menuBar.add(fileMenu);
		menuBar.add(quitMenu);
		menuBar.add(helpMenu);
					
		//mainComponents panel
		JPanel mainComponents = new JPanel();
		mainComponents.setLayout(new GridLayout(0, 3, 10, 10) ); //args(rows, cols, hgap, vgap)
		mainComponents.add(eventLog);
		mainComponents.add(chatArea);
		mainComponents.add(controlArea);
		mainComponents.add(menuBar);
		
		//add panels to frame
		add(menuBar, BorderLayout.NORTH);
		add(mainComponents, BorderLayout.CENTER);
			
		//some housekeeping variables - set windows' size and visibility
		setSize(1200, 600); //set size of window
		setVisible(true); //show window
	
	}	//close CONSTRUCTOR ChatServerGUI
	
	//METHOD: saveEventLog()
	//PRECONDITION:		JFileChooser
	//POSTCONDITION:	finds a place within computer to save file and
	//					calls overloaded saveEventLog(File file) method
	private void saveEventLog()
	{	//open METHOD saveEventLog
		
		//open JFileChooser and choose place to save file,
		//	call overloaded saveEventLog method with selected file
		if(fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
			saveEventLog(fileChooser.getSelectedFile());
		
	}	//close METHOD saveEventLog
	
	//METHOD: saveEventLog(File file)
	//PRECONDITION:		EventLogArea JTextArea
	//POSTCONDITION:	Saves a file containing EventLogArea JTextArea
	private void saveEventLog(File file)
	{	//open METHOD saveEventLog with file arg
		
		try
		{	//open TRY
			
			//create an print writer with an attached buffered writer with an attached 
			//	file writer. write entire text to file arg and close stream
			PrintWriter writer = new PrintWriter(new BufferedWriter (new FileWriter(file + ".txt")));
			displayEventLogArea.write(writer);
			writer.close();
			
			chatServer.displayEventMessage("EventLog saved -\n " + file + "\n");
			
		}	//close TRY
		catch(IOException ioe)
		{	//open CATCH
			
			//catch CANNOT.SAVE.FILE error
			chatServer.displayEventMessage("\nAn error has occured - please restart or contact the software developer.\nError Code: [IO]CANNOT.SAVE.FILE\n");
			
		}	//close CATCH
		
	}	//close METHOD saveEventLog

	//METHOD: saveChatArea()
	//PRECONDITION:		JFileChooser
	//POSTCONDITION:	finds a place within computer to save file and
	//					calls overloaded saveChatArea(File file) method
	private void saveChatArea()
	{	//open METHOD saveChatArea

		//open JFileChooser and choose place to save file,
		//	call overloaded saveEventLog method with selected file
		if(fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
			saveChatArea(fileChooser.getSelectedFile());
		
	}	//close METHOD saveChatArea
	
	//METHOD: saveEventLog(File file)
	//PRECONDITION:		EventLogArea JTextArea
	//POSTCONDITION:	Saves a file containing EventLogArea JTextArea
	private void saveChatArea(File file)
	{	//open METHOD saveChatArea with file arg
		
		try
		{	//open TRY

			//create an print writer with an attached buffered writer with an attached 
			//	file writer. write entire text to file arg and close stream
			PrintWriter writer = new PrintWriter(new BufferedWriter (new FileWriter(file + ".txt")));
			displayChatArea.write(writer);
			writer.close();
			
			chatServer.displayEventMessage("EventLog saved -\n " + file + "\n");
			
		}	//close TRY
		catch(IOException ioe)
		{	//open CATCH
			
			//catch CANNOT.SAVE.FILE error
			chatServer.displayEventMessage("\nAn error has occured - please restart or contact the software developer.\nError Code: [IO]CANNOT.SAVE.FILE\n");
			
		}	//close CATCH
		
	}	//close METHOD saveChatArea
	
}	//close CLASS ChatServerGUI