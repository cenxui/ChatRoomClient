package test;

import javax.swing.JFrame;

import client.Client;

public class ClientTest {

	public static void main(String[] args) {
		Client client = new Client("127.0.0.1");
		client.startRuning();
		client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
