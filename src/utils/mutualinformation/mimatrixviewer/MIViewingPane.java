package utils.mutualinformation.mimatrixviewer;

import javax.swing.JComponent;



public abstract class MIViewingPane extends JComponent{

	private static final long serialVersionUID = -3394871213386920614L;

	public abstract void setData(DataContainer data);
	
	public abstract void forceDrawing();
	
}
