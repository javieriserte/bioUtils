package utils.mutualinformation.mimatrixviewer;

import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JTabbedPane;

import utils.mutualinformation.mimatrixviewer.matrixview_new.MatrixViewMainPane;

public class GraphicViewerPane extends JTabbedPane{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3653200229915752191L;

	
	
	public GraphicViewerPane() {
		super();
		this.createGUI();
		
	}



	private void createGUI() {
		
		MatrixViewMainPane component = new MatrixViewMainPane();
		
		String localPath = "src" + File.separatorChar +
		           "utils" + File.separatorChar +
		           "mutualinformation" + File.separatorChar +
		           "mimatrixviewer" + File.separatorChar +
		           "test"  + File.separatorChar +
		           "mi_data_test";

		File file = new File (localPath);
		
		MIMatrixReader reader = new MIMatrixReader();
		
		MI_Matrix a;
		try {
			a = reader.read(file);
			DataContainer data = new DataContainer(a, new int[]{a.getSize()}, new String[]{"VP1"} );
			
			component.setData(data);
			this.addTab("Botón #1", component);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		this.addTab("Botón #2", new JButton("botón 2"));
		this.addTab("Botón #3", new JButton("botón 3"));
		
	}
	
	

	
	
}
