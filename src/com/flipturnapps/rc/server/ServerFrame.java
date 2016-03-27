package com.flipturnapps.rc.server;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.flipturnapps.kevinLibrary.sprite.ImageSprite;
import com.flipturnapps.kevinLibrary.sprite.SpritePanel;

public class ServerFrame extends JFrame
{

	private ImageSprite imageSprite;
	private SpritePanel sPanel;

	public ServerFrame() 
	{
		sPanel = new SpritePanel();
		readImage();
		sPanel.add(imageSprite);
		this.getContentPane().add(sPanel);
		this.setSize(500, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void readImage() {
		try {
			BufferedImage img = ImageIO.read(new File("serv.png"));
			if(imageSprite == null)
				imageSprite = new ImageSprite(img);
			else
				imageSprite.setImage(img);
			imageSprite.setAll(0, 0, sPanel.getWidth(), sPanel.getHeight());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void repaint()
	{
		readImage();
		sPanel.refresh();
		super.repaint();
	}

}
