import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class RCClient extends Socket implements Runnable
{

	public RCClient(String ip) throws UnknownHostException, IOException
	{
		super(ip,RCServer.PORT);
		new Thread(this).start();
	}

	@Override
	public void run() 
	{
		System.out.println("connection");
		File outputFile = new File("pic.png");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(outputFile);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		byte[] buffer = new byte[1024];
		int readCount = 0;
		while(true)
		{
			try {
				readCount = this.getInputStream().read(buffer);
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
			System.out.println("read " + readCount + " bytes");
		}
		try {
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			this.getInputStream().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("try read image");
		BufferedImage image = null;
		try {
			image = ImageIO.read(outputFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedImage newImage = image;
		if(newImage == null)
			System.out.println("null!");
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		JPanel panel = new JPanel()
		{
			public void paintComponent(Graphics g)
			{
				g.drawImage(newImage, 0, 0, this.getWidth(),this.getHeight(),null);
			}
		};
		frame.getContentPane().add(panel);
		frame.setVisible(true);
		System.out.println("finish");

	}

}
