package com.flipturnapps.rc.client;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
		try {
			ImageIO.write(image, "png", new File("cli.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Path path = Paths.get("cli.png");
		byte[] data = null;
		try {
			data = Files.readAllBytes(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String line = "";
		for(int i = 0; i < data.length; i++)
		{
			
			line += data[i] + ",";
		}
		this.sendMessage(line);
		
		
	}

}
