import java.awt.Dimension;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;


public class RCMouseEvent extends RCEvent 
{
	private double x;
	private double y;
	private Point point;
	public RCMouseEvent(long time, int keyId, boolean down,double x, double y)
	{
		super();
		this.setTime(time);
		this.setButtonId(keyId);
		this.setDown(down);
		this.setX(x);
		this.setY(y);
	}
	public RCMouseEvent()
	{
		super();
	}
	public String[] parse(String s)
	{
		String[] rest = super.parse(s);
		setX(Double.parseDouble(rest[0]));
		setY(Double.parseDouble(rest[1]));
		return null;
	}
	public String toString()
	{
		return super.toString() + RCEvent.SPLITCHARACTER + this.getX() + RCEvent.SPLITCHARACTER + this.getY();
	}
	public double getX() 
	{
		return x;
	}
	public void setX(double x)
	{
		this.x = x;
		calculatePoint();
	}
	public double getY()
	{
		return y;
	}
	public void setY(double y) 
	{
		this.y = y;
		calculatePoint();
	}
	@Override
	public void execute(Robot r) 
	{
		if(point == null)
			calculatePoint();
		r.mouseMove((int) point.getX(), (int) point.getY());
		if(this.isDown())
		r.mousePress(getButtonId());
		else
			r.mouseRelease(getButtonId());
	}
	private void calculatePoint() 
	{
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int preciseX = (int) ((dimension.getWidth()+0.0) * this.getX());	
		int preciseY = (int) ((dimension.getHeight()+0.0) * this.getY());	
		this.point = new Point(preciseX,preciseY);
	}

}
