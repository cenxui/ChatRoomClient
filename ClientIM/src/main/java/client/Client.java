package client;

import java.io.*;
import java.net.*;
import java.awt.*;
import javax.swing.*;

public class Client extends JFrame {

	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message = "";
	private String serverIP;
	private String clientIP;
	private Socket connection;

	/**
	 * constructor
	 * 
	 * @param host
	 */
	public Client(String host) {
		super("Client mofo!");
		this.serverIP = host;
		this.userText = new JTextField();
		this.userText.setEditable(false);
		this.userText.addActionListener((e) -> {
			sendMessage(e.getActionCommand());
			this.userText.setText("");
		});
		add(this.userText, BorderLayout.NORTH);
		this.chatWindow = new JTextArea();
		add(new JScrollPane(this.chatWindow), BorderLayout.CENTER);
		setSize(300, 150);
		setVisible(true);
	}

	public void startRuning() {
		try {
			connectToServer();
			setUpStream();
			whileChatting();
		} catch (EOFException e) {
			e.printStackTrace();
			showMessage("\n Client terminated connection");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeCrap();
		}
	}

	private void connectToServer() throws UnknownHostException, IOException {
		showMessage("Attempting connection \n");
		this.connection = new Socket(serverIP, 6789);
		this.clientIP = InetAddress.getLocalHost().getHostName() + " , " + this.connection.getLocalPort();
		showMessage("connected to:" + this.connection.getInetAddress().getHostName());
	}

	private void setUpStream() throws IOException {
		this.output = new ObjectOutputStream(this.connection.getOutputStream());
		this.output.flush();
		this.input = new ObjectInputStream(this.connection.getInputStream());
		showMessage("\n Dude your streams are now good to go");
	}

	private void whileChatting() throws IOException {
		ableToType(true);
		do {
			try {
				this.message = (String) this.input.readObject();
				showMessage("\n" + this.message);
			} catch (ClassNotFoundException e) {
				showMessage("\n I don't know that object type");
			}

		} while (!this.message.equals("SERVER - END"));
	}

	private void closeCrap() {
		showMessage("\n closing crap down...");
		ableToType(false);
		try {
			this.output.close();
			this.input.close();
			this.connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendMessage(String message) {
		try {
			this.output.writeObject(this.clientIP + " - " + message);
			this.output.flush();
		} catch (IOException e) {
			this.chatWindow.append("\n something messed up sending message hoss!! ");
		}
	}

	private void showMessage(final String message) {
		SwingUtilities.invokeLater(() -> {
			this.chatWindow.append(message);
		});
	}

	private void ableToType(final boolean tof) {
		SwingUtilities.invokeLater(() -> {
			this.userText.setEditable(tof);
		});
	}
}
