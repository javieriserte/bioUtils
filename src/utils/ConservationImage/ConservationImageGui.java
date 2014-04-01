package utils.ConservationImage;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

public class ConservationImageGui extends JFrame {

	private static final long serialVersionUID = -2915260884274179118L;
	
	/////////////////////////////////////////
	// Components
	private JPanel imagePane;
	private OptionsPane optionPane;
	// End of Components
	/////////////////////////////////////////
	
	


	///////////////////////
	// Constructor
	public ConservationImageGui() {
		super();
		this.createGUI();
	}


	////////////////////////////////////////////////
	// Private Methods
	private void createGUI() {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		this.setImagePane(new JPanel());
		
		this.setOptionPane(new OptionsPane(this));
		
		JSplitPane jsp1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		
		jsp1.setOpaque(true);
		
		jsp1.add(this.getOptionPane());

		jsp1.add(this.getImagePane());
		
		this.getImagePane().setOpaque(true);
		
		this.getImagePane().setBackground(new Color(230,230,230));
		
		System.out.println(jsp1.getComponentCount());
		
		this.add(jsp1);
		
		jsp1.setDividerLocation(200);
		
		this.setMinimumSize(new Dimension(600,400));
		
		this.setPreferredSize(new Dimension(600,400));
		
		BasicSplitPaneUI ui = (BasicSplitPaneUI) jsp1.getUI();
		
        BasicSplitPaneDivider divider = ui.getDivider();
        
		divider.setBackground(Color.green);
		
		divider.setBorder(BorderFactory.createLineBorder(Color.gray, 2));
		
		this.pack();
		
	}
	
	////////////////////////////////////////////////
	// Getters And Setters
	
	protected JPanel getImagePane() {
		return imagePane;
	}


	protected void setImagePane(JPanel imagePane) {
		this.imagePane = imagePane;
	}


	protected OptionsPane getOptionPane() {
		return optionPane;
	}


	protected void setOptionPane(OptionsPane optionPane) {
		this.optionPane = optionPane;
	}

}
