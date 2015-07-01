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
	private float compression;
	private Socket socket;
	private Robot robot;
	private Rectangle rect;
	private File output;
	private ImageWriter writer;
	private ImageWriteParam param;
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
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		rect = new Rectangle(dim);
		output = new File("pic.png");		

		Iterator<ImageWriter>writers =  ImageIO.getImageWritersByFormatName("jpg");
		writer = (ImageWriter) writers.next();

		param = writer.getDefaultWriteParam();

		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		param.setCompressionQuality(compression);


		while(true)
		{
			updateScreenshot();
			try {
				Thread.sleep(30000);
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
		writer.setOutput(iis);	   

		try {
			writer.write(null, new IIOImage(image, null, null), param);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		long bytes = output.length();
		System.out.println("bytes of file " + bytes);
		byte[] bytesAsArray = RCMain.longToBytes(bytes);
		ByteArrayInputStream bStream = new ByteArrayInputStream(bytesAsArray);
		writeToOutputStream(bStream);
		
		FileInputStream stream = null;
		try {
			stream = new FileInputStream(output);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		long startTime = System.currentTimeMillis();
		writeToOutputStream(stream);

		System.out.println("all sent! It took " + ((System.currentTimeMillis()-startTime+0.0)/(1000+0.0) + " seconds"));
	}

	private void writeToOutputStream(InputStream stream)
	{
		int readCount;
		byte[] buffer = new byte[1024];
		while(true)
		{
			try
			{
				readCount = stream.read(buffer);
				if(readCount != -1)
				{
					socket.getOutputStream().write(buffer, 0, readCount);
					socket.getOutputStream().flush();
				}
				else
					break;
			}
			catch(Exception ex)
			{
				System.out.println("gotta error");
				break;
			}
		}
		try {
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
