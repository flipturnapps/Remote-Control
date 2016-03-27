package com.flipturnapps.rc.server;

import java.io.IOException;
import java.net.Socket;

import com.flipturnapps.kevinLibrary.net.ClientData;
import com.flipturnapps.kevinLibrary.net.KServer;

public class RCClient extends ClientData implements Runnable
{

	public RCClient(Socket socket, KServer<?> server) throws IOException 
	{
		super(socket, server);
		new Thread(this).start();
	}

	@Override
	public void run()
	{
		this.getReader().stop();
		try {
			new ScreenshotReader(this.getSocket().getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	

}
