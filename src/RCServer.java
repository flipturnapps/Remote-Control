import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;


public class RCServer extends ServerSocket
{

	public static final int PORT = 12347;
	public static final int BUFFER_SIZE = 1024;//(int) Math.pow(2, 14);
	private Socket socket;
	private Robot robot;
	private Rectangle rect;
	private File output;
	private OutputStream outputStream;
	private boolean continueUpdates;
	private boolean hasAck;
	private boolean startUpdates;
	private BufferedReader reader;
	private boolean shouldListenToClient;
	private boolean gotPassword;
	private String serverPassword;
	public RCServer(String password) throws IOException 
	{
		super(PORT);
		new Thread(new ScreenshotUpdater()).start();
		continueUpdates = true;
		hasAck = false;
		startUpdates = false;
		shouldListenToClient = true;
		gotPassword = false;
		serverPassword = password;
	}

	private class ClientListener implements Runnable
	{	

		@Override
		public void run()
		{
			while(shouldListenToClient)
			{
				String line = null;
				try {
					line = reader.readLine();
				} catch (IOException e) {
					if(shouldListenToClient)
					{
					killSelf();
					
					return;
					}
				}
				if(line != null)
					useClientMessage(line);
			}

		}

		private void useClientMessage(String line)
		{
			System.out.println("using client message");
			if(!gotPassword)
			{
				String clientPassword = line;
				if(clientPassword.equals(serverPassword))
				{
					gotPassword = true;
					startUpdates = true;
				}
				else
				{
					killSelf();
				}
			}
			else if(line.equals("ack"))
				hasAck = true;
		
		}

	}
	private class ScreenshotUpdater implements Runnable
	{

		@Override
		public void run()
		{
			try {
				socket = accept();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			try {
				outputStream = socket.getOutputStream();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				robot = new Robot();
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			rect = new Rectangle(dim);
			output = new File("pic.png");					
			new Thread(new ClientListener()).start();

			while(!startUpdates)
			{
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Blocking until startUpdates is true. (ie password succeeds)");
			}
			while(continueUpdates)
			{
				updateScreenshot();
				hasAck = false;
				System.out.println("will block until get an ack");
				while(continueUpdates && (!hasAck))
				{
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void updateScreenshot()
	{
		BufferedImage image = robot.createScreenCapture(rect);

		try {
			ImageIO.write(image, "png", output);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		long bytes = output.length();
		System.out.println("bytes of file " + bytes);
		byte[] bytesAsArray = RCMain.longToBytes(bytes);
		ByteArrayInputStream bStream = new ByteArrayInputStream(bytesAsArray);
		try {
			writeToOutputStream(bStream);
		} catch (IOException e2) {
			System.out.println("write exception bytes");
			e2.printStackTrace();
		}
		System.out.println("start send image");
		FileInputStream stream = null;
		try {
			stream = new FileInputStream(output);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		long startTime = System.currentTimeMillis();
		try {
			writeToOutputStream(stream);
		} catch (IOException e) {
			System.out.println("write exception image");
			e.printStackTrace();
		}

		System.out.println("all sent! It took " + ((System.currentTimeMillis()-startTime+0.0)/(1000+0.0) + " seconds"));
	}

	public void killSelf() 
	{
		shouldListenToClient = false;
		continueUpdates = false;
		startUpdates = true;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try
		{
			socket.close();
			this.close();
		}
		catch(IOException ex)
		{
			System.out.println("error when killing self");
			ex.printStackTrace();
		}
		RCMain.main(new String[]{serverPassword});
	}

	private void writeToOutputStream(InputStream stream) throws IOException
	{
		int readCount;
		byte[] buffer = new byte[BUFFER_SIZE];
		readCount = stream.read(buffer);
		System.out.println("read " + readCount + " bytes from input");
		while(readCount != -1)		
		{	
			outputStream.write(buffer, 0, readCount);
			System.out.println("wrote " + readCount + " bytes");
			outputStream.flush();
			readCount = stream.read(buffer);
			System.out.println("read " + readCount + " bytes from input");
		}

		stream.close();

	}


}
