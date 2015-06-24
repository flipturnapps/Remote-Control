import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class RCGui extends JFrame 
{
	private Image image;
	public RCGui()
	{
		this.setTitle("RCGui");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(new Dimension(300,300));
		this.getContentPane().add(new ImagePanel());
		this.setVisible(true);
	}
	public void setImage(BufferedImage image) 
	{
		this.image = image;
		
	}
	private class ImagePanel extends JPanel
	{
		

		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			if(image != null)
			g.drawImage(image, this.getX(), this.getY(), this.getWidth(),this.getHeight(),null);
		}
	}

}
