package utils.mutualinformation.mimatrixviewer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class OptionsPane extends JPanel {

	///////////////////////////////
	// Class Variables
	private static final long serialVersionUID = -8745543031788738615L;

	///////////////////////////////
	// Instance Variables
	private MIMatrixViewer matrixViewer;

	//////////////////////////////
	// Components
	JButton loadAlignmentButton;
	JLabel lengthsLabel;
	JTextField lengthsTextBox;
	JLabel namesLabel;
	JTextField namesTextBox;
	JButton lengthsAndNamesSetButton;
	/////////////////////////////////
	
	
	//////////////////////////////////
	// Constructor
	public OptionsPane(MIMatrixViewer matrixViewer) {
		super();
		this.setMatrixViewer(matrixViewer);
		this.createGUI();
		
	}
	///////////////////////////////////

	///////////////////////////////////
	// Protected  and private methods
	private void createGUI() {
		/////////////////////////
		// Configure Components
		this.loadAlignmentButton = new JButton("Load");
		this.loadAlignmentButton.addActionListener(new LoadAlignmentActionListener());
		this.lengthsLabel = new JLabel("Prot. Lengths");
		this.lengthsTextBox = new JTextField();
		this.namesLabel = new JLabel("Prot. names");
		this.namesTextBox = new JTextField();
		this.lengthsAndNamesSetButton = new JButton("Set");
		this.lengthsAndNamesSetButton.addActionListener(new SetProteinsAndNamesActionListener());
		/////////////////////////
		
		////////////////////////
		// Set Layout Proterties
		// and add components
        GridBagLayout layout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
        
		layout.rowHeights = new int[]{20};
		layout.rowWeights = new double[]{1};
		layout.columnWidths = new int[]{50,50,100,50,100,50};
		layout.columnWeights = new double[]{0,0,1,0,1,0};
		this.setLayout(layout);
	
		c.insets = new Insets(5, 5, 5, 5);
		c.fill = GridBagConstraints.BOTH;

		c.gridy = 0;

		c.gridx = 0;
		this.add(this.loadAlignmentButton,c);
		c.gridx = 1;
		this.add(this.lengthsLabel,c);
		c.gridx = 2;
		this.add(this.lengthsTextBox,c);
		c.gridx = 3;
		this.add(this.namesLabel,c);
		c.gridx = 4;
		this.add(this.namesTextBox,c);
		c.gridx = 5;
		this.add(this.lengthsAndNamesSetButton,c);
		/////////////////////////////
		
	}
	/////////////////////////////
	
	/////////////////////////////
	// Public interface
	public MIMatrixViewer getMatrixViewer() {
		return matrixViewer;
	}

	public void setMatrixViewer(MIMatrixViewer parent) {
		this.matrixViewer = parent;
	}
	///////////////////////////////
	
	///////////////////////////////
	// Auxiliary Classes
	private class LoadAlignmentActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			////////////////////////////////
			// Shows FileChooser UI
			JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.dir")));
			fileChooser.setMultiSelectionEnabled(false);
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.showOpenDialog(null);
			/////////////////////////////////
			
			/////////////////////////////////
			// Select file and process it
			File file = fileChooser.getSelectedFile();
			OptionsPane.this.getMatrixViewer().processFile(file);
			/////////////////////////////////
			
		}
		
	}
	
	private class SetProteinsAndNamesActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if ((e.getModifiers() & ActionEvent.ALT_MASK)==0 && 
				(e.getModifiers() & ActionEvent.CTRL_MASK)==0 &&
				(e.getModifiers() & ActionEvent.SHIFT_MASK)==0 &&
				(e.getModifiers() & ActionEvent.META_MASK)==0) {
				
				String[] numberFields = OptionsPane.this.lengthsTextBox.getText().trim().split("\\s*,\\s*");
				String[] namesFields = OptionsPane.this.namesTextBox.getText().trim().split("\\s*,\\s*");
				
				boolean emptyNames = namesFields.length == 1 && namesFields[0].equals("");
				boolean sameSize   = namesFields.length == numberFields.length;
				
				if (! emptyNames && ! sameSize) {
					
					JOptionPane.showMessageDialog(null, "Lengths and names do not correspond to each other.","Error",JOptionPane.ERROR_MESSAGE);
					
				} else {
				
					try {
						List<Integer> lengths = new ArrayList<>();
						
						for (String string : numberFields) {
							
							int currentValue = Integer.valueOf(string.trim());
							
							lengths.add(currentValue);
							
						}
						
						int matrixSize = OptionsPane.this.getMatrixViewer().getMatrixPane().getMatrix().getSize();
						
						int sumOfLengths = 0;
						for (Integer length : lengths) {
							sumOfLengths += length;
						}
						
						if (sumOfLengths!=matrixSize) {
							
							JOptionPane.showMessageDialog(null, "Lengths do not correspond to matrix size.","Error",JOptionPane.ERROR_MESSAGE);
							
						} else {
	
							int nprot = lengths.size();
							int[] lengthsArray = new int[nprot];
							
							for (int i=0; i<nprot; i++) {
								lengthsArray[i] = lengths.get(i);
							}
							
							if (!emptyNames) {
								OptionsPane.this.getMatrixViewer().getMatrixPane().setNames(namesFields);
							} else {
								OptionsPane.this.getMatrixViewer().getMatrixPane().setNames(null);
							}
							
							OptionsPane.this.getMatrixViewer().getMatrixPane().setProteinLengths(lengthsArray);
							
							OptionsPane.this.getMatrixViewer().getMatrixPane().accomodateSize();
							
							OptionsPane.this.getMatrixViewer().getMatrixPane().resetImage();
							
							OptionsPane.this.getMatrixViewer().getMatrixPane().updateUI();
							
						}
						
					} catch (NumberFormatException e2) {
						
						JOptionPane.showMessageDialog(null, "There Was an error while parsing lengths.","Error",JOptionPane.ERROR_MESSAGE);
						
					}
					
				}
				
			}
			
		}
		
	}
	////////////////////////////
}
