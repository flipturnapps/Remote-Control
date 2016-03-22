package com.flipturnapps.rc.server;

import java.io.IOException;
import java.net.Socket;

import com.flipturnapps.kevinLibrary.net.ClientData;
import com.flipturnapps.kevinLibrary.net.KServer;

public class RCClient extends ClientData
{

	public RCClient(Socket socket, KServer<?> server) throws IOException {
		super(socket, server);
	}
	
	

}
