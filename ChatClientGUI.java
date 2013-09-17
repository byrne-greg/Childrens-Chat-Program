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

import javax.swing.*;
import javax.swing.border.*;

@SuppressWarnings("serial")
public class ChatClientGUI extends JFrame
{	//open CLASS ChatClientGUI

	private String versionID = "3.0";
	
	//create GUI variable placeholders
    private JMenuBar menuBar;
    private JTextField enterField;
    public JTextField changeNameField;
	public JTextArea displayChatArea;
	private JCheckBox boldFont;
	private JCheckBox italicFont;
	private JComboBox<String> fontDropDown;
	public static final String userMessageSeparator = " >>> ";
	
	//create ChatClient and serverName variable placeholders
	public ChatClient chatClient;
	public String serverName;
	
	
//---------------------------------------------------------
//		Constructors & Methods
//---------------------------------------------------------
	
	//METHOD: ChatClientGUI(ChatClient chatClient, String serverName)
	//PRECONDITION:		ChatClient object and String serverName
	//POSTCONDITION:	instantiated ChatClientGUI object
	public ChatClientGUI(final ChatClient chatClient, String serverName)
	{	//open CONSTRUCTOR ChatClientGUI
	
		super("Children's Chat Program " + "- " + serverName + "'s Chat Client");
		this.chatClient = chatClient;
		
		//some housekeeping variables - close operation and layout manager
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
			
		//components required to be final upon execution in actionPerformed
		
		//chatArea panel and subcomponents
		final JPanel chatArea = new JPanel();
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
				chatClient.sendMessage(message);
				enterField.setText("");			
			
			}   //close METHOD actionPerformed
		
		}   //close INNER CLASS anonymous
		);   //close actionListener call)
		chatArea.add(enterField,BorderLayout.SOUTH);
	
		//controlArea panel and subcomponents
		final JPanel controlArea = new JPanel();
		controlArea.setLayout(new GridLayout(2, 0, 25, 25) );
		controlArea.setBorder(new TitledBorder("Control Area"));
	
			//buttonSubPanel - panel
		final JPanel buttonSubPanel = new JPanel();
		buttonSubPanel.setLayout(new FlowLayout());
		controlArea.add(buttonSubPanel);
	
			//clientListButton - button
		final JButton clientListButton = new JButton("List Users in Room");
		clientListButton.addActionListener( new ActionListener()
		{   //new INNER CLASS anonymous
		
			//register actionPerformed method
			public void actionPerformed(ActionEvent ev)
			{	//open METHOD actionPerformed
		
					//send usersInAttendance request
					chatClient.sendCommand(".usersInAttendance");
					
			}   //close METHOD actionPerformed()
		
		}   //close INNER CLASS anonymous
		);   //close actionListener call)
		buttonSubPanel.add(clientListButton);
	
			//quitButton - button
		final JButton quitButton = new JButton("Quit");
		quitButton.addActionListener( new ActionListener()
		{   //new INNER CLASS anonymous
		
			//register actionPerformed method
			public void actionPerformed(ActionEvent ev)
			{	//open METHOD actionPerformed
			
				chatClient.closeConnectionsAndExit();
			
			}   //close METHOD actionPerformed()
		
		}   //close INNER CLASS anonymous
		);   //close actionListener call)
		buttonSubPanel.add(quitButton);
	
			//changeNameSubPanel - panel
		final JPanel changeNameSubPanel = new JPanel();
		changeNameSubPanel.setLayout(new FlowLayout() );
		changeNameSubPanel.setBorder(new TitledBorder("Change User Name"));
		controlArea.add(changeNameSubPanel);
	
			//changeNameField - text field
		changeNameField = new JTextField(15);
		changeNameField.setEditable(true);
		changeNameSubPanel.add(changeNameField);
	
			//changeNameButton - button
		final JButton changeNameButton = new JButton("Change User Name");
		changeNameButton.addActionListener( new ActionListener()
		{   //new INNER CLASS anonymous
		
			//register actionPerformed method
			public void actionPerformed(ActionEvent ev)
			{	//open METHOD actionPerformed
				
				chatClient.changeUserName();
				changeNameField.setText("");
				
			}   //close METHOD actionPerformed()
		
		}   //close INNER CLASS anonymous
		);   //close actionListener call)
		changeNameSubPanel.add(changeNameButton);
		
			//setFontSubPanel - panel
		final JPanel setFontSubPanel = new JPanel();
		setFontSubPanel.setLayout(new FlowLayout() );
		setFontSubPanel.setBorder(new TitledBorder("Change Font Style"));
		controlArea.add(setFontSubPanel);
		
		boldFont = new JCheckBox("Bold");
		boldFont.addActionListener( new ActionListener()
		{   //new INNER CLASS anonymous
		
			//register actionPerformed method
			public void actionPerformed(ActionEvent ev)
			{	//open METHOD actionPerformed
			
				setNewFont();
			
			}   //close METHOD actionPerformed()
		
		}   //close INNER CLASS anonymous
		);   //close actionListener call)
		setFontSubPanel.add(boldFont);
	
		italicFont = new JCheckBox("Italic");
		italicFont.addActionListener( new ActionListener()
		{   //new INNER CLASS anonymous
		
			//register actionPerformed method
			public void actionPerformed(ActionEvent ev)
			{	//open METHOD actionPerformed
			
				setNewFont();
			
			}   //close METHOD actionPerformed()
		
		}   //close INNER CLASS anonymous
		);   //close actionListener call)
		setFontSubPanel.add(italicFont);
		
		//create fontList array
		String[] fontList = {"Serif", "Monospaced", "SansSerif"};
		
			//fontDropDown - JComboBox
		fontDropDown = new JComboBox<String>(fontList);
		fontDropDown.setMaximumRowCount(3);
		fontDropDown.addItemListener( new ItemListener()
		{   //new INNER CLASS anonymous
		
			//register itemStateChanged method
			public void itemStateChanged(ItemEvent ev)
			{	//open METHOD itemStateChanged
					
				setNewFont();
			
			}   //close METHOD actionPerformed()
		
		}   //close INNER CLASS anonymous
		);   //close actionListener call)
		setFontSubPanel.add(fontDropDown);
	
		//JMenuBar and subcomponents
		menuBar = new JMenuBar();
		JMenu windowMenu = new JMenu("Window");
		JMenu themeColSubMenu = new JMenu("Theme Colour");
		JMenu helpMenu = new JMenu("Help");
		JMenuItem changeThemeColMenuItem = new JMenuItem("Change Theme Colour");
		JMenuItem defaultThemeColMenuItem = new JMenuItem("Default Theme Colour");
		JMenuItem aboutMI = new JMenuItem("About");
		
		//add menu items to "Window" menu
		windowMenu.add(themeColSubMenu);
		//add menu items to "Theme Colour" menu
		themeColSubMenu.add(changeThemeColMenuItem);
		themeColSubMenu.add(defaultThemeColMenuItem);
		//add menu items to "Help" menu
		helpMenu.add(aboutMI);
		
		//add ActionListener to changeBackgroundColMenuItem
		changeThemeColMenuItem.addActionListener( new ActionListener()
		{   //new INNER CLASS anonymous
				
			//register actionPerformed method
			public void actionPerformed(ActionEvent ev)
			{	//open METHOD actionPerformed
				
				Color themeColour = null;
				themeColour = JColorChooser.showDialog(ChatClientGUI.this, "Choose a Colour" , themeColour);
				
				chatArea.setBackground(themeColour);
				controlArea.setBackground(themeColour);
				buttonSubPanel.setBackground(themeColour);
				changeNameSubPanel.setBackground(themeColour);
				setFontSubPanel.setBackground(themeColour);
				
				clientListButton.setBackground(themeColour);
				changeNameButton.setBackground(themeColour);
				quitButton.setBackground(themeColour);
				
			}   //close actionPerformed method
						
		}   //close INNER CLASS anonymous
		);   //close actionListener call
				
		//add ActionListener to changeBackgroundColMenuItem
		defaultThemeColMenuItem.addActionListener( new ActionListener()
		{   //new INNER CLASS anonymous
						
			//register actionPerformed method
			public void actionPerformed(ActionEvent ev)
			{	//open METHOD actionPerformed
						
				Color themeColour = null;
						
				chatArea.setBackground(themeColour);
				controlArea.setBackground(themeColour);
				buttonSubPanel.setBackground(themeColour);
				changeNameSubPanel.setBackground(themeColour);
				setFontSubPanel.setBackground(themeColour);
										
				clientListButton.setBackground(themeColour);
				changeNameButton.setBackground(themeColour);
				quitButton.setBackground(themeColour);
						
			}   //close actionPerformed method
								
		}   //close INNER CLASS anonymous
		);   //close actionListener call
	
		//add actionListener to About menu Item
		aboutMI.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				
				JOptionPane.showMessageDialog(null, "Created by Greg Byrne, December 2012\nEmail: byrne.greg@gmail.com\nVersion: " + versionID);
				
			}
			
		});
		
		//add "Window" menu to JMenuBar
		menuBar.add(windowMenu);
		menuBar.add(helpMenu);
	
		//mainComponents panel
		JPanel mainComponents = new JPanel();
		mainComponents.setLayout(new GridLayout(0, 2, 0, 0) ); //args(rows, cols, hgap, vgap)
		mainComponents.add(chatArea);
		mainComponents.add(controlArea);
	
		//add panels to frame
		add(mainComponents, BorderLayout.CENTER);
		add(menuBar, BorderLayout.NORTH);
		
		//some housekeeping variables - set windows' size and visibility
		setSize(1050, 600); //set size of window
		setVisible(true); //show window
	
	}	//close CONSTRUCTOR ChatClientGUI
	
	//METHOD: setNewFont()
	//PRECONDITION:		ChatClientGUI object
	//POSTCONDITION:	amended JTextArea and JTextField font
	private void setNewFont()
	{	//open METHOD setNewFont
		
		//set default font
		Font font = displayChatArea.getFont();
		
		//create fontStyle
		int fontStyle = Font.PLAIN;
		fontStyle += (boldFont.isSelected() ? Font.BOLD : Font.PLAIN);
		fontStyle += (italicFont.isSelected() ? Font.ITALIC : Font.PLAIN);
		 
		//create fontName
		String fontName;
		fontName = (String) fontDropDown.getSelectedItem();
		
		//create new font with font name and font style
		Font newFont = new Font (fontName, fontStyle, font.getSize() );
	
		//set font
		enterField.setFont(newFont);
		displayChatArea.setFont(newFont);
		
	}	//close METHOD setNewFont
	
}	//close CLASS ChatClientGUI
