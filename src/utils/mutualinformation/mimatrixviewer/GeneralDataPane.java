package utils.mutualinformation.mimatrixviewer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class GeneralDataPane extends JPanel implements Observer{

	private static final long serialVersionUID = 1052136447205136958L;
	private static final String ADD_DATA_FILE_COMMAND = "add";
	private static final String PICK_DATA_COMMAND = "pick";
	private static final String SELECT_DATA_COMMAND = "pick";

	
	////////////////////////////////////////////////////////////////////////////
	// Components
	private JButton addDataButton;
	private JButton removeData;
	private JTextField protNamesTxt;
	private JTextField protLengthTxt;
	private JTextArea sequenceTxt;
	private Controller controller;
	private JList<DataContainer> dataList;

	public GeneralDataPane(Controller controller) {
		super();
		controller.registerModelObserver(this);
		this.setController(controller);
		this.createGUI();

	}
	////////////////////////////////////////////////////////////////////////////

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
		dataList = new JList<DataContainer>();
		dataList.setCellRenderer(new MIDataCellRenderer());
		dataList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		dataList.addListSelectionListener(new GeneralOptionDataSelectionListener());
		this.add(dataList,constraints);
		
		
		constraints.gridy = 1;
		 
		this.addDataButton = new JButton("Add data File");
		this.addDataButton.setActionCommand(ADD_DATA_FILE_COMMAND);
		this.addDataButton.addActionListener(new GeneralOptionActionListener());
		this.add(this.addDataButton,constraints);
		
		
		constraints.gridy = 2;
		this.removeData = new JButton("Remove data file");
		this.add(removeData,constraints);

		
		constraints.gridy = 3;
		protNamesTxt = new JTextField("protein names");
		this.add(protNamesTxt,constraints);

		
		constraints.gridy = 4;
		protLengthTxt = new JTextField("protein lengths");
		this.add(protLengthTxt,constraints);
		
		constraints.gridy = 5;
		sequenceTxt = new JTextArea("Sequence");
		sequenceTxt.setWrapStyleWord(true);
		sequenceTxt.setLineWrap(true);
		sequenceTxt.setEditable(false);
		this.add(sequenceTxt,constraints);

		
	}
	
	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	class GeneralOptionActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String actionCommand = e.getActionCommand();
			int keyModifiers = e.getModifiers();
			Object src = e.getSource();
			long time = e.getWhen();

			if (keyModifiers==16) {
				
				if (actionCommand.equals(ADD_DATA_FILE_COMMAND)) {
				
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setMultiSelectionEnabled(true);
					int r = fileChooser.showOpenDialog(GeneralDataPane.this);
					
					if (r == JFileChooser.APPROVE_OPTION) {
						
						File[] selectedFiles = fileChooser.getSelectedFiles();
						
						GeneralDataPane.this.getController().addDataFiles(selectedFiles);
						
						GeneralDataPane.this.sequenceTxt.setText(selectedFiles.toString());
						
					}
					
				}

			}

		}

	}
	
	class GeneralOptionDataSelectionListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			
			DataContainer value = GeneralDataPane.this.dataList.getSelectedValue();
			
			if (value!=null) {
			
				GeneralDataPane.this.sequenceTxt.setText(value.getData().getReferenceSequenceAsString());
			
				GeneralDataPane.this.controller.setActiveData(value);
			
			}
			
		}

	}

	@Override
	public void update(Observable o, Object arg) {
		List<DataContainer> l = ((Model)o).getData();
		DefaultListModel<DataContainer> a = new DefaultListModel<DataContainer>();
		for (DataContainer dataContainer : l) {
			a.addElement(dataContainer);
		}
		this.dataList.setModel(a);
		
	}
	
	class MIDataCellRenderer implements ListCellRenderer<DataContainer> {

		@Override
		public Component getListCellRendererComponent(
				JList<? extends DataContainer> list, DataContainer value,
				int index, boolean isSelected, boolean cellHasFocus) {
			
			int r,gb;
			gb=225;
			r = isSelected?245:225;
			
			JPanel panel = new JPanel();
			
			
			panel.setLayout(new GridLayout(2, 1, 0, 5));
			
			panel.setOpaque(true);
			panel.setBackground(new Color(r,gb,gb));
			JLabel comp = new JLabel(value.getTitle());
			Font f = comp.getFont().deriveFont(cellHasFocus?Font.BOLD:Font.PLAIN);
			comp.setFont(f);
			panel.add(comp);
			panel.add(new JLabel(String.valueOf(value.getData().count()) + " elements."));

			return panel;
			
		}


	}
	
	
	
}

