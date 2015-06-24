import java.io.IOException;
import java.net.UnknownHostException;



public class RCMain 
{

	public static void main(String[] args) throws UnknownHostException, IOException 
	{
		if(args.length > 0)
		{
			new RCClient(args[0]);

		}
		else
			new RCServer();
	}

}
