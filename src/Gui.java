import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
public class Gui extends JFrame
{
	private Image image;
	private ImagePanel panel;
	public Gui()
	{
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(500, 500);
		panel = new ImagePanel();
		this.getContentPane().add(panel);
		this.setVisible(true);
	}
	public Image getImage() {
		return image;
	}
	public void setImage(Image image)
	{
		this.image = image;
		this.repaint();
	}
	private class Listeners implements MouseListener, KeyListener
	{

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) 
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) 
		{
			// TODO Auto-generated method stub
			
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
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyPressed(KeyEvent e)
		{
			if(e.getKeyCode() != e.VK_ENTER)
				System.out.println("notenter");
		}

		@Override
		public void keyReleased(KeyEvent e)
		{
			/*
			if(e.getKeyCode() != e.VK_ENTER)
				client.flushEventList(eventList);
				*/
		}
		
	}
	private class ImagePanel extends JPanel
	{
		public ImagePanel()
		{
			this.setFocusable(true);
			this.requestFocus();
			this.addMouseListener(new Listeners());
			this.addKeyListener(new Listeners());
		}
		public void paintComponent(Graphics g)
		{
			g.drawImage(image, 0, 0, this.getWidth(),this.getHeight(),null);
		}
	}
}