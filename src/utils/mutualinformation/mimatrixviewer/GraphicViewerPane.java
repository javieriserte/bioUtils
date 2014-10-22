package utils.mutualinformation.mimatrixviewer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JTabbedPane;

import utils.mutualinformation.mimatrixviewer.matrixview_new.MatrixViewMainPane;

public class GraphicViewerPane extends JTabbedPane implements Observer{

	private static final long serialVersionUID = -3653200229915752191L;
	private Controller controller;
	private List<MIViewingPane> registeredViewingPanes;

	
	
	public GraphicViewerPane(Controller controller) {
		super();
		this.setController(controller);
		this.registeredViewingPanes = new ArrayList<>();
		this.createGUI();
		
	}

	private void createGUI() {
		
		MatrixViewMainPane component = new MatrixViewMainPane();
		this.controller.registerModelObserver(this);
		
		this.registeredViewingPanes.add(component);
		
//		String localPath = "src" + File.separatorChar +
//		           "utils" + File.separatorChar +
//		           "mutualinformation" + File.separatorChar +
//		           "mimatrixviewer" + File.separatorChar +
//		           "test"  + File.separatorChar +
//		           "mi_data_test";
//
//		File file = new File (localPath);
//		
//		MIMatrixReader reader = new MIMatrixReader();
//		
//		MI_Matrix a;
//		try {
//			a = reader.read(file);
//			DataContainer data = new DataContainer(a, "Test", new int[]{a.getSize()}, new String[]{"VP1"} );
			
//			component.setData(data);
			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		this.addTab("Bot�n #1", component);
		this.addTab("Bot�n #2", new JButton("bot�n 2"));
		this.addTab("Bot�n #3", new JButton("bot�n 3"));
		
	}



	public Controller getController() {
		return controller;
	}



	public void setController(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void update(Observable o, Object arg) {
		DataContainer data = ((Model)o).getCurrentData();
		
		if (data!=null) {
			MIViewingPane selectedComponent = (MIViewingPane)this.getSelectedComponent();
			selectedComponent.setData(data);
			selectedComponent.forceDrawing();
			
		} 
		
	}
	
	

	
	
}
