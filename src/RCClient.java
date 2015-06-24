import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;


public class RCClient extends Socket implements Runnable
{
	private RCGui gui;

	public RCClient(String ip) throws UnknownHostException, IOException
	{
		super(ip,RCServer.PORT);
		RCGui gui = new RCGui();
		new Thread(this).start();
	}

	@Override
	public void run() 
	{
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(this.getInputStream()));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		while(true)
		{
			String line = null;
			try {
				line = reader.readLine();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if(line !=  null)
			{
				String[] strings = line.split(",");
				byte[] bytes = new byte[strings.length];
				for (int i = 0; i < bytes.length; i++) 
				{
					bytes[i] = Byte.parseByte(strings[i]);
				}
				BufferedImage image = null;
				try {
					image = ImageIO.read(new ByteArrayInputStream(bytes));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				gui.setImage(image);
			}
		}
		
	}
	
}
