import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;


public class RCClient extends Socket implements Runnable
{

	private Gui gui;
	private InputStream inputStream;
	private PrintWriter writer;
	private String password;

	public RCClient(String ip, String password) throws UnknownHostException, IOException
	{
		super(ip,RCServer.PORT);
		new Thread(this).start();
		System.out.println("connection");
		gui = new Gui();
		this.password = password;
	}


	public void refresh() 
	{		
		System.out.println("start");
		try {
			inputStream = this.getInputStream();
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		System.out.println("start bytes read");
		try {
			readInputToStream(stream, 8);
		} catch (IOException e2) {
			System.out.println("read exception bytes");
			e2.printStackTrace();
		}	
		byte[] bytes = stream.toByteArray();
		long byteCount = RCMain.bytesToLong(bytes);
		System.out.println("the pic file will be " + byteCount + " long");
		File outputFile = new File("pic.png");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(outputFile);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("starting reading");
		try {
			readInputToStream(fos,byteCount);
		} catch (IOException e1) {
			System.out.println("read exception image");
			e1.printStackTrace();
		}
		System.out.println("try read image");
		BufferedImage image = null;
		try {
			image = ImageIO.read(outputFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(image == null)
			System.out.println("null!");
		gui.setImage(image);
		System.out.println("finish");
		writer.println("ack");
		writer.flush();

	}

	private void readInputToStream(OutputStream fos, long bytes) throws IOException
	{
		byte[] buffer = new byte[RCServer.BUFFER_SIZE];
		int readCount = 0;
		long readBytes = 0;

		while(readBytes < bytes && readCount != -1)
		{	
			readCount = inputStream.read(buffer);
			readBytes += readCount;		
			System.out.println("read " + readCount + " bytes. " + (bytes - readBytes) + " bytes left to read.");

			if(readCount != -1)
			{
				fos.write(buffer, 0, readCount);
				System.out.println("wrote " + readCount + " bytes");	
			}
		}
		fos.flush();
		fos.close();
	}


	@Override
	public void run() 
	{
		try
		{
		Thread.sleep(1000);
		writer = new PrintWriter(this.getOutputStream());
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		} 
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.println(password);
		writer.flush();
		while(true)		
		{
			this.refresh();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
