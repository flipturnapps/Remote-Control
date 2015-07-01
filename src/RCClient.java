import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;


public class RCClient extends Socket implements Runnable
{

	private Gui gui;
	private InputStream inputStream;

	public RCClient(String ip) throws UnknownHostException, IOException
	{
		super(ip,RCServer.PORT);
		new Thread(this).start();
		System.out.println("connection");
		gui = new Gui();
		inputStream = this.getInputStream();
	}


	public void refresh() 
	{		
		System.out.println("start");
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			readInputToStream(stream, 8);
		} catch (IOException e2) {
			System.out.println("read exception bytes");
			e2.printStackTrace();
		}
		byte[] bytes = stream.toByteArray();
		long byteCount = RCMain.bytesToLong(bytes);

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

	}

	private void readInputToStream(OutputStream fos, long bytes) throws IOException
	{
		byte[] buffer = new byte[RCServer.BUFFER_SIZE];
		int readCount = 0;
		long readBytes = 0;
		readCount = inputStream.read(buffer);
		while(readBytes < bytes && readCount != -1)
		{	
			readBytes += readCount;
			//System.out.println("read " + readCount + " bytes");
			fos.write(buffer, 0, readCount);
			fos.flush();
			
			readCount = inputStream.read(buffer);
			readBytes += readCount;				
		}
		try {
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public void run() 
	{
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
