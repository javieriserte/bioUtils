package utils.ConservationImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

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
		
		this.setImagePane(new JPanel());
		
		this.setOptionPane(new OptionsPane(this));
		
		JSplitPane jsp1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		
		jsp1.add(this.getOptionPane());

		jsp1.add(this.getImagePane());

		this.add(jsp1);
		
		jsp1.setDividerLocation(200);
		
		
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
