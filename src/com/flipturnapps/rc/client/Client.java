package com.flipturnapps.rc.client;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.net.UnknownHostException;

import com.flipturnapps.kevinLibrary.net.KClient;

public class Client extends KClient {

	private Robot robot;

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

		}

	}

}
