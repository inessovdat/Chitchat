import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class ChatFrame extends JFrame implements ActionListener, KeyListener {

	public JTextArea output;
	private JTextField input;
	public JTextField textField;
	public JTextArea activeUsers;
	private JButton login;
	private JButton logout;
	private JTextField privateMsg;
	private JButton instructions;

	private boolean active = false;
	public Boolean global = true;
	private boolean firstLogin = true;

	public ReceivedMessageRobot msgRobot;

	public ChatFrame() {
		super();
		this.setTitle("Chit Chat");
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		msgRobot = new ReceivedMessageRobot(this);

		Container pane = this.getContentPane();
		pane.setLayout(new GridBagLayout());

		// Polje za vnos vzdevka
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel nickname = new JLabel("Vzdevek:");
		panel.add(nickname);
		textField = new JTextField(System.getProperty("user.name"), 15); // Vzdevek je na zaèetku nastavljen na uporabniško ime
		panel.add(textField);
		GridBagConstraints panelConstraint = new GridBagConstraints();
		panelConstraint.fill = GridBagConstraints.BOTH;
		panelConstraint.weightx = 1;
		panelConstraint.weighty = 0;
		panelConstraint.gridx = 0;
		panelConstraint.gridy = 0;
		pane.add(panel, panelConstraint);

		// Gumb za prijavo
		login = new JButton("Prijava");
		panel.add(login);
		login.addActionListener(this);
		login.setForeground(Color.blue);
		login.setBackground(Color.white);

		// Gumb za odjavo
		logout = new JButton("Odjava");
		panel.add(logout);
		logout.addActionListener(this);
		logout.setForeground(Color.blue);
		logout.setBackground(Color.white);

		// Gumb za okno z navodili
		instructions = new JButton("Navodila");
		panel.add(instructions);
		instructions.addActionListener(this);
		
		// Prikaz sporoèil
		this.output = new JTextArea(30, 40);
		JScrollPane scrollpaneMessage = new JScrollPane(output); // Drsnik
		this.output.setEditable(false);
		GridBagConstraints outputConstraint = new GridBagConstraints();
		outputConstraint.fill = GridBagConstraints.BOTH;
		outputConstraint.weightx = 1;
		outputConstraint.weighty = 1;		
		outputConstraint.gridx = 0;
		outputConstraint.gridy = 1;
		pane.add(scrollpaneMessage, outputConstraint);
		
		//Besedilo za vnos sporoèil
		JLabel messageText = new JLabel("Vnesite sporoèilo za pogovor:");
		panel.add(messageText);
		GridBagConstraints messageConstraint = new GridBagConstraints();
		messageConstraint.fill = GridBagConstraints.BOTH;
		messageConstraint.weightx = 1;
		messageConstraint.weighty = 0;
		messageConstraint.gridx = 0;
		messageConstraint.gridy = 2;
		pane.add(messageText, messageConstraint);

		// Prostor za vnos sporoèil
		this.input = new JTextField(40);
		GridBagConstraints inputConstraint = new GridBagConstraints();
		inputConstraint.fill = GridBagConstraints.BOTH;
		inputConstraint.weightx = 1;
		inputConstraint.weighty = 0;
		inputConstraint.gridx = 0;
		inputConstraint.gridy = 3;
		pane.add(input, inputConstraint);
		input.addKeyListener(this);

		// Napis za aktivne uporabnike
		JLabel activeUsersText = new JLabel("Aktivni uporabniki:");
		panel.add(activeUsersText);
		GridBagConstraints activeUsersConstraint = new GridBagConstraints();
		activeUsersConstraint.fill = GridBagConstraints.BOTH;
		activeUsersConstraint.weightx = 1;
		activeUsersConstraint.weighty = 0;
		activeUsersConstraint.gridx = 1;
		activeUsersConstraint.gridy = 0;
		pane.add(activeUsersText, activeUsersConstraint);
			
		// Prostor za izpis aktivnih uporabnikov
		activeUsers = new JTextArea(30,20); //this  pr obeh so se zbrisal
		JScrollPane scrollpaneAUsers = new JScrollPane(activeUsers);
		activeUsers.setEditable(false);
		GridBagConstraints aUsersConstraint = new GridBagConstraints();
		aUsersConstraint.fill = GridBagConstraints.BOTH;
		aUsersConstraint.weightx = 1;
		aUsersConstraint.weighty = 1;
		aUsersConstraint.gridx = 1;
		aUsersConstraint.gridy = 1;
		pane.add(scrollpaneAUsers, aUsersConstraint);

		// Besedilo za vnos prejemnika zasebnega sporoèila
		JLabel privateMsgText = new JLabel("Vnesite prejemnika za zasebni pogovor:");
		panel.add(privateMsgText);
		GridBagConstraints privateMsgTextConstraint = new GridBagConstraints();
		privateMsgTextConstraint.fill = GridBagConstraints.BOTH;
		privateMsgTextConstraint.weightx = 1;
		privateMsgTextConstraint.weighty = 0;
		privateMsgTextConstraint.gridx = 1;
		privateMsgTextConstraint.gridy = 2;
		pane.add(privateMsgText, privateMsgTextConstraint);

		// Prostor za vnos prejemnika zasebnega sporoèila
		this.privateMsg = new JTextField(40);
		GridBagConstraints privateMsgConstraint = new GridBagConstraints();
		privateMsgConstraint.fill = GridBagConstraints.BOTH;
		privateMsgConstraint.weightx = 1;
		privateMsgConstraint.weighty = 0;
		privateMsgConstraint.gridx = 1;
		privateMsgConstraint.gridy = 3;
		pane.add(privateMsg, privateMsgConstraint);
		
		//  Ko poženemo program, je kurzor v okencu za vnos javnega sporoèila
		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				input.requestFocusInWindow();
			}
		});
		
	}
	
	/**
	 * @param person - the person sending the message
	 * @param message - the message content
	 */
	
	public void addMessage(String person, String message) {
		String chat = this.output.getText();
		this.output.setText(chat + person + ": " + message + "\n");
	}

	public boolean getStatus(){
		return  active;
	}

	public static void getInstructions(String infoMessage, String title){
		JOptionPane.showMessageDialog(null, infoMessage, title, JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton eventButton = (JButton)e.getSource();
		
		if (eventButton.getText() == "Prijava"){
			String person = textField.getText();
			try {App.login(person);
				if (firstLogin){
					msgRobot.activate();
					firstLogin = false;
				}
				active = true;
				this.logout.setEnabled(true);
				this.login.setEnabled(false);
				textField.setEnabled(false);
				privateMsg.setEnabled(true);			
				input.setEnabled(true);
				activeUsers.setEnabled(true);
				
				//System.out.println(responseBody);
				
			}  catch (IOException e1) {
				e1.printStackTrace();
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if(eventButton.getText() == "Odjava"){
			String person = textField.getText();
			try {App.logout(person);
			
			active = false;
			this.logout.setEnabled(false);
			this.login.setEnabled(true);
			textField.setEnabled(true);
			//output.setEnabled(false);
			output.setText(null);
			privateMsg.setEnabled(false);
			input.setEnabled(false);

			//System.out.println(responseBody);

			}  catch (IOException e1) {
				e1.printStackTrace();
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		else if (eventButton.getText() == "Navodila"){

			String instructionsText = "\n Za pogovarjanje na ChitChat strežniku se morate najprej prijaviti s  klikom na gumb \"Prijava\". \n"
					+ "Èe želite lahko pred prijavo spremenite svoj vzdevek, ki je primarno nastavljen na uporabniško ime. \n"
					+ "Spodaj desno je okvirèek za vnos prejemnika, èe želite zasebni pogovor. Prejemnik mora biti izbran iz seznama  \n"
					+ "aktivnih uporabnikov, ki se nahaja nad okvirèkom. \n"
					+ "Èe prejemnika ne vnesete, bo pogovor javen, sporoèilo bo poslano vsem aktivnim uporabnikom. \n"
					+ "V levem spodnjem kotu se nahaja okvirèek, kamor vnesete svoje sporoèilo in ga pošljete s tipko Enter.\n\n";

			getInstructions(instructionsText, "Navodila uporabe");
		}
	}

	
	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getSource() == this.input) {
			if (e.getKeyChar() == '\n') {
				
				if (privateMsg.getText().equals("")){
					
					//Sporoèilo je javno
					try {App.sendMessage(textField.getText(), global, input.getText());
						this.addMessage(textField.getText(), this.input.getText());
						this.input.setText(null);	// Okvirèek za vpisovanje besedila se spet nastavi na prazno
						
						} catch (URISyntaxException | IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				}
				
				// Zasebno sporoèilo
				else{ 
					try {App.sendPrivateMessage(textField.getText(), global, privateMsg.getText(), input.getText());
						this.addMessage("(zasebno sporoèilo) " + textField.getText(), this.input.getText());
						this.input.setText(null);	// Okvirèek za vpisovanje besedila se spet nastavi na prazno
					
					} catch (URISyntaxException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
						
				}
			}
		}		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
