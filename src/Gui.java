import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
public class Gui extends JFrame
{
	private Image image;
	public Gui()
	{
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(500, 500);
		JPanel panel = new ImagePanel();
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
	private class ImagePanel extends JPanel
	{
		public void paintComponent(Graphics g)
		{
			g.drawImage(image, 0, 0, this.getWidth(),this.getHeight(),null);
		}
	}
}