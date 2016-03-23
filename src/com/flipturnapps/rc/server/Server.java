package com.flipturnapps.rc.server;

import java.io.IOException;
import java.net.Socket;

import com.flipturnapps.kevinLibrary.net.KServer;

public class Server extends KServer<RCClient>
{

	public Server(int port) throws IOException 
	{
		super(port);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void newMessage(String message, RCClient client) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected RCClient getNewClientData(Socket socket, KServer<RCClient> kServer)
	{
		try {
			return new RCClient(socket, kServer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void newClient(RCClient data) 
	{
		// TODO Auto-generated method stub
		
	}

}
