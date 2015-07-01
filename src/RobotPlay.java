import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;


public class RobotPlay implements KeyListener
{

	private static final long TIME_DELAY = 10000;
	public static void main(String[] args) 
	{
		new RobotPlay().go();




	}

	private Robot robot;
	private ArrayList<TimedPress> presses;
	private void go() 
	{
		JFrame frame = new JFrame();
		frame.setSize(300, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.getContentPane().requestFocus();
		frame.getContentPane().setFocusable(true);
		frame.getContentPane().addKeyListener(this);
		frame.addKeyListener(this);
		presses = new ArrayList<TimedPress>();
		robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		robot.setAutoDelay(50);

		while(true)
		{
			if(presses.size()> 0 && presses.get(0).getTime() <= System.currentTimeMillis())
			{
				presses.get(0).executePress(robot);
				System.out.println("executed");
				presses.remove(0);
			}
			else
			{
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) 
	{
		presses.add(new TimedPress(System.currentTimeMillis() + TIME_DELAY, e.getKeyCode(), true));
		System.out.println("p: " + e.getKeyText(e.getKeyCode()));
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		presses.add(new TimedPress(System.currentTimeMillis() + TIME_DELAY, e.getKeyCode(), false));
		System.out.println("r: " + e.getKeyText(e.getKeyCode()));
	}

}
