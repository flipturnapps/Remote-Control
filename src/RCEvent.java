import java.awt.Robot;


public abstract class RCEvent 
{
	public static final String SPLITCHARACTER = "~~";
	private long time;
	private int buttonId;
	private boolean down;
	public RCEvent()
	{
		super();
	}
	
	public static RCEvent[] makeTimesRelative(RCEvent[] events)
	{
		long startTime = Long.MAX_VALUE;
		for (int i = 0; i < events.length; i++)
		{
			long time = events[i].getTime();
			if(time < startTime)
				startTime = time;
		}	
		for (int i = 0; i < events.length; i++)
		{
			long time = events[i].getTime();
			events[i].setTime(time - startTime);
		}
		return events;
	}
	public String[] parse(String s)
	{
		String[] split = s.split(SPLITCHARACTER);
		if(split.length < 3)
			return null;
		setTime(Long.parseLong(split[0]));
		setButtonId(Integer.parseInt(split[1]));
		setDown(Boolean.parseBoolean(split[2]));
		if(split.length == 5)
		{
			String[] ret = new String[split.length - 3];
			for (int i = 0; i < ret.length; i++) 
			{
				ret[i] = split[i + 3];
			}
			return ret;
		}
		else
			return null;
	}
	
	public abstract void execute(Robot r);
	
	public String toString()
	{
		return this.getTime() + SPLITCHARACTER + this.getButtonId() + SPLITCHARACTER +this.isDown();
	}
	
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int getButtonId() {
		return buttonId;
	}
	public void setButtonId(int buttonId) {
		this.buttonId = buttonId;
	}
	public boolean isDown() {
		return down;
	}
	public void setDown(boolean down) {
		this.down = down;
	}
	
	
	
}
