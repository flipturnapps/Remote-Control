import java.awt.Robot;


public class RCKeyEvent extends RCEvent
{
	public RCKeyEvent(long time, int keyId, boolean down)
	{
		super();
		this.setTime(time);
		this.setButtonId(keyId);
		this.setDown(down);
	}
	public RCKeyEvent()
	{
		super();
	}
	@Override
	public void execute(Robot r) 
	{
		if(this.isDown())
			r.keyPress(getButtonId());
		else
			r.keyRelease(getButtonId());		
	}
}
