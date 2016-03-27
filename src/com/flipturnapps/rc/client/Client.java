package com.flipturnapps.rc.client;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;

import com.flipturnapps.kevinLibrary.net.KClient;


public class Client extends KClient {

	private Robot robot;
	private Toolkit toolkit;
	private Dimension dim;

	public Client(String ip, int port) throws UnknownHostException, IOException 
	{
		super(ip, port);
	}

	@Override
	protected void disconnectedFromServer() 
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void readMessage(String read) 
	{
		if(robot == null)
		{
			try {
				robot = new Robot();
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(read != null && !read.equals(""))
		{
			String[] split = read.split(",");
			int x = Integer.parseInt(split[0]);
			int y = Integer.parseInt(split[1]);
			int button = 0;
			if(split[2].equals("l"))
			{
				button = InputEvent.BUTTON1_DOWN_MASK;
			}
			if(split[2].equals("r"))
			{
				button = InputEvent.BUTTON2_DOWN_MASK;
			}
			if(split[2].equals("m"))
			{
				button = InputEvent.BUTTON3_DOWN_MASK;
			}
			robot.mouseMove(x, y);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			robot.mousePress(button);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			robot.mouseRelease(button);
			takeAndSendScreenshot();

		}

	}

	private void takeAndSendScreenshot() 
	{
		if(dim == null)
		{
			dim = Toolkit.getDefaultToolkit().getScreenSize();
		}
		Rectangle rect = new Rectangle(0, 0, (int) dim.getWidth(), (int) dim.getHeight());
		BufferedImage image = robot.createScreenCapture(rect);
		String imgPath = "cli.png";
		try {
			ImageIO.write(image, "png", new File(imgPath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 WritableRaster raster = image.getRaster();
		 DataBufferByte dataBuff   = (DataBufferByte) raster.getDataBuffer();
		 byte[] data = dataBuff.getData();
		try {
			DataOutputStream out = new DataOutputStream(this.getOutputStream());
			out.writeInt(data.length);

			out.write(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
