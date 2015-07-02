import java.awt.Robot;


public class TimedPress 
{
	private long time;
	private int keyCode;
	private boolean isPress;
	
	public TimedPress(long time, int keyCode, boolean isPress) {
		super();
		this.time = time;
		this.keyCode = keyCode;
		this.isPress = isPress;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int getKeyCode() {
		return keyCode;
	}
	public void setKeyCode(int keyCode) {
		this.keyCode = keyCode;
	}
	public boolean isPress() {
		return isPress;
	}
	public void setPress(boolean isPress) {
		this.isPress = isPress;
	}
	public void executePress(Robot robot) 
	{
		if(this.isPress())
			robot.keyPress(getKeyCode());
		else
			robot.keyRelease(getKeyCode());
		
	}
	
}
