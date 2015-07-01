import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class RobotSSPlay extends JPanel implements MouseListener, Runnable
{

	private static final long TIME_DELAY = 500;
	public static void main(String[] args) 
	{
		new RobotSSPlay().go();
	}

	private Robot robot;
	private ArrayList<TimedClick> clicks;
	private BufferedImage image;
	private Dimension dim;
	private JFrame frame;
	private Rectangle rect;
	private void go() 
	{
		frame = new JFrame();
		frame.setSize(300, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.addMouseListener(this);
		frame.getContentPane().add(this);
		this.setFocusable(true);
		this.requestFocus();

		this.addMouseListener(this);
		frame.addMouseListener(this);
		clicks = new ArrayList<TimedClick>();
		robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		robot.setAutoDelay(50);
		dim = Toolkit.getDefaultToolkit().getScreenSize();
		rect = new Rectangle(dim);
		try {
			Thread.sleep(TIME_DELAY);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		image = robot.createScreenCapture(rect);

		frame.setVisible(true);

	}

	private void refresh() 
	{
		new Thread(this).start();

	}
	private void makePressedEvent(MouseEvent e, boolean pressed) {
		int x = (int) (((e.getX()+0.0)/(this.getWidth()+0.0)) * (dim.getWidth()+0.0));
		int y = (int) (((e.getY()+0.0)/(this.getHeight()+0.0)) * (dim.getHeight()+0.0));
		Point p = new Point(x,y);
		int button = e.getMaskForButton(e.getButton());
		long time = System.currentTimeMillis();
		this.clicks.add(new TimedClick(time, button, p, pressed));
	}
	public void paintComponent(Graphics g)
	{
		g.drawImage(image, 0,0,getWidth(),getHeight(),null);
	}
	@Override
	public void mouseClicked(MouseEvent e) 
	{
		// TODO Auto-generated method stub

	}
	@Override
	public void mousePressed(MouseEvent e) 
	{
		if(e.getButton() == MouseEvent.BUTTON2)
		{
			refresh();
		}
		else
		{
			makePressedEvent(e,true);
		}
	}




	@Override
	public void mouseReleased(MouseEvent e) 
	{
		if(e.getButton() != MouseEvent.BUTTON2)
		{
			makePressedEvent(e,false);
		}

	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run()
	{
		frame.setVisible(false);
		robot.delay(500);
		long startTime =  clicks.get(0).getTime();
		for(int i = 0; i < clicks.size(); i++)
		{
			
			long delay = (clicks.get(i).getTime() - startTime);
			System.out.println(delay);
			robot.delay((int) delay);
			
			clicks.get(i).executeClick(robot);			
		}
		clicks.clear();
		robot.delay(500);
		image = robot.createScreenCapture(rect);
		this.repaint();
		frame.setVisible(true);
	}



}
