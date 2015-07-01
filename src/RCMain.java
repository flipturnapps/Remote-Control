import java.io.IOException;
import java.net.UnknownHostException;



public class RCMain 
{

	public static void main(String[] args) throws UnknownHostException, IOException 
	{
		float compression = 0;
		boolean fail = false;
		try
		{
		compression = Float.parseFloat(args[0]);
		}
		catch(Exception ex)
		{
			fail = true;
		}
		if(fail)
		{
			System.out.println("client");
			while(true)
			{
				try
				{
					RCClient c = new RCClient(args[0]);
					break;
					
				}
				catch(Exception ex)
				{
					System.out.println("Client connect failed");
				}
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else
		{
			System.out.println("server");
			new RCServer(compression);
		}
			
	}

}
