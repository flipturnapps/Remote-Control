package com.flipturnapps.rc.client;

import java.io.IOException;
import java.net.UnknownHostException;

public class ClientMain {

	public ClientMain() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) 
	{
		new ClientMain().go();

	}

	private void go()
	{
		try {
			Client client = new Client("localhost",25565);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
