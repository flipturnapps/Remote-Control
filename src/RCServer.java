import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
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
	public RCServer() throws IOException 
	{
		super(PORT);
		new Thread(this).start();
	}

	@Override
	public void run()
	{
		Socket socket = null;
		try {
			socket = this.accept();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		Iterator<ImageWriter>writers =  ImageIO.getImageWritersByFormatName("jpg");
		ImageWriter writer = (ImageWriter) writers.next();

		ImageWriteParam param = writer.getDefaultWriteParam();

		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		param.setCompressionQuality(0.1f);
		Robot r = null;
		try {
			r = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PrintWriter pWriter = null;
		try {
			pWriter = new PrintWriter(socket.getOutputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while (true)
		{
			BufferedImage capture = r.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			try
			{
				ImageOutputStream iis = ImageIO.createImageOutputStream(stream);
				writer.setOutput(iis);	   

				writer.write(null, new IIOImage(capture, null, null), param);
				String send = "";
				for(int i = 0; i < stream.toByteArray().length; i++)
				{
					send += stream.toByteArray()[i] + ",";
				}
				send = send.substring(0,send.length()-2);
				pWriter.println(send);
				pWriter.flush();
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}

}
