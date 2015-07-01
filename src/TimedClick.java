import java.awt.Point;
import java.awt.Robot;


public class TimedClick 
{
	private long time;
	private int buttonCode;
	private Point point;
	private boolean isPress;
	public TimedClick(long time, int buttonCode, Point point, boolean isPress) {
		super();
		this.time = time;
		this.buttonCode = buttonCode;
		this.point = point;
		this.isPress = isPress;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int getButtonCode() {
		return buttonCode;
	}
	public void setButtonCode(int buttonCode) {
		this.buttonCode = buttonCode;
	}
	public Point getPoint() {
		return point;
	}
	public void setPoint(Point point) {
		this.point = point;
	}
	public boolean isPress() {
		return isPress;
	}
	public void setPress(boolean isPress) {
		this.isPress = isPress;
	}
	public void executeClick(Robot robot) 
	{
		robot.mouseMove((int) this.getPoint().getX(), (int) this.getPoint().getY());
		if(this.isPress())
			robot.mousePress(getButtonCode());
		else
			robot.mouseRelease(getButtonCode());
		
	}
	
	
}
