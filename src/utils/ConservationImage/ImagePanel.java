package utils.ConservationImage;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	/////////////////////////////
	// Private Instance Variables
	private BufferedImage bi;
	private static final long serialVersionUID = -3161799680762273792L;

	///////////////////
	// Public Interface
	public void setBi(BufferedImage bi) {
		
		this.bi = bi;
		if (bi!=null) {
			this.setPreferredSize( new Dimension(bi.getWidth(),bi.getHeight()));
		}
		this.updateUI();
		
	}

	public void paint(Graphics g) {
		super.paint(g);
		if (this.getBi()!=null ) {
			g.drawImage((Image) bi, 0, 0, null);
		}

	}

	
	//////////////////
	// Protected Methods

	protected BufferedImage getBi() {
		return this.bi;
	}
	
}
