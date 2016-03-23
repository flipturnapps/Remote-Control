package com.flipturnapps.rc.server;

import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFrame;

public class ServerMain {

	public ServerMain() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) 
	{
		new ServerMain().go();

	}

	private void go()
	{
		JFrame frame = new JFrame();
		frame.setSize(300,300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	 Server server = null;
	try {
		server = new Server(25565);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 Scanner scanner = new Scanner(System.in);
	while(true)
	{
		server.sendAll(scanner.nextLine());
	}
	}

}
