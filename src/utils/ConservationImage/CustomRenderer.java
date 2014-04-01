package utils.ConservationImage;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

public class CustomRenderer implements ListCellRenderer<Object> {

	@Override
	public Component getListCellRendererComponent(JList<? extends Object> list, 
			Object value, int index, boolean isSelected, boolean cellHasFocus) {
		
		JLabel result = new JLabel(value.toString());
		
		result.setOpaque(true);
		
		result.setBackground((isSelected)?new Color(215,215,225):Color.white);

		Border blankBorder = BorderFactory.createEmptyBorder(3,3,3,3);
		
		if (index == -1 ) {
			
			Border dashBorder = BorderFactory.createLineBorder(new Color (220,220,220), 1, true);
			
			blankBorder = new CompoundBorder(dashBorder, blankBorder);

		}
		
		result.setBorder(blankBorder);
		
		return result;
		
	}


	
	
	


}
