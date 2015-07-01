import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;


public class RCServer extends ServerSocket implements Runnable
{

	public static final int PORT = 12347;
	public static final int BUFFER_SIZE = (int) Math.pow(2, 14);
	private float compression;
	private Socket socket;
	private Robot robot;
	private Rectangle rect;
	private File output;
	private OutputStream outputStream;

	public RCServer(float compression) throws IOException 
	{
		super(PORT);
		this.compression = compression;
		new Thread(this).start();
	}

	@Override
	public void run()
	{
		try {
			socket = this.accept();
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
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		rect = new Rectangle(dim);
		output = new File("pic.png");		

		


		while(true)
		{
			updateScreenshot();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void updateScreenshot()
	{
		BufferedImage image = robot.createScreenCapture(rect);

		ImageOutputStream iis = null;
		try {
			iis = ImageIO.createImageOutputStream(output);
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		  

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

	private void writeToOutputStream(InputStream stream) throws IOException
	{
		int readCount;
		byte[] buffer = new byte[BUFFER_SIZE];
		readCount = stream.read(buffer);
		while(readCount != -1)
		{				
			outputStream.write(buffer, 0, readCount);
			outputStream.flush();
			readCount = stream.read(buffer);
		}

		stream.close();

	}


}
