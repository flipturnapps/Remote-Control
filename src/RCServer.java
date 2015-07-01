import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
	public RCServer(float compression) throws IOException 
	{
		super(PORT);
		this.compression = compression;
		new Thread(this).start();
	}

	@Override
	public void run()
	{
		System.out.println("gonna wait for client");
		Socket socket = null;
		try {
			socket = this.accept();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.println("got client");
		Robot r = null;
		try {
			r = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedImage image = r.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
		System.out.println("capture");
		File output = new File("pic.png");
		
		
		Iterator<ImageWriter>writers =  ImageIO.getImageWritersByFormatName("jpg");
		ImageWriter writer = (ImageWriter) writers.next();

		ImageWriteParam param = writer.getDefaultWriteParam();

		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		param.setCompressionQuality(compression);
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
		System.out.println(output.length());
		FileInputStream stream = null;
		try {
			stream = new FileInputStream(output);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int readCount = 0;
		byte[] buffer = new byte[1024];
		while(true)
		{
			try {
				readCount = stream.read(buffer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(readCount > 0)
			{
				try {
					socket.getOutputStream().write(buffer, 0, readCount);
					socket.getOutputStream().flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if (readCount == -1)
				break;
			System.out.println("wrote " + readCount + " bytes");
		}
		try {
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			socket.getOutputStream().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("all sent!");
		System.out.println(output.length());
	}

}
