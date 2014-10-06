package utils.mutualinformation.mimatrixviewer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GeneralDataPane extends JPanel {

	private static final long serialVersionUID = 1052136447205136958L;

	public GeneralDataPane() {
		super();
		
		this.createGUI();

	}

	private void createGUI() {
		//////////////////////////
		// General Laout Settings
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		layout.columnWidths = new int[]{300};
		layout.columnWeights = new double[]{1};
		layout.rowHeights = new  int[]{0,25,25,25,25,0};
		layout.rowWeights = new  double[]{1,0,0,0,0,1};
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		constraints.insets= new Insets(3,3,3,3);
		this.setLayout(layout);
		///////////////////////////

		constraints.gridx = 0;
		constraints.gridy = 0;
		this.add(new JList<File>(),constraints);
		
		
		constraints.gridy = 1;
		this.add(new JButton("Add data File"),constraints);
		
		
		constraints.gridy = 2;
		this.add(new JButton("Remove data file"),constraints);

		
		constraints.gridy = 3;
		this.add(new JTextField("protein names"),constraints);

		
		constraints.gridy = 4;
		this.add(new JTextField("protein lengths"),constraints);
		
		constraints.gridy = 5;
		this.add(new JTextArea("Sequence"),constraints);

		
	}
}
