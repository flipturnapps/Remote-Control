import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class RCClient extends Socket implements Runnable
{

	private Gui gui;

	public RCClient(String ip) throws UnknownHostException, IOException
	{
		super(ip,RCServer.PORT);
		new Thread(this).start();
		System.out.println("connection");
		gui = new Gui();
	}


	public void refresh() 
	{		
		System.out.println("start");
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		readInputToStream(stream, 8);
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
		readInputToStream(fos,byteCount);
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

	private void readInputToStream(OutputStream fos, long bytes)
	{
		byte[] buffer = new byte[1024];
		int readCount = 0;
		long readBytes = 0;
		while(readBytes < bytes)
		{
			try {
				readCount = this.getInputStream().read(buffer);
				readBytes += readCount;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(readCount != -1)
			{
				try {
					fos.write(buffer, 0, readCount);
					fos.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
				break;
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
