package com.flipturnapps.rc.server;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

import javax.imageio.ImageIO;

import com.flipturnapps.kevinLibrary.net.KServer;

public class Server extends KServer<RCClient>
{
	byte[] bytes;
	int place = 0;
	private ServerFrame frame;
	public Server(int port) throws IOException 
	{
		super(port);
		frame = new ServerFrame();
		frame.setVisible(true);
	}

	@Override
	protected void newMessage(String message, RCClient client) 
	{
	
		String[] split = message.split(",");
		byte[] bytes = new byte[split.length];
		for (int i = 0; i < bytes.length; i++)
		{
			bytes[i] = Byte.parseByte(split[i]);
		}
			try
			{
				BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytes));
				ImageIO.write(img, "png", new File("serv.png"));
				frame.repaint();
				System.out.println("dafsf");
			}
			catch (Exception ex)
			{

			}
		
			
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
