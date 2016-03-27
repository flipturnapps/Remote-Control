package com.flipturnapps.rc.server;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class ScreenshotReader extends DataInputStream implements Runnable
{

	public ScreenshotReader(InputStream arg0)
	{
		super(arg0);
		new Thread(this).start();
	}


	public void doReading() throws IOException
	{
		int length = this.readInt();
		if(length>0) 
		{
		    byte[] data = new byte[length];
		    this.readFully(data, 0, data.length);
		    BufferedImage img = ImageIO.read(new ByteArrayInputStream(data));
			ImageIO.write(img, "png", new File("serv.png"));
		}
	}


	@Override
	public void run() 
	{
		while(true)
		{
			try {
				doReading();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
